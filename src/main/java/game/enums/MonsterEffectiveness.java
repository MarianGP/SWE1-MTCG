package game.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor

public enum MonsterEffectiveness  {
    ONE(    MonsterType.ORK,    GeneralEffectiveness.MISSES, MonsterType.WIZZARD, null),
    TWO(    MonsterType.GOBLIN, GeneralEffectiveness.MISSES, MonsterType.DRAGON, null),
    THREE(  MonsterType.DRAGON, GeneralEffectiveness.MISSES, MonsterType.ELF, Element.FIRE);

    public static final List<MonsterEffectiveness> MonsterEffectivenessList = List.of(values());

    private MonsterType attacker;
    private GeneralEffectiveness effectiveness;
    private MonsterType defender;
    private Element defendersElement;


}

