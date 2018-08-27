package com.fwcd.palm.model;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.DefaultStyledDocument;

import com.fwcd.palm.utils.PalmException;

public class PalmDocument extends DefaultStyledDocument {
	public static final String NEWLINE = "\n";
	private static final long serialVersionUID = 8979879874598734L;
	private boolean silent = false;
	
	{
		putProperty(DefaultEditorKit.EndOfLineStringProperty, NEWLINE);
	}
	
	public void appendLine(String text) {
		try {
			insertString(getLength(), text + NEWLINE, null);
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
