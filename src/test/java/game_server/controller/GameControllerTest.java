package game_server.controller;

import game_server.db.DbConnection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

class GameControllerTest {

    @Mock
    GameController gameController = GameController.builder()
            .battleLog(null)
            .isFinished(new AtomicBoolean(false))
            .players(new ArrayBlockingQueue<>(2,true))
            .build();

    UserController userController1 = new UserController(new DbConnection(), null);
    UserController userController2 = new UserController(new DbConnection(), null);

    @Test
    @DisplayName(   "Will Fail - Special TEST to debug the Battle" +
                    "This test will fail when the players don't have enough cards (DB). IllegalArgumentException." +
                    "Why necessary: Is not possible to change threads using the debugger. This is the only way of debugging the battle.")

    void startGame() throws InterruptedException {
        userController1.setUser("altenhof-mtcgToken");
        userController2.setUser("kienboec-mtcgToken");

        gameController.addPlayer(userController1.getUser());
        gameController.addPlayer(userController2.getUser());

        try {
            gameController.startGame();
        } catch (IllegalArgumentException e) {
            System.out.println("Stack is empty");;
        }

    }

    @Test
    void getBattleLog() {
    }
}