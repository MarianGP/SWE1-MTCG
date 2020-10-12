package game.cards;

import game.enums.Element;
import game.enums.Name;
import game.interfaces.Card;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@EqualsAndHashCode

public class SpellCard implements Card {

    private String name;
    private Element cardElement;
    private int damage;
    private boolean locked;

    SpellCard(Element cardElement) {
        Name aName = Name.ONE;
        this.cardElement = cardElement;
        this.name = aName + " " + cardElement.getElementName();
        this.damage = cardElement.getMaxDamage();
        this.locked = false;
    }

    @Override
    public boolean attack(Card attacker) {
        if(attacker instanceof MonsterCard) {
            if(((MonsterCard) attacker).getDamage() > this.damage){
                return true;
            } else {
                return false;
            }
        } else if (attacker instanceof SpellCard) {
            return this.cardElement.elementDefeats(((SpellCard) attacker).cardElement);
        } else {
            throw new UnsupportedOperationException("Not a spell nor a monster card");
        }
    }

    @Override
    public void printCardStats() {

    }
}
