package game.cards;

import game.enums.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString

public class SpellCard extends Card {

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

    //for testing purpose
    public SpellCard(Element cardElement, Name aName, int damage) {
        String prefix = aName.getName();
        this.cardElement = cardElement;
        this.name = prefix + " " + cardElement.getElementName() + "-Spell";
        this.damage = damage;
        this.locked = false;
    }

    public String printCardStats() {
        String stat = "Card: " + this.name + " - AP: " + this.damage;
        System.out.println(stat);
        return stat;
    }

    public GeneralEffectiveness checkEffectiveness(MonsterCard defender) {
        for (int i = 0; i < SpellEffectiveness.spellEffectivenessList.size(); i++) {
            if (defender.getType() == SpellEffectiveness.spellEffectivenessList.get(i).getMonsterDefending() &&
                    (
                            this.cardElement == SpellEffectiveness.spellEffectivenessList.get(i).getSpellElement()
                            || SpellEffectiveness.spellEffectivenessList.get(i).getSpellElement() == null)
                    )
            {
                return SpellEffectiveness.spellEffectivenessList.get(i).getEffectiveness();
            }
        }
        return GeneralEffectiveness.ATTACKS;
    }

    //not implemented spell vs spell (only element is compared)
    public boolean compareDamage(int attackerDP){
        if (attackerDP > this.damage) {
            return true;
        } else {
            return false;
        }
    }

    public boolean receiveAttack(Card attacker) {
        if((attacker instanceof SpellCard)) {
            return (((SpellCard) attacker).getCardElement().elementDefeats(this.cardElement));
        }
        else if((attacker instanceof MonsterCard))
        {
            if( ((MonsterCard) attacker).getCardElement().elementDefeats(this.cardElement)) {
                return true;
            } else {
                return this.compareDamage(((MonsterCard) attacker).getDamage());
            }
        } else {
            throw new UnsupportedOperationException("Not a spell nor a monster card");
        }
    }

}
