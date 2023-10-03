package game.ui.helpers;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class BombermanTextFieldFilter extends DocumentFilter {
    private int limit = -1;

    public BombermanTextFieldFilter() {

    }

    public BombermanTextFieldFilter(int limit) {
        this.limit = limit;
    }

    @Override
    public void insertString(final FilterBypass fb, final int offset, final String string, final AttributeSet attr) throws BadLocationException {
        super.insertString(fb, offset, string.toUpperCase(), attr);
    }

    @Override
    public void replace(final FilterBypass fb, final int offset, final int length, final String text, final AttributeSet attrs) throws BadLocationException {
        if (fb.getDocument().getLength() >= limit && limit > 0) return;

        super.replace(fb, offset, length, text.toUpperCase(), attrs);
    }
}