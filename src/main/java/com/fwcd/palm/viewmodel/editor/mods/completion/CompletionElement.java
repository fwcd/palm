package com.fwcd.palm.viewmodel.editor.mods.completion;

public class CompletionElement {
	private final CompletionType type;
	private final String label;
	private final String detail;
	private final String completion;
	
	public CompletionElement(CompletionType type, String label, String detail, String completion) {
		this.type = type;
		this.label = label;
		this.detail = detail;
		this.completion = completion;
	}
	
	public String getDetail() { return detail; }
	
	public String getLabel() { return label; }
	
	public CompletionType getType() { return type; }
	
	public String getCompletion() { return completion; }
}
