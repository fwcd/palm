package com.fwcd.palm.theme;

import java.awt.Color;
import java.util.Optional;

public interface Theme {
	Color bgColor();

	Color mildBGColor();

	Color fgColor();

	Color mildFGColor();

	Optional<Color> colorOf(ThemedElement element);

	Optional<Color> colorOf(SyntaxElement element);
}
