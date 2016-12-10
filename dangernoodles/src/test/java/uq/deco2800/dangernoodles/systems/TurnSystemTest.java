package uq.deco2800.dangernoodles.systems;

import uq.deco2800.dangernoodles.components.stats.HealthComponent;
import uq.deco2800.dangernoodles.components.TurnComponent;
import uq.deco2800.dangernoodles.ecs.Entity;
import uq.deco2800.dangernoodles.ecs.World;
import uq.deco2800.dangernoodles.prefabs.PlayerEntities;
import uq.deco2800.dangernoodles.prefabs.NoodleEnum;
import uq.deco2800.dangernoodles.components.noodles.TeamEnum;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Test;

/**
 * Test class for the component TurnComponent.
 */
public class TurnSystemTest {
    World miniWorld = new World(0, 0);

    /**
     * Test initial setup, with 1 team, 1 noodle
     */
    @Test
    public void singleNoodle(){
        Entity player = PlayerEntities.createPlayer(miniWorld, NoodleEnum.NOODLE_PLAIN, false, TeamEnum.TEAM_ALPHA, 0);
        TurnSystem turnSys = new TurnSystem(miniWorld, null, null, null);
        Optional<TurnComponent> turn = miniWorld.getComponent(player, TurnComponent.class);
        assertTrue(turn.isPresent());
        assertTrue(turn.get().getTurn());
    }

    /**
     * Test moving to next team's turn
     */
    @Test
    public void nextTeam() {
        Entity team1Player = PlayerEntities.createPlayer(miniWorld, NoodleEnum.NOODLE_PLAIN, false, TeamEnum.TEAM_ALPHA, 0);
        Entity team2Player = PlayerEntities.createPlayer(miniWorld, NoodleEnum.NOODLE_PLAIN, false, TeamEnum.TEAM_BRAVO, 0);
        TurnSystem turnSys = new TurnSystem(miniWorld, null, null, null);

        Optional<TurnComponent> turn1 = miniWorld.getComponent(team1Player, TurnComponent.class);
        Optional<TurnComponent> turn2 = miniWorld.getComponent(team2Player, TurnComponent.class);
        assertTrue(turn2.isPresent());
        assertFalse(turn2.get().getTurn()); // make sure its initially false

        assertTrue(turn1.isPresent());
        turn1.get().clearTurn(10); //trigger turn ending

        turnSys.run(miniWorld, 0, 0); //run TurnSystem to handle next turn
        assertTrue(turn2.get().getTurn());
    }

    /**
     * Test moving to next player's turn same team
     */
    @Test
    public void nextPlayer() {
        Entity Player1 = PlayerEntities.createPlayer(miniWorld, NoodleEnum.NOODLE_PLAIN, false, TeamEnum.TEAM_ALPHA, 0);
        Entity Player2 = PlayerEntities.createPlayer(miniWorld, NoodleEnum.NOODLE_PLAIN, false, TeamEnum.TEAM_ALPHA, 0);
        TurnSystem turnSys = new TurnSystem(miniWorld, null, null, null);

        Optional<TurnComponent> turn1 = miniWorld.getComponent(Player1, TurnComponent.class);
        Optional<TurnComponent> turn2 = miniWorld.getComponent(Player2, TurnComponent.class);
        assertTrue(turn2.isPresent());
        assertFalse(turn2.get().getTurn()); // make sure its initially false

        assertTrue(turn1.isPresent());
        turn1.get().clearTurn(10); //trigger turn ending

        turnSys.run(miniWorld, 0, 0); //run TurnSystem to handle next turn
        assertTrue(turn2.get().getTurn());
    }

    /**
     * Test 2 teams of 2
     */
    @Test
    public void teamsOfTwo() {
        Entity team1Player1 = PlayerEntities.createPlayer(miniWorld, NoodleEnum.NOODLE_PLAIN, false, TeamEnum.TEAM_ALPHA, 0);
        Entity team1Player2 = PlayerEntities.createPlayer(miniWorld, NoodleEnum.NOODLE_PLAIN, false, TeamEnum.TEAM_ALPHA, 1);
        Entity team2Player1 = PlayerEntities.createPlayer(miniWorld, NoodleEnum.NOODLE_PLAIN, false, TeamEnum.TEAM_BRAVO, 0);
        Entity team2Player2 = PlayerEntities.createPlayer(miniWorld, NoodleEnum.NOODLE_PLAIN, false, TeamEnum.TEAM_BRAVO, 1);
        TurnSystem turnSys = new TurnSystem(miniWorld, null, null, null);

        Optional<TurnComponent> turn1 = miniWorld.getComponent(team1Player1, TurnComponent.class);
        Optional<TurnComponent> turn2 = miniWorld.getComponent(team2Player1, TurnComponent.class);
        Optional<TurnComponent> turn3 = miniWorld.getComponent(team1Player2, TurnComponent.class);
        Optional<TurnComponent> turn4 = miniWorld.getComponent(team2Player2, TurnComponent.class);

        assertTrue(turn1.isPresent());
        assertTrue(turn2.isPresent());
        assertTrue(turn3.isPresent());
        assertTrue(turn4.isPresent());

        turn1.get().clearTurn(10);
        turnSys.run(miniWorld, 0, 0);
        turn2.get().clearTurn(20);
        turnSys.run(miniWorld, 0, 0);
        assertTrue(turn3.get().getTurn());
        turn3.get().clearTurn(30);

        turnSys.run(miniWorld, 0, 0);
        assertFalse(turn3.get().getTurn());
        assertTrue(turn4.get().getTurn());

    }

