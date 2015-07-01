package usefull.tools;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class XmlCompare {
	
	static {
		/**
		 * Pour corriger le bogue: "JDOM2: This parser does not support specification 'null' version 'null'"
		 */
		System.setProperty("javax.xml.parsers.SAXParserFactory", "com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl");
	}

	private static SAXBuilder builder = new SAXBuilder(); 
	private static boolean valueFilePassed = false;
	
	private static HashMap<String,String> values = new HashMap<String, String>();

	private static Document valuesFile;
	private static Document ref;
	private static Document toCheck;
	
	/**
	 * Le message d'erreur g�n�r� par le comparateur
	 */
	private String message;
	
	
	
	/**
	 * Instancie un nouveau comparateur XML
	 * @param referentContent le contenu du XML r�f�rent
	 * @param actualContent le contenu du XML qui va servir � la comparaison
	 * @param parameters les valeurs des param�tres que l'on a ins�r� dans le contenu du XML r�f�rent
	 */
	public XmlCompare(String referentContent, String actualContent, HashMap<String, String> parameters) {
		try
		{
			// Parameters
			this.values = parameters;
			
			// Load xml document from string
			SAXBuilder builderXml = new SAXBuilder();
			Reader readerReferent = new StringReader(referentContent);
			Reader readerActual = new StringReader(actualContent);
			ref = builderXml.build(readerReferent);
			toCheck = builderXml.build(readerActual);
			
		} catch (IOException e) {
		} catch (JDOMException e1){
			message = "Probleme de parsage des fichiers XML. Verifiez qu'ils sont conformes.\n" + e1.toString();
			System.err.println(message);
		}
	}
	
	/**
	 * Compare (structure et contenu) les deux documents XML.
	 * @return vrai si les documents sont �gaux, faux sinon
	 */
	public boolean compare() {
		try{
			//Deux documents sont identiques si leurs �l�ments root sont identiques
			areEquals(ref.getRootElement(), toCheck.getRootElement(), values);
		}
		catch(Exception except){
			message = except.toString();
			System.err.println(message);
			return false;
		}
		return true;
	}
	
	/**
	 * Renvoi le message d'erreur si le syst�me � detecter que les fichiers sont diff�rents
	 * @return le message d'erreur
	 */
	public String getMessage() {
		return message;
	}

	public static void main(String[] args){
		
		if (args.length == 3){
			try {
				valuesFile = builder.build(new File(args[2]));
				Element root = valuesFile.getRootElement();
				
				for(Element e : root.getChildren()){
					if (e.getName().equals("key")){
						values.put(e.getValue(), e.getAttributeValue("value"));
					}
				}
				
				valueFilePassed = true;
				
			} catch (IOException e) {
				System.err.println("Impossible de lire un fichier, veuillez verifier les paths.");
				System.err.println(e.toString());
			} catch (JDOMException e1) {
				System.err.println("Probleme de parsage des fichiers XML. Verifiez qu'ils sont conformes.");
				System.err.println(e1.toString());
			}
			
		}
		

		//On r�cup�re les fichiers pass�s dans les param�tres du jar
		if(args.length == 2 || valueFilePassed){
			try {
				ref = builder.build(new File(args[0]));
				toCheck = builder.build(new File(args[1]));
				
				//On v�rifie que les fichiers sont identiques, si ils ne le sont pas, au renvoie une erreur
				if(verifyDocument(ref, toCheck, values)) return;
				else System.err.println("Fail");

			} catch (IOException e) {
				System.err.println("Impossible de lire un fichier, veuillez verifier les paths.");
				System.err.println(e.toString());
			} catch (JDOMException e1){
				System.err.println("Probleme de parsage des fichiers XML. Verifiez qu'ils sont conformes.");
				System.err.println(e1.toString());
			}
		}
		else{
			System.err.println("    Wrong number of args, usage : \n"
					+ "\tjava -jar ScriptQTP.jar [RefFile] [ToCheckFile] [[ValuesFile]]");
		}
	}

	private static boolean verifyDocument(Document ref2, Document toCheck2, HashMap<String,String> values) {
		try{
			//Deux documents sont identiques si leurs �l�ments root sont identiques
			areEquals(ref2.getRootElement(),toCheck2.getRootElement(), values);
		}
		catch(Exception except){
			System.err.println(except.toString());
			return false;
		}
		return true;
	}

	private static boolean areEquals(Element e, Element e1, HashMap<String, String> values) throws Exception {
		//Deux �l�ments sont identiques si leur nom, leurs attributs, leurs valeurs et leurs fils sont �gaux
		if(!e.getName().equals(e1.getName())) throw new Exception("Les elements " + e.getName() + " et " + e1.getName() + " ne sont pas identiques !");
		if(!e.getChildren().isEmpty()){
			if(!(e.getChildren().size()==e1.getChildren().size())) throw new Exception("Les elements " + e.getName() + " n'ont pas le meme nombre de fils !");
			for (int i = 0; i < e.getChildren().size(); i++){
				//On v�rifie r�cursivement que tous les fils sont �gaux
				areEquals(e.getChildren().get(i), e1.getChildren().get(i), values);
			}
		}
		if(!(e.getAttributes().size()==e1.getAttributes().size()) && !verifyAttributes(e,e1)) throw new Exception("Les elements " + e.getName() + " n'ont pas les meme attributs !");
		if(!(e.getValue().equals(null) && e1.getValue().equals(null)) && !checkElement(e,e1)) throw new Exception("Les elements " + e.getName() + " n'ont pas la meme valeur !");
		return true;
	}


	private static boolean verifyAttributes(Element e, Element e1) throws Exception {
		//Deux attributs sont identiques si leur valeur v�rifie la r�gle de comparaison d�finie dans checkAttribute
		boolean attributeIsPresent = false;
		if(!(e.getAttributes().size() == e1.getAttributes().size())) return false;
		for(Attribute a : e.getAttributes()){
			for (Attribute a1 : e1.getAttributes()){
				if (a.getName().equals(a1.getName())){
					attributeIsPresent = true;
					if (!a.getValue().equals(null) && !checkAttribute(a,a1)) return false;
				}
			}
		}
		return attributeIsPresent;
	}

	//R�gle de comparaison entre deux attributs
	private static boolean checkAttribute(Attribute a, Attribute a1){
		if (a.getValue().startsWith("RegEx(")) return a1.getValue().matches(a.getValue().substring(6,a.getValue().length()-1));
		else return a1.getValue().trim().equals(a.getValue().trim());
	}

	//R�gle de comparaison entre deux �l�ments finaux
	private static boolean checkElement(Element e, Element e1){
		
		// V�rifier si le contenu de la balise est � ignor�
		if (e.getValue().trim().equals("*")) return true;
		
		// V�rifier valeurs � valeurs avec des $ 
		if (values.containsKey(e.getValue())) return e1.getValue().trim().equals(values.get(e.getValue().trim()));
		
		// V�rifier les cl�s balises/valeurs
		//if (values.containsKey(e.getName())) return e1.getValue().equals(values.get(e.getName()));
		
		if (!e.getChildren().isEmpty() && !e1.getChildren().isEmpty() && ((e.getValue().equals("") && e1.getValue().equals("")) || (e.getValue().contains("\n")) && e1.getValue().contains("\n"))) return true;
		if (e.getValue().startsWith("RegEx(")) return e1.getValue().matches(e.getValue().substring(6,e.getValue().length()-1));
		else return e1.getValue().trim().equals(e.getValue().trim());
	}
	
	
}
