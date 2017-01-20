package com.docs2data.crawlers.twotd.crawler;

import java.util.List;

import org.apache.lucene.misc.TermStats;

import com.docs2data.crawlers.twotd.indexer.Indexing;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class LanguageCrawler {

	public List<TermStats> getWords(String language, String ... seeds) throws Exception{

		Indexing indexing = new Indexing();
		
		String crawlStorageFolder = "/data/crawl/root/2/"+System.currentTimeMillis()+language+System.currentTimeMillis();
		int numberOfCrawlers = 7;

		CrawlConfig config = new CrawlConfig();
		config.setCrawlStorageFolder(crawlStorageFolder);
		config.setMaxDepthOfCrawling(0);
		/*
		 * Instantiate the controller for this crawl.
		 */
		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		
		CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
                                                                      
		/*
		 * For each crawl, you need to add some seed urls. These are the first
		 * URLs that are fetched and then the crawler starts following links
		 * which are found in these pages
		 */
		                                                                                                      
		for(String seed : seeds){
			controller.addSeed(seed);
		}

		/*
		 * Start the crawl. This is a blocking operation, meaning that your code
		 * will reach the line after this only when crawling is finished.
		 */
		controller.start(MyCrawler.class, numberOfCrawlers);

		List<TermStats> words = indexing.getListMostFrequent();
		
		//indexing.cleanIndex();
		
		return words;
	}
}
