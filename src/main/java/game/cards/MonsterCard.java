package game.cards;

import game.enums.Element;
import game.enums.MonsterType;
import game.enums.Name;
import game.interfaces.Attackable;
import game.interfaces.Card;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode

public class MonsterCard implements Card, Attackable {
    private String name;
    private MonsterType type;
    private Element cardElement;
    private int damage;

    MonsterCard(MonsterType type, Element cardElement) {
        String prefix = Name.SIX.getName();
        this.name = prefix + " " + cardElement.getElementName() + "-" + type.getName(); //checked with Ctrl+Shif+P (beide sind Strings)
        this.type = type;
        this.cardElement = cardElement;
        this.damage = type.getMaxDamage();
    }


    MonsterCard(MonsterType type, Element cardElement, Name randomName) {
        String prefix = randomName.getName();
        this.name = prefix + " " + cardElement.getElementName()  + "-" + type.getName(); //checked with Ctrl+Shif+P (beide sind Strings)
        this.type = type;
        this.cardElement = cardElement;
        this.damage = type.getMaxDamage();
    }

    @Override
    public String printCardStats() {
        String stat = "Name: " + this.name + " - AP: " + this.damage;
        return stat;
    }

    @Override
    public void receiveDamage() {

    }

    @Override
    public boolean receiveAttack(Card attacker) {
        if(attacker instanceof MonsterCard) {
            if(((MonsterCard) attacker).damage > this.damage) {
                //check if this card is immune to attackers's MonsterType
                if( ((MonsterCard) attacker).type == this.type.getInmuneTo() ) {
                    return false;
                } else {
                    return true;
                }
            }
        } else if (attacker instanceof SpellCard) {
            if(this.type == MonsterType.KNIGHT && ((SpellCard) attacker).getCardElement() == Element.WATER){
                return true;
            } else {
                if( ((SpellCard) attacker).getCardElement().elementDefeats(this.cardElement))
                {
                    return true;
                }
                else
                {
                    if( ((SpellCard) attacker).getDamage() > this.damage)
                    {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        } else {
            throw new UnsupportedOperationException("Not a spell nor a monster card");
        }
        return false;
    }
}
