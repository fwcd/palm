package fwcd.palm.model.editor.mods.highlighting;

import fwcd.fructose.Observable;
import fwcd.palm.model.TextRange;
import fwcd.palm.model.editor.PalmDocument;
import fwcd.palm.utils.FontStyle;
import fwcd.palm.view.theme.SyntaxElement;
import fwcd.palm.view.theme.Theme;
import fwcd.palm.view.utils.TextStyle;

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
		document.setCharacterAttributes(range.getOffset(), range.getLength(), null, true);
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
