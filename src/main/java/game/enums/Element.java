package game.enums;

import lombok.Getter;

@Getter

public enum Element {
    FIRE("Fire", 110),
    WATER("Water", 103),
    NORMAL("Normal", 95);

    private String elementName;
    private int maxDamage;

    Element(String name, int maxDamage) {
        this.elementName = name;
        this.maxDamage = maxDamage;
    }

    //checks if the element of the card receiving the attack is defeated by the attacker's element
    public boolean elementDefeats(Element underAttackElement){
        if(this == WATER && underAttackElement == FIRE) {
            return true;
        } else if (this == NORMAL && underAttackElement == WATER) {
            return true;
        } else if (this == FIRE && underAttackElement == NORMAL) {
            return true;
        } else if (underAttackElement == null || this == null){
            throw new UnsupportedOperationException("Element not declared");
        } else {
            return false;
        }
    }
}
