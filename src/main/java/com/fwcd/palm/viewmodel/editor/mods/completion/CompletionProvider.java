package com.fwcd.palm.viewmodel.editor.mods.completion;

import java.util.List;

public interface CompletionProvider {
	List<CompletionElement> listCompletions(String text, int offset);
}
