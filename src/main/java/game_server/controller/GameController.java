package game_server.controller;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Builder
@Data
public class GameController {
    public static List<UserController> loggedUsers = Collections.synchronizedList(new ArrayList<>());
    GameServer server;

    public static void main(String[] args) {
        GameServer server = new GameServer();
        server.run();
    }

    public void addUser(UserController userController) {
        loggedUsers.add(userController);
    }

}
