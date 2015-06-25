package usefull.tools.test;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

import usefull.tools.NumberUtils;
import usefull.tools.TextUtils;

public class UsefullToolsTest {

	@Test
	public void textUtilsTest() {
		String str = "";
		
		str = "Bonjour, comment allez-vous ?";
		assertTrue("Str contains vowels", TextUtils.containVowels(str));
		assertEquals("How many vowels in String :"+str,9, TextUtils.countVowels(str));
		assertTrue("A is a vowel", TextUtils.isVowel('A'));
		assertEquals("What is the reverse String of Bonjour","ruojnoB",TextUtils.reverseString("Bonjour"));

		assertEquals("What is the reverse String of Bonjour","ab",TextUtils.reverseEachTwoCaracterWithoutBlanks("ba"));
		assertEquals("What is the reverse String of Bonjour","oBjnuor",TextUtils.reverseEachTwoCaracterWithoutBlanks("Bonjour"));
		assertEquals("What is the reverse String of Bonjour","oBjnuor",TextUtils.reverseEachTwoCaracter("Bonjour"));
		assertEquals("What is the reverse String of Bonjour aurevoir a demain","oBjnuor uaerovri a edamni",TextUtils.reverseEachTwoCaracter("Bonjour aurevoir a demain"));
		assertEquals("What is the reverse String of     Bonjour       aurevoir a       demain    ","oBjnuor uaerovri a edamni",TextUtils.reverseEachTwoCaracter("    Bonjour       aurevoir a       demain    "));

		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("key1", "val1");
		map.put("key2", "val2");
		map.put("key2", "val2");
		map.put("key3", "val3");
		map.put("key4", "val4");
		assertTrue("Key4 in HashMap", TextUtils.isWordInHashMap(map, "val3"));

	}

	@Test
	public void numberUtilsTest(){
		assertFalse("3 is not an odd number",NumberUtils.isOdd(3));
		assertTrue("2 is an odd number",NumberUtils.isOdd(2));
		
		
		HashMap<Integer,Integer> map2 = new HashMap<Integer,Integer>();
		map2.put(0, 1);
		map2.put(1, 2);
		map2.put(2, 3);
		assertTrue("Key4 in HashMap", NumberUtils.isIntegerInHashMap(map2, 2));
	}

}
