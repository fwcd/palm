package com.fwcd.palm.editor.modules;

public enum IndentationStyle {
	TABS("\t"),
	EIGHT_SPACES("        "),
	FOUR_SPACES("    "),
	THREE_SPACES("   "),
	TWO_SPACES("  "),
	ONE_SPACE(" ");

	private final String indent;

	private IndentationStyle(String indent) {
		this.indent = indent;
	}

	public String get() {
		return indent;
	}
}
