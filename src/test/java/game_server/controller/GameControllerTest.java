package game_server.controller;

import game_server.db.DbConnection;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;

class GameControllerTest {

    @Mock
    GameController gameController = GameController.builder()
            .battleLog(null)
            .isFinished(false)
            .players(new ArrayList<>())
            .build();

    UserController userController1 = new UserController(new DbConnection(), null);
    UserController userController2 = new UserController(new DbConnection(), null);

    @Test
    void startGame() {
        userController1.setUser("player1-mtcgToken");
        userController2.setUser("player2-mtcgToken");

        gameController.addPlayer(userController1.getUser());
        gameController.addPlayer(userController2.getUser());

        gameController.startGame();
    }

    @Test
    void getBattleLog() {
    }
}