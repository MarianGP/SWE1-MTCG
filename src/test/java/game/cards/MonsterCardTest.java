package game.cards;

import game.enums.Element;
import game.enums.MonsterType;
import game.enums.Name;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.rmi.UnexpectedException;

class MonsterCardTest {

    Card elf = new MonsterCard(MonsterType.FIRE_ELF,Element.FIRE);
    Card knight = new MonsterCard(MonsterType.KNIGHT, Element.WATER);
    Card kraken = new MonsterCard(MonsterType.KRAKEN, Element.FIRE);
    Card dragon = new MonsterCard(MonsterType.DRAGON, Element.FIRE, Name.FIVE);
    Card water = new SpellCard(Element.WATER);
    Card fire = new SpellCard(Element.FIRE);
    Card normal = new SpellCard(Element.NORMAL);

    @Test
    @DisplayName("DRAGONS miss attacks against ELVES")
    //Assertions.assertFalse(defending.attack(attacking));

    void dragonsCantDamageElves() {
        Assertions.assertFalse(elf.receiveAttack(dragon));
    }

    @Test
    @DisplayName("ELVES(98) can't defeat DRAGONS(120)")

    void elvesCantDefeatsDragons() {
        Assertions.assertFalse(dragon.receiveAttack(elf));
    }

    @Test
    @DisplayName("ELVES(98) can't defeat DRAGONS(120)")
    void dragonsCantDefeatEleves() {
        Assertions.assertFalse(elf.receiveAttack(dragon));
    }

    @Test
    @DisplayName("KRAKENS defeat DRAGONS(120)")

    void krakenDefeatsDragons() {
        Assertions.assertTrue(dragon.receiveAttack(kraken));
    }

    @Test
    @DisplayName("Normal-Spell defeats Water-Knight")

    void normalDefeatsKnight() {
        Assertions.assertTrue(knight.receiveAttack(normal));
    }

    @Test
    @DisplayName("Water-Spell defeats Water-Knight")

    void waterDefeatsKnight() {
        Assertions.assertTrue(knight.receiveAttack(water));
    }

    @Test
    @DisplayName("Fire-Spell(110) defeats Water-Knight(105)")

    void fireDefeatsKnight() {
        Assertions.assertTrue(knight.receiveAttack(fire));
    }

    @Test
    @DisplayName("Fire-Spell(110) cant defeat Dragon(120)")

    void fireCantDefeatsDragon() {
        Assertions.assertFalse(dragon.receiveAttack(fire));
    }

    @Test
    @DisplayName("Print Stats Dragon")
    void printStats() throws UnexpectedException {
        Assertions.assertEquals("Name: Southern Fire-Dragon - AP: 120",dragon.printCardStats());
    }



}