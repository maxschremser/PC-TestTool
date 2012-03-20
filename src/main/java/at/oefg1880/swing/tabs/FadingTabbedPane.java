package at.oefg1880.swing.tabs;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: sensi
 * Date: 25.04.2010
 * Time: 16:33:10
 * To change this template use File | Settings | File Templates.
 */
public class FadingTabbedPane extends TransitionTabbedPane {
    @Override
    public void paintTransition(Graphics2D g2d, float percent, Rectangle size, Image prev) {
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f - Math.min(1.0f, percent)));
        g2d.drawImage(prev, (int) size.getX(), (int) size.getY(), null);
    }
}
