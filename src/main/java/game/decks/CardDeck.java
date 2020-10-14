package game.decks;

import game.cards.Card;
import game.interfaces.Randomizable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CardDeck implements Randomizable {
    private List<Card> deck;

    public CardDeck(List<Card> stack) {
        this.deck = new ArrayList<Card>();
        for (int i = 0; i < 5; i++) {
            stack.get(i).printCardStats();
            this.deck.add(stack.get(i));
            //this.deck.add(randomCard(stack));
        }
    }

    public Card randomCard(List<Card> stack)  {
        int SIZE = stack.size();
        Random RANDOM = new Random();
//        temp.printCardStats();
        Card temp  = stack.get(RANDOM.nextInt(stack.size()));
        temp.printCardStats();
        //stack.remove(temp);
        return temp;
    }
}
