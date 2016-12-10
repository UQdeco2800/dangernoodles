package uq.deco2800.dangernoodles.configparser;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.SAXException;

/**
 * @author torusse
 *
 */
public class TerrainHandler extends BaseHandler {
    
    private List<List<Integer>> terrainList;

    private String[] terrainTags = { "layer" /* more tags can be added here */};

    /**
     * Constructor of the TerrainHandler class, adds all the valid tags that
     * could be in the XML file.
     */
    public TerrainHandler() {
        entryTypeName = "terrain";
        for (String tag : terrainTags) {
            taglist.add(tag);
            tags.put(tag, false);
        }
        
        terrainList = new ArrayList<List<Integer>>();
    }

    /**
     * Gets the list of the lists of numbers generated from the XML file
     * 
     * @return list of lists of numbers
     */
    public List<List<Integer>> getTerrainList() {
        return terrainList;
    }
    
    @Override
    public void endElement(String uri, String localName, String tagName) 
            throws SAXException { }

    @Override
    public void tagProcessor(String tag, String data) {
        if ("layer".equals(tag)) {
            terrainList.add(convertStringOfNumsToIntList(data));
        }
    }
    
    /**
     * Converts a string of comma separated numbers into a list of numbers
     * 
     * @param data
     *            a string of comma separated numbers
     * @return a list of numbers
     */
    public List<Integer> convertStringOfNumsToIntList(String data) {
    	List<Integer> nums = new ArrayList<Integer>();
    	String[] listOfNumbers = data.split(",");
    	
    	for(String number : listOfNumbers) {
    		nums.add(Integer.parseInt(number.trim()));
    	}
    	return nums;
    }
    
}
