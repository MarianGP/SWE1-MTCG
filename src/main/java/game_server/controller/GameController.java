package game_server.controller;

import game.battle.Battle;
import game.battle.BattleLog;
import game.cards.Card;
import game.decks.CardDeck;
import game.user.User;
import game_server.db.DbConnection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
@AllArgsConstructor

public class GameController {
    private List<User> players;
    private static DbConnection db = new DbConnection();
    private boolean isFinished;
    private BattleLog battleLog;

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

    public void addPlayer(User player) {
        if(player.getDeck().getDeckList().isEmpty())
            player.setDeck(new CardDeck(player)); //random deck

        players.add(player);
        this.isFinished = false;
    }

    public void startGame() {

        Battle newBattle = new Battle(players.get(0),players.get(1),100);

        newBattle.startBattle();

        updatePlayerResults(newBattle.getCurrentPlayer());
        updatePlayerResults(newBattle.getNextPlayer());

        battleLog = new BattleLog(
                db.getUser(players.get(0).getUsername()),
                db.getUser(players.get(1).getUsername()),
                newBattle.getWinner(),
                newBattle.getRounds());

        System.out.println(battleLog.getResultSummary());

        this.setFinished(true);
    }

}
