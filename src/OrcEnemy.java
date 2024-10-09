public class OrcEnemy extends Enemy {
    public OrcEnemy(String name, String description, int health, Weapon weapon) {
        super(name, "A fearsome orc", health, weapon);
    }

    @Override
    public void specialAbility() {
        System.out.println(name + " lets out a battle roar, intimidating the player!");
        // Add special ability logic here.
    }
}
