package com.fwcd.palm.model.editor.mods.highlighting;

public interface SyntaxHighlighter {
	void performHighlighting(String text, TextStyler styler);
}
