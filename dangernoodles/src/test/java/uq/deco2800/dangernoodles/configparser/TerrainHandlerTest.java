package uq.deco2800.dangernoodles.configparser;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TerrainHandlerTest {

    @Test
    public void testStringToIntList1() {
        int[] testArray = {1, 0, 1, 0};
        assertEquals("\"1,0,1,0\" converted to list",
        		intArrayAsList(testArray),
        		new TerrainHandler().convertStringOfNumsToIntList("1,0,1,0"));
    }
    
    @Test
    public void testStringToIntList2() {
        int[] testArray = {100, 73, 84};
        
        assertEquals("\"100,  73 ,84 \" converted to list",
        		intArrayAsList(testArray),
        		new TerrainHandler(
        				).convertStringOfNumsToIntList("100,  73 ,84 "));
    }
    
    @Test
    public void testTagProcessor() {
    	int[] testArray1 = {1, 0, 1, 0};
    	int[] testArray2 = {73, 84, 1, 7};
    	int[] testArray3 = {4, 1, 0, 8};
    	List<List<Integer>> testList = new ArrayList<List<Integer>>();
    	
        testList.add(intArrayAsList(testArray1));
        testList.add(intArrayAsList(testArray2));
        testList.add(intArrayAsList(testArray3));
        
        TerrainHandler handler = new TerrainHandler();
        handler.tagProcessor("layer", "1,0,1,0");
        handler.tagProcessor("layer", " 73, 84,  1,   7");
        handler.tagProcessor("layer", "  4,1, 0  , 8 ");
        
        assertEquals("Tag processor",
        		testList,
        		handler.getTerrainList());
    }
    
    
    
    private List<Integer> intArrayAsList(int[] array) {
    	List<Integer> list = new ArrayList<Integer>();
    	for (Integer number : array) {
    		list.add(number);
    	}
    	return list;
    }

}
