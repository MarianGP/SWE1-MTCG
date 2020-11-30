package game.user;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Credentials {
    private final String username;
    private final String password;
}
