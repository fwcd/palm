package com.fwcd.palm.view.editor.highlighting;

import com.fwcd.fructose.Observable;
import com.fwcd.palm.model.PalmDocument;
import com.fwcd.palm.model.TextRange;
import com.fwcd.palm.view.theme.SyntaxElement;
import com.fwcd.palm.view.theme.Theme;

public class TextStyler {
	private final Observable<Theme> theme;
	private final PalmDocument document;
	
	public TextStyler(Observable<Theme> theme, PalmDocument document) {
		this.theme = theme;
		this.document = document;
	}
	
	public void colorize(TextRange range, SyntaxElement style) {
		// TODO
	}
}
