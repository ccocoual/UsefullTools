package usefull.tools;

import java.util.HashMap;
import java.util.Iterator;

public class NumberUtils {

	public static boolean isOdd(Integer x){return (x % 2 != 1);}
	
	public static boolean isIntegerInHashMap(HashMap<Integer,Integer> map, int value){
		int key;
		Iterator<Integer> it = map.keySet().iterator();
		while(it.hasNext()){
			key = it.next();
			if(map.get(key) == value)
				return true;
		}
		return false;
	}
}
