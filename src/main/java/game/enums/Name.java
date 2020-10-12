package game.enums;

import lombok.Getter;

@Getter

public enum Name {
    ONE("Ultimate"),
    TWO("Mega"),
    THREE("Supreme"),
    FOUR("Giant"),
    FIVE("Southern"),
    SIX("Norden");

    String namePrefix;

    Name(String enhancer) {
        this.namePrefix = enhancer;
    }
}
