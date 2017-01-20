package com.docs2data.crawlers.twotd.indexer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.misc.HighFreqTerms;
import org.apache.lucene.misc.HighFreqTerms.DocFreqComparator;
import org.apache.lucene.misc.TermStats;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

public class Indexing {
	

	private static StandardAnalyzer analyzer = new StandardAnalyzer();
	private static IndexWriterConfig config = new IndexWriterConfig(analyzer);

	private static Directory index = new RAMDirectory();

	public void index(String content) throws IOException{
		//Directory index = FSDirectory.open(new File("index-dir"));

		IndexWriter writer = new IndexWriter(index, config);
		
		
		Document document = new Document();

		document.add(new TextField("content", content, Field.Store.YES));
		
		writer.addDocument(document);
		                                
		writer.commit();
		
		//writer.close();
	}

	public class MyComparator implements Comparator<TermStats> {

	    public int compare(TermStats t1, TermStats t2){
	       if(t1.totalTermFreq >  t2.totalTermFreq) return -1;
	       if(t1.totalTermFreq == t2.totalTermFreq) return 0;
	       return 1;
	    }
	}
	
	public void listMostFrequent() throws Exception{

	    IndexReader reader = DirectoryReader.open(index);
	    
	    DocFreqComparator cmp = new HighFreqTerms.DocFreqComparator();
	    TermStats[] highFreqTerms = HighFreqTerms.getHighFreqTerms(reader, 1000,"content", cmp);

	    ArrayList<TermStats> terms = new ArrayList<>();
	    for (TermStats ts : highFreqTerms) {
	    	terms.add(ts);
	    }
	    
	    Collections.sort(terms, new Indexing.MyComparator());
	    
	    for (TermStats ts : terms) {
	    	System.out.println(ts);
	    }
	    
	}
	public List<TermStats> getListMostFrequent() throws Exception{

	    IndexReader reader = DirectoryReader.open(index);
	    
	    DocFreqComparator cmp = new HighFreqTerms.DocFreqComparator();
	    TermStats[] highFreqTerms = HighFreqTerms.getHighFreqTerms(reader, 1000,"content", cmp);

	    ArrayList<TermStats> termStats = new ArrayList<>();
	    for (TermStats ts : highFreqTerms) {
	    	termStats.add(ts);
	    }
	    
	    Collections.sort(termStats, new Indexing.MyComparator());
	 
	    return termStats;
	}
	
	public void cleanIndex() throws IOException{
		
		index.close();
	}
	
}
                                         