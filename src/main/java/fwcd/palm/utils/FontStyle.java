package fwcd.palm.utils;

public class FontStyle {
	public static final FontStyle REGULAR = new FontStyle(false, false, false);
	public static final FontStyle BOLD = new FontStyle(true, false, false);
	public static final FontStyle ITALIC = new FontStyle(false, true, false);
	public static final FontStyle UNDERLINED = new FontStyle(false, false, true);
	
	private final boolean bold;
	private final boolean italic;
	private final boolean underlined;
	
	public FontStyle(boolean bold, boolean italic, boolean underlined) {
		this.bold = bold;
		this.italic = italic;
		this.underlined = underlined;
	}
	
	public boolean isBold() { return bold; }
	
	public boolean isItalic() { return italic; }
	
	public boolean isUnderlined() { return underlined; }
	
	public FontStyle combine(FontStyle other) { return new FontStyle(bold || other.bold, italic || other.italic, underlined || other.underlined); }
}
