package game.user;

import game.decks.CardDeck;
import game.decks.CardStack;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;


class UserTest {
    @Mock
    User player1 = User.builder()
            .username("Player1")
            .password("")
            .token("JohnDoe" + "-mtcgToken")
            .bio(":/")
            .image(":/")
            .coins(20)
            .elo(100)
            .stack(new CardStack())
            .deck(new CardDeck())
            .isAdmin(false)
            .build();

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
        Assertions.assertAll("mockUser",
                () ->  Assertions.assertEquals(98, player1.getElo())
        );
    }

    @Test
    @DisplayName("Buy Package")
    void addPackageToStack() {
        Assertions.assertAll("mockUser",
                () ->  Assertions.assertEquals(10, player1.getStack().getStackList().size()),
                () ->  Assertions.assertEquals(10, player1.getCoins())
        );
    }

    @Test
    @DisplayName("Create new Deck")
    void createNewDeck() {
        player1.prepareDeck();
        Assertions.assertEquals(4, player1.getDeck().getDeckList().size());
        Assertions.assertEquals(6, player1.getStack().getStackList().size());
    }

}