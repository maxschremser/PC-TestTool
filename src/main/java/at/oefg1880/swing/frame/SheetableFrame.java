package at.oefg1880.swing.frame;

import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * Created by IntelliJ IDEA.
 * User: schremse
 * Date: 22.04.2010
 * Time: 11:08:03
 * To change this template use File | Settings | File Templates.
 */
public class SheetableFrame extends JFrame implements ActionListener, MouseInputListener {
    protected final Logger log = Logger.getLogger(getClass());

    public final static int INCOMING = 1;
    public final static int OUTGOING = -1;
    public final static float ANIMATION_DURATION = 500f;
    public final static int ANIMATION_SLEEP = 23;

    boolean animating;
    int animationDirection;
    long animationStart;

    Timer animationTimer;
    JComponent sheet;
    JPanel glass;
    Cursor cursor;
    AnimatingSheet animatingSheet;

    public SheetableFrame(String title, String name) {
        super(title);
        setName(name);
        cursor = getCursor();
        glass = (JPanel) getGlassPane();
        glass.addMouseListener(this);
        glass.setLayout(new GridBagLayout());
        animatingSheet = new AnimatingSheet();
        animatingSheet.setBorder(new LineBorder(Color.black, 1));
    }

    public JComponent showDialogAsSheet(JDialog dialog) {
        animatingSheet = new AnimatingSheet();
        sheet = (JComponent) dialog.getContentPane();
        sheet.setBorder(new LineBorder(Color.black, 1));
        glass.removeAll();
        animationDirection = INCOMING;
        startAnimation();
        return sheet;
    }

    public void hideSheet() {
        animationDirection = OUTGOING;
        startAnimation();
    }

    private void startAnimation() {
        glass.repaint();
        animatingSheet.setSource(sheet);
        glass.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTH;
        glass.add(animatingSheet, gbc);
        gbc.gridy = 1;
        gbc.weighty = Integer.MAX_VALUE;
        glass.add(Box.createGlue(), gbc);
        glass.setVisible(true);

        animationStart = System.currentTimeMillis();
        if (animationTimer == null) {
            animationTimer = new Timer(ANIMATION_SLEEP, this);
        }

        animating = true;
        animationTimer.start();
    }

    private void stopAnimation() {
        animationTimer.stop();
        animating = false;
        setCursor(cursor);
    }

    public void actionPerformed(ActionEvent e) {
        if (animating) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            // calculate height to show
            float animationPercent = (System.currentTimeMillis() - animationStart) / ANIMATION_DURATION;
            animationPercent = Math.min(1.0f, animationPercent);
            int animatingHeight;

            if (animationDirection == INCOMING) {
                animatingHeight = (int) (animationPercent * sheet.getHeight());
            } else {
                animatingHeight = (int) ((1.0f - animationPercent) * sheet.getHeight());
            }
            animatingSheet.setAnimatingHeight(animatingHeight);
            animatingSheet.repaint();
            if (animationPercent >= 1.0f) {
                stopAnimation();
                if (animationDirection == INCOMING) {
                    finishShowingSheet();
                } else {
                    glass.removeAll();
                    glass.setVisible(false);
                }
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Toolkit.getDefaultToolkit().beep();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    private void finishShowingSheet() {
        glass.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTH;
        glass.add(sheet, gbc);
        gbc.gridy = 1;
        gbc.weighty = Integer.MAX_VALUE;
        glass.add(Box.createGlue(), gbc);
        glass.revalidate();
        glass.repaint();
    }

    class AnimatingSheet extends JPanel {
        Dimension animatingSize = new Dimension(0, 1);
        JComponent source;
        BufferedImage offscreenImage;

        public AnimatingSheet() {
            super();
            setOpaque(true);
        }

        public void setSource(JComponent source) {
            this.source = source;
            animatingSize.width = source.getWidth();
            makeOffscreenImage(source);
        }

        public void setAnimatingHeight(int height) {
            animatingSize.height = height;
            setSize(animatingSize);
        }

        private void makeOffscreenImage(JComponent source) {
            GraphicsConfiguration gfxConfig = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
            offscreenImage = gfxConfig.createCompatibleImage(source.getWidth(), source.getHeight());
            Graphics2D g2d = (Graphics2D) offscreenImage.getGraphics();
            g2d.setColor(source.getBackground());
            g2d.fillRect(0, 0, source.getWidth(), source.getHeight());
            source.paint(g2d);
        }

        @Override
        public Dimension getPreferredSize() {
            return animatingSize;
        }

        @Override
        public Dimension getMaximumSize() {
            return animatingSize;
        }

        @Override
        public Dimension getMinimumSize() {
            return animatingSize;
        }

        @Override
        public void paint(Graphics g) {
            BufferedImage fragment = offscreenImage.getSubimage(0, offscreenImage.getHeight() - animatingSize.height, source.getWidth(), animatingSize.height);
            g.drawImage(fragment, 0, 0, null);
        }

        @Override
        public void update(Graphics g) {
            paint(g);
        }
    }
}
