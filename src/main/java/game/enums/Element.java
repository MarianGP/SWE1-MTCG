package game.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Random;

@AllArgsConstructor
@Getter

public enum Element {
    FIRE("Fire", 110f, "NORMAL"),
    WATER("Water", 103f, "FIRE"),
    NORMAL("Normal", 95f, "WATER");

    private String elementName;
    private float maxDamage;
    private String defeats;

    //checks if the element of the card receiving the attack is defeated by the attacker's element
    public boolean elementDefeats(Element underAttackElement){
        if(this == WATER && underAttackElement == FIRE) {
            return true;
        } else if (this == NORMAL && underAttackElement == WATER) {
            return true;
        } else if (this == FIRE && underAttackElement == NORMAL) {
            return true;
        } else if (underAttackElement == null){
            throw new UnsupportedOperationException("Element not declared");
        } else {
            return false;
        }
    }

    private static final List<Element> listOfNames = List.of(values());
    private static final int SIZE = listOfNames.size();
    private static final Random RANDOM = new Random();

    public static Element randomElement()  {
        return listOfNames.get(RANDOM.nextInt(SIZE));
    }

    public static Element find(String name) {
        for (Element type : listOfNames) {
            if (type.getElementName().contains(name)) {
                return type;
            }
        }
        return NORMAL;
    }
}
