package game.enums;

import game.user.User;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)

class MonsterTypeTest {
    @Mock
    User user1 = new User("Hello", "10");

//    @DisplayName("Try to get MonsterElement out of String InmuneTo")
//
//    @Test
//    public void getImmuneMonsterType() {
//        Assertions.assertEquals(MonsterType.DRAGON, MonsterType.ELF.getImmuneMonsterType());
//    }
}