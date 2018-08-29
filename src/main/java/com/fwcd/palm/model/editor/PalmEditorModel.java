package com.fwcd.palm.model.editor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.fwcd.palm.utils.PalmException;

public class PalmEditorModel {
	private final PalmDocument document = new PalmDocument();
	
	public PalmDocument getDocument() { return document; }
	
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
