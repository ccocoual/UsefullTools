package usefull.tools;

public class TextUtils {

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
}
