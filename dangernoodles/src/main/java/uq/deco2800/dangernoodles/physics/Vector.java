package uq.deco2800.dangernoodles.physics;

/**
 * Utility class with common vector operations
 */
public abstract class Vector {
	/**
	 * Private constructor so as to not be able to instantiate it
	 */
    private Vector() {}

    /**
     * Gets the magnitude of a vector
     * @param x The x
     * @param y The y
     * @return The magnitude
     */
	public static double getMagnitute(double x, double y) {
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}
}
