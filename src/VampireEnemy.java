// VampireEnemy class
public class VampireEnemy extends Enemy {
    public VampireEnemy(String name, String description, int health, Weapon weapon) {
        super(name, description, health, weapon);
    }

    @Override
    public void specialAbility() {
        // Vampire's special ability: Heal by a portion of the damage dealt
        System.out.println(name + " drains your life force!");
        health += 3; // Heal for 3 health when attacking
    }
}