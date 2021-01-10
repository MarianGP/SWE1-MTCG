package game.interfaces;

import game.cards.Card;

public interface Attackable {
    boolean receiveAttack(Card attacker);
}
