package game.cards;

import game.decks.CardStack;
import game.enums.Element;
import game.enums.MonsterType;
import game.interfaces.Attackable;
import game.interfaces.CardInterface;
import lombok.Getter;

@Getter

public abstract class Card extends CardStack implements CardInterface, Attackable {
    private String cid;
    private String name;
    private MonsterType type;
    private Element cardElement;
    private float damage;
    private boolean locked;
    private String owner;
}
