package com.fwcd.palm.view.editor.completion;

import java.util.List;

public interface CompletionProvider {
	List<CompletionElement> getCompletions(String text, int offset);
}
