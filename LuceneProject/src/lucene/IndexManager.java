//CKulig BU CS 622 HW2 10/20
package lucene;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

public class IndexManager {
	public IndexManager(String indexFileLocation, String title) {
		try {
			index = FSDirectory.open(Paths.get(indexFileLocation));
		} catch (IOException e) {
			System.out.println("Invalid file path.");
			e.printStackTrace();
		}
		doc.add(new StringField("title", title, Field.Store.YES));
	}	
	
	public IndexManager() {	};	
	
	private FSDirectory index;
	private StandardAnalyzer analyzer = new StandardAnalyzer();
	private Document doc;

	public void makeNewDoc() {
		doc = new Document();
	}
	
	public FSDirectory getIndex() {
		return index;
	}

	public void setIndex(FSDirectory indexFileLocation) {
			index = indexFileLocation;
	}

	public StandardAnalyzer getAnalyzer() {
		return analyzer;
	}

	public void setAnalyzer(StandardAnalyzer analyzer) {
		this.analyzer = analyzer;
	}

	public Document getDoc() {
		return doc;
	}

	public void setDoc(Document doc) {
		this.doc = doc;
	}

//	Adds another keyword to the doc, a doc can have many keywords associated with it.
	public void addDocKeyword(String keyword) {			
		doc.add(new TextField("keyword", keyword, Field.Store.YES));	
	}
	
//	Adds the title to the doc, a doc only has one title.
	public void addDocTitle(String title) {		
		doc.add(new StringField("title", title, Field.Store.YES));	
	}
	
//	Adds the issn to the doc, a doc only has one issn.
	public void addDocIssn(String issn) {	
		doc.add(new StringField("issn", issn, Field.Store.YES));		
	}
	
//	This creates a new config from the objects analyzer, a new writer using the objects index 
//	and the recently created config. It then writes the document to the specified location
//	and closes the writer.
	public void writeDoc() {		
		try {
			IndexWriterConfig config = new IndexWriterConfig(analyzer);
			IndexWriter writer = new IndexWriter(index, config);
			writer.addDocument(doc);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	This method is a utility function that is used by the driver class to search for keywords. 
//	It takes an index location argument as the user may have already created the index repo.
//	 numHits is the number of keyword matches the search will return. 
	public void searchDocs(String keyword, int numHits, String indexLocation){		
		try {
		index = FSDirectory.open(Paths.get(indexLocation));
		String querystr = new String(keyword);
		Query q = new QueryParser("keyword", analyzer).parse(querystr);
		IndexReader reader = DirectoryReader.open(index);
		IndexSearcher searcher = new IndexSearcher(reader);
		TopDocs docs = searcher.search(q, numHits);
		ScoreDoc[] hits = docs.scoreDocs;
		System.out.println("Found " + hits.length + " hits.");
		for (int i = 0; i < hits.length; ++i) {
			int docId = hits[i].doc;
			Document d = searcher.doc(docId);
			System.out.println((i + 1) + ". issn: " + d.get("issn") + "\t title: " + d.get("title"));
		}
		reader.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ParseException ex2) {
			ex2.printStackTrace();
		}
	}
}