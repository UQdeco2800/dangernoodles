package uq.deco2800.dangernoodles.weapons;

/**
 * Immutable object that defines a type of projectile
 * 
 * @author danyon
 * 
 */
public class ProjectileDefinition {
    private int id;
    private String name;
    private String sprite;
    private int height;
    private int width;
    private double mass;
    private double damage;
    private boolean timesOut;
    private int lifeTicks;
    private boolean explodes;
    private int blastRadius;
    private boolean makesClusters;
    private int numOfClusters;
    private ProjectileDefinition clusterType;
    private int clusterID = -1;
    
    /**
     * Constructor for immutable ProjectileDefinition object
     * 
     * @param id
     *            Projectile's id
     * @param name
     *            Name of projectile
     * @param sprite
     *            String path to image sprite of projectile
     * @param width
     *            The WIDTH of the projectile's sprite
     * @param height
     *            The HEIGHT of the projectile's sprite
     * @param mass
     *            The mass of the projectile to create
     * @param damage
     *            The maximum amount of damage dealt by projectile
     * @param timesOut
     *            Whether or not there is a timeout on projectile
     * @param lifeTicks
     *            The number of ticks before the projectile is destroyed
     *            automatically
     * @param explodes
     *            Whether or not the projectile will explode when destroyed
     * @param blastRadius
     *            The radius of explosion, if projectile is to explode
     * @param makesClusters
     *            Whether or not the projectile will split into clusters
     * @param numOfClusters
     *            The number of clusters the projectile will explode into if it
     *            clusters
     * @param clusterType
     *            The ProjectileDefinition of the clusters that will be created
     *            when the projectile explodes, if it clusters
     */
    public ProjectileDefinition(int id, String name, String sprite, int width,
            int height, double mass, double damage, boolean timesOut,
            int lifeTicks, boolean explodes, int blastRadius,
            boolean makesClusters, int numOfClusters,
            ProjectileDefinition clusterType) {
        this.id = id;
        this.name = name;
        this.sprite = sprite;
        this.width = width;
        this.height = height;
        this.mass = mass;
        this.damage = damage;
        this.timesOut = timesOut;
        this.lifeTicks = timesOut ? lifeTicks : 0;
        this.explodes = explodes;
        this.blastRadius = explodes ? blastRadius : 0;
        this.makesClusters = makesClusters;
        this.numOfClusters = makesClusters ? numOfClusters : 0;
        this.clusterType = makesClusters ? clusterType : null;
    }
    
    /**
     * Constructor for immutable ProjectileDefinition object
     * 
     * @param id
     *            Projectile's id
     * @param name
     *            Name of projectile
     * @param sprite
     *            String path to image sprite of projectile
     * @param width
     *            The WIDTH of the projectile's sprite
     * @param height
     *            The HEIGHT of the projectile's sprite
     * @param mass
     *            The mass of the projectile to create
     * @param damage
     *            The maximum amount of damage dealt by projectile
     * @param timesOut
     *            Whether or not there is a timeout on projectile
     * @param lifeTicks
     *            The number of ticks before the projectile is destroyed
     *            automatically
     * @param explodes
     *            Whether or not the projectile will explode when destroyed
     * @param blastRadius
     *            The radius of explosion, if projectile is to explode
     * @param makesClusters
     *            Whether or not the projectile will split into clusters
     * @param numOfClusters
     *            The number of clusters the projectile will explode into if it
     *            clusters
     * @param clusterID
     *            Integer ID from ProjectileDefinitionList of the given cluster 
     *            type
     */
    public ProjectileDefinition(int id, String name, String sprite, int width,
            int height, double mass, double damage, boolean timesOut,
            int lifeTicks, boolean explodes, int blastRadius,
            boolean makesClusters, int numOfClusters, int clusterID) {
        this(id, name, sprite, width, height, mass, damage, timesOut, lifeTicks,
                explodes, blastRadius, makesClusters, numOfClusters, null);
        this.clusterID = clusterID;
    }

    /**
     * Constructor for immutable ProjectileDefinition object Creates a
     * ProjectileDefinition that does not cluster
     * 
     * @param id
     *            Projectile's id
     * @param name
     *            Name of projectile
     * @param sprite
     *            String path to image sprite of projectile
     * @param width
     *            The WIDTH of the projectile's sprite
     * @param height
     *            The HEIGHT of the projectile's sprite
     * @param mass
     *            The mass of the projectile to create
     * @param damage
     *            The maximum amount of damage dealt by projectile
     * @param timesOut
     *            Whether or not there is a timeout on projectile
     * @param lifeTicks
     *            The number of ticks before the projectile is destroyed
     *            automatically
     * @param explodes
     *            Whether or not the projectile will explode when destroyed
     * @param blastRadius
     *            The radius of explosion, if projectile is to explode
     */
    public ProjectileDefinition(int id, String name, String sprite, int width,
            int height, double mass, double damage, boolean timesOut,
            int lifeTicks, boolean explodes, int blastRadius) {
        // Use the full constructor with default values
        this(id, name, sprite, width, height, mass, damage, timesOut, lifeTicks,
                explodes, blastRadius, false, 0, null);
    }

