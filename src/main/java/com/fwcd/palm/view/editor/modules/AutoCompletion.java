package com.fwcd.palm.view.editor.modules;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import com.fwcd.fructose.Observable;
import com.fwcd.palm.model.PalmDocument;
import com.fwcd.palm.view.editor.PalmEditorView;
import com.fwcd.palm.view.editor.completion.CompletionProvider;

public class AutoCompletion implements EditorTypingModule, EditorViewModule {
	private final Observable<CompletionProvider> provider;
	private boolean active = false;
	
	public AutoCompletion(Observable<CompletionProvider> provider) {
		this.provider = provider;
	}
	
	public void show(String text, int offset) {
		active = true;
	}
	
	@Override
	public void onInsert(String delta, int offset, PalmEditorView editor) {
		show(editor.getModel().getText(), offset);
	}
	
	@Override
	public void renderFG(Graphics2D g2d, Dimension canvasSize, PalmEditorView editor) {
		if (active) {
			FontMetrics metrics = editor.getFontMetrics();
			PalmDocument model = editor.getModel();
			int caretLine = editor.getCaretLine();
			int caretColumn = editor.getCaretColumn();
			String charsBeforeCaret = model.getLine(caretLine)
				.substring(0, caretColumn)
				.replace("\t", "    ");
			
			int width = 350; // TODO
			int height = 100; // TODO
			int x = metrics.stringWidth(charsBeforeCaret);
			int y = metrics.getHeight() * (caretLine + 1);
			
			g2d.setColor(Color.LIGHT_GRAY);
			g2d.fillRect(x, y, width, height);
		}
	}
}
