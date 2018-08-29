package com.fwcd.palm.viewmodel.editor.mods.highlighting;

public interface SyntaxHighlighter {
	void performHighlighting(String text, TextStyler styler);
}
