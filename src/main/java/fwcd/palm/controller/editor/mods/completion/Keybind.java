package fwcd.palm.controller.editor.mods.completion;

import javax.swing.KeyStroke;

public class Keybind {
	private final String name;
	private final KeyStroke stroke;
	private final Runnable action;
	
	public Keybind(String keyStroke, Runnable action) {
		this.action = action;
		name = keyStroke;
		stroke = KeyStroke.getKeyStroke(keyStroke);
	}
	
	public String getName() { return name; }
	
	public Runnable getAction() { return action; }
	
	public KeyStroke getStroke() { return stroke; }
}
