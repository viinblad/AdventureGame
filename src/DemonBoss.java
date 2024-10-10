// DemonBoss class
public class DemonBoss extends Enemy {
    public DemonBoss(String name, String description, int health, Weapon weapon) {
        super(name, description, health, weapon);
    }

    @Override
    public void specialAbility() {
        // Demon Boss's special ability: Summon minions to aid in battle
        System.out.println(name + " summons lesser demons to fight for it!");
    }
}