public abstract class Enemy {
    protected String name;
    protected String description;
    protected int health;
    protected int maxHealth; // Maximum health of the enemy
    protected Weapon weapon;

    public Enemy(String name, String description, int health, Weapon weapon) {
        this.name = name;
        this.description = description;
        this.health = health;
        this.maxHealth = health; // Set maxHealth to the initial health value
        this.weapon = weapon;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() { // Getter for maxHealth
        return maxHealth;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    // Method to check if the enemy is dead
    public boolean isDead() {
        return health <= 0;
    }

    // Method to take damage from the player's attack
    public void takeDamage(int damage) {
        health -= damage; // Reduce health by the damage taken
    }

    // Method to drop the enemy's weapon into the room
    public void dropWeapon(Room room) {
        if (weapon != null) {
            room.addItem(weapon); // Add the weapon to the room's inventory
            weapon = null; // Clear the weapon reference
        }
    }

    // Method to attack the player, returns the damage dealt
    public int attack(Player player) {
        int damage = 0;
        if (weapon != null && weapon.canUse()) {
            damage = weapon.getDamage();
            player.takeDamage(damage); // Call player's takeDamage method
            // Optional: handle UI messages for attacking
        }
        return damage;  // Return the amount of damage dealt to the player
    }

    // Abstract method to define specific behavior for enemies
    public abstract void specialAbility();
}
