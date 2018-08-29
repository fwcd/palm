package com.fwcd.palm.view.editor;

import javax.swing.KeyStroke;

public interface Keybindable {
	void addKeybind(String name, KeyStroke bind, Runnable action);
	
	void removeKeybind(String name);
}
