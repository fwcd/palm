package fwcd.palm.view.utils;

import javax.swing.text.AbstractDocument;
import javax.swing.text.BoxView;
import javax.swing.text.ComponentView;
import javax.swing.text.Element;
import javax.swing.text.IconView;
import javax.swing.text.LabelView;
import javax.swing.text.ParagraphView;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.TabSet;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

public class CustomTabSizeEditorKit extends StyledEditorKit {
	private static final long serialVersionUID = 245426865938443963L;
	private final int tabSizePx;
	private final ViewFactory viewFactory = new ViewFactory() {

		@Override
		public View create(Element elem) {
            String kind = elem.getName();
            if (kind != null) {
                if (kind.equals(AbstractDocument.ContentElementName)) {
					return new LabelView(elem);
                } else if (kind.equals(AbstractDocument.ParagraphElementName)) {
                    return new ParagraphView(elem) {

                    	@Override
						public float nextTabStop(float x, int tabOffset) {
                            TabSet tabs = getTabSet();
                            if(tabs == null) {
                                return (float)(getTabBase() + (((int)x / tabSizePx + 1) * tabSizePx));
                            }

                            return super.nextTabStop(x, tabOffset);
                        }

                    };
                } else if (kind.equals(AbstractDocument.SectionElementName)) {
                    return new BoxView(elem, View.Y_AXIS);
                } else if (kind.equals(StyleConstants.ComponentElementName)) {
                    return new ComponentView(elem);
                } else if (kind.equals(StyleConstants.IconElementName)) {
                    return new IconView(elem);
                }
            }

            // default to text display
            return new LabelView(elem);
        }

	};

	public CustomTabSizeEditorKit(int tabSizePx) {
		this.tabSizePx = tabSizePx;
	}

	@Override
	public ViewFactory getViewFactory() {
		return viewFactory;
	}
}
