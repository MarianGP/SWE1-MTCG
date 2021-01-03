package game_server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import game.cards.Card;
import game.decks.CardDeck;
import game.decks.CardStack;
import game.user.Credentials;
import game.user.User;
import game_server.db.DbConnection;
import game_server.serializer.CustomUserSerializer;
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
        List<Card> list = db.getCardListByOwner(this.user.getUsername());
        if(list.isEmpty() || list == null) {
            return false;
        } else {
            this.user.getStack().setStackList(list);
            this.user.getDeck().setDeckList(db.getDeckCards(this.user.getUsername()));
            return true;
        }
    }

    public String addCardsToDeck(List<String> ids) {

        db.cleanupDeck(this.user.getUsername()); // ! cleanup deck

        for(String id: ids) {  // ! mark card isDeck if user-id marches
            if(!db.setIsDeck(id, this.user.getUsername())){
                db.cleanupDeck(this.user.getUsername());
                return "Card doesn't belong to this user. Not added.";
            }
        }

        // ! initialize/set new user's deck
        this.user.getDeck().setDeckList(db.getDeckCards(this.user.getUsername()));

        for(String id: ids) { // ! delete from trading market
            if(db.getTradeByCardId(id) != null)
                db.deleteTrade(id);
        }

        return null;
    }

    public boolean setUser(String token) {
        this.user = db.getLoggedUser(token);
        if(this.user != null) {
            initializeStack(); //changed check for errors
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
        db.editUserStats(this.user); // ! CAREFUL last minute change
        return "Package bought";
    }

    public String editUser(String jsonEditedUser) throws JsonProcessingException {
        CustomUserSerializer parsedUser = getParsedUser(jsonEditedUser);

        String pass = (parsedUser.getPassword() != null) ? parsedUser.getPassword() : this.user.getPassword();
        String newBio = (parsedUser.getBio() != null) ? parsedUser.getBio() : this.user.getBio();
        String newImage = (parsedUser.getImage() != null) ? parsedUser.getImage() : this.user.getImage();

        User user = User.builder()
                .username(this.user.getUsername())
                .password(pass)
                .token(this.user.getUsername() + "-mtcgToken")
                .bio(newBio)
                .image(newImage)
                .coins(this.user.getCoins())
                .elo(this.user.getElo())
                .stack(this.user.getStack())
                .deck(this.user.getDeck())
                .isAdmin(this.user.isAdmin())
                .build();

        if(db.editUser(user, this.user.getUsername())) {
            return "User info update was successful (UserController)";
        } else {
            return "User info  update failed (UserController)";
        }
    }

    public CustomUserSerializer getParsedUser(String jsonEditUser) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonEditUser, CustomUserSerializer.class);
    }


}
