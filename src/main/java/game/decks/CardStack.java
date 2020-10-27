package game.decks;

import game.cards.Card;
import game.interfaces.Randomizable;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter

public class CardStack implements Randomizable {
    List<Card> stack;
    private static final Random RANDOM = new Random();

    public CardStack() {
        this.stack = new ArrayList<Card>();
    }

    public Card randomCard() {
        int rand = RANDOM.nextInt(this.stack.size());
        Card temp = this.stack.get(rand);
        this.stack.remove(rand);
        return temp;
    }

    public void addListToStack(List<Card> list) {
        for (int i = 0; i < list.size(); i++) {
            this.stack.add(list.get(i));
        }
    }

    public void listCardsInStack() {
        int i = 0;
        System.out.println("Your Card's Stack");
        if(this.stack.size() > 0) {
            while (i < this.stack.size()) {
                this.stack.get(i).printCardStats();
                i++;
            }
        } else {
            System.out.println("Empty...");
        }

    }


//    public Card cardToTrade() {
//
//    }
}
