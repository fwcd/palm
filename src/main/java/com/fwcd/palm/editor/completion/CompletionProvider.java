package com.fwcd.palm.editor.completion;

import java.util.List;

public interface CompletionProvider {
	List<CompletionElement> listCompletions(String text, int offset);
}
