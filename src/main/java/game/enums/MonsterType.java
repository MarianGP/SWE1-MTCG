package game.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Random;


@AllArgsConstructor
@Getter

public enum MonsterType {
    GOBLIN  ("Goblin", 100.0f),
    DRAGON  ("Dragon", 120.0f),
    WIZZARD ("Wizzard", 110.0f),
    ORK     ("Org", 95.0f),
    KNIGHT  ("Knight", 105.0f),
    KRAKEN  ("Kraken", 125.0f),
    ELF     ("Elf", 98.0f);

    private static final List<MonsterType> listOfNames = List.of(values());
    private static final int SIZE = listOfNames.size();
    private static final Random RANDOM = new Random();

    private final String name;
    private final float maxDamage;

    public static MonsterType randomMonsterType() {
        return listOfNames.get(RANDOM.nextInt(SIZE));
    }

    public void randomMonster() {
        Arrays.asList(MonsterType.values())
                .forEach(m -> System.out.println(m.name));
    }

    public static MonsterType find(String name) {
        for (MonsterType type : listOfNames) {
            if (type.getName().contains(name)) {
                return type;
            }
        }
        return null;
    }
}
