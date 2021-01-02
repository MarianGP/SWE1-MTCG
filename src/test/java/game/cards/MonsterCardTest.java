package game.cards;

import game.enums.Element;
import game.enums.GeneralEffectiveness;
import game.enums.MonsterType;
import game.enums.Name;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.rmi.UnexpectedException;

class MonsterCardTest {

    @Mock
    Card fire_elf = new MonsterCard(MonsterType.ELF,Element.FIRE);
    Card water_elf = new MonsterCard(MonsterType.ELF,Element.WATER);
    Card knight = new MonsterCard(MonsterType.KNIGHT, Element.WATER);
    Card kraken = new MonsterCard(MonsterType.KRAKEN, Element.FIRE);
    Card dragon = new MonsterCard(MonsterType.DRAGON, Element.FIRE, Name.FIVE,120.0f);
    Card wizzard = new MonsterCard(MonsterType.WIZZARD, Element.FIRE, Name.ONE, 100.0f);
    Card ork = new MonsterCard(MonsterType.ORK, Element.FIRE, Name.ONE, 120.0f);
    Card goblin = new MonsterCard(MonsterType.GOBLIN, Element.FIRE, Name.ONE, 400.0f);

    Card water = new SpellCard(Element.WATER);
    Card fire = new SpellCard(Element.FIRE);
    Card normal = new SpellCard(Element.NORMAL);
    Card water2 = new SpellCard(Element.WATER, Name.FIVE, 200.0f);
    Card fire2 = new SpellCard(Element.FIRE, Name.FIVE, 200.0f);
    Card normal2 = new SpellCard(Element.NORMAL, Name.FIVE, 200.0f);

    @Test
    @DisplayName("checkEffectiveness Monster vs Monster")
    void monsterMonster () {
        Assertions.assertEquals( ((MonsterCard) wizzard).checkEffectiveness((MonsterCard) ork), GeneralEffectiveness.MISSES);
        Assertions.assertEquals( ((MonsterCard) dragon).checkEffectiveness((MonsterCard) goblin), GeneralEffectiveness.MISSES);
        Assertions.assertEquals( ((MonsterCard) water_elf).checkEffectiveness((MonsterCard) dragon), GeneralEffectiveness.ATTACKS);
        Assertions.assertEquals( ((MonsterCard) fire_elf).checkEffectiveness((MonsterCard) dragon), GeneralEffectiveness.MISSES);
    }

    @Test
    @DisplayName("compareDamage")
    void compareDamage () {
        Assertions.assertFalse( ( (MonsterCard) wizzard).compareDamage( ( (MonsterCard) wizzard).getDamage() ) );
        Assertions.assertTrue( ( (MonsterCard) wizzard).compareDamage( ( (MonsterCard) ork).getDamage() ) );
    }

    @Test
    @DisplayName("result boolean")
    void result () {
        Assertions.assertFalse( ( (MonsterCard) wizzard).result(GeneralEffectiveness.MISSES, 120f) );
        Assertions.assertTrue( ( (MonsterCard) wizzard).result(GeneralEffectiveness.DEFEATES, 110f) );
    }


    @Test
    @DisplayName("Spell vs Monster: Kraken is immune to Spells")
    void KrakenImmuneToSpell() {
        Assertions.assertFalse(kraken.receiveAttack(fire2));
        Assertions.assertFalse(kraken.receiveAttack(water2));
        Assertions.assertFalse(kraken.receiveAttack(normal2));
    }

    @Test
    @DisplayName("DRAGONS miss attacks against ELVES")
    //Assertions.assertFalse(defending.attack(attacking));

    void dragonsCantDamageElves() {
        Assertions.assertFalse(fire_elf.receiveAttack(dragon));
    }

    @Test
    @DisplayName("ELVES(98) can't defeat DRAGONS(120)")

    void elvesCantDefeatsDragons() {
        Assertions.assertFalse(dragon.receiveAttack(fire_elf));
    }

    @Test
    @DisplayName("ELVES(98) can't defeat DRAGONS(120)")
    void dragonsCantDefeatEleves() {
        Assertions.assertFalse(fire_elf.receiveAttack(dragon));
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
        Assertions.assertEquals("Card: Southern Fire-Dragon - AP: 120.0",dragon.getCardStats());
    }





}