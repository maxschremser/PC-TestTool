package at.oefg1880.swing.frame.dissolver;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Created by IntelliJ IDEA.
 * User: schremse
 * Date: 03.05.2010
 * Time: 10:40:15
 * To change this template use File | Settings | File Templates.
 */
public class SpinDissolver extends Dissolver {
  @Override
  public void paint(Graphics g) {
    if (screen_buffer != null && frame_buffer != null) {
      Graphics2D g2d = (Graphics2D) g;

      // draw the screen
      g.drawImage(screen_buffer, -fullscreen.getX(), -fullscreen.getY(), null);

      // draw the frame
      AffineTransform old = g2d.getTransform();

      // move to upper-left corner of the frame
      g2d.translate(frame.getX(), frame.getY());

      g2d.translate(-((count + 1) * (frame.getX() + frame.getWidth()) / 20), 0);

      // shrink the frame
      float scale = 1f / ((float) count + 1);
      g2d.scale(scale, scale);

      // rotate around the center
      g2d.rotate(((float) count) / Math.PI / 1.3,
              frame.getWidth() / 2, frame.getHeight() / 2);

      g2d.drawImage(frame_buffer, 0, 0, null);

      g2d.setTransform(old);
    }
  }
}
