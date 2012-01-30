package at.oefg1880.swing.button;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Created by IntelliJ IDEA.
 * User: schremse
 * Date: 03.05.2010
 * Time: 10:59:37
 * To change this template use File | Settings | File Templates.
 */
public class ToolTipButton extends JButton {
    JToolTip toolTip;
    Color LIGHT_YELLOW = new Color(255, 255, 225);
    Color BG_COLOR = Color.black;

    public ToolTipButton(String text) {
        this(text, "");
    }

    public ToolTipButton(String text, String toolTipText) {
        super(text);
        toolTip = new CustomToolTip();
        toolTip.setComponent(this);
        setToolTipText(toolTipText);
    }

    @Override
    public JToolTip createToolTip() {
        return toolTip;
    }

    private class CustomToolTip extends JToolTip {
        private CustomToolTip() {
            super();
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Component parent = getParent();
            if (parent != null) {
                if (parent instanceof JComponent) {
                    JComponent jParent = (JComponent) parent;
                    if (jParent.isOpaque()) {
                        jParent.setOpaque(false);
                    }
                }
            }

            Graphics2D g2d = (Graphics2D) g;

            Shape shape = new Rectangle2D.Float(0, 0, getWidth(), getHeight());

            // draw the background
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(LIGHT_YELLOW);
            g2d.fill(shape);

            // draw the border
            g2d.setColor(BG_COLOR);
            g2d.draw(shape);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_DEFAULT);

            // draw the text
            String text = this.getComponent().getToolTipText();
            if (text != null) {
                FontMetrics fm = g2d.getFontMetrics();
                int h = fm.getAscent();
                g2d.setColor(BG_COLOR);
                g2d.drawString(text, 2, (this.getHeight() + h) / 2 - 2);
            }
        }
    }
}
