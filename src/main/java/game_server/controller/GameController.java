package game_server.controller;

import game.battle.Battle;
import game.cards.Card;
import game.user.User;
import game_server.db.DbConnection;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class GameController { //TODO: ???
    public List<User> players; // will also need to be static
    public static DbConnection db = new DbConnection();


    public static void updateGameResults(User player) {

        for(Card card: player.getDeck().getDeckList()){
            db.setCardOwner(card.getCid(), player.getUsername());
        }

        db.cleanupDeck(player.getUsername());

        db.editUserStats(player);

        List<Card> list = db.getCardListByOwner(player.getUsername());
        if(!list.isEmpty()) {
            player.getStack().setStackList(list);
        }
    }

    public static void main(String[] args) {
        // ! 2 user controller to have access to

        User p1 = db.getUser("player1");
        p1.getDeck().setDeckList(db.getDeckCards(p1.getUsername()));

        User p2 = db.getUser("player2");
        p2.getDeck().setDeckList(db.getDeckCards(p2.getUsername()));

        Battle newBattle = new Battle(p1,p2,1000);

        System.out.println(p1.printUserDetails());
        System.out.println(p2.printUserDetails());

        newBattle.startBattle();
        newBattle.gameStats();

        System.out.println(p1.printUserDetails());
        System.out.println(p2.printUserDetails());

        // ! Update stack in DB
        updateGameResults(p1);
        updateGameResults(p2);



    }



}
