package com.docs2data.crawlers.twotd.classifier;

import java.io.StringReader;
import java.util.List;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.trees.Tree;

public class Parser {
	
	private final TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(),
			"invertible=true");

	private final LexicalizedParser parser;

	public Parser(String model){
		parser = LexicalizedParser.loadModel(model);
	}

	public Parser(){
		parser = LexicalizedParser.loadModel();		
	}
	
	public String parse(String str) {
		List<CoreLabel> tokens = tokenize(str);
		Tree tree = parser.apply(tokens);
		

		List<Tree> leaves = tree.getLeaves();
		// Print words and Pos Tags
		for (Tree leaf : leaves) {
			Tree parent = leaf.parent(tree);
			
			return parent.label().value();
			//System.out.print(leaf.label().value() + "-" + parent.label().value() + " ");
		}
		return null;
		
	}
	
	private List<CoreLabel> tokenize(String str) {
		Tokenizer<CoreLabel> tokenizer = tokenizerFactory.getTokenizer(new StringReader(str));
		return tokenizer.tokenize();
	}
	
}