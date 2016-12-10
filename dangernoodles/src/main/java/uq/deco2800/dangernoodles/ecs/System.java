package uq.deco2800.dangernoodles.ecs;

/**
 * The interface every system has to implement
 */
@FunctionalInterface
public interface System {
    /**
     * Is called every tick and runs with the t and dt parameters
     * @param t Time since the beginning of the game
     * @param dt The delta time, i.e. the time since last frame
     */
    void run(World world, double t, double dt);
}
