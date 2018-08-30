package com.fwcd.palm.model.editor.mods.completion;

public class CompletionElement {
	private final CompletionType type;
	private final String label;
	private final String detail;
	private final TextCompletion completion;
	
	public CompletionElement(CompletionType type, String label, String detail, TextCompletion completion) {
		this.type = type;
		this.label = label;
		this.detail = detail;
		this.completion = completion;
	}
	
	public String getDetail() { return detail; }
	
	public String getLabel() { return label; }
	
	public CompletionType getType() { return type; }
	
	public TextCompletion getCompletion() { return completion; }
}
