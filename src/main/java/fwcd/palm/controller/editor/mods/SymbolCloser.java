package fwcd.palm.controller.editor.mods;

import java.util.HashMap;
import java.util.Map;

import fwcd.palm.model.editor.PalmEditorModel;

public class SymbolCloser implements EditorControllerModule {
	private final Map<String, String> symbols = new HashMap<>();
	private String recentlyInserted = null;

	public SymbolCloser() {
		symbols.put("(", ")");
		symbols.put("'", "'");
		symbols.put("\"", "\"");
		symbols.put("[", "]");
		symbols.put("{", "}");
	}

	@Override
	public void onInsert(String delta, int offset, PalmEditorModel editor) {
		recentlyInserted = null;

		// FIXME: Bug - deleting wrong symbols when typing for example System.out.println("test")

		if (delta.length() == 1) {
			for (String symbol : symbols.keySet()) {
				String closeSymbol = symbols.get(symbol);

				if (delta.endsWith(symbol)) {
					editor.insertSilentlyAfterCaret(closeSymbol);
					recentlyInserted = symbol;
				} else if (delta.endsWith(closeSymbol) && offset != editor.getTextLength() - 1) {
					editor.removeSilentlyAfterCaret(1);
				}
			}
		}
	}

	@Override
	public void onRemove(int length, int offset, PalmEditorModel editor) {
		// FIXME: Fix bug with symbols that have equal opening and closing symbols

		for (String symbol : symbols.keySet()) {
			String closeSymbol = symbols.get(symbol);

			int posAfterCaret = offset - length + 1;
			if (recentlyInserted != null
					&& recentlyInserted.equals(symbol)
					&& editor.getDocument().getText(posAfterCaret, 1).equals(closeSymbol)) {
				editor.removeSilently(posAfterCaret, 1);
			}
		}
	}
}