    /**
     * Test dead players
     */
    @Test
    public void deadPlayer() {
        Entity team1Player1 = PlayerEntities.createPlayer(miniWorld, NoodleEnum.NOODLE_PLAIN, false, TeamEnum.TEAM_ALPHA, 0);
        Entity team1Player2 = PlayerEntities.createPlayer(miniWorld, NoodleEnum.NOODLE_PLAIN, false, TeamEnum.TEAM_ALPHA, 1);
        Entity team2Player1 = PlayerEntities.createPlayer(miniWorld, NoodleEnum.NOODLE_PLAIN, false, TeamEnum.TEAM_BRAVO, 0);
        Entity team2Player2 = PlayerEntities.createPlayer(miniWorld, NoodleEnum.NOODLE_PLAIN, false, TeamEnum.TEAM_BRAVO, 1);
        TurnSystem turnSys = new TurnSystem(miniWorld, null, null, null);

        Optional<TurnComponent> turn1 = miniWorld.getComponent(team1Player1, TurnComponent.class);
        Optional<TurnComponent> turn2 = miniWorld.getComponent(team2Player1, TurnComponent.class);
        Optional<TurnComponent> turn3 = miniWorld.getComponent(team1Player2, TurnComponent.class);
        Optional<TurnComponent> turn4 = miniWorld.getComponent(team2Player2, TurnComponent.class);

        assertTrue(turn1.isPresent());
        assertTrue(turn2.isPresent());
        assertTrue(turn3.isPresent());
        assertTrue(turn4.isPresent());

        Optional<HealthComponent> playerHealth = miniWorld.getComponent(team1Player2, HealthComponent.class);
        assertTrue(playerHealth.isPresent());
        playerHealth.get().setHealth(0);

        turn1.get().clearTurn(10);
        turnSys.run(miniWorld, 0, 0);
        turn2.get().clearTurn(20);
        turnSys.run(miniWorld, 0, 0);
        assertFalse(turn3.get().getTurn()); //turn 3 player is dead
        assertTrue(turn1.get().getTurn()); //turn 1 player is next on turn 3 player's team
        turn1.get().clearTurn(30);
        turnSys.run(miniWorld, 0, 0);
        assertTrue(turn4.get().getTurn()); //team 2's order shouldn't be affected
    }

    /**
     * Test completely dead player team
     */
    @Test
    public void deadTeam() {
        Entity team1Player1 = PlayerEntities.createPlayer(miniWorld, NoodleEnum.NOODLE_PLAIN, false, TeamEnum.TEAM_ALPHA, 0);
        Entity team2Player1 = PlayerEntities.createPlayer(miniWorld, NoodleEnum.NOODLE_PLAIN, false, TeamEnum.TEAM_BRAVO, 0);
        TurnSystem turnSys = new TurnSystem(miniWorld, null, null, null);

        Optional<TurnComponent> turn1 = miniWorld.getComponent(team1Player1, TurnComponent.class);
        Optional<TurnComponent> turn2 = miniWorld.getComponent(team2Player1, TurnComponent.class);

        assertTrue(turn1.isPresent());
        assertTrue(turn2.isPresent());

        Optional<HealthComponent> playerHealth = miniWorld.getComponent(team2Player1, HealthComponent.class);
        assertTrue(playerHealth.isPresent());
        playerHealth.get().setHealth(0);

        turn1.get().clearTurn(10);
        turnSys.run(miniWorld, 0, 0);
        assertFalse(turn2.get().getTurn()); //turn 2 player is dead
        assertTrue(turn1.get().getTurn()); //team 2 has no living players, go to team 1
    }

