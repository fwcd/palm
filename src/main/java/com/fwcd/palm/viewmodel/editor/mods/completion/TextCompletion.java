package com.fwcd.palm.viewmodel.editor.mods.completion;

import com.fwcd.palm.model.TextEdit;
import com.fwcd.palm.model.TextRange;
import com.fwcd.palm.utils.Strings;

public class TextCompletion {
	private final TextEdit edit;
	
	public TextCompletion(TextEdit edit) {
		this.edit = edit;
	}
	
	public TextCompletion(int offset, String inserted) {
		edit = new TextEdit(new TextRange(offset, 0), inserted);
	}
	
	public static TextCompletion ofRest(CompletionContext context, String suggestedWord) {
		int i = Strings.matchedCharsFromStart(suggestedWord, context.getNewWord());
		return new TextCompletion(context.getNewOffset(), suggestedWord.substring(i));
	}
	
	public TextEdit getEdit() { return edit; }
}
