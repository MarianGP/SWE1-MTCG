package game_server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import game.cards.Card;
import game.cards.MonsterCard;
import game.cards.SpellCard;
import game.decks.CardDeck;
import game.decks.CardStack;
import game.enums.CardName;
import game.enums.Element;
import game.enums.MonsterType;
import game.user.User;
import game_server.db.DbConnection;
import game_server.model.RequestHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.sql.SQLException;
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
            .stack(new CardStack())
            .deck(new CardDeck())
            .isAdmin(false)
            .build();

    UserController userController = UserController.builder()
            .db(new DbConnection())
            .user(user)
            .build();

    String correctJson = "{\n" +
            "    \"username\": \"marian\",\n" +
            "    \"password\": \"1234\"\n" +
            "}";

    String wrongJson = "{\n" +
            "    \"username\": \"marian\",\n" +
            "    \"password\": \"999\"\n" +
            "}";

    List<Card> cardsList = new ArrayList<>();
    Card ork = new MonsterCard(MonsterType.ORK, Element.FIRE, CardName.ONE, 100.0f);
    Card dragon = new MonsterCard(MonsterType.DRAGON, Element.FIRE, CardName.ONE, 100.0f);
    Card water = new SpellCard(Element.WATER, CardName.FIVE, 50.0f);
    Card fire = new SpellCard(Element.FIRE, CardName.FIVE, 200.0f);
    Card normal = new SpellCard(Element.NORMAL, CardName.FIVE, 200.0f);

    boolean isLogged = this.userController.getDb().deleteSession("marian-mtcgToken");


    @Test
    @DisplayName("Log In: Wrong/Correct Pass, Logged")
    void testLogin() throws JsonProcessingException, SQLException {
        Assertions.assertEquals("Wrong user or password",
                this.userController.signIn(RequestHandler.getCredentials(wrongJson)));
        Assertions.assertEquals("Login was successful",
                this.userController.signIn(RequestHandler.getCredentials(correctJson)));
        Assertions.assertEquals("User is already logged in",
                this.userController.signIn(RequestHandler.getCredentials(correctJson)));
    }

    @Test
    void testGetLoggedUser() {
        Assertions.assertTrue(this.userController.setUser("test1-mtcgToken"));
    }

    @Test
    @DisplayName("Buy new package")
    void testBuyPackage() {
        createList();
        this.userController.buyPackage(cardsList);
        Assertions.assertEquals(15, this.userController.getUser().getCoins());
    }


    void createList() {
        cardsList.add(dragon);
        cardsList.add(water);
        cardsList.add(fire);
        cardsList.add(normal);
        cardsList.add(ork);
    }
}