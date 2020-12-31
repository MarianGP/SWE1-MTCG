package game.enums;

import lombok.Getter;

import java.util.List;
import java.util.Random;

@Getter

public enum Name {
    ONE("Ultimate"),
    TWO("Mega"),
    THREE("Supreme"),
    FOUR("Giant"),
    FIVE("Southern"),
    SIX("Northern");

    String name;

    Name(String enhancer) {
        this.name = enhancer;
    }

    private static final List<Name> listOfNames = List.of(values());
    private static final int SIZE = listOfNames.size();
    private static final Random RANDOM = new Random();

    public static Name randomName()  {
        return listOfNames.get(RANDOM.nextInt(SIZE));
    }

    public static Name find(String name) {
        for (Name type : listOfNames) {
            if (type.getName().contains(name)) {
                return type;
            }
        }
        return null;
    }
}
