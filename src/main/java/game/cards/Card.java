package game.cards;

import game.enums.Element;
import game.enums.MonsterType;
import game.enums.Name;
import game.interfaces.Attackable;
import game.interfaces.CardInterface;

abstract class Card implements CardInterface, Attackable {
    private String name;
    private Element cardElement;
    private int damage;
    private boolean locked;

    Card(MonsterType type, Element cardElement) {

    }

    Card(MonsterType type, Element cardElement, Name name) {

    }

    public String printCardStats() {

    }

}
