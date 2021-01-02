package game_server.serializer;

import lombok.Getter;

@Getter
public class CustomUserSerializer {
    private String password;
    private String bio;
    private String image;
}
