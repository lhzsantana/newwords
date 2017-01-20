package com.docs2data.crawlers.twotd.classifier;

public class LabeledWord {
	
	private String word;
	private POSGermanTag value;
	
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public POSGermanTag getValue() {
		return value;
	}
	public void setValue(POSGermanTag value) {
		this.value = value;
	}

}
