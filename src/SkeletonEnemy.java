// SkeletonEnemy class
public class SkeletonEnemy extends Enemy {
    public SkeletonEnemy(String name, String description, int health, Weapon weapon) {
        super(name, description, health, weapon);
    }

    @Override
    public void specialAbility() {
        // Skeleton's special ability: Reduce player's defense temporarily
        System.out.println(name + " casts a curse, reducing your defense!");
    }
}