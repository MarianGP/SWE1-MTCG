package game.enums;

import lombok.Getter;

@Getter

public enum Element {
    Fire("Fire", 110),
    Water("Water", 100),
    Normal("Normal", 95);

    private String elementName;
    private int maxDamage;

    Element(String elementName, int maxDamage) {
        this.elementName = elementName;
        this.maxDamage = maxDamage;
    }

    public boolean elementDefeats(Element el){
        if(this == Water && el == Fire) {
            return true;
        } else if (this == Normal && el == Water) {
            return true;
        } else if (this == Fire && el == Normal) {
            return true;
        } else {
            return false;
        }
    }
}
