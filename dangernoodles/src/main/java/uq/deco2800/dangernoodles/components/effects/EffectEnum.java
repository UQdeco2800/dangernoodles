package uq.deco2800.dangernoodles.components.effects;

/**
 * Created by khoi_truong on 2016/08/30.
 * <p>
 * This enumeration will be used with EffectComponent and EffectSystem to ensure consistency between these two files.
 */
public enum EffectEnum {

    /**
     * Allow the noodle to ignore the gravity temporarily.
     */
    FLY_BUFF("Fly buff", "/Buff_Sprites/fly.png", true),

    /**
     * Increase movement speed with duration.
     */
    SPEED_BUFF("Speed buff", "/Buff_Sprites/speed.png", true),

    /**
     * Increase shooting angle with duration.
     */
    ANGLE_BUFF("Shooting angle buff", "/Buff_Sprites/angle.png", true),

    /**
     * Increase current health (capped at max health) with duration.
     */
    HEALTH_BUFF("Health buff", "/Buff_Sprites/hp up.png", true),

    /**
     * Increase damage with duration.
     */
    DAMAGE_BUFF("Damage buff", "/Buff_Sprites/damage_up.png", true),

    /**
     * Increase shield power with duration.
     */
    SHIELD_BUFF("Shield buff", "/Buff_Sprites/shield.png", true),

    /**
     * Make noodle invulnerable with duration.
     */
    INVULNERABLE_BUFF("Invulnerability buff", "/Buff_Sprites/king.png", true),

    /**
     * Increase current mana with duration.
     */
    MANA_BUFF("Mana buff", "/Buff_Sprites/mp up.png", true),

    /**
     * Increase mana capacity.
     */
    MAX_MANA_BUFF("Max mana buff", "/Buff_Sprites/mp up.png", true),
    
    /**
     * Make more slippery during rainy weather
     */
    FAST_RAIN_BUFF("Fast rain buff", "/Weather_Effect_Sprites/fast_rain.png", true),

    //--------------------------- DEBUFFS -------------------------------------

    /**
     * Decrease movement speed with duration.
     */
    SPEED_DEBUFF("Speed debuff", "/Debuff_Sprites/speed debuff.png", false),

    /**
     * Decrease shooting angle with duration.
     */
    ANGLE_DEBUFF("Shooting angle debuff", "/Debuff_Sprites/angle debuff.png", false),

    /**
     * Decrease health.
     */
    HEALTH_DEBUFF("Health debuff", "/Debuff_Sprites/hp debuff.png", false),

    /**
     * Decrease damage with duration.
     */
    DAMAGE_DEBUFF("Damage debuff", "/Debuff_Sprites/damage down.png", false),

    /**
     * Decrease shield power with duration.
     */
    SHIELD_DEBUFF("Shield debuff", "/Debuff_Sprites/debuff shield.png", false),

    /**
     * Make noodle vulnerable if it is currently invulnerable.
     */
    INVULNERABLE_DEBUFF("Invulnerability debuff", "/Debuff_Sprites/invulnerable debuuff.png", false),

    /**
     * Decrease mana.
     */
    MANA_DEBUFF("Mana debuff", "/Debuff_Sprites/mp debuff.png", false),

    /**
     * Decrease mana capacity with duration.
     */
    MAX_MANA_DEBUFF("Max mana debuff", "/Debuff_Sprites/mp debuff.png", false),
    
    /**
     * Make it slower to move when it is snowing.
     */
    SLOW_SNOW_DEBUFF("Slow snow debuff", "/Weather_Effect_Sprites/slow_snow.png", false),
	
	/**
	 * Freezes noodle for one turn
	 */
	FROZEN_NOODLE_DEBUFF("Frozen snow debuff", "/Weather_Effect_Sprites/Frozen_Debuff.gif", false);


    // Private variable to store default duration.
    private String name;
    private String imageLocation;
    private boolean isBuff;

    /**
     * Constructor for this enumeration.
     * <p>
     * Name of this EffectEnum will be used consistently across files to avoid
     * possible strange behaviours when running the game.
     *
     * @param name
     *         a string representing the name of this effect
     * @param imageLocation
     *         a string representing the image path of the effect
     */
    EffectEnum(String name, String imageLocation, boolean isBuff) {
        this.name = name;
        this.imageLocation = imageLocation;
        this.isBuff = isBuff;
    }

    /**
     * Return default name of the effect.
     *
     * @return a string representing default name of the effect
     *
     * @ensure a string representing default name of the effect
     */
    public String getName() {
        return name;
    }

    /**
     * Return image path of the effect.
     *
     * @return a string representing the image path of the effect
     *
     * @ensure a string representing the image path of the effect
     */
    public String getImageLocation() {
        return imageLocation;
    }

    /**
     * Check if this effect is a buff.
     *
     * @return a boolean representing whether this effect is a buff
     *
     * @ensure a boolean representing whether this effect is a buff
     */
    public boolean isBuff() {
        return isBuff;
    }

    @Override
    public String toString() {
        return "Current effect type: " + name;
    }
}
