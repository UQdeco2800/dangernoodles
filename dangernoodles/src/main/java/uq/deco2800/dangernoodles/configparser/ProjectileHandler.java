package uq.deco2800.dangernoodles.configparser;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.SAXException;

import uq.deco2800.dangernoodles.weapons.ProjectileDefinition;

/**
 * @author torusse
 *
 */
public class ProjectileHandler extends BaseHandler {

    private String name;
    private String sprite;
    private int height;
    private int width;
    private double mass;
    private int damage;
    private boolean timesOut = false;
    private int lifeTicks = 0;
    private boolean explodes = false;
    private int blastRadius = 0;
    private boolean makesClusters = false;
    private int numOfClusters = 0;
    private int clusterType = -1;

    private List<ProjectileDefinition> projectileList;

    private String[] projectileTags = { "name", "sprite", "HEIGHT", "WIDTH",
        "mass", "damage", "timesOut", "lifeTicks", "explodes",
        "blastRadius", "makesClusters", "numOfClusters", "clusterType" 
    };
    
    /**
     * Constructor of the ProjectileHandler class, adds all the valid tags that
     * could be in the XML file.
     */
    public ProjectileHandler() {
        prepareTags();
        projectileList = new ArrayList<ProjectileDefinition>();
    }

    /**
     * Prepare for a projectile XML file to be processed.
     */
    private void prepareTags() {
        entryTypeName = "projectile";
        for (String tag : projectileTags) {
            taglist.add(tag);
            tags.put(tag, false);
        }
    }

    /**
     * Gets the list of ProjectileDefinitions generated from the XML file
     * 
     * @return list of ProjectileDefinitions
     */
    public List<ProjectileDefinition> getProjectileList() {
        return projectileList;
    }

    @Override
    public void endElement(String uri, String localName, String tagName)
            throws SAXException {
        if (tagName.equalsIgnoreCase(entryTypeName)) {
            projectileList.add(new ProjectileDefinition(id, name, sprite, 
            		width, height, mass, damage, timesOut, lifeTicks, 
            		explodes, blastRadius, makesClusters, numOfClusters, 
            		clusterType));
        }
    }
    

    @Override
    public void tagProcessor(String tag, String data) {
        switch (tag) {
            case "name":
                name = data;
                break;
            case "sprite":
                sprite = data;
                break;
            case "HEIGHT":
                height = Integer.parseInt(data);
                break;
            case "WIDTH":
                width = Integer.parseInt(data);
                break;
            case "mass":
                mass = Double.parseDouble(data);
                break;
            case "damage":
                damage = Integer.parseInt(data);
                break;
            case "timesOut":
                timesOut = Boolean.parseBoolean(data);
                break;
            case "lifeTicks":
                lifeTicks = Integer.parseInt(data);
                break;
            case "explodes":
                explodes = Boolean.parseBoolean(data);
                break;
            case "blastRadius":
                blastRadius = Integer.parseInt(data);
                break;
            case "makesClusters":
                makesClusters = Boolean.parseBoolean(data);
                break;
            case "numOfClusters":
                numOfClusters = Integer.parseInt(data);
                break;
            case "clusterType":
            	clusterType = Integer.parseInt(data);
            default:
                break;
    	}
    }

}
