package com.fwcd.palm.model.editor.mods.completion;

import java.util.Collections;
import java.util.List;

public class NoCompletionProvider implements CompletionProvider {
	@Override
	public List<CompletionElement> listCompletions(CompletionContext context) {
		return Collections.emptyList();
	}
}
