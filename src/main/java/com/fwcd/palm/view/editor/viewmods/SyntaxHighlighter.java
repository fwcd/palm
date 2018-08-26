package com.fwcd.palm.view.editor.viewmods;

import com.fwcd.fructose.Observable;
import com.fwcd.palm.view.editor.PalmEditorView;
import com.fwcd.palm.view.editor.highlighting.SyntaxHighlighting;
import com.fwcd.palm.view.editor.highlighting.TextStyler;

public class SyntaxHighlighter implements EditorViewModule {
	private final Observable<SyntaxHighlighting> highlighting;
	private final TextStyler styler;

	public SyntaxHighlighter(PalmEditorView editor, Observable<SyntaxHighlighting> highlighting) {
		this.highlighting = highlighting;
		styler = new TextStyler(editor.getTheme(), editor.getDocument());
	}

	@Override
	public void format(String text, PalmEditorView editor) {
		highlighting.get().performHighlighting(styler);
	}
}
