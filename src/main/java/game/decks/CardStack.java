package game.decks;

import game.cards.Card;
import game.interfaces.Randomizable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CardStack implements Randomizable {
    List<Card> stack;
    private static final Random RANDOM = new Random();

    public CardStack() {
        this.stack = new ArrayList<Card>();
    }

    @Override
    public Card randomCard(List<Card> cardList)  {
        int SIZE = cardList.size();
        return cardList.get(RANDOM.nextInt(SIZE));
    }

    public void listCardsInStack() {
        int i = 0;
        System.out.println("Your Card's Stack");
//        while (i < this.size()) {
//            this.get(i).printCardStats();
//            i++;
//        }
    }

//    public Card cardToTrade() {
//
//    }
}
