package usefull.tools.test;

import static org.junit.Assert.*;

import org.junit.Test;

import usefull.tools.NumberUtils;
import usefull.tools.TextUtils;

public class UsefullToolsTest {

	@Test
	public void textUtilsTest() {
		String str = "Bonjour, comment allez-vous ?";
		
		assertTrue("Str contains vowels", TextUtils.containVowels(str));
		assertEquals("How many vowels in String :"+str,9, TextUtils.countVowels(str));
		assertTrue("A is a vowel", TextUtils.isVowel('A'));
	}
	
	@Test
	public void numberUtilsTest(){
		assertFalse("3 is not an odd number",NumberUtils.isOdd(3));
		assertTrue("2 is an odd number",NumberUtils.isOdd(2));
	}

}
