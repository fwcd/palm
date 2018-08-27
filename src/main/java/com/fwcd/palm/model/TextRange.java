package com.fwcd.palm.model;

public class TextRange {
	private final int offset;
	private final int length;
	
	public TextRange(int offset, int length) {
		this.offset = offset;
		this.length = length;
	}
	
	public static TextRange between(int startIndex, int endIndexExclusive) {
		return new TextRange(startIndex, endIndexExclusive - startIndex);
	}
	
	public int getOffset() { return offset; }
	
	public int getLength() { return length; }
}
