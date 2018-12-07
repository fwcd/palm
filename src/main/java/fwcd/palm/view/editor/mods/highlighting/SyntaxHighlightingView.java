package fwcd.palm.view.editor.mods.highlighting;

import fwcd.fructose.Observable;
import fwcd.palm.model.editor.PalmDocument;
import fwcd.palm.view.editor.PalmEditorView;
import fwcd.palm.view.editor.mods.EditorViewModule;
import fwcd.palm.view.theme.Theme;
import fwcd.palm.model.editor.mods.highlighting.SyntaxHighlighter;
import fwcd.palm.model.editor.mods.highlighting.TextStyler;

public class SyntaxHighlightingView implements EditorViewModule {
	private final Observable<SyntaxHighlighter> highlighting;
	private final TextStyler styler;

	public SyntaxHighlightingView(Observable<Theme> theme, PalmDocument document, Observable<SyntaxHighlighter> highlighting) {
		this.highlighting = highlighting;
		styler = new TextStyler(theme, document);
	}

	@Override
	public void format(String text, PalmEditorView editor) {
		highlighting.get().performHighlighting(text, styler);
	}
}
