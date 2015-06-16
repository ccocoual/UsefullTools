package usefull.tools.test;

import static org.junit.Assert.*;

import org.junit.Test;

import usefull.tools.TextUtils;

public class TextUtilsTest {

	@Test
	public void test() {
		String str = "Bonjour, comment allez-vous ?";
		
		assertTrue("Str contains vowels", TextUtils.containVowels(str));
		assertEquals("How many vowels in String :"+str,9, TextUtils.countVowels(str));
		assertTrue("A is a vowel", TextUtils.isVowel('A'));
	}

}
