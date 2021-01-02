package game_server.controller;

import game.cards.Card;
import game.decks.CardDeck;
import game.decks.CardStack;
import game.user.Credentials;
import game.user.User;
import game_server.db.DbConnection;
import lombok.Builder;
import lombok.Data;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class UserController {
    private DbConnection db;
    private User user;

    public String addUserDB(Credentials credentials) throws SQLException {
        try {
            this.user = User.builder()
                    .username(credentials.getUsername())
                    .password(credentials.getPassword())
                    .token(credentials.getUsername() + "-mtcgToken")
                    .bio("-")
                    .image(":|")
                    .coins(20)
                    .elo(100)
                    .stack(new CardStack())
                    .deck(new CardDeck())
                    .isAdmin(false)
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

    public boolean initializeStack() {
        List<Card> list = db.getCardByOwner(this.user.getUsername());
        if(list.isEmpty()) {
            return false;
        } else {
            this.user.getStack().setStack(list);
            return true;
        }
    }

    public boolean addCardsToDeck(String request) {
        List<Card> stackCards = db.getCardByOwner(this.user.getUsername());

        return false;
    }

    public boolean deleteCardsList(List <Card> cardList)  {
        for(Card temp: cardList) {
            db.deleteCard(temp.getCid());
        }
        //TODO: maybe use it
        return false;
    }

    public boolean setUser(String token) {
        this.user = db.getLoggedUser(token);
        if(this.user != null) {
            return true;
        } else {
            return false;
        }
    }

    public String login(Credentials credentials) {
        try {
            this.user = db.getUser(credentials.getUsername(), credentials.getPassword());
            if(this.user != null) {
                if(db.addSession(credentials.getUsername() + "-mtcgToken")){
                    System.out.println("Login was successful (UserController)");
                    return "Login was successful";
                } else {
                    return "User is already logged in";
                }
            } else {
                return "Wrong user or password";
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            System.out.println("Login failed (UserController)");
            return "Something failed";
        }
    }

    public String buyNewPackage(List<Card> packageDB) {
        if(packageDB == null) {
            packageDB = new ArrayList<>(db.getPackageCards(db.getMaxPackageId()));
            if(packageDB.size() == 0) {
                return "The are no packages available to buy";
            }
            db.addOwnerToPackage(this.user.getUsername(), db.getMaxPackageId());
        }
        this.user.buyPackage(packageDB);
        return "Package bought";
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
