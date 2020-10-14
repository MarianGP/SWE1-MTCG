package game.cards;

import game.enums.Element;
import game.enums.MonsterType;
import game.enums.Name;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SpellCardTest {

    Card elf = new MonsterCard(MonsterType.FIRE_ELF,Element.NORMAL);
    Card knight = new MonsterCard(MonsterType.KNIGHT, Element.FIRE);
    Card kraken = new MonsterCard(MonsterType.KRAKEN, Element.WATER);
    Card dragon = new MonsterCard(MonsterType.DRAGON, Element.WATER);
    Card water = new SpellCard(Element.WATER, Name.FIVE);
    Card fire = new SpellCard(Element.FIRE);
    Card normal = new SpellCard(Element.NORMAL);

    @Test
    @DisplayName("Spells Battle: Water defeats Fire")

    void fireWinsAgainstWater() {
        Assertions.assertTrue(fire.receiveAttack(water));
    }

    @Test
    @DisplayName("Spells Battle: Fire-Spell does not affect Water-Spell")
    void FireCantDefeatWater() {
        Assertions.assertFalse(water.receiveAttack(fire));
    }

    @Test
    @DisplayName("Spells Battle: Normal-Spell defeats Water-Spell")
    void NormalDefeatsWater() {
        Assertions.assertTrue(water.receiveAttack(normal));
    }

    @Test
    @DisplayName("Spells Battle: Water does not affect Normal")
    void FireDefeatsWater() {
        Assertions.assertFalse(normal.receiveAttack(water));
    }

    @Test
    @DisplayName("Monster vs Spell: Water-Knight(105) cant defeats Fire-Spell(120)")
    void waterKnightCantDefeatsFireSpell() {
        Assertions.assertFalse(fire.receiveAttack(knight));
    }

    @Test
    @DisplayName("Monster vs Spell: Water-Kraken(125) defeats Fire-Spell(120)")
    void waterKrakenDefeatsFireSpell() {
        Assertions.assertTrue(fire.receiveAttack(kraken));
    }

    @Test
    @DisplayName("Fire-Dragon defeats Fire-Spell (Dragons has higher damage")
    void waterDragonDefeatsFireSpell() {
        Assertions.assertTrue(fire.receiveAttack(dragon));
    }

    @Test
    @DisplayName("Print Stats Fire")
    void printStatsFire() {
        Assertions.assertEquals("Name: Ultimate Fire-Spell - AP: 110", fire.printCardStats());
    }

    @Test
    @DisplayName("Print Stats Water")
    void printStatsWater() {
        Assertions.assertEquals("Name: Southern Water-Spell - AP: 103", water.printCardStats());

    }
}