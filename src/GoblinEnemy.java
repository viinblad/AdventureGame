// GoblinEnemy class
public class GoblinEnemy extends Enemy {
    public GoblinEnemy(String name, String description, int health, Weapon weapon) {
        super(name, description, health, weapon);
    }

    @Override
    public void specialAbility() {
        // Goblin's special ability: Steal a random item from the player
        System.out.println(name + " sneaks up and attempts to steal an item!");
    }
}