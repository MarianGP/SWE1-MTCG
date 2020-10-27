package game.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Random;


@AllArgsConstructor
@Getter

public enum MonsterType {
    GOBLIN  ("Goblin", 100),
    DRAGON  ("Dragon", 120),
    WIZZARD ("Wizzard", 110),
    ORK     ("Org", 95),
    KNIGHT  ("Knight", 105),
    KRAKEN  ("Kraken", 125),
    ELF     ("Elf", 98);

    private static final List<MonsterType> listOfNames = List.of(values());
    private static final int SIZE = listOfNames.size();
    private static final Random RANDOM = new Random();

    private String name;
    private int maxDamage;

    public static MonsterType randomMonsterType() {
        return listOfNames.get(RANDOM.nextInt(SIZE));
    }

    public void randomMonster() {
        Arrays.asList(MonsterType.values())
                .forEach(m -> System.out.println(m.name));
    }
}
