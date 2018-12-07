package fwcd.palm.model.editor;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;

import fwcd.palm.utils.PalmException;

public class PalmDocument extends DefaultStyledDocument {
	public static final String NEWLINE = "\n";
	private static final long serialVersionUID = 8979879874598734L;
	private boolean silent = false;
	
	{
		putProperty(DefaultEditorKit.EndOfLineStringProperty, NEWLINE);
	}
	
	public String[] getLines() {
		return getText().split(NEWLINE, -1);
	}
	
	public String getText() {
		return getText(0, getLength());
	}
	
	public char charAt(int offset) {
		return getText(offset, 1).charAt(0);
	}
	
	public void setText(String text) {
		try {
			replace(0, getLength(), text, null);
		} catch (BadLocationException e) {
			throw new PalmException(e);
		}
	}
	
	@Override
	public String getText(int offset, int length) {
		try {
			return super.getText(offset, length);
		} catch (BadLocationException e) {
			throw new PalmException(e);
		}
	}
	
	public String getLine(int line) {
		// TODO: This relies on the internal element structure
		// but is much faster than getLines()[line]
		
		Element element = getDefaultRootElement().getElement(line);
		int offset = element.getStartOffset();
		int length = element.getEndOffset() - element.getStartOffset();
		return getText(offset, length);
	}
	
	public void appendLine(String text) {
		try {
			insertString(getLength(), text + NEWLINE, null);
		} catch (BadLocationException e) {
			throw new PalmException(e);
		}
	}

	public void replaceSilently(int offset, int length, String text) {
		try {
			silent = true;
			replace(offset, length, text, null);
			silent = false;
		} catch (BadLocationException e) {
			throw new PalmException(e);
		}
	}

	public void insertSilently(int offset, String text) {
		try {
			silent = true;
			insertString(offset, text, null);
			silent = false;
		} catch (BadLocationException e) {
			throw new PalmException(e);
		}
	}

	public void removeSilently(int offset, int length) {
		try {
			silent = true;
			remove(offset, length);
			silent = false;
		} catch (BadLocationException e) {
			throw new PalmException(e);
		}
	}

	public boolean isSilent() {
		return silent;
	}
}
