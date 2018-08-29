package com.fwcd.palm.controller.editor;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;

import com.fwcd.palm.controller.editor.mods.EditorControllerModule;
import com.fwcd.palm.controller.editor.mods.completion.AutoCompletionController;
import com.fwcd.palm.model.editor.PalmDocument;
import com.fwcd.palm.view.editor.PalmEditorView;
import com.fwcd.palm.view.editor.mods.Indentation;
import com.fwcd.palm.view.editor.mods.completion.AutoCompletionView;
import com.fwcd.palm.view.utils.DocumentAdapter;
import com.fwcd.palm.viewmodel.editor.PalmEditorViewModel;

public class PalmEditorController {
	private final PalmEditorView view;
	private final PalmEditorViewModel viewModel;
	private final List<EditorControllerModule> modules = new ArrayList<>();
	
	public PalmEditorController(PalmEditorView view, PalmEditorViewModel viewModel) {
		this.view = view;
		this.viewModel = viewModel;
		
		AutoCompletionView completionView = new AutoCompletionView(viewModel.getCompletionProvider(), view.getTheme());
		
		view.getModules().add(completionView);
		modules.add(new Indentation());
		modules.add(new AutoCompletionController(completionView, view));
		
		setupListeners();
	}
	
	private void setupListeners() {
		PalmDocument document = viewModel.getDocument();
		document.addDocumentListener(new DocumentAdapter() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				if (!document.isSilent()) {
					String delta = document.getText(e.getOffset(), e.getLength());

					SwingUtilities.invokeLater(() -> {
						for (EditorControllerModule module : modules) {
							module.onInsert(delta, e.getOffset(), viewModel);
						}
					});
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				if (!document.isSilent()) {
					SwingUtilities.invokeLater(() -> {
						for (EditorControllerModule module : modules) {
							module.onRemove(e.getLength(), e.getOffset(), viewModel);
						}
					});
				}
			}
		});
	}
}
