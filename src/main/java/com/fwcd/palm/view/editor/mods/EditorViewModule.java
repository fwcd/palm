package com.fwcd.palm.view.editor.mods;

import java.awt.Dimension;
import java.awt.Graphics2D;

import com.fwcd.palm.view.editor.PalmEditorView;

public interface EditorViewModule {
	default void format(String text, PalmEditorView editor) {}

	default void renderBG(Graphics2D g2d, Dimension canvasSize, PalmEditorView editor) {}

	default void renderFG(Graphics2D g2d, Dimension canvasSize, PalmEditorView editor) {}
}
