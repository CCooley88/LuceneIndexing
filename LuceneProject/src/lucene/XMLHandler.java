//CKulig BU CS 622 HW2 10/20
package lucene;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLHandler extends DefaultHandler{
	private static final String ARTICLETITLE = "ArticleTitle";
	private static final String KEYWORD = "Keyword";
	private static final String PUBMEDARTICLE = "PubmedArticle";
	private static final String PUBYEAR = "PubDate";
	private static final String ISSN = "Journal";
	private boolean articelTitle= true;
	private boolean keyword = true;
	private boolean pubMedArticle = true;
	private boolean pubDate = true;
	private boolean issnBool = true;
	private String articleTitleForPrint = "";
	private String issnForPrint = "";
	private IndexManager indexManager;
		
	public XMLHandler(IndexManager indexManager) {
		this.indexManager = indexManager;
	}
	
//	This is a callback method for the SAX API and is overridden to identify the tag SAX is on so 
//	other callback methods can take the appropriate action
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException{
		switch (qName) {
        case ARTICLETITLE:
        	articelTitle = true;
        	break;
        case KEYWORD:
        	keyword = true;
        	break;
        case PUBMEDARTICLE:
        	pubMedArticle = true;
        	break;
        case PUBYEAR:
        	pubDate = true;
        	break;
        case ISSN:
        	issnBool = true; 
        	break;
		}
	}
	
//	This is a callback method for the SAX API and is overridden to reset the articlePrinted 
//	and pubMedArticle bools, it also has the indexManager write the current doc when it is 
//	done adding keywords (when the final article tag is reached)
	@Override
	public void endElement(String uri, String localName, String qName) {
		// If this is the closing tag of the article then reset found keyword variable to false
				if(pubMedArticle) {
					pubMedArticle = false;
					indexManager.writeDoc();
				}
	}
	
//	This is a callback method for the SAX API and performs the heavy lifting for the class
//	by calling the indexManager to create a new doc, add the doc title, keywords, and issn.
//	A new doc is created for each new article and subsequent keywords are added as necessary
	@Override
	public void characters(char[] chars, int start, int length) throws SAXException{
		// If this is the article title element then assign the title for printing
		if(articelTitle) {
			articleTitleForPrint = new String(chars, start, length);
			articelTitle = false;
			indexManager.makeNewDoc();
			indexManager.addDocTitle(articleTitleForPrint);
		}		
		// If this is a keyword element and it contains the search term then indicate it was found
		if(keyword) {
			String keywordText = new String(chars, start, length);
			indexManager.addDocKeyword(keywordText);
			indexManager.addDocIssn(issnForPrint);
			keyword = false;
		}	
		if(pubDate) {
			new String(chars, start, length);
			pubDate = false;
		}
		if(issnBool) {
			issnForPrint = new String(chars, start, length);
			issnBool = false;			
		}
	}			
}
