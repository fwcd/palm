package com.fwcd.palm.model;

public class TextEdit {
	private final TextRange range;
	private final String newText;
	
	public TextEdit(TextRange range, String newText) {
		this.range = range;
		this.newText = newText;
	}
	
	public String getNewText() { return newText; }
	
	public TextRange getRange() { return range; }
}
