package com.fwcd.palm.viewmodel.editor;

import javax.swing.text.BadLocationException;
import javax.swing.text.Element;

import com.fwcd.fructose.Observable;
import com.fwcd.palm.model.TextEdit;
import com.fwcd.palm.model.TextRange;
import com.fwcd.palm.model.editor.PalmDocument;
import com.fwcd.palm.model.editor.PalmEditorModel;
import com.fwcd.palm.utils.PalmException;
import com.fwcd.palm.utils.TwoWayObservable;
import com.fwcd.palm.viewmodel.editor.mods.completion.CompletionProvider;
import com.fwcd.palm.viewmodel.editor.mods.completion.NoCompletionProvider;
import com.fwcd.palm.viewmodel.editor.mods.highlighting.NoHighlighting;
import com.fwcd.palm.viewmodel.editor.mods.highlighting.SyntaxHighlighter;

public class PalmEditorViewModel {
	private final PalmEditorModel model;
	private final Observable<CompletionProvider> completionProvider = new Observable<>(new NoCompletionProvider());
	private final Observable<SyntaxHighlighter> highlighting = new Observable<>(new NoHighlighting());
	private final TwoWayObservable<Integer> caretOffset = new TwoWayObservable<>(0);
	
	public PalmEditorViewModel(PalmEditorModel model) {
		this.model = model;
	}
	
	public Observable<CompletionProvider> getCompletionProvider() { return completionProvider; }
	
	public Observable<SyntaxHighlighter> getSyntaxHighlighter() { return highlighting; }
	
	public String getText() { return getDocument().getText(); }

	public String getText(int offset, int length) { return getDocument().getText(offset, length); }
	
	public String getWordAt(int offset) {
		PalmDocument document = model.getDocument();
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
			model.getDocument().replace(range.getOffset(), range.getLength(), edit.getNewText(), null);
		} catch (BadLocationException e) {
			throw new PalmException(e);
		}
	}
	
	public synchronized void performSilently(TextEdit edit) {
		TextRange range = edit.getRange();
		model.getDocument().replaceSilently(range.getOffset(), range.getLength(), edit.getNewText());
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
	
	public PalmDocument getDocument() { return model.getDocument(); }
	
	public TwoWayObservable<Integer> getCaretOffset() { return caretOffset; }
}
