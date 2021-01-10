package game_server.controller;

import game.battle.Battle;
import game.battle.BattleLog;
import game.cards.Card;
import game.decks.CardDeck;
import game.user.User;
import game_server.db.DbConnection;
import game_server.interfaces.Playable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

@Builder
@Data
@AllArgsConstructor

public class GameController  implements Playable {
    private ArrayBlockingQueue<User> players;
    private static DbConnection db = new DbConnection();
    private AtomicBoolean isFinished;
    private BattleLog battleLog;

    public void addPlayer(User player) {
        if(player.getDeck().getDeckList().isEmpty())
            player.setDeck(new CardDeck(player)); //random deck

        players.add(player);
        this.isFinished.set(false);
    }

    public void startGame() throws InterruptedException {

        User player1 = players.take();
        User player2 = players.take();

        Battle newBattle = new Battle(player1,player2,1000);

        newBattle.startBattle();

        updatePlayerResults(newBattle.getCurrentPlayer());
        updatePlayerResults(newBattle.getNextPlayer());

        battleLog = new BattleLog(
                db.getUser(player1.getUsername()),
                db.getUser(player2.getUsername()),
                newBattle.getWinner(),
                newBattle.getRounds());

        System.out.println(battleLog.getResultSummary());

        this.isFinished.set(true);
    }

    public void updatePlayerResults(User player) {
        for(Card card: player.getDeck().getDeckList()){
            db.setCardOwner(card.getCid(), player.getUsername());
        }

        db.cleanupDeck(player.getUsername());
        db.editUserStats(player);
        updateStack(player);
    }

    public void updateStack(User player) {
        List<Card> list = db.getCardListByOwner(player.getUsername());
        if(!list.isEmpty()) {
            player.getStack().setStackList(list);
        }
    }

}
