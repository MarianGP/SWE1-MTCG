package game.enums;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MonsterTypeTest {

    @Test
    void testFind() {
        Assertions.assertEquals(MonsterType.DRAGON, MonsterType.find("Dragon"));
    }
}