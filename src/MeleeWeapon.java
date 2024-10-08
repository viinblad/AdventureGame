public class MeleeWeapon extends Weapon {

    public MeleeWeapon(String shortName, String longName, String description, int damage) {
        super(shortName, longName, description, damage);
    }

    // Abstract method implementation to check if the weapon can be used (always true for melee)
    @Override
    public boolean canUse() {
        return true; // Melee weapons can always be used
    }

    // Abstract method implementation to simulate using the weapon
    @Override
    public void useWeapon() {
        System.out.println("You swing the " + getLongName() + " dealing " + getDamage() + " damage.");
    }

    @Override
    public String toString() {
        return super.toString(); // Call the toString() method from the Weapon class
    }
}
