package game.user;

import game.cards.Card;
import game.decks.CardDeck;
import game.decks.CardStack;
import game.decks.Package;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Random;

@Setter
@Getter
@Builder
public class User {
    private String username;
    private String password;
    private String token;
    private String bio;
    private String image;
    private int coins = 20;
    private int elo = 100;
    private CardStack stack = new CardStack();;
    private CardDeck deck;
    private final static Random RANDOM = new Random();
    private final static int DECKSIZE = 5;

    public void buyPackage(){ //for testing purposes
        Package newPackage = new Package();
        Card temp;
        if(this.coins - newPackage.getPrice() > 0) {
            this.coins = this.coins - newPackage.getPrice();
            this.stack.addListToStack(newPackage.getCardsInPackage());
        }
    }

    public void buyPackage(List<Card> list){
        Package newPackage = new Package(list);
        if(this.coins - newPackage.getPrice() > 0) {
            this.coins = this.coins - newPackage.getPrice();
            this.stack.addListToStack(newPackage.getCardsInPackage());
        }
    }

    public void prepareDeck() {
        this.deck = new CardDeck(this);
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
        temp = this.elo - 5;
        if(temp >= 0) {
            this.elo = temp;
        } else {
            this.elo = 0;
        }
    }

    public void reorganizeCards() {
        this.stack.addListToStack(this.deck.getDeck());
        this.deck.clearDeck();
    }

    public void eloUp(){
        this.elo = this.elo + 3;
    }

    public void printUserStats() {
        System.out.println(
            "\nUser: " + this.username +
            " - ELO: " + this.elo +
            " - cois: " + this.coins
        );
        this.stack.listCardsInStack();
    }

    public String printUserDetails() {
        return  "\nUser: " + this.username +
                " - ELO: " + this.elo + " - cois: " + this.coins +
                "\nBio: "+ this.bio +
                "\nImage: "+ this.image +
                "\nToken: "+ this.token + " ";
    }



}
