package com.fwcd.palm.view.editor.viewmods;

import java.awt.Dimension;
import java.awt.Graphics2D;

import com.fwcd.palm.view.editor.PalmEditor;

public interface EditorViewModule {
	// TODO: Stuff like line highlights and syntax highlighters belong here

	default void format(String text, PalmEditor editor) {}

	default void renderBG(Graphics2D g2d, Dimension canvasSize, PalmEditor editor) {}

	default void renderFG(Graphics2D g2d, Dimension canvasSize, PalmEditor editor) {}
}
