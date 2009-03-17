package es.eucm.eadventure.editor.control.writer.domwriters.lomes;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import es.eucm.eadventure.common.auxiliar.ReportDialog;
import es.eucm.eadventure.editor.data.meta.auxiliar.LOMClassificationTaxon;
import es.eucm.eadventure.editor.data.meta.auxiliar.LOMClassificationTaxonPath;
import es.eucm.eadventure.editor.data.meta.auxiliar.LOMTaxon;
import es.eucm.eadventure.editor.data.meta.ims.IMSClassification;
import es.eucm.eadventure.editor.data.meta.ims.IMSRights;
import es.eucm.eadventure.editor.data.meta.lomes.LOMESClassification;

public class LOMESClassificationDOMWriter extends LOMESSimpleDataWriter{
	
	/**
	 * Private constructor.
	 */
	private LOMESClassificationDOMWriter( ) {}

	public static Node buildDOM( LOMESClassification classification) {
		Element classificationElement = null;

		try {
			// Create the necessary elements to create the DOM
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance( );
			DocumentBuilder db = dbf.newDocumentBuilder( );
			Document doc = db.newDocument( );
			
			// Create the root
			classificationElement = doc.createElement("lomes:classification");
			classificationElement.setAttribute("vocElement", "classification");
			
			// Create the purpose node
			classificationElement.appendChild( buildVocabularyNode(doc,"lomes:purpose",classification.getPurpose()));
				
			// Create taxon path node
			for (int i=0; i<classification.getTaxonPath().getSize();i++){
        			Element taxonPath = doc.createElement("lomes:taxonPath");
        			Element source = doc.createElement( "lomes:source" );
        			source.setAttribute("vocElement", "source");
        			source.appendChild( buildLangStringNode(doc,((LOMClassificationTaxonPath)classification.getTaxonPath().get(i)).getSource()));
        			taxonPath.appendChild( source );
        			
        			// Create taxon node
        			LOMTaxon tax = ((LOMClassificationTaxonPath)classification.getTaxonPath().get(i)).getTaxon();
        			for (int j = 0; j<tax.getSize();j++){
                			Element taxon = doc.createElement("lomes:taxon");
                			Element identifier = doc.createElement("lomes:id");
                			identifier.setAttribute("vocElement", "id");
                			identifier.setTextContent(((LOMClassificationTaxon)tax.get(j)).getIdentifier());
                			taxon.appendChild(identifier);
                			
                			Element entry = doc.createElement( "lomes:entry" );
                			entry.appendChild( buildLangStringNode(doc,((LOMClassificationTaxon)tax.get(j)).getEntry()));
                			taxon.appendChild( entry );
                			
                			taxonPath.appendChild(taxon);
			}
			
			
			classificationElement.appendChild(taxonPath);
			}
			
			
			// Create the description node
			Element description = doc.createElement( "lomes:description" );
			description.appendChild( buildLangStringNode(doc,classification.getDescription()));
			classificationElement.appendChild( description );
			
			//Create the keyword node
			Element keyword = doc.createElement( "lomes:keyword" );
			keyword.appendChild( buildLangStringNode(doc, classification.getKeyword( )));
			classificationElement.appendChild( keyword );
			
			
		} catch( ParserConfigurationException e ) {
	    	ReportDialog.GenerateErrorReport(e, true, "UNKNOWERROR");
		}

		return classificationElement;
	}
}
