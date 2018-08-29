package com.fwcd.palm.editor.modules;

import com.fwcd.palm.editor.PalmEditorView;

public interface EditorControllerModule {
	/**
	 * Called when something is inserted into the editor.
	 *
	 * @param delta - The String typed
	 * @param offset - The offset at the beginning of the change
	 * @param editor - The editor
	 */
	default void onInsert(String delta, int offset, PalmEditorView editor) {}

	/**
	 * Called when something is removed from the editor.
	 *
	 * @param length - The length of the change
	 * @param offset - The offset at the beginning of the change
	 * @param editor - The editor
	 */
	default void onRemove(int length, int offset, PalmEditorView editor) {}
}
