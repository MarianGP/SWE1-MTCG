package game.user;

import game.decks.Package;
import game.interfaces.CardInterface;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter

public class User {
    private String username;
    private String password;
    private String token;
    private int coins;
    private int ELO;
    private ArrayList<CardInterface> stack;

    public User(String username, String password){
        this.username = username;
        this.username = password;
        this.token = username + "-mtcgToken";
        this.coins = 20;
        this.ELO = 100;
    }

    public void buyPackage(){
        Package newPackage = new Package();
        if(this.coins - newPackage.getPrice() > 0) {
            this.coins = this.coins + newPackage.getPrice();
            for (int i = 0; i < newPackage.getCardsInPackage().size(); i++) {
                newPackage.getCardsInPackage().get(i).printCardStats();
            }
        }

    }

    public void listCardsInStack() {
        int i = 0;
        System.out.println("Your Card's Stack");
        while (i < stack.size()) {
            stack.get(i).printCardStats();
            i++;
        }
    }



}
