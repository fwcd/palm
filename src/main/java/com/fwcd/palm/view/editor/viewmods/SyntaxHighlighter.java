package com.fwcd.palm.view.editor.viewmods;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fwcd.palm.model.languages.Language;
import com.fwcd.palm.view.editor.PalmEditor;
import com.fwcd.palm.view.theme.ThemedElement;
import com.fwcd.palm.view.utils.TextStyle;

public class SyntaxHighlighter implements EditorViewModule {
	private final Map<Pattern, TextStyle> syntax = new HashMap<>();

	public SyntaxHighlighter() {

	}

	public SyntaxHighlighter(PalmEditor editor, Language language) {
		StringBuilder keywordRegexBuilder = new StringBuilder();

		for (String keyword : language.getKeywords()) {
			keywordRegexBuilder.append("(\\b").append(keyword).append("\\b)|");
		}

		String keywordRegex = keywordRegexBuilder.substring(0, keywordRegexBuilder.length() - 1);
		Pattern pattern = Pattern.compile(keywordRegex);
		editor.getTheme().listenAndFire(theme -> {
			syntax.put(pattern, new TextStyle(true, theme.colorOf(ThemedElement.SYNTAX_KEYWORD).orElse(theme.fgColor())));
		});
	}

	@Override
	public void format(String text, PalmEditor editor) {
		for (Pattern pattern : syntax.keySet()) {
			Matcher matcher = pattern.matcher(text.replace("\n", ""));
			TextStyle style = syntax.get(pattern);

			while (matcher.find()) {
				int start = matcher.start();
				int end = matcher.end();
				int length = end - start;

				editor.getStyledDoc().setCharacterAttributes(start, length, style, true);
			}
		}
	}
}
