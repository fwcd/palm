package com.fwcd.palm.view.editor.viewmods;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import com.fwcd.palm.view.editor.PalmEditorView;
import com.fwcd.palm.view.theme.ThemedElement;

public class CurrentLineHighlight implements EditorViewModule {
	@Override
	public void renderBG(Graphics2D g2d, Dimension canvasSize, PalmEditorView editor) {
		FontMetrics metrics = editor.getFontMetrics();
		int padding = 5;
		int caretLine = editor.getCaretLine();

		int width = (int) canvasSize.getWidth();
		int height = metrics.getHeight() + padding;
		int x = 0;
		int y = caretLine * metrics.getHeight();

		g2d.setColor(editor.getTheme().get().colorOf(ThemedElement.LINE_HIGHLIGHT).orElse(getColor(editor.getBGColor())));
		g2d.fillRect(x, y, width, height);

	}

	private Color getColor(Color color) {
		if (color.getRed() > 128 && color.getGreen() > 128 && color.getBlue() > 128) {
			return modify(color, -15);
		} else {
			return modify(color, 15);
		}
	}

	private Color modify(Color color, int delta) {
		int r = color.getRed() + delta;
		int g = color.getGreen() + delta;
		int b = color.getBlue() + delta;

		if (r <= 255 && r >= 0
				&& g <= 255 && g >= 0
				&& b <= 255 && b >= 0) {
			return new Color(r, g, b);
		} else {
			return color;
		}
	}
}
