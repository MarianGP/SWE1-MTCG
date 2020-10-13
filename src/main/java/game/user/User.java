package game.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class User {
    private String username;
    private String password;
    private String token;
    private int coins;
    private int ELO;

    User(String username, String password){
        this.username = username;
        this.username = password;
        this.token = username + "-mtcgToken";
        this.coins = 20;
        this.ELO = 100;
    }
}
