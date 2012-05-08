package at.oefg1880.swing.tabs;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

/**
 * Created by IntelliJ IDEA.
 * User: sensi
 * Date: 25.04.2010
 * Time: 11:01:12
 * To change this template use File | Settings | File Templates.
 */
public abstract class TransitionTabbedPane extends JTabbedPane implements ChangeListener, ActionListener {
    public final static float ANIMATION_DURATION = 500f;
    public final static int ANIMATION_SLEEP = 10;
    boolean animating;
    long animationStart;
    float animationPercent;
    Timer animationTimer;
    protected BufferedImage buf = null;
    protected int previous_tab = -1;

    public abstract void paintTransition(Graphics2D g2d, float percent, Rectangle size, Image prev);

    public TransitionTabbedPane() {
        super();
        addChangeListener(this);
    }

    public TransitionTabbedPane(int tabPlacement) {
        super(tabPlacement);
        addChangeListener(this);
    }


    @Override
    public void stateChanged(ChangeEvent e) {
        startAnimation();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (animating) {
            setAnimationPercent((System.currentTimeMillis() - animationStart) / ANIMATION_DURATION);
            if (previous_tab != -1) {
                Component comp = getComponentAt(previous_tab);
                buf = new BufferedImage(comp.getWidth(), comp.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
                comp.paint(buf.getGraphics());
            }

            if (getAnimationPercent() >= 1.0f) {
                stopAnimation();
            }

            repaint();
        }
    }

    @Override
    protected void paintChildren(Graphics g) {
        super.paintChildren(g);
        if (animating) {
            Rectangle size = getComponentAt(0).getBounds();
            Graphics2D g2d = (Graphics2D) g;
            paintTransition(g2d, getAnimationPercent(), size, buf);
        }
    }

    @Override
    public void update(Graphics g) {
        paint(g);
    }


    private void startAnimation() {
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
        previous_tab = this.getSelectedIndex();
        repaint();
    }

    public float getAnimationPercent() {
        return animationPercent;
    }

    public void setAnimationPercent(float animationPercent) {
        this.animationPercent = animationPercent;
    }
}
