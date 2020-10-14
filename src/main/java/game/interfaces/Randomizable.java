package game.interfaces;

import game.cards.Card;

import java.util.List;

public interface Randomizable {
    public Card randomCard(List<Card> cardList);
}
