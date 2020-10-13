package game.interfaces;

public interface Attackable {
    public boolean receiveAttack(CardInterface attacker);
    public void receiveDamage();
}
