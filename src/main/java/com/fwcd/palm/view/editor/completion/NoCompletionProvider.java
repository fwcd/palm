package com.fwcd.palm.view.editor.completion;

import java.util.Collections;
import java.util.List;

public class NoCompletionProvider implements CompletionProvider {
	@Override
	public List<CompletionElement> getCompletions(String text, int offset) {
		return Collections.emptyList();
	}
}
