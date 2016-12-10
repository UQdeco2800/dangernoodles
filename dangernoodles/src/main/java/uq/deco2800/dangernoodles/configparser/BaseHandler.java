package uq.deco2800.dangernoodles.configparser;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This class is the base handler for all configuration file parsers.
 * 
 * @author torusse
 *
 */
public abstract class BaseHandler extends DefaultHandler {

    protected String entryTypeName;
    protected int id;

    protected ArrayList<String> taglist = new ArrayList<String>();
    protected HashMap<String, Boolean> tags = new HashMap<String, Boolean>();

    @Override
    public void startElement(String uri, String localName, String tagName, 
            Attributes attributes) throws SAXException {
        if (tagName.equalsIgnoreCase(entryTypeName)) {
            id = Integer.parseInt(attributes.getValue("id"));
        }
        for (String tag : taglist) {
            if (tagName.equalsIgnoreCase(tag)) {
                tags.put(tag, true);
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        for (String tag : taglist) {
            if (tags.get(tag)) {
                tagProcessor(tag, new String(ch, start, length));
                tags.put(tag, false);
                break;
            }
        }
    }

    /**
     * Takes a tag and the data associated with it and applies it to a variable
     * (perhaps using a switch statement)
     * 
     * @param tag
     *            the current tag being read by the parser
     * @param data
     *            the String stored within the tag
     */
    public abstract void tagProcessor(String tag, String data);
    
    /**
     * Getter method for the name of the XML entry type
     * eg. <weapon></weapon> -> is "weapon"
     * 
     * @return String of entry type name
     */
    public String getEntryTypeName() {
        return entryTypeName;
    }
    
    /**
     * Getter method for the id of the entry
     * 
     * @return the id integer
     */
    public int getId() {
        return id;
    }
    
    /**
     * Getter method for the list of tags supported by the handler
     * 
     * @return copy of the tags list
     */
    public List<String> getTaglist() {
        List<String> list = new ArrayList<String>();
        list.addAll(taglist);
        return list;
    }

    /**
     * Getter method for the HashMap of type (tag, isRead)
     * 
     * @return tags HashMap
     */
    public Map<String, Boolean> getTags() {
        Map<String, Boolean> list = new HashMap<String, Boolean>();
        list.putAll(tags);
        return list;
    }
}
