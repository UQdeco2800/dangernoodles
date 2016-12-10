package uq.deco2800.dangernoodles.components.effects;

import uq.deco2800.dangernoodles.ecs.Component;

/**
 * Created by khoi_truong on 2016/09/16.
 * <p>
 * This class is used to construct different effects components.
 */
public abstract class EffectComponent extends Component {
    // Package-private fields to store information
    EffectEnum name;
    int turnDuration;
    boolean isBuff;

    /**
     * Return the name of this effect.
     *
     * @return an EffectEnum representing the name of this effect
     *
     * @ensure an EffectEnum representing the name of this effect
     */
    public EffectEnum getName() {
        return name;
    }

    /**
     * Return the duration of this effect.
     *
     * @return an integer representing the remaining turn of this effect
     *
     * @ensure an integer representing the remaining turn of this effect
     */
    public int getTurnDuration() {
        return turnDuration;
    }

    /**
     * Set the duration of this effect.
     *
     * @param turnDuration
     *         an integer representing the remaining turn of this effect
     *
     * @throws IllegalArgumentException
     *         if turnDuration is less than zero
     * @require turnDuration >= 0
     * @ensure getTurnDuration() == turnDuration
     */
    public void setTurnDuration(int turnDuration) {
        if (turnDuration < 0) {
            throw new IllegalArgumentException("Turn duration cannot be less " +
                    "than zero.");
        }
        this.turnDuration = turnDuration;
    }

    /**
     * Check if this effect is a buff.
     *
     * @return a boolean representing if this effect is a buff
     *
     * @ensure a boolean representing if this effect is a buff
     */
    public boolean isBuff() {
        return isBuff;
    }
}
