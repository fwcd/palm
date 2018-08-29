package com.fwcd.palm.view.editor.mods.highlighting;

import com.fwcd.fructose.Observable;
import com.fwcd.palm.model.editor.PalmDocument;
import com.fwcd.palm.view.editor.PalmEditorView;
import com.fwcd.palm.view.editor.mods.EditorViewModule;
import com.fwcd.palm.view.theme.Theme;
import com.fwcd.palm.viewmodel.editor.mods.highlighting.SyntaxHighlighter;
import com.fwcd.palm.viewmodel.editor.mods.highlighting.TextStyler;

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
