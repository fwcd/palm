package fwcd.palm.view.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Rectangle;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;

import fwcd.fructose.Observable;
import fwcd.fructose.geometry.Vector2D;
import fwcd.fructose.swing.View;
import fwcd.palm.model.editor.PalmEditorModel;
import fwcd.palm.utils.PalmException;
import fwcd.palm.view.editor.mods.CurrentLineHighlight;
import fwcd.palm.view.editor.mods.EditorViewModule;
import fwcd.palm.view.editor.mods.highlighting.SyntaxHighlightingView;
import fwcd.palm.view.theme.LightTheme;
import fwcd.palm.view.theme.Theme;
import fwcd.palm.view.theme.ThemedElement;
import fwcd.palm.view.utils.PalmScrollPane;

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
		modules.add(new CurrentLineHighlight(config));
		
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
	
	public void setShowLineHighlight(boolean showLineHighlight) {
		config.setShowLineHighlight(showLineHighlight);
		update();
	}
	
	public void setFont(Font font) {
		config.setFont(font);
		update();
	}
	
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
	
	public PalmEditorModel getModel() { return viewModel; }
	
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
