package game.cards;

import game.decks.CardStack;
import game.enums.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString

public class MonsterCard extends Card {
    private String name;
    private MonsterType type;
    private Element cardElement;
    private int damage;
    private boolean locked;

    MonsterCard(MonsterType type, Element cardElement) {
        String prefix = Name.SIX.getName();
        this.name = prefix + " " + cardElement.getElementName() + "-" + type.getName(); //checked with Ctrl+Shif+P (beide sind Strings)
        this.type = type;
        this.cardElement = cardElement;
        this.damage = type.getMaxDamage();
    }

    public MonsterCard(MonsterType type, Element cardElement, Name randomName) {
        String prefix = randomName.getName();
        this.name = prefix + " " + cardElement.getElementName() + "-" + type.getName(); //checked with Ctrl+Shif+P (beide sind Strings)
        this.type = type;
        this.cardElement = cardElement;
        this.damage = type.getMaxDamage();
    }

    //for testing purpose
    public MonsterCard(MonsterType type, Element cardElement, Name randomName, int damage) {
        String prefix = randomName.getName();
        this.name = prefix + " " + cardElement.getElementName() + "-" + type.getName(); //checked with Ctrl+Shif+P (beide sind Strings)
        this.type = type;
        this.cardElement = cardElement;
        this.damage = damage;
    }

    public String printCardStats() {
        String stat = "Card: " + this.name + " - AP: " + this.damage;
        System.out.println(stat);
        return stat;
    }

    public GeneralEffectiveness checkEffectiveness(MonsterCard attacker) {
        GeneralEffectiveness effectiveness;
        for (int i = 0; i < MonsterEffectiveness.MonsterEffectivenessList.size(); i++) {
            effectiveness = MonsterEffectiveness.MonsterEffectivenessList.get(i).getEffectiveness();
            if (attacker.getType() == MonsterEffectiveness.MonsterEffectivenessList.get(i).getAttacker() &&
                    this.type == MonsterEffectiveness.MonsterEffectivenessList.get(i).getDefender())
            {
                if (MonsterEffectiveness.MonsterEffectivenessList.get(i).getDefendersElement() == null) {
                    return effectiveness;
                }
                else if(this.cardElement == MonsterEffectiveness.MonsterEffectivenessList.get(i).getDefendersElement() ){
                    return effectiveness;
                }
            }
        }
        return GeneralEffectiveness.ATTACKS;
    }

    public boolean compareDamage(int attackerDP){
        if (attackerDP > this.damage) {
            return true;
        } else {
            return false;
        }
    }

    public boolean result(GeneralEffectiveness effect, int attackerDP) {
        if (effect == GeneralEffectiveness.ATTACKS) {
            return compareDamage(attackerDP);
        } else if (effect == GeneralEffectiveness.MISSES) {
            return false;
        } else if (effect == GeneralEffectiveness.DEFEATES) {
            return true;
        }
        throw new UnsupportedOperationException("None allowed Effectiveness Value");
    }

    public boolean receiveAttack(Card attacker) {
        if(attacker instanceof MonsterCard) {
            GeneralEffectiveness effect = checkEffectiveness((MonsterCard) attacker);
            return result(effect, ((MonsterCard) attacker).getDamage());
        } else if (attacker instanceof SpellCard) {
            GeneralEffectiveness effect = ((SpellCard) attacker).checkEffectiveness(this);
            if(effect == GeneralEffectiveness.ATTACKS && ((SpellCard) attacker).getCardElement().elementDefeats(this.cardElement)) {
                return true;
            } else {
                return result(effect, ((SpellCard) attacker).getDamage());
            }
        } else {
            throw new UnsupportedOperationException("Not a spell nor a monster card or null");
        }
    }
}
