// AngelEnemy class
public class AngelEnemy extends Enemy {
    public AngelEnemy(String name, String description, int health, Weapon weapon) {
        super(name, description, health, weapon);
    }

    @Override
    public void specialAbility() {
        // Angel's special ability: Buff allies or heal them
        System.out.println(name + " blesses allies, increasing their strength!");
    }
}