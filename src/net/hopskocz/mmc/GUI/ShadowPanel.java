package net.hopskocz.mmc.GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by hopskocz on 07.04.14.
 */
public class ShadowPanel extends JPanel {

    private Image status;
    private URL url;
    Image image;

    public ShadowPanel(String imagePath) {
        super();

        image = new ImageIcon(this.getClass().getResource(imagePath+"logo.png")).getImage();

        try {
            url = new URL("http://polandcraft.eu/status/polandcraft.png");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            status = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
            status = new ImageIcon(this.getClass().getResource("/net/hopskocz/mmc/res/status.png")).getImage();
        }
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g.drawImage(image, 0, 0, image.getWidth(null), image.getHeight(null), this);
        g.drawImage(status, 0, getHeight()-status.getHeight(null), status.getWidth(null), status.getHeight(null), this);

        Color color1 = new Color(0,0,0,80);
        Color color2 = new Color(0,0,0,0);
        int w = getWidth();
        int h = 10;
        GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);
        gp = new GradientPaint(0, this.getHeight()-h, color2, 0, this.getHeight(), color1);
        g2d.setPaint(gp);
        g2d.fillRect(0, this.getHeight()-h, w, h);

        super.paintComponent(g);
    }

    public void reload() {
        try {
            status = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        repaint();
    }
}
