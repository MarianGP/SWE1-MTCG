package game.battle;


import game.cards.Card;
import game.user.User;
import lombok.Getter;

@Getter

public class Battle {

    static User winner = null;
    static private User loser = null;
    static private int rounds = 0;
    static private int maxRounds;
    private User currentPlayer;
    private User nextPlayer;

    Battle(User player1, User player2, int maxR) {
        this.currentPlayer = player1;
        this.nextPlayer = player2;
        maxRounds = maxR;
    }

    public void startBattle() {
        while (winner == null && rounds < maxRounds) {
            winner = playRound(this.currentPlayer, this.nextPlayer);
            rounds++;
        }
        System.out.println("Game Over");

        if(winner != null) {
            loser = getLoser(this.currentPlayer, this.nextPlayer);
            winner.eloUp(); //+3 pts
            loser.eloDown(); //-5 pts
        } else {
            System.out.println("Nobody won the game");
        }
    }

    private User playRound(User currentPlayer, User nextPlayer) {
        //current player attacks next player (both draw random cards out of the deck)
        boolean cardWasDefeated = false;
        Card defendingCard = nextPlayer.chooseRandomCard();
        Card attackingCard = currentPlayer.chooseRandomCard();
        cardWasDefeated = defendingCard.receiveAttack(attackingCard);
        if(cardWasDefeated) {
            currentPlayer.getDeck().add(defendingCard);
            nextPlayer.getDeck().remove(defendingCard);
        }
        winner = checkWinner(currentPlayer, nextPlayer);
        swapAttacker(currentPlayer, nextPlayer);
        //no winner returns null
        return winner;
    }

    public void swapAttacker(User playerA, User playerB) {
        User temp = playerA;
        this.currentPlayer = playerB;
        this.nextPlayer = temp;
    }

    private User checkWinner(User currentPlayer, User nextPlayer)  {
        if(currentPlayer.getDeck().size() == 0) {
            winner = nextPlayer;
        } else if(nextPlayer.getDeck().size() == 0) {
            winner = currentPlayer;
        } else {
            winner =  null;
        }
        return winner;
    }

    private User getLoser(User currentPlayer, User nextPlayer) {
        if(currentPlayer.getDeck().size() == 0) {
            return currentPlayer;
        } else if (nextPlayer.getDeck().size() == 0) {
            return nextPlayer;
        }
        throw new UnsupportedOperationException("ERR: there is a winner, but not a loser");
    }

    public void gameStats(User winner, User loser) {
        System.out.println("Winner: " + winner.getUsername());
        winner.printUserStats();
        loser.printUserStats();
    }

    public static void main(String[] args) {
        User newUser = new User("hallo" , "0");
        newUser.buyPackage();
        newUser.prepareDeck();
        System.out.println("======DRAW======");
        newUser.chooseRandomCard().printCardStats();
    }

}
