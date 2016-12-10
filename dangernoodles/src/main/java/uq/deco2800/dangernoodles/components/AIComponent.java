package uq.deco2800.dangernoodles.components;

import uq.deco2800.dangernoodles.ecs.Component;

public class AIComponent extends Component {

    // The target for the AI to attack
    private PositionComponent targetPosition; // AI player's enemy's position
    private PositionComponent weaponPosition; // AI player's weapon position (for trajectory calculation)
    private PositionComponent destination; // AI player's movement destination
    private boolean firedWeapon;
    private String difficulty;
    private double randomness; // randomness should never be greater than 1 or less than 0

    /**
     * The constructor of AIComponent for setting difficulty level, target's position, weapon's position, a flag
     * indicating weapon has been fired, and randomness that directly affects the accuracy of AI's attacks.
     * 
     * @author Irsan and Tony
     * 
     * @param targetPosition
     *            represents the location of AI's attack target
     */
    public AIComponent(PositionComponent targetPosition, String difficulty) {
        this.targetPosition = targetPosition;
        this.weaponPosition = null;
        this.firedWeapon = false;
        this.difficulty = difficulty;
        this.randomness = 0.15; // default

        // set randomness to affect the accuracy of attacks; a lower randomness translates to higher accuracy
        if ("easy".equalsIgnoreCase(difficulty)) {
            randomness = 0.30;
        } else if ("top".equalsIgnoreCase(difficulty)) {
            randomness = 0;
        } else {
            this.difficulty = "normal";
        }
    }

    /**
     * @return the position of AI's target
     */
    public PositionComponent getTargetPosition() {
        return targetPosition;
    }

    /**
     * @return the position of the weapon that AI is currently using
     */
    public PositionComponent getWeaponPosition() {
        return weaponPosition;
    }

    /**
     * @return the destination of AI
     */
    public PositionComponent getDestination() {
        return destination;
    }

    /**
     * @return the randomness factor determined by difficulty level getDifficulty()
     */
    public double getRandomness() {
        return randomness;
    }

    /**
     * @return the difficulty level which is either "easy", "normal", or "top
     */
    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String newDifficulty) {
        this.difficulty = newDifficulty;
    }

    /**
     * @return true if and only if AI has fired a weapon; however, the status can be reset any value by setFired()
     */
    public boolean hasFired() {
        return firedWeapon;
    }

    /**
     * This method sets the target position.
     * 
     * @param targetPosition
     *            the position of the AI's target
     */
    public void setTarget(PositionComponent targetPosition) {
        this.targetPosition = targetPosition;
    }

    /**
     * This method sets the position of the weapon that AI is currently using.
     * 
     * @param weaponPosition
     *            the position of AI's weapon
     */
    public void setWeaponPosition(PositionComponent weaponPosition) {
        this.weaponPosition = weaponPosition;
    }

    /**
     * This method sets the movement destination.
     * 
     * @param destination
     */
    public void setDestination(PositionComponent destination) {
        this.destination = destination;
    }

    /**
     * This method clears the target of AI.
     */
    public void clearTarget() {
        this.targetPosition = null;
    }

    /**
     * This method sets the status of whether AI has fired its weapon.
     * 
     * @param firedWeapon
     *            if set to true, it means AI has fired its weapon; otherwise, AI has not.
     */
    public void setFired(boolean firedWeapon) {
        this.firedWeapon = firedWeapon;
    }
}