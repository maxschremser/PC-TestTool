package at.oefg1880.swing.frame.dissolver;

import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by IntelliJ IDEA.
 * User: schremse
 * Date: 03.05.2010
 * Time: 09:11:29
 * To change this template use File | Settings | File Templates.
 */
public class Dissolver extends JComponent implements Runnable {
  Frame frame;
  Window fullscreen;
  int count;
  BufferedImage frame_buffer;
  BufferedImage screen_buffer;

  public Dissolver() {
    super();
  }

  public Dissolver(String name) {
  }

  public void dissolveExit(JFrame frame) {
    try {
      this.frame = frame;
      Robot robot = new Robot();

      // capture screen with frame to frame_buffer
      Rectangle frame_rect = frame.getBounds();
      frame_buffer = robot.createScreenCapture(frame_rect);

      // hide frame
      frame.setVisible(false);

      // capture screen without frame
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      Rectangle screen_rect = new Rectangle(0, 0, screenSize.width, screenSize.height);
      Thread.currentThread().sleep(23);
      screen_buffer = robot.createScreenCapture(screen_rect);

      // create big window without decorations
      fullscreen = new Window(new JFrame());
      fullscreen.setSize(screenSize);
      fullscreen.add(this);
      this.setSize(screenSize);
      fullscreen.setVisible(true);

      new Thread(this).start();
    } catch (Exception e) {
      Logger.getLogger(getClass()).error(e);
      e.printStackTrace();
    }
  }

  @Override
  public void run() {
    try {
      count = 0;
      Thread.currentThread().sleep(100);
      for (int i = 0; i < 20; i++) {
        count = i;
        fullscreen.repaint();
        Thread.currentThread().sleep(100);
      }
    } catch (InterruptedException ie) {
    }
    System.exit(0);
  }

  @Override
  public void paint(Graphics g) {
    if (screen_buffer != null && frame_buffer != null) {
      Graphics2D g2d = (Graphics2D) g;

      // draw the screen
      g.drawImage(screen_buffer, -fullscreen.getX(), -fullscreen.getY(), null);

      // draw the frame
      Composite old_comp = g2d.getComposite();
      Composite fade = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f - ((float) count) / 20f);
      g2d.setComposite(fade);
      g2d.drawImage(frame_buffer, frame.getX(), frame.getY(), null);
      g2d.setComposite(old_comp);
    }
  }
}
