package com.fwcd.palm.controller.editor.mods.completion;

import java.util.ArrayList;
import java.util.List;

import javax.swing.KeyStroke;

import com.fwcd.palm.controller.editor.mods.EditorControllerModule;
import com.fwcd.palm.view.editor.Keybindable;
import com.fwcd.palm.view.editor.mods.completion.AutoCompletionView;
import com.fwcd.palm.viewmodel.editor.PalmEditorViewModel;

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
	public void onInsert(String delta, int offset, PalmEditorViewModel editor) {
		view.show(editor.getText(), offset);
	}
}
