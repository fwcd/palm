package com.fwcd.palm.viewmodel.editor.mods.completion;

import com.fwcd.palm.viewmodel.editor.PalmEditorViewModel;

public class CompletionContext {
	private final PalmEditorViewModel textModel;
	private final String delta;
	private final int offset;
	private final String word;
	
	public CompletionContext(PalmEditorViewModel textModel, String delta, int offset) {
		this.textModel = textModel;
		this.delta = delta;
		this.offset = offset;
		
		word = textModel.getWordAt(offset);
	}
	
	public String getDelta() { return delta; }
	
	public int getOldOffset() { return offset; }
	
	public int getNewOffset() { return offset + delta.length(); }
	
	public PalmEditorViewModel getTextModel() { return textModel; }
	
	public String getOldWord() { return word; }
	
	public String getNewWord() { return word + delta; }
}
