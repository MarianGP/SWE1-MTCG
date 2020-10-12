package game.cards;

import game.enums.Element;
import game.enums.MonsterType;
import game.interfaces.Card;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class MonsterCard implements Card {

    private MonsterType type;
    private int damage;

    MonsterCard(MonsterType aType) {
        this.type = aType;
        this.damage = type.getMaxDamage();
    }

    @Override
    public void printCardStats() {
    }

    @Override
    public boolean attack(Card attacker) {
        if(attacker instanceof MonsterCard) {
            if(((MonsterCard) attacker).damage > this.damage) {
                if((((MonsterCard) attacker).type == this.type.getInmuneTo())) {
                    return false;
                } else {
                    return true;
                }
            }
        } else if (attacker instanceof SpellCard) {
            if(this.type == MonsterType.Knight && ((SpellCard) attacker).getCardElement() == Element.Water){
                return true;
            } else {
                return false;
            }
        } else {
            throw new UnsupportedOperationException("Not a spell nor a monster card");
        }
        return false;
    }
}
