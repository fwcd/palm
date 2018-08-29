package com.fwcd.palm.utils;

import java.awt.Color;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

import com.fwcd.fructose.Multerator;

public class TextStyle implements Style {
	private final Map<Object, Object> attribs;
	private Set<ChangeListener> listeners = new HashSet<>();

	public TextStyle(Map<Object, Object> attribs) {
		this.attribs = new HashMap<>(attribs);
	}

	public TextStyle(FontStyle style, Color color) {
		this(style.isBold(), style.isItalic(), style.isUnderlined(), color);
	}

	public TextStyle(Color color) {
		this(false, false, false, color);
	}

	public TextStyle(boolean bold, Color color) {
		this(bold, false, false, color);
	}

	public TextStyle(boolean bold, boolean italic, boolean underlined, Color color) {
		attribs = new HashMap<>();
		StyleConstants.setBold(this, bold);
		StyleConstants.setItalic(this, italic);
		StyleConstants.setUnderline(this, underlined);
		StyleConstants.setForeground(this, color);
	}

	private void put(Object key, Object value) {
		attribs.put(key, value);
		fireListeners();
	}

	private void remove(Object key) {
		attribs.remove(key);
		fireListeners();
	}

	public boolean isBold() { return (boolean) attribs.get("bold"); }

	public boolean isItalic() { return (boolean) attribs.get("italic"); }

	public boolean isUnderlined() { return (boolean) attribs.get("underlined"); }

	public Color getColor() { return (Color) attribs.get("color"); }
	
	public FontStyle getFontStyle() { return new FontStyle(isBold(), isItalic(), isUnderlined()); }
	
	private void fireListeners() {
		for (ChangeListener listener : listeners) {
			listener.stateChanged(new ChangeEvent(this));
		}
	}

	@Override
	public void addAttribute(Object name, Object value) {
		put(name, value);
	}

	@Override
	public void addAttributes(AttributeSet attributes) {
		for (Object key : new Multerator<>(attributes.getAttributeNames())) {
			Object value = attributes.getAttribute(key);
			put(key, value);
		}
	}

	@Override
	public void removeAttribute(Object name) {
		remove(name);
	}

	@Override
	public void removeAttributes(Enumeration<?> names) {
		for (Object key : new Multerator<>(names)) {
			remove(key);
		}
	}

	@Override
	public void removeAttributes(AttributeSet attributes) {
		for (Object key : new Multerator<>(attributes.getAttributeNames())) {
			remove(key);
		}
	}

	@Override
	public void setResolveParent(AttributeSet parent) {

	}

	@Override
	public int getAttributeCount() {
		return 0;
	}

	@Override
	public boolean isDefined(Object attrName) {
		return attribs.containsKey(attrName);
	}

	@Override
	public boolean isEqual(AttributeSet attr) {
		return equals(attr);
	}

	@Override
	public AttributeSet copyAttributes() {
		return new TextStyle(attribs);
	}

	@Override
	public Object getAttribute(Object key) {
		return attribs.get(key);
	}

	@Override
	public Enumeration<?> getAttributeNames() {
		return new Multerator<>(attribs.keySet());
	}

	@Override
	public boolean containsAttribute(Object name, Object value) {
		return attribs.containsKey(name) && attribs.get(name).equals(value);
	}

	@Override
	public boolean containsAttributes(AttributeSet attributes) {
		for (Object key : new Multerator<>(attributes.getAttributeNames())) {
			if (!containsAttribute(key, attributes.getAttribute(key))) {
				return false;
			}
		}

		return true;
	}

	@Override
	public AttributeSet getResolveParent() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getName() {
		return toString();
	}

	@Override
	public void addChangeListener(ChangeListener l) {
		listeners.add(l);
	}

	@Override
	public void removeChangeListener(ChangeListener l) {
		listeners.remove(l);
	}

	@Override
	public String toString() {
		return "TextStyle [bold=" + isBold() + ", italic=" + isItalic() + ", underlined=" + isUnderlined() + ", color=" + getColor() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attribs == null) ? 0 : attribs.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		TextStyle other = (TextStyle) obj;
		if (attribs == null) {
			if (other.attribs != null) {
				return false;
			}
		} else if (!attribs.equals(other.attribs)) {
			return false;
		}
		return true;
	}
}
