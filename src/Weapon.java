public abstract class Weapon extends Item {
    protected int damage; // Damage dealt by the weapon

    public Weapon(String shortName, String longName, String description, int damage) {
        super(shortName, longName, description);
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }

    // Setter method for shortName
    public void setShortName(String shortName) {
        this.shortName = shortName; // Assuming shortName is a protected or private member in Item
    }

    // Abstract method to check if the weapon can be used (overridden by subclasses)
    public abstract boolean canUse();

    // Abstract method to simulate weapon use (overridden by subclasses)
    public abstract void useWeapon();

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // Reference equality
        if (!(obj instanceof Weapon)) return false; // Check if the other object is a Weapon
        Weapon other = (Weapon) obj; // Cast to Weapon
        boolean isEqual = this.getShortName().equals(other.getShortName()); // Compare based on short name
        System.out.println("Comparing " + this.getShortName() + " with " + other.getShortName() + ": " + isEqual); // Debug output
        return isEqual;
    }

    @Override
    public int hashCode() {
        return this.getShortName().hashCode(); // Use short name for hashing
    }
}
