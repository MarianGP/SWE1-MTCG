package game.battle;

import game.cards.Card;
import game.user.User;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter

public class Battle {

    static private User winner = null;
    static private User loser = null;
    private int rounds = 0;
    private final int MAXROUNDS;
    private User currentPlayer;
    private User nextPlayer;
    private List<Card> table;

    Battle(User player1, User player2, int setMaxRounds) {
        this.currentPlayer = player1;
        this.nextPlayer = player2;
        this.table = new ArrayList<Card>();
        MAXROUNDS = setMaxRounds;
    }

    public void startBattle() {
        while (winner == null && rounds < MAXROUNDS) {
            rounds++;
            winner = playRound(this.currentPlayer, this.nextPlayer);
        }
        System.out.println("Game Over");

        if(winner != null) {
            loser = getLoser(this.currentPlayer, this.nextPlayer);
            winner.eloUp();     //+3 pts
            loser.eloDown();    //-5 pts
        }
        this.currentPlayer.reorganizeCards();
        this.nextPlayer.reorganizeCards();
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
            winner = nextPlayer;
        } else if (j == 0) {
            winner = currentPlayer;
        } else {
            winner =  null;
        }

        return winner;
    }

    public User getLoser(User currentPlayer,User nextPlayer) {
        if(currentPlayer.getDeck().getDeckList().size() == 0) {
            return currentPlayer;
        } else if (nextPlayer.getDeck().getDeckList().size() == 0) {
            return nextPlayer;
        }
        throw new UnsupportedOperationException("ERR: there is a winner, but not a loser");
    }

    public void gameStats() {
        if(winner != null) {
            System.out.println("Winner: " + winner.getUsername());
        } else {
            System.out.println("Nobody won the game");
        }
        System.out.println("Rounds: " + rounds);
        currentPlayer.userStats("");
        nextPlayer.userStats("");

    }

//    public static void main(String[] args) {
//        User p1 = new User("Player1" , "0");
//        User p2 = new User("Player2" , "0");
//        p1.buyPackage();
//        p1.prepareDeck();
//        p2.buyPackage();
//        p2.prepareDeck();
//
//        Battle newBattle = new Battle(p1,p2,100);
//        newBattle.startBattle();
//        newBattle.gameStats();
//    }

}
