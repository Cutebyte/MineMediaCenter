package net.hopskocz.mmc.GUI.LoginBox;

import net.hopskocz.mmc.launcher.BetterLauncher;
import net.hopskocz.mmc.launcher.Launcher;
import net.hopskocz.mmc.GUI.*;
import net.hopskocz.mmc.Options.Options;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by hopskocz on 08.04.14.
 */
public class LoginBox implements ActionListener {
    // bool that determines that box should be visible or not
    private boolean visible;
    private JPanel rightBottom;
    private AwesomePanel loginPanel;
    private UberTextField loginField;
    private JPasswordField passwordField;
    private CuteButton launchButton;
    private MMCFrame frame;

    private Color panelBgColor;

    public LoginBox(MMCFrame frame) {
        visible = false;

        this.frame = frame;


        rightBottom = new NicePanel();
        rightBottom.setBackground(new Color(255, 255, 255, 0)); // transparent
        rightBottom.setLayout(new FlowLayout(FlowLayout.RIGHT));
        loginPanel = new AwesomePanel();
        loginPanel.setPreferredSize(Theme.getLoginBoxSize()); // size
        loginPanel.setBgColor(Theme.getLoginBoxPanelBgColor());
        loginPanel.setBorder(new EmptyBorder(10,10,50,10));


        // loginPanel components

        loginField = new UberTextField("Login");
        loginField.setText(Options.getPreferences().get("LastLogin", "Login"));
        loginField.setPreferredSize(Theme.getLoginFieldSize());
        loginField.setFont(new Font("Arial", Font.BOLD, 18));
        loginField.setBackground(Theme.getLoginFieldBackgroundColor());
        //loginField.setBackground(new Color(0,0,0,0));
        //loginField.setOpaque(false);

        passwordField = new JPasswordField("Password");
        passwordField.setPreferredSize(Theme.getPasswordFieldSize());
        passwordField.setFont(new Font("Arial", Font.BOLD, 18));
        passwordField.setBackground(Theme.getPasswordFieldBackgroundColor());
        passwordField.setEnabled(Options.getMode());
        passwordField.setOpaque(false);

        JPanel buttonPanel = new NicePanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setPreferredSize(Theme.getLoginBoxButtonPanelSize());
        buttonPanel.setBackground(new Color(255, 255, 255, 0));
        buttonPanel.setBorder(new EmptyBorder(0,0,0,0));

        launchButton = new CuteButton();
        launchButton.setPreferredSize(Theme.getLoginBoxButtonSize());
        //launchButton.setBorder(new EmptyBorder(0,0,0,0));
        launchButton.setBackground(new Color(0, 0, 0, 0));
        launchButton.setRolloverEnabled(false);
        launchButton.setText("Odpalaj");
        launchButton.setActionCommand("launch");
        launchButton.addActionListener(this);
        //buttonPanel.add(new JLabel("Wersja:"));
        buttonPanel.add(launchButton);

        loginPanel.add(loginField);
        loginPanel.add(passwordField);
        loginPanel.add(buttonPanel);

        rightBottom.add(loginPanel);

        loginPanel.setVisible(visible);
    }



    public void hide() {
        visible = false;
        loginPanel.setVisible(visible);
    }

    public void changeVisibility() {
        visible = !visible;
        passwordField.setEnabled(Options.getMode());
        loginPanel.setVisible(visible);
    }

    public JPanel getRightBottom() {
        return rightBottom;
    }

    public void actionPerformed(ActionEvent a) {
        if(a.getActionCommand().equals("launch")) {
            if(!Launcher.getLock()) {
                Options.getPreferences().put("LastLogin",loginField.getText());
                //Launcher.launch(loginField.getText(), String.valueOf(passwordField.getPassword()),comboBox.getSelectedItem().toString(), Options.getMode(), Options.getGamePath(), frame);
                if(checkFuckOff())
                    JOptionPane.showMessageDialog(null,"Błąd 563: Jesteś zjebem, chuj Ci w dupę");
                else
                    launch();
            }
            else
                JOptionPane.showMessageDialog(null, "Gra jest właśnie uruchamiana. Ogarnij się...");
        }
    }

    public void lock(boolean value) {
        if(value)
            launchButton.setText("Czekaj...");
        else
            launchButton.setText("Odpalaj");
        launchButton.setEnabled(!value);
    }

    public void launch() {
        BetterLauncher launcher = new BetterLauncher();
        launcher.setup(loginField.getText(),String.valueOf(passwordField.getPassword()),Options.getCurrentVersion(), Options.getMode(), Options.getGamePath(), frame);
        launcher.start();
    }

    private boolean checkFuckOff() {
        ArrayList<String> fuckOffList = new ArrayList<String>();
        fuckOffList.add("bartezz5");

        for(Iterator itr = fuckOffList.iterator(); itr.hasNext(); ) {
            //System.out.println(loginField+" "+itr.next());
            if(loginField.getText().equals(itr.next())) {
                return true;
            }
        }

        return false;
    }
}
