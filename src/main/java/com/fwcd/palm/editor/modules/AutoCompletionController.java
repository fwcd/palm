package com.fwcd.palm.editor.modules;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.KeyStroke;

import com.fwcd.palm.editor.Keybindable;
import com.fwcd.palm.editor.PalmEditorView;

public class AutoCompletionController implements EditorControllerModule {
	private final AutoCompletionView view;
	private final Keybindable keyBinder;
	private final List<Keybind> keyBinds = new ArrayList<>();
	
	public AutoCompletionController(AutoCompletionView view, Keybindable keyBinder) {
		this.view = view;
		this.keyBinder = keyBinder;
		
		addActiveKeybind("UP", () -> view.changeSelectedIndex(-1));
		addActiveKeybind("DOWN", () -> view.changeSelectedIndex(1));
		addActiveKeybind("ESCAPE", () -> view.hide());
		
		view.isActive().listen(this::toggleKeybinds);
	}
	
	private static class Keybind {
		String name;
		KeyStroke stroke;
		Runnable action;
	}
	
	private void addActiveKeybind(String keyStroke, Runnable action) {
		Keybind bind = new Keybind();
		bind.name = keyStroke;
		bind.stroke = KeyStroke.getKeyStroke(keyStroke);
		bind.action = action;
		keyBinds.add(bind);
	}
	
	private void toggleKeybinds(boolean active) {
		for (Keybind bind : keyBinds) {
			if (active) {
				keyBinder.addKeybind(bind.name, bind.stroke, bind.action);
			} else {
				keyBinder.removeKeybind(bind.name);
			}
		}
	}
	
	@Override
	public void onInsert(String delta, int offset, PalmEditorView editor) {
		view.show(editor.getModel().getText(), offset);
	}
}
