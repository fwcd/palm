package com.fwcd.palm.viewmodel.editor.mods.highlighting;

import java.util.HashMap;

import com.fwcd.fructose.Observable;
import com.fwcd.palm.model.TextRange;
import com.fwcd.palm.model.editor.PalmDocument;
import com.fwcd.palm.utils.FontStyle;
import com.fwcd.palm.view.theme.SyntaxElement;
import com.fwcd.palm.view.theme.Theme;
import com.fwcd.palm.view.utils.TextStyle;

public class TextStyler {
	private final Observable<Theme> theme;
	private final PalmDocument document;
	
	public TextStyler(Observable<Theme> theme, PalmDocument document) {
		this.theme = theme;
		this.document = document;
	}
	
	public void clearAll() {
		clear(new TextRange(0, 0));
	}
	
	public void clear(TextRange range) {
		document.setCharacterAttributes(range.getOffset(), range.getLength(), new TextStyle(new HashMap<>()), true);
	}
	
	public void colorize(TextRange range, SyntaxElement element) {
		TextStyle style = new TextStyle(theme.get().colorOf(element).orElseGet(theme.get()::fgColor));
		document.setCharacterAttributes(range.getOffset(), range.getLength(), style, true);
	}
	
	public void colorize(TextRange range, SyntaxElement element, FontStyle fontStyle) {
		TextStyle style = new TextStyle(fontStyle, theme.get().colorOf(element).orElseGet(theme.get()::fgColor));
		document.setCharacterAttributes(range.getOffset(), range.getLength(), style, true);
	}
}
