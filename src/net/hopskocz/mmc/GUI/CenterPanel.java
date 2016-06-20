package net.hopskocz.mmc.GUI;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by hopskocz on 07.04.14.
 */
public class CenterPanel extends JPanel {

    public CenterPanel() {
        super();
        setOpaque(false);
        setBackground(new Color(0,0,0,0));
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        Color color1 = new Color(0,0,0,50);
        Color color2 = new Color(0,0,0,0);
        int w = getWidth();
        int h = 10;
        GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);
        gp = new GradientPaint(0, this.getHeight()-h, color2, 0, this.getHeight(), color1);
        g2d.setPaint(gp);
        g2d.fillRect(0, this.getHeight()-h, w, h);


    }
}
