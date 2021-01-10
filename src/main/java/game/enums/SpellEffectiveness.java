package game.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor


public enum SpellEffectiveness  {
    ONE(    null, GeneralEffectiveness.MISSES,    MonsterType.KRAKEN), //immune to any spell
    TWO(   Element.WATER,  GeneralEffectiveness.DEFEATS,  MonsterType.KNIGHT);

    public static final List<SpellEffectiveness> spellEffectivenessList = List.of(values());

    private Element spellElement;
    private GeneralEffectiveness effectiveness;
    private MonsterType monsterDefending;

}
