package uq.deco2800.dangernoodles;

/**
 * Produces a game tick every 16 ms (62.5 Hz) for the game given.
 * 
 * @author Javadocs and comments by Paul Haley
 */
public class GameTimer extends Thread {
	private Game game;

	/**
	 * Constructor for a new Game Timer instance. The game timer will run the
	 * given game's onTick method at approximately 60 Hz (actually 62.5 Hz or 16
	 * ms between each run).
	 * 
	 * @require game != null
	 * @ensure GameTime is made for the given game.
	 * @param game
	 *            The game to be run by the Game Timer.
	 */
	public GameTimer(Game game) {
		this.game = game;
	}

	/**
	 * Overriding run method that will run the game world every 16 ms (62.5 Hz).
	 */
	@Override
	public void run() {

		final double dt = 0.016; // Time in milliseconds to wait between each
									// tick call to the game.
		double acc = 0.0; // Time accumulated since the last tick call.

		// Getting current system time for a base time to work from.
		long currentTime = System.currentTimeMillis();

		// Will now check the current time and check if it is time to call a
		// game tick.
		while (true) {
			long newTime = System.currentTimeMillis();
			long frameTime = newTime - currentTime;
			currentTime = newTime;

			// Converts frametime from milliseconds to seconds
			acc += ((double) frameTime) / 1000;

			// Tests if the accumulated time exceeds the threshold to call for a
			// game tick.
			while (acc >= dt) {
				game.onTick(dt); // Calls a game tick with the current time
									// and the delta since the last.
				acc -= dt;
			}
		}
	}
}
