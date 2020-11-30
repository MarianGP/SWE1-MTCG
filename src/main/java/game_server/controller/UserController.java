package game_server.controller;

import game.user.User;
import lombok.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class UserController {
    public static ConcurrentHashMap<String, User> allUsers = new ConcurrentHashMap<>();
    public static List<String> loggedUsers = Collections.synchronizedList(new ArrayList<>());


    public void addUser(String username, User newUser) {
        allUsers.put(username,newUser);
    }

    public boolean login(String username, String password) {
        if(allUsers.get(username).getPassword().equals(password)) {
            loggedUsers.add(username + "-mtcgToken");
            return true;
        }
        return false;
    }

    public ConcurrentHashMap<String, User> getAllUsers() {
        return allUsers;
    }


}
