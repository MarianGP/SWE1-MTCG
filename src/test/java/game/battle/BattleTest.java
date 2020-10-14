package game.battle;

import game.user.User;
import org.junit.jupiter.api.Assertions;
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
    Battle newBattle = new Battle(player1, player2, 100);
    void setUp() {
        player1.buyPackage();
        player2.buyPackage();
        player1.buyPackage();
        player2.buyPackage();
        player1.prepareDeck();
        player2.prepareDeck();
    }

    @Test
    @DisplayName("Battle")
    void tryBattle() {
        setUp();


    }

    @Test
    @DisplayName("Swap Players: CurrentPlayer becomes NextPlayer")
    void tryToSwapAttackers() {
        User temp = newBattle.getCurrentPlayer();
        newBattle.swapAttacker(newBattle.getCurrentPlayer(),newBattle.getNextPlayer());
        Assertions.assertEquals(temp, newBattle.getNextPlayer());
    }

    @Test
    void ControlGameStats() {

    }
}