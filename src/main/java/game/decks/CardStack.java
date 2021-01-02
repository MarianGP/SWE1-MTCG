package game.decks;

import game.cards.Card;
import game.interfaces.Randomizable;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
@Setter
public class CardStack implements Randomizable {
    List<Card> stack;
    private static final Random RANDOM = new Random();

    public CardStack() {
        this.stack = new ArrayList<>();
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

//    public Card cardToTrade() {
//
//    }
}
