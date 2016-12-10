package uq.deco2800.dangernoodles.components.noodles;

import javafx.scene.paint.*;

/**
 * TeamEnum is used to define and store data values for teams of noodles;
 */
public enum TeamEnum {

    /**
     * Team definitions.
     */
    TEAM_ALPHA(0, "ALPHA", Color.BLUEVIOLET),
    TEAM_BRAVO(1, "BRAVO", Color.ORANGE),
    TEAM_CHARLIE(2, "CHARLIE", Color.FORESTGREEN),
    TEAM_DELTA(3, "DELTA", Color.YELLOW),
    TEAM_NULL(-1, "", Color.TRANSPARENT);

    /**
     * Class variables.
     */
    private String name;
    private Color color;
    private int teamId;

    /**
     * Constructor.
     */
    TeamEnum(int teamId, String name, Color color) {
        this.teamId = teamId;
        this.name = name;
        this.color = color;
    }

    public int getTeamId() {
        return this.teamId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getColor() {
        return this.color;
    }

    /**
     * Copys all parameters from given TeamEnum instance into this instance.
     * @param copy The TeamEnum instance to be copied
     */
    public void copyTeam(TeamEnum copy) {
        this.name = copy.getName();
        this.color = copy.getColor();
        this.teamId = copy.getTeamId();
    }
}
