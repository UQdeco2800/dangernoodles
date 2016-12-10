package uq.deco2800.dangernoodles.prefabs;

/**
 * @author Anant Tuli
 * @author Paul Haley
 *         <p>
 *         NoodleEnum is used store the data values for each of the noodles
 *         implemented in to the game.
 *         <p>
 *         The noodle type can be called and the getter methods used to obtain
 *         the Noodle's data, this is normally used for the creation of the
 *         noodle. See the constructor for details of what values are currently
 *         implemented.
 */
public enum NoodleEnum {

    /**
     * Plain Noodle with no benefits or disadvantages
     */
    NOODLE_PLAIN("PLAIN", 0, 0, 0, 0, 0, 0, 0, 0, 0),

    /**
     * + health
     * - acceleration
     * + mass
     * + angle
     * + shield
     */
    NOODLE_TANK("TANK", 150, 0, 5, -0.8, 0.0, -800.0, 40.0, 0.0, 30.0),
    /**
     * + jump HEIGHT
     * - shield
     * + acceleration
     */
    NOODLE_AGILITY("AGILITY", -10, 0, -10, 0.3, 0.0, 300.0, -2.5, 0.0, 20.0),

    /**
     * + mana
     * + shield : 10 ?
     * + shooting angles
     * - speed (less decrease than tank)
     */
    NOODLE_WARRIOR("WARRIOR", 75, 200, 2, -0.1, 0.0, -200.0, 0, 0.0, 25.0);

    //Class variables
    private String name; //Name of the class
    private String image; //Image to be used for character, eventually this will point to idle
    private int healthOffset; //Health offset from default
    private int manaOffset; //Mana offset from default
    private double shieldOffset; //Shield offset from default
    private double axOffset; //Acceleration (x) offset, makes noodle accelerate faster
    private double ayOffset; //Acceleration (y) offset
    private double maxSpeedOffset; //Maximum speed offset from default
    private double massOffset; //Noodle mass offset from default (heavier noodles fall faster)
    private double minAngleOffset; //Offset of lowest angle noodle can attack from
    private double maxAngleOffset; //Offset of highest angle noodle can attack from

    /**
     * Constructor for each EnumNoodle.
     *
     * @param name
     * @param healthOffset
     * @param manaOffset
     * @param shieldOffset
     * @param axOffset
     * @param ayOffset
     * @param maxSpeedOffset
     * @param massOffset
     * @param minAngleOffset
     * @param maxAngleOffset
     */
    NoodleEnum(String name, int healthOffset, int manaOffset, double shieldOffset, double axOffset,
               double ayOffset, double maxSpeedOffset, double massOffset, double minAngleOffset, double maxAngleOffset) {
        this.name = name;
        this.image = "/snakesprite.png";
        this.healthOffset = healthOffset;
        this.manaOffset = manaOffset;
        this.shieldOffset = shieldOffset;
        this.axOffset = axOffset;
        this.ayOffset = ayOffset;
        this.maxSpeedOffset = maxSpeedOffset;
        this.massOffset = massOffset;
        this.minAngleOffset = minAngleOffset;
        this.maxAngleOffset = maxAngleOffset;
    }

	/*
     * Below is the getter methods for all the defined variables of a Noodle.
	 */


    /**
     * @return the imageOffset
     */
    public String getImage() {
        return image;
    }

    /**
     * @return the healthOffset
     */
    public int getHealthOffset() {
        return healthOffset;
    }

    /**
     * @return the manaOffset
     */
    public int getManaOffset() {
        return manaOffset;
    }

    /**
     * @return the shieldOffset
     */
    public double getShieldOffset() {
        return shieldOffset;
    }

    /**
     * @return the axOffset
     */
    public double getAxOffset() {
        return axOffset;
    }

    /**
     * @return the ayOffset
     */
    public double getAyOffset() {
        return ayOffset;
    }

    /**
     * @return the maxSpeedOffset
     */
    public double getMaxSpeedOffset() {
        return maxSpeedOffset;
    }

    /**
     * @return the massOffset
     */
    public double getMassOffset() {
        return massOffset;
    }

    /**
     * @return the minAngleOffset
     */
    public double getMinAngleOffset() {
        return minAngleOffset;
    }

    /**
     * @return the maxAngleOffset
     */
    public double getMaxAngleOffset() {
        return maxAngleOffset;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }


}
