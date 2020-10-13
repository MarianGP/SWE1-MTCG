package game.cards;

import game.enums.Element;
import game.enums.Name;
import game.interfaces.CardInterface;
import game.interfaces.Attackable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@EqualsAndHashCode

public class SpellCard implements CardInterface, Attackable {

    private String name;
    private Element cardElement;
    private int damage;
    private boolean locked;

    public SpellCard(Element cardElement) {
        String prefix = Name.ONE.getName();
        this.cardElement = cardElement;
        this.name = prefix + " " + cardElement.getElementName() + "-Spell";
        this.damage = cardElement.getMaxDamage();
        this.locked = false;
    }

    public SpellCard(Element cardElement, Name randomName) {
        String prefix = randomName.getName();
        this.cardElement = cardElement;
        this.name = prefix + " " + cardElement.getElementName() + "-Spell";
        this.damage = cardElement.getMaxDamage();
        this.locked = false;
    }

    @Override
    public String printCardStats() {
        String stat = "Name: " + this.name + " - AP: " + this.damage;
        System.out.println(stat);
        return stat;
    }

    @Override
    public void receiveDamage() {

    }

    @Override
    public boolean receiveAttack(CardInterface attacker) {
        if((attacker instanceof SpellCard))
        {
            /* ***elementDefeats()*** checks if the element of the card receiving the attack is defeated by the attacker's element*/
            return (((SpellCard) attacker).getCardElement().elementDefeats(this.cardElement));
        }
        else if((attacker instanceof MonsterCard))
        {
            if( ((MonsterCard) attacker).getCardElement().elementDefeats(this.cardElement))
            {
                return true;
            }
            else
            {
                if(((MonsterCard) attacker).getDamage() > this.damage)
                {
                    return true;
                } else {
                    return false;
                }
            }
        } else
            {
            throw new UnsupportedOperationException("Not a spell nor a monster card");
        }
    }

}
