package com.fwcd.palm.editor.modules;

import com.fwcd.fructose.Observable;
import com.fwcd.palm.editor.PalmEditorView;
import com.fwcd.palm.editor.highlighting.SyntaxHighlighting;
import com.fwcd.palm.editor.highlighting.TextStyler;

public class SyntaxHighlightingView implements EditorViewModule {
	private final Observable<SyntaxHighlighting> highlighting;
	private final TextStyler styler;

	public SyntaxHighlightingView(PalmEditorView editor, Observable<SyntaxHighlighting> highlighting) {
		this.highlighting = highlighting;
		styler = new TextStyler(editor.getTheme(), editor.getModel());
	}

	@Override
	public void format(String text, PalmEditorView editor) {
		highlighting.get().performHighlighting(text, styler);
	}
}
