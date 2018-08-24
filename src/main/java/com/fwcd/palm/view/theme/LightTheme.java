package com.fwcd.palm.view.theme;

import java.awt.Color;

public class LightTheme extends TemplateTheme {
	public LightTheme() {
		setBgColor(Color.WHITE);
		setMildBgColor(Color.LIGHT_GRAY);
		setFgColor(Color.BLACK);
		setMildFgColor(Color.DARK_GRAY);
		set(ThemedElement.EDITOR_BG, Color.WHITE);
		set(ThemedElement.TOOLBAR, Color.GRAY);
		set(ThemedElement.LINE_HIGHLIGHT, new Color(0xdddddd));
		set(ThemedElement.SYNTAX_KEYWORD, Color.BLUE);
	}
}
