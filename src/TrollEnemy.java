// TrollEnemy class
public class TrollEnemy extends Enemy {
    public TrollEnemy(String name, String description, int health, Weapon weapon) {
        super(name, description, health, weapon);
    }

    @Override
    public void specialAbility() {
        // Troll's special ability: Regenerate health
        health += 5; // Regain 5 health
        if (health > maxHealth) {
            health = maxHealth; // Ensure health does not exceed maxHealth
        }
        System.out.println(name + " regenerates health! Current health: " + health);
    }
}