package net.hopskocz.mmc.GUI;

import javax.swing.*;
import java.awt.*;

/**
 * Created by hopskocz on 07.07.14.
 */
public class NicePanel extends JPanel {

    public NicePanel() {
        super();
        setOpaque(false);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(this.getBackground());
        g.fillRect(0,0,this.getWidth(),this.getHeight());
    }
}
