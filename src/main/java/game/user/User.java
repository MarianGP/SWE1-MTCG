package game.user;

import game.cards.Card;
import game.decks.Package;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
@Setter

public class User {
    private String username;
    private String password;
    private String token;
    private int coins;
    private int ELO;
    private List<Card> stack;
    private List<Card> deck;
    private final static Random RANDOM = new Random();
    private final static int DECKSIZE = 5;

    public User(String username, String password){
        this.username = username;
        this.password = password;
        this.token = username + "-mtcgToken";
        this.coins = 20;
        this.ELO = 100;
        this.stack = new ArrayList<>();
        this.deck = new ArrayList<>();
    }

    public void buyPackage(){
        Package newPackage = new Package();
        Card temp;
        if(this.coins - newPackage.getPrice() > 0) {
            this.coins = this.coins - newPackage.getPrice();
            for (int i = 0; i < newPackage.getCardsInPackage().size(); i++) {
                this.stack.add(newPackage.getCardsInPackage().get(i));
            }
        }
    }

    public void prepareDeck() {
        for (int i = 0; i < DECKSIZE; i++) {
            Card temp  = this.stack.get(RANDOM.nextInt(this.stack.size()));
            this.deck.add(temp);
            this.stack.remove(temp);
        }
    }

    public Card chooseRandomCard() {
        return this.deck.get(RANDOM.nextInt(this.stack.size()));
    }

    public void listCards(List<Card> aStack) {
        int i = 0;
        System.out.println("Your Card's Stack");
        while (i < aStack.size()) {
            aStack.get(i).printCardStats();
            i++;
        }
    }

    public void eloDown() {
        int temp;
        temp = this.ELO - 5;
        if(temp >= 0) {
            this.ELO = temp;
        } else {
            this.ELO = 0;
        }
    }

    public void eloUp(){
        this.ELO = this.ELO + 3;
    }

    public void printUserStats() {
        System.out.println(
            "User: " + this.username +
            " - ELO: " + this.ELO +
            " - cois: " + this.coins
        );
    }



}
