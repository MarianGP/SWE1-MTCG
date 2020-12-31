package game_server.controller;

import game.user.Credentials;
import game.user.User;
import game_server.db.DbConnection;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserController {
    private static DbConnection db;
    private User user;

    public String addNewUser(Credentials credentials){
        try {
            user = User.builder()
                    .username(credentials.getUsername())
                    .password(credentials.getPassword())
                    .token(credentials.getUsername() + "-mtcgToken")
                    .image(":|")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return "Signup failed (UserController)";
        }

        if(db.insertUser(user)) {
            return "Signup was successful (UserController)";
        } else {
            return "Signup failed (UserController)";
        }
    }

    public String editUser(User user){
        try {
            user = User.builder()
                    .username(credentials.getUsername())
                    .password(credentials.getPassword())
                    .token(credentials.getUsername() + "-mtcgToken")
                    .image(":|")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return "Signup failed (UserController)";
        }

        if(db.updateUser(user)) {
            return "Data update was successful (UserController)";
        } else {
            return "Data update failed (UserController)";
        }
    }

    public User login(Credentials credentials) {
        try {
            User user = db.getUser(credentials.getUsername(), credentials.getPassword());
//            if(user != null) {
//                gameCtr.addToLoggedUsers(user);
//            }
            System.out.println("Login was successful (UserController)");
            return user;
        } catch (RuntimeException e) {
            e.printStackTrace();
            System.out.println("Login failed (UserController)");
            return null;
        }
    }

}
