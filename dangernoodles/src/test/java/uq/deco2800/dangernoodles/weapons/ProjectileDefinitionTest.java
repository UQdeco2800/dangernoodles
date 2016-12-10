package uq.deco2800.dangernoodles.weapons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ProjectileDefinitionTest {

    @Test
    public void testFirstConstructor() {
        ProjectileDefinition proj = new ProjectileDefinition(1, "bullet",
                "image.jpeg", 100, 10, 5.0, 13);
        assertEquals("Unexpected ID", 1, proj.getId());
        assertEquals("Unexpected sprite", "image.jpeg", proj.getSprite());
        assertEquals("Unexpected name", "bullet", proj.getName());
        assertEquals("Unexpected WIDTH", 100, proj.getWidth());
        assertEquals("Unexpected HEIGHT", 10, proj.getHeight());
        assertTrue("Unexpected mass", Math.abs(5.0 - proj.getMass()) < 0.01);
        assertTrue("Unexpected damage", Math.abs(13 - proj.getDamage()) < 0.01);
        assertFalse("Unexpected timesOut", proj.timesOut());
        assertEquals("Unexpected life ticks", 0, proj.getLifeTicks());
        assertFalse("Unexpected explodes", proj.explodes());
        assertEquals("Unexpected blast radius", 0, proj.getBlastRadius());
        assertFalse("Unexpected clustering action", proj.makesClusters());
        assertEquals("Unexpected number of clusters", 0,
                proj.getNumOfClusters());
        assertNull("Unexpected cluster type", proj.getClusterType());
    }

    @Test
    public void testSecondConstructor() {
        ProjectileDefinition proj = new ProjectileDefinition(1, "bullet",
                "image.jpeg", 100, 10, 5.0, 13, false, 0);
        assertEquals("Unexpected ID", 1, proj.getId());
        assertEquals("Unexpected sprite", "image.jpeg", proj.getSprite());
        assertEquals("Unexpected name", "bullet", proj.getName());
        assertEquals("Unexpected WIDTH", 100, proj.getWidth());
        assertEquals("Unexpected HEIGHT", 10, proj.getHeight());
        assertTrue("Unexpected mass", Math.abs(5.0 - proj.getMass()) < 0.01);
        assertTrue("Unexpected damage", Math.abs(13 - proj.getDamage()) < 0.01);
        assertFalse("Unexpected timesOut", proj.timesOut());
        assertEquals("Unexpected life ticks", 0, proj.getLifeTicks());
        assertFalse("Unexpected explodes", proj.explodes());
        assertEquals("Unexpected blast radius", 0, proj.getBlastRadius());
        assertFalse("Unexpected clustering action", proj.makesClusters());
        assertEquals("Unexpected number of clusters", 0,
                proj.getNumOfClusters());
        assertNull("Unexpected cluster type", proj.getClusterType());

        proj = new ProjectileDefinition(1, "bullet", "image.jpeg", 100, 10, 5.0,
                13, false, 100);
        assertEquals("Unexpected ID", 1, proj.getId());
        assertEquals("Unexpected sprite", "image.jpeg", proj.getSprite());
        assertEquals("Unexpected name", "bullet", proj.getName());
        assertEquals("Unexpected WIDTH", 100, proj.getWidth());
        assertEquals("Unexpected HEIGHT", 10, proj.getHeight());
        assertTrue("Unexpected mass", Math.abs(5.0 - proj.getMass()) < 0.001);
        assertTrue("Unexpected damage", Math.abs(13 - proj.getDamage()) < 0.01);
        assertFalse("Unexpected timesOut", proj.timesOut());
        assertEquals("Unexpected life ticks", 0, proj.getLifeTicks());
        assertFalse("Unexpected explodes", proj.explodes());
        assertEquals("Unexpected blast radius", 0, proj.getBlastRadius());
        assertFalse("Unexpected clustering action", proj.makesClusters());
        assertEquals("Unexpected number of clusters", 0,
                proj.getNumOfClusters());
        assertNull("Unexpected cluster type", proj.getClusterType());

        proj = new ProjectileDefinition(1, "bullet", "image.jpeg", 100, 10, 5.0,
                13, true, 100);
        assertEquals("Unexpected ID", 1, proj.getId());
        assertEquals("Unexpected sprite", "image.jpeg", proj.getSprite());
        assertEquals("Unexpected name", "bullet", proj.getName());
        assertEquals("Unexpected WIDTH", 100, proj.getWidth());
        assertEquals("Unexpected HEIGHT", 10, proj.getHeight());
        assertTrue("Unexpected mass", Math.abs(5.0 - proj.getMass()) < 0.001);
        assertTrue("Unexpected damage", Math.abs(13 - proj.getDamage()) < 0.01);
        assertTrue("Unexpected timesOut", proj.timesOut());
        assertEquals("Unexpected life ticks", 100, proj.getLifeTicks());
        assertFalse("Unexpected explodes", proj.explodes());
        assertEquals("Unexpected blast radius", 0, proj.getBlastRadius());
        assertFalse("Unexpected clustering action", proj.makesClusters());
        assertEquals("Unexpected number of clusters", 0,
                proj.getNumOfClusters());
        assertNull("Unexpected cluster type", proj.getClusterType());
    }

    @Test
    public void testThirdConstructor() {
        ProjectileDefinition proj = new ProjectileDefinition(1, "bullet",
                "image.jpeg", 100, 10, 5.0, 13, false, 0, false, 0);
        assertEquals("Unexpected ID", 1, proj.getId());
        assertEquals("Unexpected sprite", "image.jpeg", proj.getSprite());
        assertEquals("Unexpected name", "bullet", proj.getName());
        assertEquals("Unexpected WIDTH", 100, proj.getWidth());
        assertEquals("Unexpected HEIGHT", 10, proj.getHeight());
        assertTrue("Unexpected mass", Math.abs(5.0 - proj.getMass()) < 0.001);
        assertTrue("Unexpected damage", Math.abs(13 - proj.getDamage()) < 0.01);
        assertFalse("Unexpected timesOut", proj.timesOut());
        assertEquals("Unexpected life ticks", 0, proj.getLifeTicks());
        assertFalse("Unexpected explodes", proj.explodes());
        assertEquals("Unexpected blast radius", 0, proj.getBlastRadius());
        assertFalse("Unexpected clustering action", proj.makesClusters());
        assertEquals("Unexpected number of clusters", 0,
                proj.getNumOfClusters());
        assertNull("Unexpected cluster type", proj.getClusterType());

        proj = new ProjectileDefinition(1, "bullet", "image.jpeg", 100, 10, 5.0,
                13, false, 0, false, 100);
        assertEquals("Unexpected ID", 1, proj.getId());
        assertEquals("Unexpected sprite", "image.jpeg", proj.getSprite());
        assertEquals("Unexpected name", "bullet", proj.getName());
        assertEquals("Unexpected WIDTH", 100, proj.getWidth());
        assertEquals("Unexpected HEIGHT", 10, proj.getHeight());
        assertTrue("Unexpected mass", Math.abs(5.0 - proj.getMass()) < 0.001);
        assertTrue("Unexpected damage", Math.abs(13 - proj.getDamage()) < 0.01);
        assertFalse("Unexpected timesOut", proj.timesOut());
        assertEquals("Unexpected life ticks", 0, proj.getLifeTicks());
        assertFalse("Unexpected explodes", proj.explodes());
        assertEquals("Unexpected blast radius", 0, proj.getBlastRadius());
        assertFalse("Unexpected clustering action", proj.makesClusters());
        assertEquals("Unexpected number of clusters", 0,
                proj.getNumOfClusters());
        assertNull("Unexpected cluster type", proj.getClusterType());

        proj = new ProjectileDefinition(1, "bullet", "image.jpeg", 100, 10, 5.0,
                13, false, 0, true, 13);
        assertEquals("Unexpected ID", 1, proj.getId());
        assertEquals("Unexpected sprite", "image.jpeg", proj.getSprite());
        assertEquals("Unexpected name", "bullet", proj.getName());
        assertEquals("Unexpected WIDTH", 100, proj.getWidth());
        assertEquals("Unexpected HEIGHT", 10, proj.getHeight());
        assertTrue("Unexpected mass", Math.abs(5.0 - proj.getMass()) < 0.001);
        assertTrue("Unexpected damage", Math.abs(13 - proj.getDamage()) < 0.01);
        assertFalse("Unexpected timesOut", proj.timesOut());
        assertEquals("Unexpected life ticks", 0, proj.getLifeTicks());
        assertTrue("Unexpected explodes", proj.explodes());
        assertEquals("Unexpected blast radius", 13, proj.getBlastRadius());
        assertFalse("Unexpected clustering action", proj.makesClusters());
        assertEquals("Unexpected number of clusters", 0,
                proj.getNumOfClusters());
        assertNull("Unexpected cluster type", proj.getClusterType());
    }

    @Test
    public void testFourthConstructor() {
        ProjectileDefinition cluster = new ProjectileDefinition(0, "", "", 0, 0,
                0, 0);

        ProjectileDefinition proj = new ProjectileDefinition(1, "bullet",
                "image.jpeg", 100, 10, 5.0, 13, false, 0, false, 0, false, 0,
                null);
        assertEquals("Unexpected ID", 1, proj.getId());
        assertEquals("Unexpected sprite", "image.jpeg", proj.getSprite());
        assertEquals("Unexpected name", "bullet", proj.getName());
        assertEquals("Unexpected WIDTH", 100, proj.getWidth());
        assertEquals("Unexpected HEIGHT", 10, proj.getHeight());
        assertTrue("Unexpected mass", Math.abs(5.0 - proj.getMass()) < 0.001);
        assertTrue("Unexpected damage", Math.abs(13 - proj.getDamage()) < 0.01);
        assertFalse("Unexpected timesOut", proj.timesOut());
        assertEquals("Unexpected life ticks", 0, proj.getLifeTicks());
        assertFalse("Unexpected explodes", proj.explodes());
        assertEquals("Unexpected blast radius", 0, proj.getBlastRadius());
        assertFalse("Unexpected clustering action", proj.makesClusters());
        assertEquals("Unexpected number of clusters", 0,
                proj.getNumOfClusters());
        assertNull("Unexpected cluster type", proj.getClusterType());

        proj = new ProjectileDefinition(1, "bullet", "image.jpeg", 100, 10, 5.0,
                13, false, 0, false, 0, false, 100, cluster);
        assertEquals("Unexpected ID", 1, proj.getId());
        assertEquals("Unexpected sprite", "image.jpeg", proj.getSprite());
        assertEquals("Unexpected name", "bullet", proj.getName());
        assertEquals("Unexpected WIDTH", 100, proj.getWidth());
        assertEquals("Unexpected HEIGHT", 10, proj.getHeight());
        assertTrue("Unexpected mass", Math.abs(5.0 - proj.getMass()) < 0.001);
        assertTrue("Unexpected damage", Math.abs(13 - proj.getDamage()) < 0.01);
        assertFalse("Unexpected timesOut", proj.timesOut());
        assertEquals("Unexpected life ticks", 0, proj.getLifeTicks());
        assertFalse("Unexpected explodes", proj.explodes());
        assertEquals("Unexpected blast radius", 0, proj.getBlastRadius());
        assertFalse("Unexpected clustering action", proj.makesClusters());
        assertEquals("Unexpected number of clusters", 0,
                proj.getNumOfClusters());
        assertNull("Unexpected cluster type", proj.getClusterType());

        proj = new ProjectileDefinition(1, "bullet", "image.jpeg", 100, 10, 5.0,
                13, false, 0, false, 0, true, 4, cluster);
        assertEquals("Unexpected ID", 1, proj.getId());
        assertEquals("Unexpected sprite", "image.jpeg", proj.getSprite());
        assertEquals("Unexpected name", "bullet", proj.getName());
        assertEquals("Unexpected WIDTH", 100, proj.getWidth());
        assertEquals("Unexpected HEIGHT", 10, proj.getHeight());
        assertTrue("Unexpected mass", Math.abs(5.0 - proj.getMass()) < 0.001);
        assertTrue("Unexpected damage", Math.abs(13 - proj.getDamage()) < 0.01);
        assertFalse("Unexpected timesOut", proj.timesOut());
        assertEquals("Unexpected life ticks", 0, proj.getLifeTicks());
        assertFalse("Unexpected explodes", proj.explodes());
        assertEquals("Unexpected blast radius", 0, proj.getBlastRadius());
        assertTrue("Unexpected clustering action", proj.makesClusters());
        assertEquals("Unexpected number of clusters", 4,
                proj.getNumOfClusters());
        assertEquals("Unexpected cluster type", cluster, proj.getClusterType());
    }
    
    @Test
    public void testFifthConstructor() {
    	ProjectileDefinition cluster = new ProjectileDefinition(0, "", "", 0, 0,
                0, 0);
        ProjectileDefinition testProj = new ProjectileDefinition(1, "bullet", 
        		"image.jpeg", 100, 10, 5.0,
                13, false, 0, false, 0, true, 4, cluster);
        ProjectileDefinition proj = new ProjectileDefinition(testProj);
        
        assertEquals("Unexpected ID", 1, proj.getId());
        assertEquals("Unexpected sprite", "image.jpeg", proj.getSprite());
        assertEquals("Unexpected name", "bullet", proj.getName());
        assertEquals("Unexpected width", 100, proj.getWidth());
        assertEquals("Unexpected height", 10, proj.getHeight());
        assertTrue("Unexpected mass", Math.abs(5.0 - proj.getMass()) < 0.001);
        assertTrue("Unexpected damage", Math.abs(13 - proj.getDamage()) < 0.01);
        assertFalse("Unexpected timesOut", proj.timesOut());
        assertEquals("Unexpected life ticks", 0, proj.getLifeTicks());
        assertFalse("Unexpected explodes", proj.explodes());
        assertEquals("Unexpected blast radius", 0, proj.getBlastRadius());
        assertTrue("Unexpected clustering action", proj.makesClusters());
        assertEquals("Unexpected number of clusters", 4,
                proj.getNumOfClusters());
        assertEquals("Unexpected cluster type", cluster, proj.getClusterType());
    }
}
