package com.fwcd.palm.languages;

public class Java implements ProgrammingLang {
	private final String[] keywords = {
			"abstract",
			"assert",
			"boolean",
			"break",
			"byte",
			"case",
			"catch",
			"char",
			"class",
			"const",
			"continue",
			"default",
			"do",
			"double",
			"else",
			"enum",
			"extends",
			"final",
			"finally",
			"float",
			"for",
			"goto",
			"if",
			"implements",
			"import",
			"instanceof",
			"int",
			"interface",
			"long",
			"native",
			"new",
			"package",
			"private",
			"protected",
			"public",
			"return",
			"short",
			"static",
			"strictfp",
			"super",
			"switch",
			"synchronized",
			"this",
			"throw",
			"throws",
			"transient",
			"try",
			"void",
			"volatile",
			"while",
			"true",
			"false"
	};

	private final String[] extensions = {
			"java"
	};

	@Override
	public String[] getKeywords() {
		return keywords;
	}

	@Override
	public String[] getFileExtensions() {
		return extensions;
	}
}
