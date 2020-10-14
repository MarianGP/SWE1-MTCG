package game.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)
class UserTest {

    User mockUser = new User("usernameMock", "1234");
    void setUp() {
        mockUser.buyPackage();
        mockUser.buyPackage();
    }

    @Test
    @DisplayName("Buy Package")
    void addPackageToStack() {
        setUp();
        assertAll("mockUser",
                () -> assertEquals(10, mockUser.getStack().size()),
                () -> assertEquals(10, mockUser.getCoins())
        );
    }

    @Test
    @DisplayName("Create new Deck")
    void createNewDeck() {
        setUp();
        mockUser.prepareDeck();
        Assertions.assertEquals(5,mockUser.getDeck().size());
        Assertions.assertEquals(5,mockUser.getStack().size());
    }


}