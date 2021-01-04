package game.battle;

import game.cards.Card;
import game.user.User;
import game_server.db.DbConnection;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter

public class Battle {

    private static DbConnection db = new DbConnection();

    private User winner = null;
    private User loser = null;
    private int rounds = 0;
    private final int MAXROUNDS;
    private User currentPlayer;
    private User nextPlayer;
    private List<Card> table;

    public Battle(User player1, User player2, int setMaxRounds) {
        this.currentPlayer = player1;
        this.nextPlayer = player2;
        this.table = new ArrayList<>();
        MAXROUNDS = setMaxRounds;
    }

    public void startBattle() {
        while (this.winner == null && rounds < MAXROUNDS) {
            rounds++;
            this.winner = playRound(this.currentPlayer, this.nextPlayer);
        }
        System.out.println("Game Over");

        if(this.winner != null) {
            this.loser = getLoser(this.currentPlayer, this.nextPlayer);
            this.winner.eloUp();     //+3 pts
            this.loser.eloDown();    //-5 pts
        }
    }

    private User playRound(User currentPlayer, User nextPlayer) {
        compareCards(currentPlayer.getDeck().randomCard(), nextPlayer.getDeck().randomCard());
        winner = checkWinner(currentPlayer, nextPlayer);

        if(winner != null) {
            swapAttacker(currentPlayer, nextPlayer);
        }

        return winner; //no winner returns null
    }

    public void compareCards(Card table0, Card table1) {
        this.table.add(table0);
        this.table.add(table1);

        User temp = currentPlayer;
        User temp2 = nextPlayer;

        if( table1.receiveAttack( table0 ) ) { //next receives the attack from current
            temp2 = currentPlayer;
        } else if( table0.receiveAttack( table1 ) ) { //current receives the attack from next
            temp = nextPlayer;
        }

        temp.getDeck().getDeckList().add(this.table.get(0));
        temp2.getDeck().getDeckList().add(this.table.get(1));

        this.table.clear(); //empty tableList for next round
    }

    public void swapAttacker(User playerA, User playerB) {
        User temp = playerA;
        this.currentPlayer = playerB;
        this.nextPlayer = temp;
    }

    public User checkWinner(User currentPlayer, User nextPlayer)  {
        int i = currentPlayer.getDeck().getDeckList().size();
        int j = nextPlayer.getDeck().getDeckList().size();

        if (i == 0) {
            this.winner = nextPlayer;
        } else if (j == 0) {
            this.winner = currentPlayer;
        } else {
            this.winner =  null;
        }

        return this.winner;
    }

    public User getLoser(User currentPlayer, User nextPlayer) {
        if(currentPlayer.getDeck().getDeckList().size() == 0) {
            return currentPlayer;
        } else if (nextPlayer.getDeck().getDeckList().size() == 0) {
            return nextPlayer;
        }
        throw new UnsupportedOperationException("ERR: there is a winner, but not a loser");
    }

//    public static void main(String[] args) {
//        User p1 = User.builder().username("Player1").password("").token("")
//                .bio(":/").image(":/").coins(20).elo(100).stack(new CardStack())
//                .deck(new CardDeck()).isAdmin(false).build();
//
//        User p2 = User.builder().username("Player2").password("").token("")
//                .bio(":/").image(":/").coins(20).elo(100).stack(new CardStack())
//                .deck(new CardDeck()).isAdmin(false).build();
//
//        p1.buyPackage();
//        p1.prepareDeck();
//        p2.buyPackage();
//        p2.prepareDeck();
//
//        Battle newBattle = new Battle(p1,p2,1000);
//
//        newBattle.startBattle();
//        newBattle.gameStats();
//
//    }

}
