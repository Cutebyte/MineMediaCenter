package net.hopskocz.mmc.GUI.LoginBox;

import net.hopskocz.mmc.GUI.Theme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by hopskocz on 08.04.14.
 */
public class LoginBoxButton extends JButton implements MouseListener {
    //variables
    private Color color;
    private Color hoverColor;
    private Color clickedColor;
    private Color curColor;
    //path to icon
    private String imagePath;

    public LoginBoxButton(Theme theme) {
        super();
        //hehehe
        color = theme.getLoginButtonColor();
        hoverColor = theme.getLoginButtonHover();
        clickedColor = theme.getLoginButtonClicked();
        curColor = theme.getLoginButtonColor();
        imagePath = theme.getLoginButtonIconPath();
        addMouseListener(this);
    }

    public void setColors(Theme theme) {

    }

    public void paintComponent(Graphics g) {
        //
        g.setColor(curColor);
        g.fillRect(0,0,this.getWidth(),this.getHeight());
        //g.setColor(Color.black);
        //g.drawString(getText(),0,getHeight()-(int)(getHeight()/2.5));

        super.paintComponent(g);
    }

    public void mouseClicked(MouseEvent e) {
        //curColor = clickedColor;
    }

    public void mousePressed(MouseEvent e) {
        curColor = clickedColor;
    }

    public void mouseReleased(MouseEvent e) {
        curColor = color;
    }

    public void mouseEntered(MouseEvent e) {
        curColor = hoverColor;
    }

    public void mouseExited(MouseEvent e) {
        curColor = color;
    }
}
