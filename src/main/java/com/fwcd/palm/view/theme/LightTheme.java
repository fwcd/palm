package com.fwcd.palm.view.theme;

import java.awt.Color;

public class LightTheme extends TemplateTheme {
	public LightTheme() {
		setBgColor(Color.WHITE);
		setMildBgColor(Color.LIGHT_GRAY);
		setFgColor(Color.BLACK);
		setMildFgColor(Color.DARK_GRAY);
		setPopupBgColor(Color.WHITE.darker());
		set(ThemedElement.EDITOR_BG, Color.WHITE);
		set(ThemedElement.TOOLBAR, Color.GRAY);
		set(ThemedElement.LINE_HIGHLIGHT, new Color(0xdddddd));
		set(SyntaxElement.KEYWORD, Color.BLUE);
		set(SyntaxElement.TYPE, Color.GREEN);
		set(SyntaxElement.VARIABLE, Color.CYAN);
		set(SyntaxElement.STRING, Color.ORANGE);
		set(SyntaxElement.COMMENT, Color.GREEN);
		set(SyntaxElement.FUNCTION, Color.BLUE);
		set(SyntaxElement.OTHER, Color.BLACK);
	}
}
