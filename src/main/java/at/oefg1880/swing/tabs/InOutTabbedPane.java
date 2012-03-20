package at.oefg1880.swing.tabs;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: sensi
 * Date: 25.04.2010
 * Time: 11:16:41
 * To change this template use File | Settings | File Templates.
 */
public class InOutTabbedPane extends TransitionTabbedPane {
    @Override
    public void paintTransition(Graphics2D g2d, float percent, Rectangle size, Image prev) {

        int offset = 0;
        // calculate fade out part
        if (percent >= 0 && percent < 0.5f) {
            // draw the saved version of the old tab component
            if (prev != null) {
                g2d.drawImage(prev, (int) size.getX(), (int) size.getY(), null);
            }
            offset = (int) (size.getWidth() * percent);
        }

        // calculate fade in part
        if (percent >= 0.5f && percent < 1.0f) {
            offset = (int) (size.getWidth() * (1 - percent));
        }

        // draw the rectangle
        g2d.setColor(Color.white);
        Rectangle area = new Rectangle(
                (int) (size.getX() + offset),
                (int) (size.getY() + offset),
                (int) (size.getWidth() - (offset * 2)),
                (int) (size.getHeight() - (offset * 2))
        );
        g2d.fill(area);
    }
}
