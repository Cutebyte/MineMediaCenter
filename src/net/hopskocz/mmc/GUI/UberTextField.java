package net.hopskocz.mmc.GUI;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Created by hopskocz on 13.04.14.
 */
public class UberTextField extends JTextField {

    public UberTextField(String text) {
        super(text);
        setOpaque(false);
    }

    @Override
    public void setBorder(Border border) {
        //lel
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(getBackground());
        //g.clearRect(0,0,this.getWidth(),this.getHeight());
        g.fillRect(0,0,this.getWidth(),this.getHeight());

        super.paintComponent(g);
    }
}
