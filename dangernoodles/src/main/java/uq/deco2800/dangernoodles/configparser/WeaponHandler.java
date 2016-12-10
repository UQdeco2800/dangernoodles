package uq.deco2800.dangernoodles.configparser;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.SAXException;

import uq.deco2800.dangernoodles.weapons.ProjectileDefinition;
import uq.deco2800.dangernoodles.weapons.ProjectileDefinitionList;
import uq.deco2800.dangernoodles.weapons.WeaponDefinition;

/**
 * @author torusse
 *
 */
public class WeaponHandler extends BaseHandler {
	
    private String name;
    private String category;
    private String images;
    private int height;
    private int width;
    private boolean powered = false;
    private boolean makesProjectiles = false;
    private int numOfProjectiles = 0;
    private ProjectileDefinition projectileType = null;
    
    private ProjectileDefinitionList projectiles;
    private ArrayList<WeaponDefinition> weaponList;

    private String[] weaponTags = { "name", "category", "spritesheet", "HEIGHT",
        "WIDTH", "powered", "makesProjectiles",
        "numOfProjectiles", "projectileType"
    };

    /**
     * Constructor of the WeaponHandler class, adds all the valid tags that
     * could be in the XML file.
     */
    public WeaponHandler(ProjectileDefinitionList projectiles) {
    	this.projectiles = projectiles;
    	
        entryTypeName = "weapon";
        for (String tag : weaponTags) {
            taglist.add(tag);
            tags.put(tag, false);
        }

        weaponList = new ArrayList<WeaponDefinition>();
    }

    /**
     * Gets the list of WeaponDefinitions generated from the XML file
     * 
     * @return list of WeaponDefinitions
     */
    public List<WeaponDefinition> getWeaponList() {
        return weaponList;
    }

    @Override
    public void endElement(String uri, String localName, String tagName) 
            throws SAXException {
        if (tagName.equalsIgnoreCase(entryTypeName)) {
            weaponList.add(new WeaponDefinition(id, name, category, images, 
                    width, height, powered, makesProjectiles,
                    numOfProjectiles, projectileType));
        }
    }

    @Override
    public void tagProcessor(String tag, String data) {
        switch (tag) {
            case "name":
                name = data;
                break;
            case "category":
                category = data;
                break;
            case "spritesheet":
                images = data;
                break;
            case "HEIGHT":
                height = Integer.parseInt(data);
                break;
            case "WIDTH":
                width = Integer.parseInt(data);
                break;
            case "powered":
                if ("true".equals(data)) {
                    powered = true;
                } else {
                    powered = false;
                }
                break;
            default:
            	break;
        }
        if (projectiles != null) {
        	switch (tag) {
	            case "makesProjectiles":
	                if ("true".equals(data)) {
	                    makesProjectiles = true;
	                } else {
	                    makesProjectiles = false;
	                }
	                break;
	            case "numOfProjectiles":
	                numOfProjectiles = Integer.parseInt(data);
	                break;
	            case "projectileType":
	                projectileType = new ProjectileDefinition(
	                		projectiles.getProjectileByID(
	                				Integer.parseInt(data)));
	                break;
	            default:
	                break;
        	}
        }
    }
    
    
}
