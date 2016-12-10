package uq.deco2800.dangernoodles.physicsTest;

import static org.junit.Assert.*;

import org.junit.Test;

import uq.deco2800.dangernoodles.physics.Vector;

public class VectorTest {

	@Test
	public void magnitudeTest() {
		double magnitude = Vector.getMagnitute(1, 1);
		// 
		double expected = Math.sqrt(2);
		
		assertTrue( expected == magnitude );
		
	}

}
