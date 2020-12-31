package game_server.db;

import game.cards.Card;
import game.cards.MonsterCard;
import game.enums.Element;
import game.enums.MonsterType;
import game.enums.Name;
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
            .image(":/")
            .build();
    Card dragon = new MonsterCard(MonsterType.DRAGON, Element.FIRE, Name.FIVE, 120.0f);


    @Test
    void testConnect() {
        Assertions.assertTrue(db.test("marian"));
    }

    @Test
    void testInsertUser() {
        Assertions.assertTrue(db.insertUser(user));
        Assertions.assertEquals("JohnDoe", db.getUser("JohnDoe", "1234").getUsername());
        Assertions.assertTrue(db.deleteUser(user));
        Assertions.assertNull(db.getUser("JohnDoe", "1234"));
    }

    @Test
    @DisplayName("Get one user from DB")
    void testGetUser() {
        Assertions.assertEquals("marian", db.getUser("marian", "1234").getUsername());
        Assertions.assertNull(db.getUser("marian", "."));
    }

    @Test
    @DisplayName("Insert MonsterCard")
    void testInsertMonsterCard() throws SQLException {
        Assertions.assertTrue(db.insertMonster((MonsterCard) dragon, false, db.getUser("marian", "1234"), "bla-bla-1234"));
        Assertions.assertEquals(120.0f, db.getCard("bla-bla-1234").getDamage());
        Assertions.assertTrue(db.deleteCard("bla-bla-1234"));
        if(db.getCard("bla-bla-1234") == null) {
            System.out.println("nullpointer");
        }
    }
}
