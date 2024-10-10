// WerewolfEnemy class
public class WerewolfEnemy extends Enemy {
    public WerewolfEnemy(String name, String description, int health, Weapon weapon) {
        super(name, description, health, weapon);
    }

    @Override
    public void specialAbility() {
        // Werewolf's special ability: Increase attack damage temporarily
        System.out.println(name + " howls, increasing its attack damage!");
    }
}
