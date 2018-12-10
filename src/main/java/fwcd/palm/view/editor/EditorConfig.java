package fwcd.palm.view.editor;

import java.awt.Font;

public class EditorConfig {
	private static final Font DEFAULT_FONT = new Font(Font.MONOSPACED, Font.PLAIN, 16);
	private Font font = DEFAULT_FONT;
	private boolean showLineHighlight = true;

	public void setFont(Font font) {
		this.font = font;
	}

	public void setSize(float size) {
		font = font.deriveFont(size);
	}

	public Font getFont() {
		return font;
	}
	
	public void setShowLineHighlight(boolean showLineHighlight) {
		this.showLineHighlight = showLineHighlight;
	}
	
	public boolean doesShowLineHighlight() {
		return showLineHighlight;
	}
}
