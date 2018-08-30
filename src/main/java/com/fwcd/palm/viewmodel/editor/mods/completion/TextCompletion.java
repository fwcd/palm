package com.fwcd.palm.viewmodel.editor.mods.completion;

import com.fwcd.palm.model.TextEdit;
import com.fwcd.palm.model.TextRange;

public class TextCompletion {
	private final TextEdit edit;
	
	public TextCompletion(TextEdit edit) {
		this.edit = edit;
	}
	
	public TextCompletion(int offset, String inserted) {
		edit = new TextEdit(new TextRange(offset, 0), inserted);
	}
	
	public TextEdit getEdit() { return edit; }
}
