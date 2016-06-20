package net.hopskocz.mmc.GUI;

import javax.swing.*;
import java.awt.*;

/**
 * Created by hopskocz on 08.07.14.
 */
public class MainPage {

    public MainPage() {

    }

    public static JPanel createInterface() {
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(255,255,255,50));

        mainPanel.add(new JButton("Test"));

        return mainPanel;
    }
}
