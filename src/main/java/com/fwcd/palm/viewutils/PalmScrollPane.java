package com.fwcd.palm.viewutils;

import java.awt.Container;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneLayout;

import com.fwcd.palm.theme.Theme;

public class PalmScrollPane extends JScrollPane {
	private static final long serialVersionUID = 9879879438759345L;
	private final int scrollBarThickness = 5;

	public PalmScrollPane(JComponent component, Theme theme) {
		super(component, VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_ALWAYS);

		setOpaque(false);
		getViewport().setOpaque(false);

		JScrollBar vbar = getVerticalScrollBar();
		vbar.setOpaque(false);
		vbar.setUI(new PalmScrollBar(theme));
		vbar.setUnitIncrement(15);

		JScrollBar hbar = getHorizontalScrollBar();
		hbar.setOpaque(false);
		hbar.setUI(new PalmScrollBar(theme));
		hbar.setUnitIncrement(10);

		setBorder(null);
		setLayout(new ScrollPaneLayout() {
			private static final long serialVersionUID = 1L;

			@Override
			public void layoutContainer(Container parent) {
				JScrollPane scrollPane = PalmScrollPane.this;

				Rectangle availR = scrollPane.getBounds();
				availR.x = availR.y = 0;

				Insets insets = parent.getInsets();
				availR.x = insets.left;
				availR.y = insets.top;
				availR.width -= insets.left + insets.right;
				availR.height -= insets.top + insets.bottom;

				Rectangle vsbR = new Rectangle();
				vsbR.width = scrollBarThickness;
				vsbR.height = availR.height;
				vsbR.x = availR.x + availR.width - vsbR.width;
				vsbR.y = availR.y;

				Rectangle hsbR = new Rectangle();
				hsbR.width = availR.width;
				hsbR.height = scrollBarThickness;
				hsbR.x = availR.x;
				hsbR.y = availR.y + availR.height - hsbR.height;

				if (viewport != null) {
					viewport.setBounds(availR);
				}

				vbar.setBounds(vsbR);
				hbar.setBounds(hsbR);
			}
		});

		setComponentZOrder(vbar, 0);
		setComponentZOrder(hbar, 1);
		setComponentZOrder(getViewport(), 2);
	}
}
