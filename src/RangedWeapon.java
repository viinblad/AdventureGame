public class RangedWeapon extends Weapon {
    private int ammo; // Ammunition available for the weapon

    public RangedWeapon(String shortName, String longName, String description, int damage, int ammo) {
        super(shortName, longName, description, damage);
        this.ammo = ammo; // Initialize ammo
    }

    // Get the current ammo count
    public int getAmmo() {
        return ammo;
    }

    // Abstract method implementation to check if the weapon can be used (needs ammo)
    @Override
    public boolean canUse() {
        return ammo > 0; // Can only use if ammo is available
    }

    // Abstract method implementation to simulate using the weapon
    @Override
    public void useWeapon() {
        if (canUse()) {
            ammo--; // Decrease ammo after use
            System.out.println("You shot the " + getLongName() + ", dealing " + getDamage() + " damage. Ammo remaining: " + ammo);
        } else {
            System.out.println("Out of ammo! You cannot use the " + getLongName());
        }
    }

    // Method to reload ammo
    public void reload(int amount) {
        ammo += amount; // Add ammo
        System.out.println("Reloaded " + amount + " ammo into the " + getLongName() + ". Total ammo: " + ammo);
    }

    @Override
    public String toString() {
        return super.toString() + " (Ammo: " + ammo + ")";
    }
}
