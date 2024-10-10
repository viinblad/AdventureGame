// DragonEnemy class
public class DragonEnemy extends Enemy {
    public DragonEnemy(String name, String description, int health, Weapon weapon) {
        super(name, description, health, weapon);
    }

    @Override
    public void specialAbility() {
        // Dragon's special ability: Breath attack that deals AoE damage
        System.out.println(name + " breathes fire, dealing area damage!");
    }
}