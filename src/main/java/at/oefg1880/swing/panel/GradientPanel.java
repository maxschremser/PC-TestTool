package at.oefg1880.swing.panel;

import at.oefg1880.swing.IConfig;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: schremse
 * Date: 21.04.2010
 * Time: 10:24:17
 * To change this template use File | Settings | File Templates.
 */
public class GradientPanel extends JPanel implements IConfig {
    private Color c1, c2;
    private GradientPaint paint;
    private Rectangle bounds;
    private int direction;

    public GradientPanel() {
        this(color_1, color_2);
    }

    public GradientPanel(int direction) {
        this(color_1, color_2, direction);
    }


    public GradientPanel(Color c1, Color c2) {
        this(c1, c2, DIAGONAL);
    }

    public GradientPanel(Color c1, Color c2, int direction) {
        super();
        this.c1 = c1;
        this.c2 = c2;
        this.direction = direction;
        if (direction < 0 || direction >= 5)
            throw new UnsupportedOperationException("direction (" + direction + ") is invalid. " +
                    "Use 0 for VERTICAL, 1 for HORIZONTAL and 2 for DIAGONAL, 3 for PLAIN_1, 4 for PLAIN_2.");
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public void setColor1(Color c1) {
        this.c1 = c1;
    }

    public void setColor2(Color c2) {
        this.c2 = c2;
    }

    public void paintComponent(Graphics _g) {
        Graphics2D g = (Graphics2D) _g;
        this.bounds = getBounds();
        switch (direction) {
            case DIAGONAL:
                this.paint = new GradientPaint(bounds.x, bounds.y, c1, bounds.width, bounds.height, c2);
                break;
            case VERTICAL:
                this.paint = new GradientPaint(0, bounds.x, c1, bounds.x, bounds.width, c2);
                break;
            case HORIZONTAL:
                this.paint = new GradientPaint(0, 0, c1, bounds.width, 0, c2);
                break;
            case PLAIN_1:
                this.paint = new GradientPaint(0, 0, c1, 0, 0, c2);
                break;
            case PLAIN_2:
                this.paint = new GradientPaint(bounds.x + bounds.width, bounds.y + bounds.height, c1, bounds.width, bounds.height, c2);
                break;
            default:
                this.paint = new GradientPaint(0, bounds.y, c1, bounds.x, bounds.width, c2);
        }
        g.setPaint(paint);
        g.fillRect(0, 0, bounds.width, bounds.height);
    }
}
