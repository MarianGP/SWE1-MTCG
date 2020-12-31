package game_server.controller;

import game.user.User;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Builder
@Data
public class GameController {
    public List<User> loggedUsers = Collections.synchronizedList(new ArrayList<>());

    public void addToLoggedUsers(User user) {
        loggedUsers.add(user);
    }

}
