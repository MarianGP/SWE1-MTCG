package game_server.controller;

import game.cards.Card;
import game.cards.MonsterCard;
import game.cards.SpellCard;
import game.enums.Element;
import game.enums.MonsterType;
import game.enums.Name;
import game.user.Credentials;
import game.user.User;
import game_server.db.DbConnection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

class UserControllerTest {
    @Mock
    User user = User.builder()
            .username("JohnDoe")
            .password("1234")
            .token("JohnDoe" + "-mtcgToken")
            .bio(":/")
            .image(":/")
            .coins(20)
            .elo(100)
            .build();
    UserController userController = UserController.builder()
            .db(new DbConnection())
            .user(user)
            .build();
    Credentials credentials1 = new Credentials("marian","1234");

    List<Card> cardsList = new ArrayList<>();
    Card ork = new MonsterCard(MonsterType.ORK, Element.FIRE, Name.ONE, 100.0f);
    Card dragon = new MonsterCard(MonsterType.DRAGON, Element.FIRE, Name.ONE, 100.0f);
    Card water = new SpellCard(Element.WATER, Name.FIVE, 50.0f);
    Card fire = new SpellCard(Element.FIRE, Name.FIVE, 200.0f);
    Card normal = new SpellCard(Element.NORMAL, Name.FIVE, 200.0f);


    @Test
    void testLogin() {
        Assertions.assertEquals("Login was successful", userController.login(credentials1));
        Assertions.assertTrue(userController.getDb().isLogged("marian-mtcgToken"));
        Assertions.assertTrue(userController.getDb().deleteSession("marian-mtcgToken"));
    }

    @Test
    void testGetLoggedUser() {
        Assertions.assertEquals("Session opened",userController.getLoggedUser("stefan-mtcgToken"));
    }

    @Test
    @DisplayName("Add package")
    void testPackage() {
        createList();
        userController.buyNewPackage(cardsList);
    }


    void createList() {
        cardsList.add(dragon);
        cardsList.add(water);
        cardsList.add(fire);
        cardsList.add(normal);
        cardsList.add(ork);
    }
}