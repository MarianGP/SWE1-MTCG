package game.cards;

import game.enums.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString

public class MonsterCard extends Card {
    private String cid;
    private String name;
    private MonsterType type;
    private Element cardElement;
    private float damage;
    private boolean locked;

    public String getName() {
        return name;
    }

    MonsterCard(MonsterType type, Element cardElement) {
        String prefix = Name.SIX.getName();
        this.name = prefix + " " + cardElement.getElementName() + "-" + type.getName(); //checked with Ctrl+Shif+P (beide sind Strings)
        this.type = type;
        this.cardElement = cardElement;
        this.damage = type.getMaxDamage();
    }

    //for testing purpose
    public MonsterCard(MonsterType type, Element cardElement, Name randomName, float damage) {
        String prefix = randomName.getName();
        this.name = prefix + " " + cardElement.getElementName() + "-" + type.getName(); //checked with Ctrl+Shif+P (beide sind Strings)
        this.type = type;
        this.cardElement = cardElement;
        this.damage = damage;
    }

    public MonsterCard(String cid, MonsterType type, Element cardElement, String name, float damage) {
        this.cid = cid;
        this.name = name;
        this.type = type;
        this.cardElement = cardElement;
        this.damage = damage;
    }

    public String getCardStats() {
        String stat =   "CardId: " + this.cid +
                        " - Name: " + this.name +
                        " - AP: " + this.damage +
                        " - Element: " + this.cardElement.getElementName() +
                        " - Type : " + this.type.getName() + "\n";
        System.out.print(stat);
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

    public boolean compareDamage(float attackerDP){
        return attackerDP > this.damage;
    }

    public boolean result(GeneralEffectiveness effect, float attackerDP) {
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
