package game.enums;

import game.cards.Card;
import game.cards.MonsterCard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

class MonsterTypeTest {
    @Mock
    Card goblin = new MonsterCard(MonsterType.GOBLIN, Element.FIRE, Name.ONE, 400.0f);


    @Test
    void testFind() {
        Assertions.assertEquals(MonsterType.DRAGON, MonsterType.find("Dragon"));
    }

    @Test
    void returnStringType() {
        System.out.println(goblin.getType().getName());
    }
}