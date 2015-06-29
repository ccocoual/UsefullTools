package parsifal.toolbox.business.util;

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
	 * Le message d'erreur généré par le comparateur
	 */
	private String message;
	
	
	
	/**
	 * Instancie un nouveau comparateur XML
	 * @param referentContent le contenu du XML référent
	 * @param actualContent le contenu du XML qui va servir à la comparaison
	 * @param parameters les valeurs des paramètres que l'on a inséré dans le contenu du XML référent
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
	 * @return vrai si les documents sont égaux, faux sinon
	 */
	public boolean compare() {
		try{
			//Deux documents sont identiques si leurs éléments root sont identiques
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
	 * Renvoi le message d'erreur si le système à detecter que les fichiers sont différents
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
		

		//On récupère les fichiers passés dans les paramètres du jar
		if(args.length == 2 || valueFilePassed){
			try {
				ref = builder.build(new File(args[0]));
				toCheck = builder.build(new File(args[1]));
				
				//On vérifie que les fichiers sont identiques, si ils ne le sont pas, au renvoie une erreur
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
			//Deux documents sont identiques si leurs éléments root sont identiques
			areEquals(ref2.getRootElement(),toCheck2.getRootElement(), values);
		}
		catch(Exception except){
			System.err.println(except.toString());
			return false;
		}
		return true;
	}

	private static boolean areEquals(Element e, Element e1, HashMap<String, String> values) throws Exception {
		//Deux éléments sont identiques si leur nom, leurs attributs, leurs valeurs et leurs fils sont égaux
		if(!e.getName().equals(e1.getName())) throw new Exception("Les elements " + e.getName() + " et " + e1.getName() + " ne sont pas identiques !");
		if(!e.getChildren().isEmpty()){
			if(!(e.getChildren().size()==e1.getChildren().size())) throw new Exception("Les elements " + e.getName() + " n'ont pas le meme nombre de fils !");
			for (int i = 0; i < e.getChildren().size(); i++){
				//On vérifie récursivement que tous les fils sont égaux
				areEquals(e.getChildren().get(i), e1.getChildren().get(i), values);
			}
		}
		if(!(e.getAttributes().size()==e1.getAttributes().size()) && !verifyAttributes(e,e1)) throw new Exception("Les elements " + e.getName() + " n'ont pas les meme attributs !");
		if(!(e.getValue().equals(null) && e1.getValue().equals(null)) && !checkElement(e,e1)) throw new Exception("Les elements " + e.getName() + " n'ont pas la meme valeur !");
		return true;
	}


	private static boolean verifyAttributes(Element e, Element e1) throws Exception {
		//Deux attributs sont identiques si leur valeur vérifie la règle de comparaison définie dans checkAttribute
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

	//Règle de comparaison entre deux attributs
	private static boolean checkAttribute(Attribute a, Attribute a1){
		if (a.getValue().startsWith("RegEx(")) return a1.getValue().matches(a.getValue().substring(6,a.getValue().length()-1));
		else return a1.getValue().trim().equals(a.getValue().trim());
	}

	//Règle de comparaison entre deux éléments finaux
	private static boolean checkElement(Element e, Element e1){
		
		// Vérifier si le contenu de la balise est à ignoré
		if (e.getValue().trim().equals("*")) return true;
		
		// Vérifier valeurs à valeurs avec des $ 
		if (values.containsKey(e.getValue())) return e1.getValue().trim().equals(values.get(e.getValue().trim()));
		
		// Vérifier les clés balises/valeurs
		//if (values.containsKey(e.getName())) return e1.getValue().equals(values.get(e.getName()));
		
		if (!e.getChildren().isEmpty() && !e1.getChildren().isEmpty() && ((e.getValue().equals("") && e1.getValue().equals("")) || (e.getValue().contains("\n")) && e1.getValue().contains("\n"))) return true;
		if (e.getValue().startsWith("RegEx(")) return e1.getValue().matches(e.getValue().substring(6,e.getValue().length()-1));
		else return e1.getValue().trim().equals(e.getValue().trim());
	}
	
	
}
