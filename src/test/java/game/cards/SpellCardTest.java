package game.cards;

import game.enums.Element;
import game.enums.GeneralEffectiveness;
import game.enums.MonsterType;
import game.enums.Name;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;


class SpellCardTest {

    @Mock
    Card elf = new MonsterCard(MonsterType.ELF,Element.FIRE);
    Card knight = new MonsterCard(MonsterType.KNIGHT, Element.FIRE);
    Card kraken = new MonsterCard(MonsterType.KRAKEN, Element.WATER);
    Card dragon = new MonsterCard(MonsterType.DRAGON, Element.WATER);
    Card water = new SpellCard(Element.WATER, Name.FIVE);
    Card fire = new SpellCard(Element.FIRE);
    Card normal = new SpellCard(Element.NORMAL);
    Card water2 = new SpellCard(Element.WATER, Name.FIVE, 50);
    Card fire2 = new SpellCard(Element.FIRE, Name.FIVE, 200);
    Card normal2 = new SpellCard(Element.NORMAL, Name.FIVE, 200);

    @Test
    @DisplayName("check Spell Effectiveness")
    void checkMonsterEffectiveness() {
        Assertions.assertEquals(((SpellCard) water2).checkEffectiveness((MonsterCard) kraken), GeneralEffectiveness.MISSES);
        Assertions.assertEquals(((SpellCard) fire2).checkEffectiveness((MonsterCard) kraken), GeneralEffectiveness.MISSES);
        Assertions.assertEquals(((SpellCard) normal2).checkEffectiveness((MonsterCard) kraken), GeneralEffectiveness.MISSES);
        Assertions.assertEquals(((SpellCard) water).checkEffectiveness((MonsterCard) knight), GeneralEffectiveness.DEFEATES);
        Assertions.assertEquals(((SpellCard) fire).checkEffectiveness((MonsterCard) knight), GeneralEffectiveness.ATTACKS);
    }

    @Test
    @DisplayName("compare damage")
    void compareDP() {
        Assertions.assertFalse( ((SpellCard) water).compareDamage( ((SpellCard) water).getDamage() ) );
    }

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
    @DisplayName("Monster vs Spell: Water-Knight(105) cant defeat Fire-Spell(120.0)")
    void waterKnightCantDefeatsFireSpell() {
        Assertions.assertFalse(fire.receiveAttack(knight));
    }

    @Test
    @DisplayName("Monster vs Spell: Water-Kraken(125) defeats Fire-Spell(120.0)")
    void KrakenDefeatsFireSpell() {
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
        Assertions.assertEquals("Card: Ultimate Fire-Spell - AP: 110.0", fire.printCardStats());
    }

    @Test
    @DisplayName("Print Stats Water")
    void printStatsWater() {
        Assertions.assertEquals("Card: Southern Water-Spell - AP: 103.0", water.printCardStats());

    }
}