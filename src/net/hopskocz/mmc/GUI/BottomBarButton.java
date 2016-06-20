package net.hopskocz.mmc.GUI;

import javax.swing.*;
import java.awt.*;

/**
 * Created by hopskocz on 07.04.14.
 */
public class BottomBarButton extends JButton {
    public BottomBarButton() {
        super();
        //LOL
    }

    public void paintComponent(Graphics g) {
        //super.paintComponent(g);
        String label = "l";
        g.fillRect(0,0,32,32);
    }
}
