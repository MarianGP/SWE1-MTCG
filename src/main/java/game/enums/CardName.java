package game.enums;

import lombok.Getter;

import java.util.List;
import java.util.Random;

@Getter

public enum CardName {
    ONE("Ultimate"),
    TWO("Mega"),
    THREE("Supreme"),
    FOUR("Giant"),
    FIVE("Southern"),
    SIX("Northern");

    String name;

    CardName(String enhancer) {
        this.name = enhancer;
    }

    private static final List<CardName> listOfNames = List.of(values());
    private static final int SIZE = listOfNames.size();
    private static final Random RANDOM = new Random();

    public static CardName randomName()  {
        return listOfNames.get(RANDOM.nextInt(SIZE));
    }

    public static CardName find(String name) {
        for (CardName type : listOfNames) {
            if (type.getName().contains(name)) {
                return type;
            }
        }
        return null;
    }
}
