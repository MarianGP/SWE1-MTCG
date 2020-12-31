package game_server.controller;

import game.cards.Card;
import game.user.Credentials;
import game.user.User;
import game_server.db.DbConnection;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserController {
    private DbConnection db;
    private User user;

    public String addNewUser(Credentials credentials){
        try {
            user = User.builder()
                    .username(credentials.getUsername())
                    .password(credentials.getPassword())
                    .token(credentials.getUsername() + "-mtcgToken")
                    .bio("-")
                    .image(":|")
                    .coins(20)
                    .elo(100)
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

    public String getLoggedUser(String token) {
        this.user = db.getLoggedUser(token);
        if(this.user != null) {
            return "Session is open";
        } else {
            return "Can't access player's data";
        }
    }

    public String login(Credentials credentials) {
        try {
            this.user = db.getUser(credentials.getUsername(), credentials.getPassword());
            if(this.user != null) {
                if(db.addSession(credentials.getUsername() + "-mtcgToken")){
                    System.out.println("Login was successful (UserController)");
                    return "Login was successful";
                }
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            System.out.println("Login failed (UserController)");
        }
        return "Something failed";
    }

    public String buyNewPackage(List<Card> packageDB) {

        if(packageDB.isEmpty()) {
            user.buyPackage();
        } else {
            user.buyPackage(packageDB);
        }

        return "TODO";
    }

    public String editUser(User user){
//        try {
//            user = User.builder()
//                    .username(credentials.getUsername())
//                    .password(credentials.getPassword())
//                    .token(credentials.getUsername() + "-mtcgToken")
//                    .image(":|")
//                    .build();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "Signup failed (UserController)";
//        }
//
//        if(db.updateUser(user)) {
//            return "Data update was successful (UserController)";
//        } else {
//            return "Data update failed (UserController)";
//        }
        return "delete me";
    }
}
