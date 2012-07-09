package at.oefg1880.swing.panel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: schremse
 * Date: 21.04.2010
 * Time: 10:24:17
 * To change this template use File | Settings | File Templates.
 */
public class ImagePanel extends GradientPanel {
    private Image image;

    public ImagePanel(String image) {
        this(new ImageIcon(image).getImage());
    }

    public ImagePanel(URL url) {
        this(new ImageIcon(url).getImage());
    }

    public ImagePanel(Image image) {
        super(HORIZONTAL);
        this.image = image;
        Dimension size = new Dimension(image.getWidth(null) + 14, image.getHeight(null) + 14);
        setPreferredSize(size);
        setMaximumSize(size);
        setMinimumSize(size);
        setSize(size);
        setBackground(Color.white);
    }

    public void paintComponent(Graphics g) {
        BufferedImage buf = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D _g = buf.createGraphics();
        _g.setComposite(AlphaComposite.Src);
        _g.drawImage(image, 0, 0, null);
        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.drawImage(buf, 7, 7, null);
    }
}