    /**
     * Test every team dead
     */
    @Test
    public void allTeamsDead() {
        Entity team1Player1 = PlayerEntities.createPlayer(miniWorld, NoodleEnum.NOODLE_PLAIN, false, TeamEnum.TEAM_ALPHA, 0);
        Entity team2Player1 = PlayerEntities.createPlayer(miniWorld, NoodleEnum.NOODLE_PLAIN, false, TeamEnum.TEAM_BRAVO, 0);
        TurnSystem turnSys = new TurnSystem(miniWorld, null, null, null);

        Optional<TurnComponent> turn1 = miniWorld.getComponent(team1Player1, TurnComponent.class);
        Optional<TurnComponent> turn2 = miniWorld.getComponent(team2Player1, TurnComponent.class);

        assertTrue(turn1.isPresent());
        assertTrue(turn2.isPresent());

        Optional<HealthComponent> player1Health = miniWorld.getComponent(team1Player1, HealthComponent.class);
        Optional<HealthComponent> player2Health = miniWorld.getComponent(team2Player1, HealthComponent.class);
        assertTrue(player1Health.isPresent());
        assertTrue(player2Health.isPresent());
        player1Health.get().setHealth(0);
        player2Health.get().setHealth(0);
        
        turnSys.run(miniWorld, 0, 0);
        //No player should have an active turn
        assertFalse(turn2.get().getTurn());
        assertFalse(turn1.get().getTurn());
    }
    
    /**
     * Test for winning team, no winner
     */
    @Test
    public void noWinner(){
    	Entity team1Player1 = PlayerEntities.createPlayer(miniWorld, NoodleEnum.NOODLE_PLAIN, false, TeamEnum.TEAM_ALPHA, 0);
        Entity team2Player1 = PlayerEntities.createPlayer(miniWorld, NoodleEnum.NOODLE_PLAIN, false, TeamEnum.TEAM_BRAVO, 0);
        TeamEnum winner = TeamEnum.TEAM_NULL;
        assertTrue(winner == TeamEnum.TEAM_NULL);
        //By passing winner instead of null, turn and delay timers will be active
        TurnSystem turnSys = new TurnSystem(miniWorld, null, null, winner);
        
        //Jump past delay time
        turnSys.run(miniWorld, 4, 0);
        
        Optional<TurnComponent> turn1 = miniWorld.getComponent(team1Player1, TurnComponent.class);
        assertTrue(turn1.isPresent());
        turn1.get().clearTurn(0);
        
        //Switch to next turn, testing for winners
        turnSys.run(miniWorld, 30, 0);

        assertTrue(winner == TeamEnum.TEAM_NULL);
    }
    
    /**
     * Test for winning team, only one team
     */
    @Test
    public void winnerOneTeam(){
    	Entity team1Player1 = PlayerEntities.createPlayer(miniWorld, NoodleEnum.NOODLE_PLAIN, false, TeamEnum.TEAM_BRAVO, 0);
        TeamEnum winner = TeamEnum.TEAM_NULL;
        //By passing winner instead of null, turn and delay timers will be active
        TurnSystem turnSys = new TurnSystem(miniWorld, null, null, winner);

        //Jump past delay time
        turnSys.run(miniWorld, 4, 0);
        
        Optional<TurnComponent> turn1 = miniWorld.getComponent(team1Player1, TurnComponent.class);
        assertTrue(turn1.isPresent());
        turn1.get().clearTurn(0);
        
        //Switch to next turn, testing for winners
        turnSys.run(miniWorld, 30, 0);

        
        assertTrue(winner.getTeamId() == TeamEnum.TEAM_BRAVO.getTeamId());
    }
    
    /**
     * Test for winning team, kill other team
     */
    @Test
    public void winnerTwoTeams(){
    	Entity team1Player1 = PlayerEntities.createPlayer(miniWorld, NoodleEnum.NOODLE_PLAIN, false, TeamEnum.TEAM_ALPHA, 0);
        Entity team2Player1 = PlayerEntities.createPlayer(miniWorld, NoodleEnum.NOODLE_PLAIN, false, TeamEnum.TEAM_BRAVO, 0);
        TeamEnum winner = TeamEnum.TEAM_NULL;
        assertTrue(winner == TeamEnum.TEAM_NULL);
        //By passing winner instead of null, turn and delay timers will be active
        TurnSystem turnSys = new TurnSystem(miniWorld, null, null, winner);
        
        //Jump past delay time
        turnSys.run(miniWorld, 4, 0);
        
        Optional<HealthComponent> health1 = miniWorld.getComponent(team1Player1, HealthComponent.class);
        assertTrue(health1.isPresent());
        health1.get().setHealth(0);
        
        //Switch to next turn, testing for winners
        turnSys.run(miniWorld, 30, 0);

        assertTrue(winner.getTeamId() == TeamEnum.TEAM_BRAVO.getTeamId());
    }
}
