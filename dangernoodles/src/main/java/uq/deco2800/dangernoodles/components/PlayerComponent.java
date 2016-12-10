package uq.deco2800.dangernoodles.components;

import javafx.scene.paint.Color;
import uq.deco2800.dangernoodles.components.noodles.TeamEnum;
import uq.deco2800.dangernoodles.ecs.Component;

public class PlayerComponent extends Component {
    private int playerId;
    private TeamEnum team;
    private boolean selected = false;

    public PlayerComponent(int playerId, TeamEnum team) {
        this.playerId = playerId;
        this.team = team;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getTeamId() {
        return team.getTeamId();
    }

    public String getTeamName() {
        return team.getName();
    }

    public Color getTeamColor() {
        return team.getColor();
    }
    
    public TeamEnum getTeam() {
    	return team;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
