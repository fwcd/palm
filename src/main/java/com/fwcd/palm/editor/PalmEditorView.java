package com.fwcd.palm.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FontMetrics;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;

import com.fwcd.fructose.Observable;
import com.fwcd.fructose.swing.View;
import com.fwcd.palm.editor.completion.CompletionProvider;
import com.fwcd.palm.editor.completion.NoCompletionProvider;
import com.fwcd.palm.editor.highlighting.NoHighlighting;
import com.fwcd.palm.editor.highlighting.SyntaxHighlighting;
import com.fwcd.palm.editor.modules.AutoCompletionController;
import com.fwcd.palm.editor.modules.AutoCompletionView;
import com.fwcd.palm.editor.modules.CurrentLineHighlight;
import com.fwcd.palm.editor.modules.EditorControllerModule;
import com.fwcd.palm.editor.modules.EditorViewModule;
import com.fwcd.palm.editor.modules.Indentation;
import com.fwcd.palm.editor.modules.SyntaxHighlightingView;
import com.fwcd.palm.model.PalmDocument;
import com.fwcd.palm.theme.DarkTheme;
import com.fwcd.palm.theme.Theme;
import com.fwcd.palm.theme.ThemedElement;
import com.fwcd.palm.utils.DocumentAdapter;
import com.fwcd.palm.utils.PalmException;
import com.fwcd.palm.utils.PalmScrollPane;

public class PalmEditorView implements View, Keybindable {
	private final JPanel view;
	private final PalmScrollPane scrollPane;
	private final TextBufferView textBuffer;
	private final List<EditorControllerModule> typingModules = new ArrayList<>();
	private final EditorConfig config = new EditorConfig();
	
	private final Observable<CompletionProvider> completionProvider = new Observable<>(new NoCompletionProvider());
	private final Observable<SyntaxHighlighting> highlighting = new Observable<>(new NoHighlighting());
	private final Observable<Theme> theme = new Observable<>(new DarkTheme());
	
	private PalmDocument model;

	public PalmEditorView() {
		textBuffer = new TextBufferView(this);
		textBuffer.setTheme(theme);

		scrollPane = new PalmScrollPane(textBuffer.getComponent(), theme);

		view = new JPanel();
		view.setLayout(new BorderLayout());
		theme.listenAndFire(it -> {
			view.setBackground(it.colorOf(ThemedElement.EDITOR_BG).orElse(it.bgColor()));
		});
		
		view.add(scrollPane, BorderLayout.CENTER);

		setModel(new PalmDocument());

		List<EditorViewModule> viewModules = getViewModules();
		AutoCompletionView completion = new AutoCompletionView(completionProvider, theme);
		
		typingModules.add(new Indentation());
		typingModules.add(new AutoCompletionController(completion, this));
		viewModules.add(new SyntaxHighlightingView(this, highlighting));
		viewModules.add(new CurrentLineHighlight());
		viewModules.add(completion);

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

	private void setModel(PalmDocument model) {
		this.model = model;
		textBuffer.setModel(model);
		model.addDocumentListener(new DocumentAdapter() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				if (!model.isSilent()) {
					String delta = model.getText(e.getOffset(), e.getLength());

					SwingUtilities.invokeLater(() -> {
						for (EditorControllerModule module : typingModules) {
							module.onInsert(delta, e.getOffset(), PalmEditorView.this);
						}
					});
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				if (!model.isSilent()) {
					SwingUtilities.invokeLater(() -> {
						for (EditorControllerModule module : typingModules) {
							module.onRemove(e.getLength(), e.getOffset(), PalmEditorView.this);
						}
					});
				}
			}
		});
	}

	public void save(File file) {
		try {
			textBuffer.getFG().write(new FileWriter(file));
		} catch (IOException e) {
			throw new PalmException(e);
		}
	}

	public void open(File file) {
		try {
			open(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			throw new PalmException(e);
		}
	}

	public void open(InputStream stream) {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
			setModel(new PalmDocument());
			String line;
			while ((line = reader.readLine()) != null) {
				model.appendLine(line);
			}
		} catch (IOException e) {
			throw new PalmException(e);
		}
	}

	public void focus() {
		textBuffer.getFG().requestFocusInWindow();
	}

	public synchronized void insert(int offset, String delta) {
		try {
			model.insertString(offset, delta, null);
		} catch (BadLocationException e) {
			throw new PalmException(e);
		}
	}

	public synchronized void insertSilently(int offset, String delta) {
		model.insertSilently(offset, delta);
	}

	public synchronized void removeSilently(int offset, int length) {
		model.removeSilently(offset, length);
	}

	public synchronized void remove(int offset, int length) {
		try {
			model.remove(offset, length);
		} catch (BadLocationException e) {
			throw new RuntimeException(e);
		}
	}

	public synchronized void removeSilentlyBeforeCaret(int length) {
		removeSilently(getCaretOffset() - length, length);
	}

	public synchronized void removeSilentlyAfterCaret(int length) {
		removeSilently(getCaretOffset(), length);
	}

	public synchronized void insertSilentlyBeforeCaret(String delta) {
		insertSilently(getCaretOffset(), delta);
	}

	public synchronized void insertSilentlyAfterCaret(String delta) {
		int prevCaretPos = getCaretOffset();
		insertSilently(prevCaretPos, delta);
		setCaretOffset(prevCaretPos);
	}

	public int getCaretLine() { return model.getDefaultRootElement().getElementIndex(getCaretOffset()); }
	
	public int getCaretColumn() {
		int caretOffset = getCaretOffset();
		Element element = model.getDefaultRootElement().getElement(getCaretLine());
		if (element == null) {
			return 0;
		} else {
			return caretOffset - element.getStartOffset();
		}
	}

	public void setCaretOffset(int pos) { textBuffer.getFG().setCaretPosition(pos); }

	public int getCaretOffset() { return textBuffer.getFG().getCaretPosition(); }

	public List<EditorControllerModule> getTypingModules() { return typingModules; }

	public List<EditorViewModule> getViewModules() { return textBuffer.getModules(); }

	public void setFontSize(int size) {
		config.setSize(size);
		update();
	}

	private void update() {
		textBuffer.getFG().setFont(config.getFont());
	}

	public TextBufferView getTextBuffer() { return textBuffer; }
	
	@Override
	public JComponent getComponent() { return view; }

	public PalmDocument getModel() { return model; }

	public Color getBGColor() { return view.getBackground(); }

	public FontMetrics getFontMetrics() { return textBuffer.getFG().getGraphics().getFontMetrics(); }
	
	public Observable<Theme> getTheme() { return theme; }
	
	public Observable<SyntaxHighlighting> getHighlighting() { return highlighting; }
	
	public Observable<CompletionProvider> getCompletionProvider() { return completionProvider; }
}
