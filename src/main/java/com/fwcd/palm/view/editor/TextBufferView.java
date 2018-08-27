package com.fwcd.palm.view.editor;

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

import com.fwcd.fructose.Observable;
import com.fwcd.fructose.swing.View;
import com.fwcd.palm.model.PalmDocument;
import com.fwcd.palm.view.editor.modules.EditorViewModule;
import com.fwcd.palm.view.theme.Theme;
import com.fwcd.palm.view.utils.CustomTabSizeEditorKit;
import com.fwcd.palm.view.utils.DocumentAdapter;
import com.fwcd.palm.view.utils.TextStyle;

public class TextBufferView implements View {
	private final JPanel component;
	private final JTextPane foreground;

	private final TextStyle defaultStyle;
	private final List<EditorViewModule> modules = new ArrayList<>();
	private final PalmEditorView parent;

	private PalmDocument doc;

	public TextBufferView(PalmEditorView parent) {
		this.parent = parent;
		defaultStyle = new TextStyle(parent.getTheme().get().fgColor());

		component = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				renderBG((Graphics2D) g, getSize());
			}
		};
		component.setOpaque(false);
		component.setLayout(new BorderLayout());

		foreground = new JTextPane() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				super.paintComponent(g2d);
				renderFG(g2d);
				component.repaint();
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
				component.repaint();
			}
		});

		setModel(new PalmDocument());

		component.add(foreground, BorderLayout.CENTER);
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

	public JComponent getComponent() {
		return component;
	}

	public void setTheme(Observable<Theme> theme) {
		theme.listenAndFire(it -> {
			foreground.setForeground(it.fgColor());
			foreground.setCaretColor(it.fgColor());
		});
	}

	public void setModel(PalmDocument doc) {
		this.doc = doc;
		foreground.setDocument(doc);
		doc.addDocumentListener(new DocumentAdapter() {
			@Override
			public void insertUpdate(DocumentEvent e) { fireUpdate(); }

			@Override
			public void removeUpdate(DocumentEvent e) { fireUpdate(); }
			
			private void fireUpdate() {
				if (!doc.isSilent()) {
					update();
				}
			}
		});
		foreground.setDocument(doc);
	}

	public PalmDocument getModel() {
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
