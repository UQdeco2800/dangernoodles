package uq.deco2800.dangernoodles.components;

import uq.deco2800.dangernoodles.ecs.Component;

/**
 * A component which is used to keep track of current turns for each noodle. In a game there will be a turn component in the
 * turn system stored for each noodle and toggled to true when it is their turn. The component also keeps track of the current
 * time to check how much time remains.
 */
public class TurnComponent extends Component {
    private boolean isTurn;
    private int turnCount;
    private double turnStartTime;
    private double turnEndTime;

    /**
     * A component which allows noodles to take a turn. Does not take any parameters and defaults to false as the turn
     * system dictates the ordering of turns.
     */
    public TurnComponent() {
        this.isTurn = false;
        this.turnCount = 0;
    }

    /**
     * Returns the status of the current noodles turn to tell if it is currently their turn.
     *
     * @return Component's turn status, true meaning it is their turn.
     */
    public boolean getTurn() {
        return isTurn;
    }

    /**
     * A method of setting the turn of the current noodle to true which also sets the start time to allow tracking of
     * the end of the turn.
     *
     * @param startTime, the current game time that represents when the noodles turn started.
     */
    public void setTurn(double startTime) {
        this.isTurn = true;
        this.turnStartTime = startTime;
    }

    /**
     * Counts up the current turn counter to keep track of total number of turns taken so far for a noodle.
     */
    public void incrementTurnCount() {
        this.turnCount++;
    }

    /**
     * Gets the current number of turns taken by a noodle.
     *
     * @return the number of turns taken so far by a noodle.
     */
    public int getTurnCount() {
        return this.turnCount;
    }

    /**
     * Sets the start time of the current turn for the noodle based on the game time.
     *
     * @param startTime, a double, which represents the current game time.
     */
    public void setStartTime(double startTime) {
    	this.turnStartTime = startTime;
    }

    /**
     * Retrieves the start time of the turn as stored when a noodles turn starts.
     *
     * @return the start time as a double based on the game time when the turn started.
     */
    public double getTurnStartTime() {
        return this.turnStartTime;
    }

    /**
     * Retrieves the end time of the noodle as stored based on the current game time when the turn ended.
     *
     * @return the current turns end time as a double similar to the game time.
     */
    public double getTurnEndTime() {
        return this.turnEndTime;
    }

    /**
     * Ends the current turn for the noodle and sets the end time based on the current game time.
     * 
     * @param endTime, the time in the game when the noodles turn ended.
     */
    public void clearTurn(double endTime) {
        this.turnEndTime = endTime;
        this.isTurn = false;
    }
    
    /**
     * Determines if a TurnComponent is the same as this one
     * 
     * @return true if the turnComponents are equal, false otherwise
     * @param obj to compare to
     */
    public boolean equals(Object obj) {
        if (obj == null) 
            return false;
        if (obj == this)
            return true;
        if (!(obj instanceof TurnComponent)) {
        	return false;
        } else {
        	TurnComponent turnComp = (TurnComponent)obj;
        	if ((turnComp.getTurn() == this.isTurn)
        			&& (turnComp.getTurnCount() == this.turnCount)
        			&& (turnComp.getTurnStartTime() == this.turnStartTime)
        			&& (turnComp.getTurnEndTime() == this.turnEndTime)) {
        		return true;
        	} else {
        		return false;
        	}
    	}
    }
}
