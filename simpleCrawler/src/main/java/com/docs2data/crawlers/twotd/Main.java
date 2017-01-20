package com.docs2data.crawlers.twotd;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.lucene.misc.TermStats;
import org.apache.lucene.store.RAMDirectory;

import com.docs2data.crawlers.twotd.classifier.Parser;
import com.docs2data.crawlers.twotd.crawler.LanguageCrawler;

public class Main {
	
	private final static String PCG_MODEL_GERMAN = "edu/stanford/nlp/models/lexparser/germanPCFG.ser.gz";
	private final static String PCG_MODEL_FRENCH = "edu/stanford/nlp/models/lexparser/frenchFactored.ser.gz";
	private final static String PCG_MODEL_SPANISH = "edu/stanford/nlp/models/lexparser/spanishPCFG.ser.gz";

	static String[] germanSeeds = { "http://www.spiegel.de/", "http://www.bild.de/", "https://www.welt.de/",
			"http://www.sueddeutsche.de/", "http://www.faz.net/" };

	static String[] spanishSeeds = { "http://www.lanacion.com.ar/", "http://www.pagina12.com.ar/", "http://elpais.com/?cp=1"};

	static String[] frenchSeeds = { "http://www.lefigaro.fr/", "https://charliehebdo.fr", "http://www.lemonde.fr/"};

	static String[] englishSeeds = { "http://www.washingtonpost.com", "http://www.thetimes.co.uk",  "http://www.nytimes.com", "http://www.cnn.com"};

	
	private static String date = "20.01.2017";
	
	public static void main(String[] args) throws Exception {

		doGerman();
		//doSpanish();
		//doFrench();
		//doEnglish();
	}

	private static void doGerman() throws Exception{
		List<TermStats> words = craw(germanSeeds,"german");
		parse(new Parser(PCG_MODEL_GERMAN), "german", words, "de");
	}
	private static void doSpanish() throws Exception{
		List<TermStats> words = craw(spanishSeeds,"spanish");
		parse(new Parser(PCG_MODEL_SPANISH), "spanish", words, "es");
		
	}
	private static void doFrench() throws Exception{
		List<TermStats> words = craw(frenchSeeds,"french");
		parse(new Parser(PCG_MODEL_FRENCH), "french", words, "fr");
		
	}
	private static void doEnglish() throws Exception{
		List<TermStats> words = craw(englishSeeds, "english");
		parse(new Parser(), "english", words, "en");
	}
	
	private static List<TermStats> craw(String[] seeds, String language) throws Exception {

		LanguageCrawler crawler = new LanguageCrawler();

		return crawler.getWords(language, seeds);
	}
	
	static void parse(Parser parser, String fileName, List<TermStats> germanWords, String translate) throws IOException {

		int i = 1;

		List<String> lines = new ArrayList<>();
		Path file = Paths.get("/languages/"+fileName+"-"+date+".html");

		String word;

		for (TermStats ts : germanWords) {
			word = ts.termtext.utf8ToString();

			String line = "<tr>" + "<th scope='row'>" + i++ + "</th>" + "<td>" + word + "</td><td>"
					+ parser.parse(word) + "</td>" + "</td><td>" + ts.totalTermFreq + "</td>"
					+ "</td><td><a href='https://translate.google.com/#"+translate+"/en/" + word + "'>" + word + "</a></td>"

					+ "</tr>";

			
			System.out.println(line);
			lines.add(line);
		}

		Files.write(file, lines, Charset.forName("UTF-8"));
	}

}
