package net.hopskocz.mmc.GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by hopskocz on 08.04.14.
 */
public class CuteButton extends JButton implements MouseListener {
    //variables
    private Color color;
    private Color hoverColor;
    private Color clickedColor;
    private Color curColor;
    //path to icon
    private String imagePath;

    public CuteButton() {
        super();
        //hehehe
        color = Theme.getLoginButtonColor();
        hoverColor = Theme.getLoginButtonHover();
        clickedColor = Theme.getLoginButtonClicked();
        curColor = Theme.getLoginButtonColor();
        addMouseListener(this);
        setBackground(new Color(255, 255, 255, 0));
        setForeground(new Color(255,255,255,255));
        setBorderPainted(false);
        setOpaque(false);
        setRolloverEnabled(false);
        super.setContentAreaFilled(false);
        setFocusPainted(false);
    }

    public CuteButton(String text) {
        super(text);
        //hehehe
        color = Theme.getLoginButtonColor();
        hoverColor = Theme.getLoginButtonHover();
        clickedColor = Theme.getLoginButtonClicked();
        curColor = Theme.getLoginButtonColor();
        addMouseListener(this);
        setBackground(new Color(255, 255, 255, 0));
        setForeground(new Color(255,255,255,255));
        setBorderPainted(false);
        setOpaque(false);
        setRolloverEnabled(false);
        super.setContentAreaFilled(false);
        setFocusPainted(false);
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setColors(Theme theme) {

    }

    public void paintComponent(Graphics g) {
        //super.paintComponent(g);

        g.setColor(curColor);
        g.fillRect(0,0,this.getWidth(),this.getHeight());
        if(imagePath!=null) {
            try {
                Image image = new ImageIcon(this.getClass().getResource(imagePath)).getImage();
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            } catch (NullPointerException e) {
            }
        }


        super.paintComponent(g);
    }

    public void mouseClicked(MouseEvent e) {
        //curColor = clickedColor;
        //repaint();
    }

    public void mousePressed(MouseEvent e) {
        curColor = clickedColor;
        repaint();
    }

    public void mouseReleased(MouseEvent e) {
        curColor = color;
        repaint();
    }

    public void mouseEntered(MouseEvent e) {
        curColor = hoverColor;
        repaint();
    }

    public void mouseExited(MouseEvent e) {
        curColor = color;
        repaint();
    }
}
