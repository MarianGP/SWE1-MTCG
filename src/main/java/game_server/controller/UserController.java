package game_server.controller;

import game.user.Credentials;
import game.user.User;
import game_server.db.PostgreSQLJDBC;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Data
@Builder
public class UserController {
    public static User user;
    private PostgreSQLJDBC db;

    public String addNewUser(Credentials credentials){
        try {
            user = User.builder()
                    .username(credentials.getUsername())
                    .password(credentials.getPassword())
                    .token(credentials.getUsername() + "-mtcgToken")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return "Signup failed";
        }
        db.insertUser(user);
        return "Signup was successful";
    }

    public String login(String username, String password) {
        if(allUsers.get(username).getPassword().equals(password)) {
            loggedUsers.add(username + "-mtcgToken");
            return "Login was successful";
        }
        return "Login failer";
    }




}
