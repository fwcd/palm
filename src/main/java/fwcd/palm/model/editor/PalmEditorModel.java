package fwcd.palm.model.editor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.text.BadLocationException;
import javax.swing.text.Element;

import fwcd.fructose.Observable;
import fwcd.palm.model.TextEdit;
import fwcd.palm.model.TextRange;
import fwcd.palm.model.editor.PalmDocument;
import fwcd.palm.utils.PalmException;
import fwcd.palm.utils.TwoWayObservable;
import fwcd.palm.model.editor.mods.completion.CompletionProvider;
import fwcd.palm.model.editor.mods.completion.NoCompletionProvider;
import fwcd.palm.model.editor.mods.highlighting.NoHighlighting;
import fwcd.palm.model.editor.mods.highlighting.SyntaxHighlighter;

public class PalmEditorModel {
	private final PalmDocument document = new PalmDocument();
	private final Observable<CompletionProvider> completionProvider = new Observable<>(new NoCompletionProvider());
	private final Observable<SyntaxHighlighter> highlighting = new Observable<>(new NoHighlighting());
	private final TwoWayObservable<Integer> caretOffset = new TwoWayObservable<>(0);
	
	public Observable<CompletionProvider> getCompletionProvider() { return completionProvider; }
	
	public Observable<SyntaxHighlighter> getSyntaxHighlighter() { return highlighting; }
	
	public String getText() { return getDocument().getText(); }

	public String getText(int offset, int length) { return getDocument().getText(offset, length); }
	
	public String getWordAt(int offset) {
		int maxIndex = getTextLength() - 1;
		int left = offset;
		int right = offset;
		
		while ((left >= 0) && Character.isLetterOrDigit(document.charAt(left))) {
			left--;
		}
		left++;
		while ((right < maxIndex) && Character.isLetterOrDigit(document.charAt(right))) {
			right++;
		}
		
		int length = right - left;
		
		if (length > 0) {
			return getText(left, length);
		} else {
			return "";
		}
	}
	
	public synchronized void perform(TextEdit edit) {
		TextRange range = edit.getRange();
		try {
			document.replace(range.getOffset(), range.getLength(), edit.getNewText(), null);
		} catch (BadLocationException e) {
			throw new PalmException(e);
		}
	}
	
	public synchronized void performSilently(TextEdit edit) {
		TextRange range = edit.getRange();
		document.replaceSilently(range.getOffset(), range.getLength(), edit.getNewText());
	}
	
	public synchronized void insert(int offset, String delta) {
		try {
			getDocument().insertString(offset, delta, null);
		} catch (BadLocationException e) {
			throw new PalmException(e);
		}
	}

	public synchronized void insertSilently(int offset, String delta) { getDocument().insertSilently(offset, delta); }

	public synchronized void removeSilently(int offset, int length) { getDocument().removeSilently(offset, length); }

	public synchronized void remove(int offset, int length) {
		try {
			getDocument().remove(offset, length);
		} catch (BadLocationException e) {
			throw new RuntimeException(e);
		}
	}

	public synchronized void removeSilentlyBeforeCaret(int length) { removeSilently(caretOffset.getActual().get() - length, length); }

	public synchronized void removeSilentlyAfterCaret(int length) { removeSilently(caretOffset.getActual().get(), length); }

	public synchronized void insertSilentlyBeforeCaret(String delta) {
		insertSilently(caretOffset.getActual().get(), delta);
	}

	public synchronized void insertSilentlyAfterCaret(String delta) {
		int prevCaretPos = caretOffset.getActual().get();
		insertSilently(prevCaretPos, delta);
		caretOffset.getRequested().set(prevCaretPos);
	}

	public int getCaretLine() { return getDocument().getDefaultRootElement().getElementIndex(caretOffset.getActual().get()); }
	
	public int getCaretColumn() {
		int offset = caretOffset.getActual().get();
		Element element = getDocument().getDefaultRootElement().getElement(getCaretLine());
		if (element == null) {
			return 0;
		} else {
			return offset - element.getStartOffset();
		}
	}
	
	public String[] getLines() { return getDocument().getLines(); }
	
	public int getTextLength() { return getDocument().getLength(); }
	
	public PalmDocument getDocument() { return document; }
	
	public TwoWayObservable<Integer> getCaretOffset() { return caretOffset; }
	
	public void save(Path path) {
		try (BufferedWriter writer = Files.newBufferedWriter(path)) {
			writer.write(document.getText());
		} catch (IOException e) {
			throw new PalmException(e);
		}
	}

	public void open(Path path) {
		try (BufferedReader reader = Files.newBufferedReader(path)) {
			StringBuilder contents = new StringBuilder();
			String line = reader.readLine();
			
			while (line != null) {
				contents.append(line + PalmDocument.NEWLINE);
				line = reader.readLine();
			}
			
			document.setText(contents.toString());
		} catch (IOException e) {
			throw new PalmException(e);
		}
	}
}
