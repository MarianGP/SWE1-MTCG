package game_server.serializer;

import lombok.Getter;

@Getter
public class CustomeTradeSerializer {
    private String cardToTrade;
    private String type;
    private float minimumDamage;

}
