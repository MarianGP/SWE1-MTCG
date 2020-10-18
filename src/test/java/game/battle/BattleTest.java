package game.battle;

import game.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)

class BattleTest {
    @Mock
    User player1 = new User("Player 1", "0");
    User player2 = new User("Player 2", "0");
    User player3 = new User("Player 3", "0");

    Battle newBattle = new Battle(player1, player2, 100);
    Battle anotherBattle = new Battle(player2, player3, 100);

    @BeforeEach
    void setUp() {
        player1.buyPackage();
        player2.buyPackage();
        player3.buyPackage();
        player1.buyPackage();
        player2.buyPackage();
        player3.buyPackage();

        player1.prepareDeck();
        player2.prepareDeck();
        player3.prepareDeck();
    }

    @Test
    @DisplayName("None winners this round. Both still cards in the deck")
    void noWinnersYet() {
        Assertions.assertEquals(null,newBattle.checkWinner(player3, player2));
    }

    @Test
    @DisplayName("Winner Player2. Player1 has 0 cards")
    void returnWinnerPlayer2() {
        player1.setDeck(null);
        Assertions.assertEquals(player2,newBattle.checkWinner(player1, player2));
        Assertions.assertEquals(player1,newBattle.getLoser(player1, player2));
    }

    @Test
    @DisplayName("")
    void checkIfWinner() {
        //when(newBattle.checkWinner(player1, player2).thenReturn(false));
        //verify(newBattle.checkWinner(player1, player2);
    }

    @Test
    @DisplayName("Swap Players: CurrentPlayer becomes NextPlayer")
    void tryToSwapAttackers() {
        User temp = newBattle.getCurrentPlayer();
        newBattle.swapAttacker(newBattle.getCurrentPlayer(),newBattle.getNextPlayer());
        Assertions.assertEquals(temp, newBattle.getNextPlayer());
    }

    @Test
    @DisplayName("Move defeated Card to opponents Deck")
    void moveDefeatedCard() {
        Assertions.assertEquals(5, anotherBattle.getCurrentPlayer().getDeck().size());
        Assertions.assertEquals(5, anotherBattle.getNextPlayer().getDeck().size());
        if(true) {
            newBattle.moveCard(anotherBattle.getNextPlayer().getDeck().get(0));
        }
        Assertions.assertEquals(6, anotherBattle.getCurrentPlayer().getDeck().size());
        Assertions.assertEquals(4, anotherBattle.getNextPlayer().getDeck().size());
    }

}