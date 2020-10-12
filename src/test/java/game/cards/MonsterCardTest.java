package game.cards;

import game.enums.Element;
import game.enums.MonsterType;
import game.interfaces.Card;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MonsterCardTest {

    Card elf = new MonsterCard(MonsterType.FireElf);
    Card dragon = new MonsterCard(MonsterType.Dragon);

    @Test
    @DisplayName("Elves are immune to dragons")

    void dragonsCantDamageElves() {
        //Assertions.assertFalse(defending.attack(attacking));
        Assertions.assertFalse(elf.attack(dragon));
    }

    Card water = new SpellCard(Element.Water);
    Card fire = new SpellCard(Element.Fire);

    @Test
    @DisplayName("Fire wins agains water")

    void fireCantDefeatesWater() {
        Assertions.assertTrue(water.attack(fire));
    }

    @Test
    @DisplayName("Water loses agains water")

    void waterCantDefeat() {
        Assertions.assertFalse(fire.attack(water));
    }

    //Assertions.assertEquals(false, );
}