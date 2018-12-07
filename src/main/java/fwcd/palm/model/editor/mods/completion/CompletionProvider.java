package fwcd.palm.model.editor.mods.completion;

import java.util.List;

public interface CompletionProvider {
	List<CompletionElement> listCompletions(CompletionContext context);
}
