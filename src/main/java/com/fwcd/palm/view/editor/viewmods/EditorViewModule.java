package com.fwcd.palm.view.editor.viewmods;

import java.awt.Dimension;
import java.awt.Graphics2D;

import com.fwcd.palm.view.editor.PalmEditorView;

public interface EditorViewModule {
	// TODO: Stuff like line highlights and syntax highlighters belong here

	default void format(String text, PalmEditorView editor) {}

	default void renderBG(Graphics2D g2d, Dimension canvasSize, PalmEditorView editor) {}

	default void renderFG(Graphics2D g2d, Dimension canvasSize, PalmEditorView editor) {}
}
