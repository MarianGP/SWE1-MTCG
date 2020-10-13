package game.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Getter

public enum MonsterType {
    GOBLIN("Goblin", 100, null),
    DRAGON("Dragon", 120, null),
    WIZZARD("Wizzard", 110, null),
    ORK("Org", 95, null),
    KNIGHT("Knight", 105, null),
    KRAKEN("Kraken", 125, null),
    FIRE_ELF("Elf", 98, DRAGON);

    private String name;
    private int maxDamage;
    private MonsterType inmuneTo;

    MonsterType(String name, int maxDamage, MonsterType monster){
        this.name = name;
        this.maxDamage = maxDamage;
        this.inmuneTo = monster;
    }

    //public List<MonsterType> monstersList = new ArrayList<>();

    public void randomMonster() {
        Arrays.asList(MonsterType.values())
                .forEach(m -> System.out.println(m.name));
    }


    private static final List<MonsterType> listOfNames = List.of(values());
    private static final int SIZE = listOfNames.size();
    private static final Random RANDOM = new Random();

    public static MonsterType randomMonsterType()  {
        return listOfNames.get(RANDOM.nextInt(SIZE));
    }
}
