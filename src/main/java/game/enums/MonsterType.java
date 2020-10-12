package game.enums;

import lombok.Getter;

import java.util.Arrays;
@Getter

public enum MonsterType {
    Goblin("Goblin", 100, null),
    Dragon("Dragon", 120, null),
    Wizzard("Wizzard", 110, null),
    Ork("Org", 95, null),
    Knight("Knight", 105, null),
    Kraken("Kraken", 125, null),
    FireElf("Fire Elf", 98, Dragon);

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
}
