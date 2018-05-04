package com.fwcd.palm.theme;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class TemplateTheme implements Theme {
	private final Map<ThemedElement, Color> elementMap = new HashMap<>();
	private Color bgColor = Color.WHITE;
	private Color mildBgColor = Color.LIGHT_GRAY;
	private Color fgColor = Color.BLACK;
	private Color mildFgColor = Color.DARK_GRAY;

	@Override
	public Color bgColor() {
		return bgColor;
	}

	@Override
	public Color mildBGColor() {
		return mildBgColor;
	}

	@Override
	public Color fgColor() {
		return fgColor;
	}

	@Override
	public Color mildFGColor() {
		return mildFgColor;
	}

	@Override
	public Optional<Color> colorOf(ThemedElement element) {
		return Optional.ofNullable(elementMap.get(element));
	}

	protected void set(ThemedElement element, Color color) {
		elementMap.put(element, color);
	}

	protected void setBgColor(Color bgColor) {
		this.bgColor = bgColor;
	}

	protected void setMildBgColor(Color mildBgColor) {
		this.mildBgColor = mildBgColor;
	}

	protected void setFgColor(Color fgColor) {
		this.fgColor = fgColor;
	}

	protected void setMildFgColor(Color mildFgColor) {
		this.mildFgColor = mildFgColor;
	}
}
