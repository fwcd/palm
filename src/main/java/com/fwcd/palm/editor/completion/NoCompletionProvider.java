package com.fwcd.palm.editor.completion;

import java.util.Collections;
import java.util.List;

public class NoCompletionProvider implements CompletionProvider {
	@Override
	public List<CompletionElement> listCompletions(String text, int offset) {
		return Collections.emptyList();
	}
}
