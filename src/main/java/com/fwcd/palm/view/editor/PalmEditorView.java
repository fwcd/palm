package com.fwcd.palm.view.editor;

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

import com.fwcd.fructose.Observable;
import com.fwcd.fructose.swing.View;
import com.fwcd.palm.model.PalmDocument;
import com.fwcd.palm.utils.PalmException;
import com.fwcd.palm.view.editor.highlighting.EmptyHighlighting;
import com.fwcd.palm.view.editor.highlighting.SyntaxHighlighting;
import com.fwcd.palm.view.editor.modules.CurrentLineHighlight;
import com.fwcd.palm.view.editor.modules.EditorTypingModule;
import com.fwcd.palm.view.editor.modules.EditorViewModule;
import com.fwcd.palm.view.editor.modules.Indentation;
import com.fwcd.palm.view.editor.modules.SyntaxHighlighter;
import com.fwcd.palm.view.theme.LightTheme;
import com.fwcd.palm.view.theme.Theme;
import com.fwcd.palm.view.theme.ThemedElement;
import com.fwcd.palm.view.utils.DocumentAdapter;
import com.fwcd.palm.view.utils.PalmScrollPane;

public class PalmEditorView implements View {
	private final JPanel view;
	private final PalmScrollPane scrollPane;
	private final TextBufferView textBuffer;
	private final List<EditorTypingModule> typingModules = new ArrayList<>();
	private final EditorConfig config = new EditorConfig();
	
	private final Observable<SyntaxHighlighting> highlighting = new Observable<>(new EmptyHighlighting());
	private final Observable<Theme> theme = new Observable<>(new LightTheme());
	
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

		typingModules().add(new Indentation());
		viewModules().add(new SyntaxHighlighter(this, highlighting));
		viewModules().add(new CurrentLineHighlight());

		update();
	}

	public void addKeybind(KeyStroke bind, Runnable onPress) {
		textBuffer.addKeybind(bind, onPress);
	}

	private void setModel(PalmDocument model) {
		this.model = model;
		textBuffer.setModel(model);
		model.addDocumentListener(new DocumentAdapter() {

			@Override
			public void insertUpdate(DocumentEvent e) {
				if (!model.isSilent()) {
					String delta;
					try {
						delta = model.getText(e.getOffset(), e.getLength());
					} catch (BadLocationException ex) {
						throw new RuntimeException(ex);
					}

					SwingUtilities.invokeLater(() -> {
						for (EditorTypingModule module : typingModules) {
							module.onInsert(delta, e.getOffset(), PalmEditorView.this);
						}
					});
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				if (!model.isSilent()) {
					SwingUtilities.invokeLater(() -> {
						for (EditorTypingModule module : typingModules) {
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

	public String[] getLines() {
		try {
			return model.getText(0, model.getLength()).split(PalmDocument.NEWLINE, -1);
		} catch (BadLocationException e) {
			throw new PalmException(e);
		}
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
		removeSilently(getCaretPos() - length, length);
	}

	public synchronized void removeSilentlyAfterCaret(int length) {
		removeSilently(getCaretPos(), length);
	}

	public synchronized void insertSilentlyBeforeCaret(String delta) {
		insertSilently(getCaretPos(), delta);
	}

	public synchronized void insertSilentlyAfterCaret(String delta) {
		int prevCaretPos = getCaretPos();
		insertSilently(prevCaretPos, delta);
		setCaretPos(prevCaretPos);
	}

	public int getCaretLine() { return model.getDefaultRootElement().getElementIndex(getCaretPos()); }

	public void setCaretPos(int pos) {
		textBuffer.getFG().setCaretPosition(pos);
	}

	public int getCaretPos() { return textBuffer.getFG().getCaretPosition(); }

	public List<EditorTypingModule> typingModules() { return typingModules; }

	public List<EditorViewModule> viewModules() { return textBuffer.getModules(); }

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

	public String getText() { return getText(0, getTextLength()); }

	public String getText(int offset, int length) {
		try {
			return model.getText(offset, length);
		} catch (BadLocationException e) {
			return "";
		}
	}

	public int getTextLength() { return model.getLength(); }

	public PalmDocument getModel() { return model; }

	public Color getBGColor() { return view.getBackground(); }

	public FontMetrics getFontMetrics() { return textBuffer.getFG().getGraphics().getFontMetrics(); }
	
	public Observable<Theme> getTheme() { return theme; }
	
	public Observable<SyntaxHighlighting> getHighlighting() { return highlighting; }
}
