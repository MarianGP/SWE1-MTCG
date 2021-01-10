package game.user;

import game.cards.Card;
import game.decks.CardDeck;
import game.decks.CardStack;
import game.decks.Package;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class User implements Comparable<User> {
    private String username;
    private String password;
    private String token;
    private String bio;
    private String image;
    private int coins;
    private int elo;
    private int gamesPlayed;
    private CardStack stack;
    private CardDeck deck;

    private boolean isAdmin;

    private final static int ELOUP = 3;
    private final static int ELODOWN = 5;

    public void buyPackage(){ // ! generate random package for testing
        Package newPackage = new Package();
        if(this.coins - newPackage.getPRICE() > 0) {
            this.coins = this.coins - newPackage.getPRICE();
            this.stack.addListToStack(newPackage.getCardsInPackage());
        }
    }

    public boolean buyPackage(List<Card> list){ // ! DB cards
        Package newPackage = new Package(list);
        if(this.coins >= newPackage.getPRICE()) {
            this.coins = this.coins - newPackage.getPRICE();
            this.stack.addListToStack(newPackage.getCardsInPackage());
            return true;
        } else {
            return false;
        }
    }

    public void prepareDeck() {
        this.deck = new CardDeck(this);
    }

    public void eloDown() {
        int temp;
        temp = this.elo - ELODOWN;
        if(temp >= 0) {
            this.elo = temp;
        } else {
            this.elo = 0;
        }
    }

    public void reorganizeCards() {
        this.stack.addListToStack(this.deck.getDeckList());
        this.deck.clearDeck();
    }

    public void eloUp(){
        this.elo = this.elo + ELOUP;
    }

    public String userStats(String rank) {
        if(!this.isAdmin) {
            if(rank.isEmpty()) {
                return  rank +
                        " User: " + this.username +
                        " - Coins: " + this.coins +
                        " - Games Played: " + this.gamesPlayed +
                        " - ELO: " + this.elo + "\n";
            }
            return  rank +
                    " User: " + this.username +
                    " - Games Played: " + this.gamesPlayed +
                    " - ELO: " + this.elo + "\n";
        } else {
            return "";
        }
    }

    public String printUserDetails() {
        return  "-- User Account Summary -- \n" +
                "\tUser: " + this.username +
                " - ELO: " + this.elo + " - coins: " + this.coins +
                "\nBio: "+ this.bio +
                "\nImage: "+ this.image +
                "\nToken: "+ this.token + " \n";
    }

    public String getUserStats() {
        return  "-- User Account Summary -- \n" +
                "User: " + this.username +
                " - ELO: " + this.elo +
                " - Games Played: " + this.gamesPlayed +
                " - Total Cards: " + this.stack.getStackList().size() + " \n";
    }

    @Override
    public int compareTo(User user) {
        return this.elo - user.getElo();
    }

}
