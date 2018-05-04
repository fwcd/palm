package com.fwcd.palm.editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.text.EditorKit;
import javax.swing.text.PlainDocument;

import com.fwcd.palm.editor.viewmods.EditorViewModule;
import com.fwcd.palm.model.CodeDoc;
import com.fwcd.palm.theme.Theme;
import com.fwcd.palm.viewutils.CustomTabSizeEditorKit;
import com.fwcd.palm.viewutils.DocumentAdapter;
import com.fwcd.palm.viewutils.TextStyle;

public class CodePane {
	private final JPanel view;
	private final JTextPane foreground;

	private final TextStyle defaultStyle;
	private final List<EditorViewModule> modules = new ArrayList<>();
	private final PalmEditor parent;

	private CodeDoc doc;

	public CodePane(PalmEditor parent) {
		this.parent = parent;
		defaultStyle = new TextStyle(parent.getTheme().fgColor());

		view = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				renderBG((Graphics2D) g, getSize());
			}
		};
		view.setOpaque(false);
		view.setLayout(new BorderLayout());

		foreground = new JTextPane() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				super.paintComponent(g2d);
				renderFG(g2d);
				view.repaint();
			}

			@Override
			protected EditorKit createDefaultEditorKit() {
				return new CustomTabSizeEditorKit(36);
			}
		};
		foreground.setOpaque(false);
		foreground.addCaretListener(new CaretListener() {

			@Override
			public void caretUpdate(CaretEvent e) {
				view.repaint();
			}

		});

		setModel(new CodeDoc());

		view.add(foreground, BorderLayout.CENTER);
		defaultStyle.addAttribute(PlainDocument.tabSizeAttribute, 4);
	}

	private void renderBG(Graphics2D g2d, Dimension canvasSize) {
		for (EditorViewModule module : modules) {
			module.renderBG(g2d, canvasSize, parent);
		}
	}

	private void renderFG(Graphics2D g2d) {
		for (EditorViewModule module : modules) {
			module.renderFG(g2d, foreground.getSize(), parent);
		}
	}

	public List<EditorViewModule> getModules() {
		return modules;
	}

	private void update() {
		SwingUtilities.invokeLater(() -> {
			doc.setCharacterAttributes(0, doc.getLength(), defaultStyle, true);
			for (EditorViewModule module : modules) {
				module.format(foreground.getText(), parent);
			}
		});
	}

	public JTextPane getFG() {
		return foreground;
	}

	public JComponent getView() {
		return view;
	}

	public void setTheme(Theme theme) {
		foreground.setForeground(theme.fgColor());
		foreground.setCaretColor(theme.fgColor());
	}

	public void setModel(CodeDoc doc) {
		this.doc = doc;
		foreground.setDocument(doc);
		doc.addDocumentListener(new DocumentAdapter() {

			@Override
			public void insertUpdate(DocumentEvent e) {
				if (!doc.isSilent()) {
					update();
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				if (!doc.isSilent()) {
					update();
				}
			}

		});
		foreground.setDocument(doc);
	}

	public CodeDoc getModel() {
		return doc;
	}

	public void addKeybind(KeyStroke bind, Runnable onPress) {
		foreground.getInputMap().put(bind, bind);
		foreground.getActionMap().put(bind, new AbstractAction() {
			private static final long serialVersionUID = -3602216290452545358L;

			@Override
			public void actionPerformed(ActionEvent e) {
				onPress.run();
			}
		});
	}
}
