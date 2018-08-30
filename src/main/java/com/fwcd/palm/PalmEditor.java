package com.fwcd.palm;

import com.fwcd.palm.controller.editor.PalmEditorController;
import com.fwcd.palm.view.editor.PalmEditorView;
import com.fwcd.palm.model.editor.PalmEditorModel;

public class PalmEditor {
	private final PalmEditorModel model = new PalmEditorModel();
	private final PalmEditorView view = new PalmEditorView(model);
	private final PalmEditorController controller = new PalmEditorController(view, model);
	
	public PalmEditorView getView() { return view; }
	
	public PalmEditorController getController() { return controller; }
	
	public PalmEditorModel getModel() { return model; }
}
