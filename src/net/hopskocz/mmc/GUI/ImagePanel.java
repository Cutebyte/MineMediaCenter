package net.hopskocz.mmc.GUI;

import javax.swing.*;
import java.awt.*;

/**
 * Created by hopskocz on 06.04.14.
 */
public class ImagePanel extends JPanel {
    //variables
    //String imagePath;
    Image image;
    Image anim;

    public ImagePanel() {
        super();
        setLayout(null);
        //imagePath = null;
    }

    public ImagePanel(String imagePath) {
        super();
        setLayout(null);
        //this.imagePath = imagePath;
        image = new ImageIcon(this.getClass().getResource(imagePath+"bg.png")).getImage();
        //anim = new ImageIcon(this.getClass().getResource(imagePath+".gif")).getImage();
        //SVGUniverse universe = new SVGUniverse();
        //universe.loadSVG(this.getClass().getResource("res/TapetkaWIP5.svg"));
        //universe.getDiagram();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        //g.drawImage(anim, 0, 0, getWidth(), getHeight(), this);
    }

/*
    public void validate() {
        super.validate();
        Graphics g = this.getGraphics();
        //g.drawImage(anim, 0, 0, getWidth(), getHeight(), this);
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
    }

    public void revalidate() {
        super.revalidate();
        Graphics g = this.getGraphics();
        //g.drawImage(anim, 0, 0, getWidth(), getHeight(), this);
        if(image != null)
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);

    }
/*
    public void validate() {
        super.validate();

        this.updateUI();
    }
    */
}
