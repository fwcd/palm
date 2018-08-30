package com.fwcd.palm.view.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Rectangle;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;

import com.fwcd.fructose.Observable;
import com.fwcd.fructose.geometry.Vector2D;
import com.fwcd.fructose.swing.View;
import com.fwcd.palm.utils.PalmException;
import com.fwcd.palm.view.editor.mods.CurrentLineHighlight;
import com.fwcd.palm.view.editor.mods.EditorViewModule;
import com.fwcd.palm.view.editor.mods.highlighting.SyntaxHighlightingView;
import com.fwcd.palm.view.theme.LightTheme;
import com.fwcd.palm.view.theme.Theme;
import com.fwcd.palm.view.theme.ThemedElement;
import com.fwcd.palm.view.utils.PalmScrollPane;
import com.fwcd.palm.model.editor.PalmEditorModel;

public class PalmEditorView implements View, Keybindable {
	private final JPanel component;
	private final PalmScrollPane scrollPane;
	private final TextBufferView textBuffer;
	private final EditorConfig config = new EditorConfig();
	
	private final Observable<Theme> theme = new Observable<>(new LightTheme());
	private final PalmEditorModel viewModel;

	public PalmEditorView(PalmEditorModel viewModel) {
		this.viewModel = viewModel;
		
		textBuffer = new TextBufferView(this);
		textBuffer.setTheme(theme);
		textBuffer.getCaretListeners().add(e -> viewModel.getCaretOffset().getActual().set(e.getDot()));
		textBuffer.setModel(viewModel.getDocument());
		
		viewModel.getCaretOffset().getRequested().listen(offset -> textBuffer.setCaretOffset(offset));
		
		scrollPane = new PalmScrollPane(textBuffer.getComponent(), theme);

		component = new JPanel();
		component.setLayout(new BorderLayout());
		theme.listenAndFire(it -> {
			component.setBackground(it.colorOf(ThemedElement.EDITOR_BG).orElse(it.bgColor()));
		});
		
		component.add(scrollPane, BorderLayout.CENTER);

		List<EditorViewModule> modules = getModules();
		modules.add(new SyntaxHighlightingView(theme, viewModel.getDocument(), viewModel.getSyntaxHighlighter()));
		modules.add(new CurrentLineHighlight());
		
		update();
	}
	
	@Override
	public void addKeybind(String name, KeyStroke bind, Runnable onPress) {
		textBuffer.addKeybind(name, bind, onPress);
	}
	
	@Override
	public void removeKeybind(String name) {
		textBuffer.removeKeybind(name);
	}

	public void focus() {
		textBuffer.getFG().requestFocusInWindow();
	}

	public List<EditorViewModule> getModules() { return textBuffer.getModules(); }

	public void setFontSize(int size) {
		config.setSize(size);
		update();
	}

	private void update() {
		textBuffer.getFG().setFont(config.getFont());
	}
	
	@Override
	public JComponent getComponent() { return component; }

	public Color getBGColor() { return component.getBackground(); }

	public FontMetrics getFontMetrics() { return textBuffer.getFG().getGraphics().getFontMetrics(); }
	
	public Observable<Theme> getTheme() { return theme; }
	
	public PalmEditorModel getViewModel() { return viewModel; }
	
	public Vector2D getCaretPixelPosition() {
		try {
			JTextComponent fg = textBuffer.getFG();
			Rectangle rect = fg.getUI().modelToView(fg, viewModel.getCaretOffset().getActual().get());
			return new Vector2D(rect.getMinX(), rect.getMinY());
		} catch (BadLocationException e) {
			throw new PalmException(e);
		}
	}
}
