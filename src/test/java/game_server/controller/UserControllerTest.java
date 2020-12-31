package game_server.controller;

import game.user.Credentials;
import game.user.User;
import game_server.db.DbConnection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

class UserControllerTest {
    @Mock
    User user = User.builder()
            .username("JohnDoe")
            .password("1234")
            .token("JohnDoe" + "-mtcgToken")
            .image(":/")
            .build();
    Credentials credentials1 = new Credentials("marian","1234");

    @Test
    void testLogin() {
        UserController userCtr = UserController.builder().db(new DbConnection()).build();
        Assertions.assertEquals("marian", userCtr.login(credentials1).getUsername());

    }

//    @Test
//    void testAddNewUser() {
//
//    }
}