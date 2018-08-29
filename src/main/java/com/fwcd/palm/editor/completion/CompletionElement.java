package com.fwcd.palm.editor.completion;

public class CompletionElement {
	private final CompletionType type;
	private final String label;
	private final String detail;
	
	public CompletionElement(CompletionType type, String label, String detail) {
		this.type = type;
		this.label = label;
		this.detail = detail;
	}
	
	public String getDetail() { return detail; }
	
	public String getLabel() { return label; }
	
	public CompletionType getType() { return type; }
}
