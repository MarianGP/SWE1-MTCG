package game_server.db;

import game.cards.Card;
import game.cards.MonsterCard;
import game.decks.CardDeck;
import game.decks.CardStack;
import game.enums.Element;
import game.enums.MonsterType;
import game.enums.CardName;
import game.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.sql.SQLException;

class DbConnectionTest {

    @Mock
    DbConnection db = new DbConnection();
    User user = User.builder()
            .username("JohnDoe")
            .password("1234")
            .token("JohnDoe" + "-mtcgToken")
            .bio(":/")
            .image(":/")
            .coins(20)
            .elo(100)
            .stack(new CardStack())
            .deck(new CardDeck())
            .isAdmin(false)
            .gamesPlayed(2)
            .build();

    User user2 = User.builder()
            .username("JohnDoe")
            .coins(15)
            .elo(101)
            .build();

    User updatedUser = User.builder()
            .username("JohnDowwww")
            .password("12345")
            .token("JohnDowwww" + "-mtcgToken")
            .image(":D")
            .build();

    Card dragon = new MonsterCard(MonsterType.DRAGON, Element.FIRE, CardName.FIVE, 120.0f);

    @Test
    @DisplayName("Insert User into DB")
    void testInsertUser() throws SQLException {
        Assertions.assertTrue(db.insertUser(user));
        Assertions.assertEquals("JohnDoe", db.getUser("JohnDoe", "1234").getUsername());
        Assertions.assertTrue(db.deleteUser(user));
        Assertions.assertNull(db.getUser("JohnDoe", "1234"));
    }

    @Test
    @DisplayName("Get User from DB")
    void testGetUser() {
        Assertions.assertEquals("marian", db.getUser("marian", "1234").getUsername());
        Assertions.assertNull(db.getUser("marian", "."));
    }

    @Test
    @DisplayName("Insert and Delete MonsterCard")
    void testInsertAndDeleteMonsterCard() throws SQLException {
        Assertions.assertTrue(db.insertCard((MonsterCard) dragon, false, db.getUser("marian", "1234"), "bla-bla-1234", 9999));
        Assertions.assertEquals(120.0f, db.getCardById("bla-bla-1234").getDamage());
        Assertions.assertTrue(db.deleteCard("bla-bla-1234"));
        if(db.getCardById("bla-bla-1234") == null) {
            System.out.println("nullpointer");
        }
    }

    @Test
    @DisplayName("Return one logged user")
    void testGetLoggedUser() {
        Assertions.assertEquals("stefan", db.getLoggedUser("stefan-mtcgToken").getUsername());
    }

    @Test
    @DisplayName("Edit user stats and Data")
    void testEditUserInfo() throws SQLException {
        db.insertUser(user);

        //edit stats
        Assertions.assertEquals(true, db.editUserStats(user2));
        Assertions.assertEquals(15, db.getUser("JohnDoe").getCoins());
        Assertions.assertEquals(101, db.getUser("JohnDoe").getElo());

        //edit user information
        Assertions.assertTrue(db.editUser(updatedUser, "JohnDoe"));
        user = db.getUser("JohnDowwww");
        Assertions.assertEquals("JohnDowwww-mtcgToken", user.getToken());

        db.deleteUser(db.getUser("JohnDowwww"));
    }
}
