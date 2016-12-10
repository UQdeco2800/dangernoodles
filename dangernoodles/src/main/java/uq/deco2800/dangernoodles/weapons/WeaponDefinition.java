package uq.deco2800.dangernoodles.weapons;

public class WeaponDefinition {
    private int id;
    private String name;
    private String category;
    private String images;
    private int height;
    private int width;
    private boolean powered;
    private boolean makesProjectiles;
    private int numOfProjectiles;
    private ProjectileDefinition projectileType;

    /**
     * Constructor for WeaponDefinition Creates a weapon that does not create a
     * projectile and is not powered
     * 
     * @param id
     *            ID of WeaponDefinition
     * @param name
     *            The name of the weapon
     * @param category
     *            The type of weapon (e.g. Projectile)
     * @param images
     *            A String of the file path to a spritesheet
     * @param height
     *            The HEIGHT of the weapon sprite
     * @param width
     *            The WIDTH of the weapon sprite
     */
    public WeaponDefinition(int id, String name, String category,
            String images, int width, int height) {
        // use the full constructor
        this(id, name, category, images, width, height, false, false, 0, null);
    }

    /**
     * Constructor for WeaponDefinition Creates a weapon that does not create a
     * projectile
     * 
     * @param id
     *            ID of WeaponDefinition
     * @param name
     *            The name of the weapon
     * @param category
     *            The type of weapon (e.g. Projectile)
     * @param images
     *            A String of the file path to a spritesheet
     * @param height
     *            The HEIGHT of the weapon sprite
     * @param width
     *            The WIDTH of the weapon sprite
     */
    public WeaponDefinition(int id, String name, String category,
            String images, int width, int height,
            boolean powered) {
        // use the full constructor
        this(id, name, category, images, width, height, powered, false, 0,
                null);
    }

    /**
     * Constructor for WeaponDefinition Creates a weapon that may create a
     * projectile
     * 
     * @param id
     *            ID of WeaponDefinition
     * @param name
     *            The name of the weapon
     * @param category
     *            The type of weapon (e.g. Projectile)
     * @param images
     *            A String of the file path to a spritesheet
     * @param height
     *            The HEIGHT of the weapon sprite
     * @param width
     *            The WIDTH of the weapon sprite
     * @param powered
     *            Whether or not the firing of the weapon should have variable
     *            power
     * @param makesProjectiles
     *            Whether or not the weapon creates projectiles (OPTIONAL)
     * @param numOfProjectiles
     *            The number of projectiles the weapon creates, if any
     *            (OPTIONAL)
     * @param projectileType
     *            The ProjectileDefinition for the projectile(s) the weapon
     *            creates, if any (OPTIONAL)
     */
    public WeaponDefinition(int id, String name, String category,
            String images, int width, int height,
            boolean powered, boolean makesProjectiles, int numOfProjectiles,
            ProjectileDefinition projectileType) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.images = images;
        this.height = height;
        this.width = width;
        this.powered = powered;
        this.makesProjectiles = makesProjectiles;
        this.numOfProjectiles = makesProjectiles ? numOfProjectiles : 0;
        this.projectileType = makesProjectiles ? projectileType : null;

    }

    /**
     * Gets the id of the weapon definition
     * 
     * @return the weapon's id
     */
    public int getID() {
        return id;
    }

    /**
     * Gets the name of the weapon
     * 
     * @return the weapon's name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the category of the weapon
     * 
     * @return the weapon category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Gets the map of weapon sprites
     * 
     * @return the map of weapon sprites
     */
    public String getImages() {
        return images;
    }

    /**
     * Gets the HEIGHT of the weapon
     * 
     * @return the HEIGHT
     */
    public int getHeight() {
        return height;
    }

    /**
     * Gets the WIDTH of the weapon
     * 
     * @return the WIDTH
     */
    public int getWidth() {
        return width;
    }

    /**
     * Checks if the weapon fires with variable power
     * 
     * @return Whether or not the weapon fires with variable power
     */
    public boolean isPowered() {
        return powered;
    }

    /**
     * Checks if weapon creates a projectile
     * 
     * @return the makesProjectiles
     */
    public boolean makesProjectiles() {
        return makesProjectiles;
    }

    /**
     * Returns the number of projectiles the weapon creates, if it creates
     * projectiles
     * 
     * @return the number of projectiles created by a weapon if it creates
     *         projectiles, else returns 0
     */
    public int getNumOfProjectiles() {
        if (!makesProjectiles) {
            return 0;
        }
        return numOfProjectiles;
    }

    /**
     * Returns the definition of the projectile the weapon creates, if it
     * creates projectiles
     * 
     * @return the definition of the projectile(s) it creates if it creates
     *         projectiles, else returns null
     */
    public ProjectileDefinition getProjectileType() {
        if (!makesProjectiles) {
            return null;
        }
        return projectileType;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof WeaponDefinition) {
            WeaponDefinition other = (WeaponDefinition) o;
            // two WeaponDefinitions shouldn't have the same ids
            return this.id == other.id;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    @Override
    public String toString() {
        return "{ID: " + this.id + " - " + this.name + "}";
    }
}
