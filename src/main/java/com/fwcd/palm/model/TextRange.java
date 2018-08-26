package com.fwcd.palm.model;

public class TextRange {
	private final int startOffset;
	private final int endOffset;
	
	public TextRange(int startOffset, int endOffset) {
		this.startOffset = startOffset;
		this.endOffset = endOffset;
	}
	
	public int getStartOffset() { return startOffset; }
	
	public int getEndOffset() { return endOffset; }
}
