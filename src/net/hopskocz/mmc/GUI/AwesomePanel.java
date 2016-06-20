package net.hopskocz.mmc.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;

/**
 * Created by hopskocz on 08.04.14.
 */
public class AwesomePanel extends JPanel {

    private Color bgColor;
    private boolean type;

    public AwesomePanel() {
        super();
        //todo: constructor
        setOpaque(false);
        type = true;
    }

    public AwesomePanel(boolean type) {
        super();
        setOpaque(false);
        this.type = type;
    }

    public void paintComponent(Graphics g) {
        //
        //todo: further

        g.setColor(bgColor);

        if(type) {
        //System.out.println(this.getHeight());
        g.fillRect(10,0,getWidth()-20,getHeight()-10);
        g.fillRect(0,10,10,getHeight()-30);
        g.fillRect(getWidth()-10,10,10,getHeight()-30);

        g.fillArc(0,0,20,20,90,90);
        g.fillArc(0,getHeight()-30,20,20,180,90);
        g.fillArc(getWidth()-20,getHeight()-30,20,20,270,90);
        g.fillArc(getWidth()-20,0,20,20,0,90);


            // now its time for triangle ;-;
            Point p1 = new Point(getWidth() - 22, getHeight() - 10);
            Point p2 = new Point(getWidth() - 15, getHeight());
            Point p3 = new Point(getWidth() - 8, getHeight() - 10);

            int[] xs = {p1.x, p2.x, p3.x};
            int[] ys = {p1.y, p2.y, p3.y};

            Polygon triangle = new Polygon(xs, ys, xs.length);

            g.fillPolygon(triangle);
        }
        else {
            g.fillRect(10,0,getWidth()-20,getHeight());
            g.fillRect(0,10,10,getHeight()-20);
            g.fillRect(getWidth()-10,10,10,getHeight()-20);

            g.fillArc(0,0,20,20,90,90);
            g.fillArc(0,getHeight()-20,20,20,180,90);
            g.fillArc(getWidth()-20,getHeight()-20,20,20,270,90);
            g.fillArc(getWidth()-20,0,20,20,0,90);
        }

        super.paintComponent(g);
    }

    public void setBgColor(Color color) {
        bgColor = color;
    }
}
