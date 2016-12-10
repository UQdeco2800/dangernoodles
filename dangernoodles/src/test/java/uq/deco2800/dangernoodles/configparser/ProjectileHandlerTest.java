package uq.deco2800.dangernoodles.configparser;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;
import org.junit.Test;

import uq.deco2800.dangernoodles.weapons.ProjectileDefinition;

public class ProjectileHandlerTest {
	private List<ProjectileDefinition> testList1() {
		List<ProjectileDefinition> list = new ArrayList<ProjectileDefinition>();
		list.add(new ProjectileDefinition(1, "Shrapnel", "test.png", 3, 6, 
	    		0.01, 5));
		list.add(new ProjectileDefinition(2, "Grenade", "test.png", 3, 6, 0.01,
				5, true, 1, true, 2, true, 4, null));
	    return list;
	}
	
	private List<ProjectileDefinition> testList2() {
		List<ProjectileDefinition> list = new ArrayList<ProjectileDefinition>();
		list.add(new ProjectileDefinition(1, "Shrapnel", "test.png", 3, 6, 
	    		0.01, 5));
		list.add(new ProjectileDefinition(2, "Grenade", "test.png", 3, 6, 0.01,
				5, true, 1, true, 2, true, 4, list.get(0)));
	    return list;
	}

	/*@Test
	public void testInitialise() {
		ProjectileHandler defaultHandler = new ProjectileHandler();
		assertFalse("Failed to initialise empty handler", 
				defaultHandler.isProcessingClusters());

		ProjectileHandler clusterHandler = new ProjectileHandler(testList1());
		assertTrue("Failed to initialise cluster handler", 
				clusterHandler.isProcessingClusters());
	}*/

	@Test
	public void testDefault() {
		assertEquals("Basic projectiles are not equal",
				testList1().get(0),
				defaultTestEnvironment().getProjectileList().get(0));
		assertEquals("Projectiles with null cluster are not equal",
				testList1().get(1),
				defaultTestEnvironment().getProjectileList().get(1));
	}
	
	/*@Test
	public void testClusters() {
		assertEquals("Cluster processed projectile without cluster is not equal",
				testList2().get(0),
				clusterTestEnvironment().getProjectileList().get(0));
		assertEquals("Cluster processed projectile with cluster is not equal",
				testList2().get(1),
				clusterTestEnvironment().getProjectileList().get(1));
	}*/
	
	private ProjectileHandler defaultTestEnvironment() {
		ProjectileHandler defaultHandler = new ProjectileHandler();
		
		String[] tags = { "name", "sprite", "HEIGHT", "WIDTH", 
				"mass", "damage", "timesOut", "lifeTicks", "explodes", 
				"blastRadius", "makesClusters", "numOfClusters", 
				"clusterType" };
		
		String[] entry1 = { "Shrapnel", "test.png", "3", "6", "0.01",
		"5" };
		
		String[] entry2 = { "Grenade", "test.png", "3", "6", "0.01",
				"5", "true", "1", "true", "2", "true", "4", "1" };

		AttributesImpl attributes1 = new AttributesImpl();
		attributes1.addAttribute("", "", "id", "", "1");
		Attributes attribute1 = (Attributes) attributes1;
		
		try {
			defaultHandler.startElement("", "", "projectile", attribute1);
		} catch (Exception e) { }
		for (int i = 0; i < 6; i++) {
			defaultHandler.tagProcessor(tags[i], entry1[i]);
		}
		try {
			defaultHandler.endElement("", "", "projectile");
		} catch (Exception e) { }
		
		AttributesImpl attributes2 = new AttributesImpl();
		attributes2.addAttribute("", "", "id", "", "2");
		Attributes attribute2 = (Attributes) attributes2;
		
		try {
			defaultHandler.startElement("", "", "projectile", attribute2);
		} catch (Exception e) { }
		for (int i = 0; i < 13; i++) {
			defaultHandler.tagProcessor(tags[i], entry2[i]);
		}
		try {
			defaultHandler.endElement("", "", "projectile");
		} catch (Exception e) { }
		
		return defaultHandler;
	}
	
	/*private ProjectileHandler clusterTestEnvironment() {
		ProjectileHandler clusterHandler = new ProjectileHandler(
				defaultTestEnvironment().getProjectileList());
		
		AttributesImpl attributes1 = new AttributesImpl();
		attributes1.addAttribute("", "", "id", "", "1");
		Attributes attribute1 = (Attributes) attributes1;
		try {
			clusterHandler.startElement("", "", "projectile", attribute1);
			clusterHandler.endElement("", "", "projectile");
		} catch (Exception e) { }
		
		AttributesImpl attributes2 = new AttributesImpl();
		attributes2.addAttribute("", "", "id", "", "2");
		Attributes attribute2 = (Attributes) attributes2;
		try {
			clusterHandler.startElement("", "", "projectile", attribute2);
			clusterHandler.endElement("", "", "projectile");
		} catch (Exception e) { }
		
		return clusterHandler;
	}*/
}
