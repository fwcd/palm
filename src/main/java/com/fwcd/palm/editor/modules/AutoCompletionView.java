package com.fwcd.palm.editor.modules;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import com.fwcd.fructose.Observable;
import com.fwcd.fructose.io.ResourceFile;
import com.fwcd.palm.model.PalmDocument;
import com.fwcd.palm.editor.PalmEditorView;
import com.fwcd.palm.editor.completion.CompletionElement;
import com.fwcd.palm.editor.completion.CompletionProvider;
import com.fwcd.palm.editor.completion.CompletionType;
import com.fwcd.palm.theme.Theme;

public class AutoCompletionView implements EditorViewModule {
	private static final Map<CompletionType, Image> icons = new HashMap<>();
	private final Observable<CompletionProvider> provider;
	private final Observable<Theme> theme;
	private int selectedIndex = 0;
	private List<CompletionElement> completions = Collections.emptyList();
	private Observable<Boolean> active = new Observable<>(false);
	
	static {
		icons.put(CompletionType.METHOD, load("/icon/method.png"));
		icons.put(CompletionType.KEYWORD, load("/icon/keyword.png"));
		icons.put(CompletionType.TYPE, load("/icon/type.png"));
		icons.put(CompletionType.VARIABLE, load("/icon/variable.png"));
		icons.put(CompletionType.OTHER, load("/icon/other.png"));
	}
	
	public AutoCompletionView(Observable<CompletionProvider> provider, Observable<Theme> theme) {
		this.provider = provider;
		this.theme = theme;
	}
	
	private static Image load(String resourcePath) {
		return new ResourceFile(resourcePath).mapStream(ImageIO::read);
	}
	
	public void changeSelectedIndex(int delta) {
		int newIndex = selectedIndex + delta;
		if ((newIndex >= 0) && (newIndex < completions.size())) {
			selectedIndex = newIndex;
		}
	}
	
	public void show(String text, int offset) {
		active.set(true);
		completions = provider.get().listCompletions(text, offset);
	}
	
	public void hide() {
		active.set(false);
	}
	
	@Override
	public void renderFG(Graphics2D g2d, Dimension canvasSize, PalmEditorView editor) {
		if (active.get() && !completions.isEmpty()) {
			Theme currentTheme = theme.get();
			FontMetrics metrics = editor.getFontMetrics();
			PalmDocument model = editor.getModel();
			int caretLine = editor.getCaretLine();
			int caretColumn = editor.getCaretColumn();
			String charsBeforeCaret = model.getLine(caretLine)
				.substring(0, caretColumn)
				.replace("\t", "    "); // TODO: Configurable tab size
			
			int lineHeight = metrics.getHeight();
			int startX = metrics.stringWidth(charsBeforeCaret);
			int startY = lineHeight * (caretLine + 1);
			int x = startX;
			int y = startY;
			int i = 0;
			
			for (CompletionElement element : completions) {
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
	
	public Observable<Boolean> isActive() { return active; }
}
