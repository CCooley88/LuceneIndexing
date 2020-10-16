//CKulig BU CS 622 HW2 10/20
package lucene;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

public class LuecneTest {

	public static void main(String[] args) {
		  String selectedOption = "-1";
		  String searchTerm = "";
		  String indexDestination = "";
		  Scanner scanner = new Scanner(System.in); 
		  String sourceFile = "";
		  int numberOfRuns;
		  int hits = 5;
		  IndexManager indexManager = new IndexManager();
		  System.out.println("Welcome to the Lucene based PubMed topic searcher. Options as follows:");
	      printMenu();
	       
	       System.out.println("Enter command:");
			 selectedOption = scanner.nextLine();
			    while(!selectedOption.equals("0")){
			    	switch (selectedOption) {	
				    case "1":		
				    	System.out.println("Enter the location of the file to be indexed:");
				    	sourceFile = scanner.nextLine();
				    	System.out.println("Enter the location where the index will be written to:");
				    	indexDestination = scanner.nextLine();
			    		System.out.println("Parsing index from source...");
			    		parseDocument(sourceFile, indexManager, indexDestination);
			    		break;
				    case "2":		    		
			    		System.out.println("Enter search term:");
			    		searchTerm = scanner.nextLine();
			    		System.out.println("How many results would you like to show:");
			    		hits = Integer.parseInt(scanner.nextLine());
			    		System.out.println("Enter the location of the index:");
				    	indexDestination = scanner.nextLine();
			    		indexManager.searchDocs(searchTerm, hits, indexDestination);
			    		break;
				    case "3":
				    	System.out.println("How many times would you like to run the search function:");
				    	numberOfRuns = Integer.parseInt(scanner.nextLine());
				    	System.out.println("Enter search term:");
			    		searchTerm = scanner.nextLine();
			    		System.out.println("How many results would you like to show:");
			    		hits = Integer.parseInt(scanner.nextLine());
			    		System.out.println("Enter the location of the index:");
				    	indexDestination = scanner.nextLine();
				    	StopWatch stopWatch = new StopWatch(numberOfRuns);
				    	for(int i =0;i<numberOfRuns;i++) {
				    		stopWatch.start();
				    		indexManager.searchDocs(searchTerm, hits, indexDestination);
				    		stopWatch.stop();
				    	}
				    	System.out.println("\n\n\n");
				    	System.out.println("The average time in ms was:" + stopWatch.averageTime()/1000000 + " based on " + numberOfRuns + " iterations of the method.");
			    		break;
		    	}
		     System.out.println();
		     printMenu();
		   	 System.out.println("Enter command:");
			 selectedOption = scanner.nextLine(); 
		    }		    
	}
	
   public static void printMenu() {    	
		System.out.println("1 - Create index");
	   	System.out.println("2 - Search keyword");
	   	System.out.println("3 - Time Trials");
	   	System.out.println("0 - Exit");
    }
   
//	Calls the SAXParser and XML handler class, takes the sourcePath, the indexManager object, and 
//   the target location of the index as arguments
	public static void parseDocument(String sourcePath, IndexManager indexManager, String targetLocation) {	
		try {			
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			// set the location of the index
			indexManager.setIndex(FSDirectory.open(Paths.get(targetLocation)));
			// the handler uses the indexManager
			XMLHandler handler = new XMLHandler(indexManager);	
			saxParser.parse(sourcePath.toString(), handler);
		
		}
		catch(IOException ex) {
			System.out.println("Invalid file location.");
			ex.printStackTrace();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}	
	}
	

}