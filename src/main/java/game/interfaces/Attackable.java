package game.interfaces;

import game.cards.Card;

public interface Attackable {
    public boolean receiveAttack(Card attacker);
}
