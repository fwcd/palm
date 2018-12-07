package fwcd.palm.controller.editor;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;

import fwcd.palm.controller.editor.mods.EditorControllerModule;
import fwcd.palm.controller.editor.mods.Indentation;
import fwcd.palm.controller.editor.mods.completion.AutoCompletionController;
import fwcd.palm.model.editor.PalmDocument;
import fwcd.palm.model.editor.PalmEditorModel;
import fwcd.palm.model.editor.mods.completion.AutoCompletionModel;
import fwcd.palm.view.editor.PalmEditorView;
import fwcd.palm.view.editor.mods.completion.AutoCompletionView;
import fwcd.palm.view.utils.DocumentAdapter;

public class PalmEditorController {
	private final PalmEditorModel viewModel;
	private final List<EditorControllerModule> modules = new ArrayList<>();
	
	public PalmEditorController(PalmEditorView view, PalmEditorModel viewModel) {
		this.viewModel = viewModel;
		
		AutoCompletionModel completionModel = new AutoCompletionModel(viewModel.getCompletionProvider());
		AutoCompletionView completionView = new AutoCompletionView(completionModel, view.getTheme());
		
		view.getModules().add(completionView);
		modules.add(new Indentation());
		modules.add(new AutoCompletionController(completionView, completionModel, viewModel, view));
		
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
