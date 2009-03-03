package es.eucm.eadventure.editor.control.writer.domwriters.lom;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import es.eucm.eadventure.editor.data.lom.LangString;
import es.eucm.eadventure.editor.data.lom.Vocabulary;

public class LOMSimpleDataWriter {

	public static Node buildVocabularyNode (Document doc, String name,Vocabulary vocabulary,boolean scorm){
		Element vocElement = null;

			vocElement = doc.createElement( "imsmd:" +name );
			
			//Create the source element
			Node source = doc.createElement( "imsmd:source" );
			source.appendChild( buildStringNode(doc, vocabulary.getSource( ),scorm) );
			vocElement.appendChild( source );
			
			//Create the value element
			Node value = doc.createElement("imsmd:value" );
			value.appendChild( buildStringNode(doc, vocabulary.getValue( ),scorm) );
			vocElement.appendChild( value );
			

		return vocElement;
		
	}
	
	public static Node buildLangStringNode (Document doc, LangString lang,boolean scorm){
		Element langElement = null;

			langElement = doc.createElement(scorm?"imsmd:string": "imsmd:langstring" );
			langElement.setAttribute( scorm?"language":"xml:lang", lang.getLanguage( 0 ) );
			langElement.setTextContent( lang.getValue( 0 ) );

		return langElement;

	}
	
	public static Node buildStringNode (Document doc, String lang, boolean scorm){
		return buildLangStringNode (doc, new LangString("x-none", lang),scorm);
	}
	
	 
	public static boolean isStringSet(String string){
		return string != null && !string.equals( "" );
	}
	
	public static boolean isStringSet (LangString string){
		return string!= null && string.getLanguage( 0 )!= null && !string.getLanguage( 0 ).equals( "" ) &&
		string.getValue( 0 )!= null && !string.getValue( 0 ).equals( "" );
	}
}
