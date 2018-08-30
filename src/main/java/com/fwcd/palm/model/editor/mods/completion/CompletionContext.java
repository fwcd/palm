package com.fwcd.palm.model.editor.mods.completion;

import com.fwcd.palm.model.editor.PalmEditorModel;

public class CompletionContext {
	private final PalmEditorModel textModel;
	private final String delta;
	private final int offset;
	private final String word;
	
	public CompletionContext(PalmEditorModel textModel, String delta, int offset) {
		this.textModel = textModel;
		this.delta = delta;
		this.offset = offset;
		
		word = textModel.getWordAt(offset);
	}
	
	public String getDelta() { return delta; }
	
	public int getOldOffset() { return offset; }
	
	public int getNewOffset() { return offset + delta.length(); }
	
	public PalmEditorModel getTextModel() { return textModel; }
	
	public String getOldWord() { return word; }
	
	public String getNewWord() { return word + delta; }
}
