package com.fwcd.palm;

import com.fwcd.palm.controller.editor.PalmEditorController;
import com.fwcd.palm.model.editor.PalmEditorModel;
import com.fwcd.palm.view.editor.PalmEditorView;
import com.fwcd.palm.viewmodel.editor.PalmEditorViewModel;

public class PalmEditor {
	private final PalmEditorModel model = new PalmEditorModel();
	private final PalmEditorViewModel viewModel = new PalmEditorViewModel(model);
	private final PalmEditorView view = new PalmEditorView(viewModel);
	private final PalmEditorController controller = new PalmEditorController(view, viewModel);
	
	public PalmEditorView getView() { return view; }
	
	public PalmEditorController getController() { return controller; }
	
	public PalmEditorViewModel getViewModel() { return viewModel; }
	
	public PalmEditorModel getModel() { return model; }
}
