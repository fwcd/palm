package com.fwcd.palm.view.editor.mods.completion;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.fwcd.fructose.Observable;
import com.fwcd.fructose.geometry.Vector2D;
import com.fwcd.fructose.io.ResourceFile;
import com.fwcd.palm.model.editor.mods.completion.AutoCompletionModel;
import com.fwcd.palm.view.editor.PalmEditorView;
import com.fwcd.palm.view.editor.mods.EditorViewModule;
import com.fwcd.palm.view.theme.Theme;
import com.fwcd.palm.viewmodel.editor.mods.completion.CompletionElement;
import com.fwcd.palm.viewmodel.editor.mods.completion.CompletionType;

public class AutoCompletionView implements EditorViewModule {
	private static final Map<CompletionType, Image> icons = new HashMap<>();
	private final Observable<Theme> theme;
	private final AutoCompletionModel model;
	
	static {
		icons.put(CompletionType.METHOD, load("/icon/method.png"));
		icons.put(CompletionType.KEYWORD, load("/icon/keyword.png"));
		icons.put(CompletionType.TYPE, load("/icon/type.png"));
		icons.put(CompletionType.VARIABLE, load("/icon/variable.png"));
		icons.put(CompletionType.OTHER, load("/icon/other.png"));
	}
	
	public AutoCompletionView(AutoCompletionModel model, Observable<Theme> theme) {
		this.model = model;
		this.theme = theme;
	}
	
	private static Image load(String resourcePath) {
		return new ResourceFile(resourcePath).mapStream(ImageIO::read);
	}
	
	@Override
	public void renderFG(Graphics2D g2d, Dimension canvasSize, PalmEditorView editor) {
		if (model.isActive().get() && !model.getCompletions().isEmpty()) {
			Theme currentTheme = theme.get();
			FontMetrics metrics = editor.getFontMetrics();
			Vector2D caretPos = editor.getCaretPixelPosition();
			int selectedIndex = model.getSelectedIndex().get();
			
			int lineHeight = metrics.getHeight();
			int startX = (int) caretPos.getX();
			int startY = (int) caretPos.getY() + lineHeight;
			int x = startX;
			int y = startY;
			int i = 0;
			
			for (CompletionElement element : model.getCompletions()) {
				Image icon = icons.get(element.getType());
				String label = element.getLabel() + " - " + element.getDetail();
				int iconWidth = icon.getWidth(null);
				int iconHeight = icon.getHeight(null);
				int width = iconWidth + metrics.stringWidth(label);
				int height = iconHeight;
				Color bgColor = (i == selectedIndex)
					? currentTheme.bgColor()
					: currentTheme.mildBGColor();
				
				g2d.setColor(bgColor);
				g2d.fillRect(x, y, width, height);
				g2d.drawImage(icon, x, y, null);
				g2d.setColor(currentTheme.fgColor());
				g2d.drawString(label, x + iconWidth, y + (lineHeight / 2));
				
				y += height;
				i++;
			}
		}
	}
}
