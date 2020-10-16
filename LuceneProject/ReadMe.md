Lucene PubMed Indexing for Keyword Searchs

This resource allows the user to do multiple activities including:
- Create indexes for XML files using Lucene and SAX parser 
  SAX parser acts as a utility to traverse keywords in the XML document and
  subsequently add them to the document and index
- Search for keywords within specified indexes

There are a number of classes used as follows:

LuceneTest
- Acts as the drver for the program and contains the main method

IndexManager
- Used as a utility to create new documents and add keywords

XMLHandler
- Manages the callbacks for parsing the XML document using SAX parser
- Calls on the IndexManager to add keywords to documents
- Each PubMed article has its own doc and each doc has keywords based on the XML

StopWatch
- A utility class for keeping track of start/stop times and the average run time
  of a function