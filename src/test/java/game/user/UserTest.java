package game.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;


class UserTest {
    @Mock
    User player1 = User.builder().username("Player1").build();

    @BeforeEach
    void setUp() {
        player1.buyPackage();
        player1.buyPackage();
    }

    @Test
    @DisplayName("ELO update")
    void eloManipulation() {
        player1.eloDown();
        player1.eloUp();
        assertAll("mockUser",
                () -> assertEquals(98, player1.getELO())
        );
    }

    @Test
    @DisplayName("Buy Package")
    void addPackageToStack() {
        assertAll("mockUser",
                () -> assertEquals(10, player1.getStack().getStack().size()),
                () -> assertEquals(10, player1.getCoins())
        );
    }

    @Test
    @DisplayName("Create new Deck")
    void createNewDeck() {
        player1.prepareDeck();
        Assertions.assertEquals(5, player1.getDeck().getDeck().size());
        Assertions.assertEquals(5, player1.getStack().getStack().size());
    }

    @Test
    @DisplayName("compareTableCards")
    void compareCards() {
        player1.prepareDeck();
        Assertions.assertEquals(5, player1.getDeck().getDeck().size());
        player1.getDeck().randomCard();
        Assertions.assertEquals(4, player1.getDeck().getDeck().size());
    }

}