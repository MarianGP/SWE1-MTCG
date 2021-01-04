package game.battle;

import game.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BattleLog {
    private User p1;
    private User p2;
    private User winner;
    private int rounds;

    public String getResultSummary() {
        String result = this.winner != null ? "Winner: " + this.winner.getUsername() : ("Nobody won the game");
        return  result + "\n" +
                "Rounds: " + rounds + "\n\n" +
                this.p1.getUserStats() +
                this.p2.getUserStats();
    }
}
