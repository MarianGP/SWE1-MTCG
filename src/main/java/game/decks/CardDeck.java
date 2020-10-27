package game.decks;

import game.cards.Card;
import game.interfaces.Randomizable;
import game.user.User;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
@Setter

public class CardDeck implements Randomizable {
    private final static int DECKSIZE = 5;
    private List<Card> deck;
    Random RANDOM = new Random();

    public CardDeck(User player) {
        this.deck = new ArrayList<Card>();
        for (int i = 0; i < DECKSIZE; i++) {
            this.deck.add( player.getStack().randomCard() ); //deletes from stack and adds to deck
        }
    }

    public Card randomCard() {
        int randomPosition = RANDOM.nextInt(this.deck.size());
        Card dropped = this.deck.get(randomPosition);
        this.deck.remove(randomPosition);
        return dropped;
    }

    public void clearDeck() {
        this.deck.clear();
    }

    //adding cards defeated during battle
    public void addCardsToDeck(List oneList) {
        for (int i = 0; i < oneList.size(); i++) {
            oneList.add(oneList.get(i));
        }
    }



}
