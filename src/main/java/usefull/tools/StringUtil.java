package parsifal.toolbox.business.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class StringUtil {

	public static String fold(List<? extends Object> elements, String separator) {
		
		if ( elements == null ) return "";
		
		String result = "";
		
		for ( int i = 0; i < elements.size() - 1; i++) {
			result += elements.get(i).toString() + separator;
		}
		if ( elements.size() > 0 ) {
			result += elements.get(elements.size() - 1).toString();
		}
		return result;
	}
	public static String fold(HashMap<? extends Object, ? extends Object> elements, String separator) {
		
		if ( elements == null ) return "";
		
		String result = "";
		
		
		// First item
		Iterator<? extends Object> it = elements.keySet().iterator(); 
		if ( it.hasNext()) {
			Object key = it.next();
			Object value = elements.get(key);
			result += key + " = " + value;
		}
		
		// Next item
		while ( it.hasNext()) {
			Object key = it.next();
			Object value = elements.get(key);
			result += separator + key + " = " + value;
		}
		
		return result;
	}
	
	
	public static String padRight(String s, int n) {
	     return String.format("%1$-" + n + "s", s);  
	}

	public static String padLeft(String s, int n) {
	    return String.format("%1$" + n + "s", s);  
	}
}