    /**
     * Constructor for immutable ProjectileDefinition object Creates a
     * ProjectileDefinition that does not cluster or explode
     * 
     * @param id
     *            Projectile's id
     * @param name
     *            Name of projectile
     * @param sprite
     *            String path to image sprite of projectile
     * @param width
     *            The WIDTH of the projectile's sprite
     * @param height
     *            The HEIGHT of the projectile's sprite
     * @param mass
     *            The mass of the projectile to create
     * @param damage
     *            The maximum amount of damage dealt by projectile
     * @param timesOut
     *            Whether or not there is a timeout on projectile
     * @param lifeTicks
     *            The number of ticks before the projectile is destroyed
     *            automatically
     */
    public ProjectileDefinition(int id, String name, String sprite, int width,
            int height, double mass, double damage, boolean timesOut,
            int lifeTicks) {
        // Use the full constructor with default values
        this(id, name, sprite, width, height, mass, damage, timesOut, lifeTicks,
                false, 0, false, 0, null);
    }

    /**
     * Constructor for immutable ProjectileDefinition object Creates a
     * ProjectileDefinition that does not cluster, explode or time out
     * 
     * @param id
     *            Projectile's id
     * @param name
     *            Name of projectile
     * @param sprite
     *            String path to image sprite of projectile
     * @param width
     *            The WIDTH of the projectile's sprite
     * @param height
     *            The HEIGHT of the projectile's sprite
     * @param mass
     *            The mass of the projectile to create
     * @param damage
     *            The maximum amount of damage dealt by projectile
     */
    public ProjectileDefinition(int id, String name, String sprite, int width,
            int height, double mass, double damage) {
        // Use the full constructor with default values
        this(id, name, sprite, width, height, mass, damage, false, 0, false, 0,
                false, 0, null);
    }
    
    /**
     * Constructor for immutable ProjectileDefinition object
     * 
     * @param projectile
     *            A ProjectileDefinition
     */
    public ProjectileDefinition(ProjectileDefinition projectile) {
        this(projectile.getId(), projectile.getName(), projectile.getSprite(),
        		projectile.getWidth(), projectile.getHeight(), 
        		projectile.getMass(), projectile.getDamage(), 
        		projectile.timesOut(), projectile.getLifeTicks(),
        		projectile.explodes(), projectile.getBlastRadius(),
        		projectile.makesClusters(), projectile.getNumOfClusters(),
        		projectile.getClusterType());
    }

    /**
     * Get the id of the projectile
     * 
     * @return The projectile ID
     */
    public int getId() {
        return id;
    }

    /**
     * Get the name of the projectile
     * 
     * @return The projectile name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the path to the sprite
     * 
     * @return The path to the projectile sprite
     */
    public String getSprite() {
        return sprite;
    }

    /**
     * Gets the WIDTH of the Projectile's sprite
     * 
     * @return WIDTH of projectile's sprite
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the HEIGHT of the projectile's sprite
     * 
     * @return HEIGHT of the projectile's sprite
     */
    public int getHeight() {
        return height;
    }

    /**
     * Get the mass of the projectile
     * 
     * @return The mass of the projectile
     */
    public double getMass() {
        return mass;
    }

    /**
     * Get the amount of damage inflicted by the projectile
     * 
     * @return The amount of damage inflicted by the projectile
     */
    public double getDamage() {
        return damage;
    }

    /**
     * Check whether the projectile will time-out
     * 
     * @return Whether projectile times-out
     */
    public boolean timesOut() {
        return timesOut;
    }

    /**
     * Get the maximum life of the projectile in ticks
     * 
     * @return The maximum life of the projectile in ticks
     */
    public int getLifeTicks() {
        return lifeTicks;
    }

    /**
     * Check whether projectile explodes when destroyed
     * 
     * @return Whether projectile explodes
     */
    public boolean explodes() {
        return explodes;
    }

    /**
     * Get the size of the explosion created when projectile is destroyed
     * 
     * @return The blast radius of the explosion created when projectile is
     *         destroyed
     */
    public int getBlastRadius() {
        return blastRadius;
    }

    /**
     * Check whether projectile splits into clusters when destroyed
     * 
     * @return Whether or not the projectile clusters when destroyed
     */
    public boolean makesClusters() {
        return makesClusters;
    }

    /**
     * Get the number of clusters created when projectile is destroyed
     * 
     * @return The number of clusters created when projectile is destroyed
     */
    public int getNumOfClusters() {
        return numOfClusters;
    }

    /**
     * Get the projectile definition of the clusters created when projectile is
     * destroyed (if it splits into clusters when destroyed)
     * 
     * @return The ProjectileDefinition of clusters if it splits into clusters,
     *         else, null
     */
    public ProjectileDefinition getClusterType() {
        return clusterType;
    }
    
    /**
     * Gets the ID of the projectile's cluster type if it clusters, -1 if not
     */
    public int getClusterID() {
        return this.clusterID;
    }
    
    /**
     * Set the projectile definition of the clusters created when projectile is
     * destroyed (if it splits into clusters when destroyed)
     * 
     * @param cluster
     *            the ProjectileDefinition to be created by the cluster
     */
    public void setClusterType(ProjectileDefinition cluster) {
        clusterType = makesClusters ? cluster : null;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ProjectileDefinition) {
            ProjectileDefinition other = (ProjectileDefinition) o;
            // two projectileDefinitions shouldn't have the same ids
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
