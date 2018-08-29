package com.fwcd.palm.viewmodel.editor.mods.completion;

import java.util.Collections;
import java.util.List;

public class NoCompletionProvider implements CompletionProvider {
	@Override
	public List<CompletionElement> listCompletions(String text, int offset) {
		return Collections.emptyList();
	}
}
