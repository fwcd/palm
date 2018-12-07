package fwcd.palm;

import fwcd.palm.controller.editor.PalmEditorController;
import fwcd.palm.view.editor.PalmEditorView;
import fwcd.palm.model.editor.PalmEditorModel;

public class PalmEditor {
	private final PalmEditorModel model = new PalmEditorModel();
	private final PalmEditorView view = new PalmEditorView(model);
	private final PalmEditorController controller = new PalmEditorController(view, model);
	
	public PalmEditorView getView() { return view; }
	
	public PalmEditorController getController() { return controller; }
	
	public PalmEditorModel getModel() { return model; }
}
