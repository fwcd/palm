package com.fwcd.palm.view.utils;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicScrollBarUI;

import com.fwcd.fructose.Observable;
import com.fwcd.palm.view.theme.Theme;

public class PalmScrollBar extends BasicScrollBarUI {
	private Color color = Color.LIGHT_GRAY;

	public PalmScrollBar(Observable<Theme> theme) {
		theme.listenAndFire(it -> {
			color = it.mildFGColor();
		});
	}

	@Override
	protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {

	}

	@Override
	protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
		Graphics2D g2d = (Graphics2D) g;

		g2d.setColor(color);
		g2d.fill(thumbBounds);
		g2d.dispose();
	}

	@Override
	protected JButton createDecreaseButton(int orientation) {
		return new NoButton();
	}

	@Override
	protected JButton createIncreaseButton(int orientation) {
		return new NoButton();
	}
}
