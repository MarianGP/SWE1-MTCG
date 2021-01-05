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

<<<<<<< HEAD
public class CardDeck extends User implements Randomizable {
    private final static int DECKSIZE = 5;
    private List<Card> deck;
=======
public class CardDeck implements Randomizable {
    private final static int DECKSIZE = 4;
    private List<Card> deckList;
>>>>>>> integration-game-server
    Random RANDOM = new Random();

    public CardDeck() {
        this.deckList = new ArrayList<>();
    }

    //  ! random Deck from stack
    public CardDeck(User player) {
        this.deckList = new ArrayList<>();
        for (int i = 0; i < DECKSIZE; i++) {
            this.deckList.add( player.getStack().randomCard() );
        }
    }

    public Card randomCard() {
        int randomPosition = RANDOM.nextInt(this.deckList.size());
        Card dropped = this.deckList.get(randomPosition);
        this.deckList.remove(randomPosition);
        return dropped;
    }

    public void clearDeck() {
        this.deckList.clear();
    }

    //adding cards defeated during battle
    public void addCardsToDeck(List oneList) {
        for (int i = 0; i < oneList.size(); i++) {
            oneList.add(oneList.get(i));
        }
    }

}
