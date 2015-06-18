package usefull.tools;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Scanner;


public class TextUtils {
	public static void main(String[] args) {
	}
	
	public static boolean isVowel(char c) { return "AEIOUYaeiouy".indexOf(c) != -1; }
	
	public static boolean containVowels(String str){
		return 	str.toLowerCase().contains("a") ||
				str.toLowerCase().contains("e") ||
				str.toLowerCase().contains("i") ||
				str.toLowerCase().contains("o") ||
				str.toLowerCase().contains("u") ||
				str.toLowerCase().contains("y");
	}
	
	public static int countVowels(String str){
		int count = 0;
		for(int i=0;i<str.length();i++){
			switch (str.toLowerCase().charAt(i)) {
            	case 'a':
                case 'e':
                case 'i':
                case 'o':
                case 'u':
                case 'y':
                    count++;
                    break;
                default:
			}
		}
		return count;
	}
	
	public static ArrayList<String>  readFileByLine(String nomFichier){
		ArrayList<String> liste = new ArrayList<String>();
		Reader reader = null;
		try{
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(nomFichier))); 
		} catch (FileNotFoundException e) { 
			e.printStackTrace(); 
		} 
		Scanner scanner = new Scanner(reader);
		//System.out.println(scanner.next(Pattern.compile(separateur)));
		while (scanner.hasNextLine())
			liste.add(scanner.nextLine());
		scanner.close();
		return liste;
	}
	
	public static void afficherListe(ArrayList<Object> liste){
		for(int i=0; i< liste.size(); i++ ){
			System.out.println("Ligne " + i + " : " + liste.get(i).toString());
		}
	}
	
	public static String reverseString(String str){
		String reverse = "";
		for(int i=str.length() - 1 ; i>=0 ; i--){
			reverse = reverse + str.charAt(i);
		}
		return reverse;
	}
	
	public static String reverseEachTwoCaracter(String s){
		String res = "";
		String[] tab = s.split(" ");

		String str = "";
		for(int i =0; i < tab.length; i++){
			str = reverseEachTwoCaracterWithoutBlanks(tab[i]) + " ";
			res += str.equals(" ") ? "" : str;
		}

		res = res.trim();
		return res;
	}
	
	public static String reverseEachTwoCaracterWithoutBlanks(String s){
		String res = "";
		String lastChar = "";
		int stringSize = s.length();
		int endI = stringSize;
		
		if(!NumberUtils.isOdd(s.length())){
			lastChar = "" + s.charAt(stringSize-1);
			s = s.substring(0, stringSize-1);
			endI = stringSize-2;
		}
		
		for(int i = 0; i < endI; i+=2){
			res += reverseTwoCaracter(s.substring(i, i+2));
		}
		res = res + lastChar;
		return res;
	}
	
	/**
	 * @param s : String with 2 characters
	 */
	public static String reverseTwoCaracter(String s){
		char a = s.charAt(0);
		char b = s.charAt(1);
		return "" + b + a;
	}
}
