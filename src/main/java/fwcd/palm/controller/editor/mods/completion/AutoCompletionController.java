package fwcd.palm.controller.editor.mods.completion;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import fwcd.palm.controller.editor.mods.EditorControllerModule;
import fwcd.palm.model.editor.PalmEditorModel;
import fwcd.palm.model.editor.mods.completion.AutoCompletionModel;
import fwcd.palm.model.editor.mods.completion.CompletionContext;
import fwcd.palm.view.editor.Keybindable;
import fwcd.palm.view.editor.mods.completion.AutoCompletionView;

public class AutoCompletionController implements EditorControllerModule {
	private final AutoCompletionModel model;
	private final Keybindable keyBinder;
	private final List<Keybind> keyBinds = new ArrayList<>();
	private final Set<Character> activationChars = new HashSet<>();
	
	public AutoCompletionController(
		AutoCompletionView view,
		AutoCompletionModel model,
		PalmEditorModel editor,
		Keybindable keyBinder
	) {
		this.model = model;
		this.keyBinder = keyBinder;
		
		activationChars.add('.');
		
		addKeybind("UP", () -> model.changeSelectedIndex(-1));
		addKeybind("DOWN", () -> model.changeSelectedIndex(1));
		addKeybind("ESCAPE", () -> model.hide());
		addKeybind("SPACE", () -> {
			if (!activationChars.contains(' ')) {
				model.hide();
			}
		});
		addKeybind("ENTER", () -> {
			editor.performSilently(model
				.getSelectedElement()
				.orElseThrow(NoSuchElementException::new)
				.getCompletion()
				.getEdit());
			model.hide();
		});
		
		model.isActive().listen(this::toggleKeybinds);
	}
	
	private void addKeybind(String keyStroke, Runnable action) {
		keyBinds.add(new Keybind(keyStroke, action));
	}
	
	private void toggleKeybinds(boolean active) {
		for (Keybind bind : keyBinds) {
			if (active) {
				keyBinder.addKeybind(bind.getName(), bind.getStroke(), bind.getAction());
			} else {
				keyBinder.removeKeybind(bind.getName());
			}
		}
	}
	
	private boolean isToggleCharacter(char c) {
		return Character.isLetter(c) || activationChars.contains(c);
	}
	
	public void addActivationChar(char c) {
		activationChars.add(c);
	}
	
	public void removeActivationChar(char c) {
		activationChars.remove(c);
	}
	
	public void setAllActivationChars(char... cs) {
		activationChars.clear();
		for (char c : cs) {
			activationChars.add(c);
		}
	}
	
	public Set<? extends Character> getActivationChars() {
		return activationChars;
	}
	
	@Override
	public void onInsert(String delta, int offset, PalmEditorModel editor) {
		if (isToggleCharacter(delta.charAt(delta.length() - 1))) {
			model.show(new CompletionContext(editor, delta, offset));
		}
	}
	
	/**
	 * @deprecated Use {@link getActivationChars}
	 */
	@Deprecated
	public boolean doesHideOnSpace() {
		return activationChars.contains(' ');
	}
	
	/**
	 * @deprecated Use {@code addActivationChar}, {@code removeActivationChar} or {@code setAllActivationChars}
	 */
	@Deprecated
	public void setHideOnSpace(boolean hideOnSpace) {
		if (hideOnSpace) {
			activationChars.remove(' ');
		} else {
			activationChars.add(' ');
		}
	}
}
