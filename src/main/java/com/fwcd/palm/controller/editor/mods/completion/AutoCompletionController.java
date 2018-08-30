package com.fwcd.palm.controller.editor.mods.completion;

import java.util.ArrayList;
import java.util.List;

import javax.swing.KeyStroke;

import com.fwcd.palm.controller.editor.mods.EditorControllerModule;
import com.fwcd.palm.model.editor.mods.completion.AutoCompletionModel;
import com.fwcd.palm.view.editor.Keybindable;
import com.fwcd.palm.view.editor.mods.completion.AutoCompletionView;
import com.fwcd.palm.viewmodel.editor.PalmEditorViewModel;

public class AutoCompletionController implements EditorControllerModule {
	private final AutoCompletionModel model;
	private final Keybindable keyBinder;
	private final List<Keybind> keyBinds = new ArrayList<>();
	
	public AutoCompletionController(
		AutoCompletionView view,
		AutoCompletionModel model,
		PalmEditorViewModel editor,
		Keybindable keyBinder
	) {
		this.model = model;
		this.keyBinder = keyBinder;
		
		addActiveKeybind("UP", () -> model.changeSelectedIndex(-1));
		addActiveKeybind("DOWN", () -> model.changeSelectedIndex(1));
		addActiveKeybind("ESCAPE", () -> model.hide());
		addActiveKeybind("ENTER", () -> {
			editor.insertSilentlyBeforeCaret(model.getSelectedElement().get().getCompletion());
			model.hide();
		});
		
		model.isActive().listen(this::toggleKeybinds);
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
	
	private boolean isToggleCharacter(char c) {
		return Character.isLetter(c) || (c == '.');
	}
	
	@Override
	public void onInsert(String delta, int offset, PalmEditorViewModel editor) {
		if (isToggleCharacter(delta.charAt(delta.length() - 1))) {
			model.show(editor.getWordAt(offset), delta, offset);
		}
	}
}
