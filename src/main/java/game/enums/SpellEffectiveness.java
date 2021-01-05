package game.enums;

import game.cards.MonsterCard;
import game.cards.SpellCard;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor

<<<<<<< HEAD
public enum SpellEffectiveness extends SpellCard {
=======
public enum SpellEffectiveness  {
>>>>>>> integration-game-server
    ONE(    null, GeneralEffectiveness.MISSES,    MonsterType.KRAKEN), //immune to any spell
    TWO(   Element.WATER,  GeneralEffectiveness.DEFEATES,  MonsterType.KNIGHT);

    public static final List<SpellEffectiveness> spellEffectivenessList = List.of(values());

    private Element spellElement;
    private GeneralEffectiveness effectiveness;
    private MonsterType monsterDefending;


}
