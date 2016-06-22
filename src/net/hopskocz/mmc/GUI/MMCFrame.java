package net.hopskocz.mmc.GUI;

import net.hopskocz.mmc.GUI.LoginBox.LoginBox;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Callable;

/**
 * MineMediaCenter - main frame
 * Setting up GUI
 * It's a mess, but it's working. :D
 */
public class MMCFrame extends JFrame implements ActionListener {
    // variables
    private ImagePanel mainPanel;
    private NicePanel upperPanel;
    private ShadowPanel centerPanel;
    private NicePanel bottomPanel;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private CuteButton loginButton;
    private LoginBox loginBox;
    private JPanel cards;
    private JProgressBar progressbar1;
    private JProgressBar progressbar2;
    private NicePanel progressPanel;

    private ArrayList<String> commandTabList = new ArrayList<String>();

    private String lastCard;

    private JPanel front;
    private JPanel behind;
    private ShadowPanel shadow;

    private int borderVerticalSize;
    private int borderHorizontalSize;

    public MMCFrame( String version ) {
        super();

        lastCard = "";

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("MineMediaCenter v" + version);

        this.setSize(800,480);

        mainPanel = new ImagePanel(Theme.getBgImagePath());
        mainPanel.setLayout(new BorderLayout());

        // + upper panel -------------------------------------------------------------
        upperPanel = new NicePanel();
        upperPanel.setBackground(Theme.getUpperBarColor());
        upperPanel.setPreferredSize(new Dimension(100,Theme.getUpperBarSize()));
        upperPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        // + center panel ------------------------------------------------------------
        centerPanel = new ShadowPanel(Theme.getBgImagePath());
        centerPanel.setOpaque(false);

        ServerStatus status = new ServerStatus(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                centerPanel.reload();
                return null;
            }
        });
        status.start();

        //test
        centerPanel.setLayout(new BorderLayout());
        centerPanel.setVisible(true);

        JLayeredPane pane = new JLayeredPane();

        front = new JPanel();
        front.setLayout(new BorderLayout());
        front.setBounds(0,0,800,500);
        front.setBackground(new Color(255, 255, 255, 0));
        front.setOpaque(false);

        behind = new JPanel(new BorderLayout());
        behind.setBounds(0, 0, 800, 500);
        behind.setBackground(new Color(255,255,255,0));
        behind.setOpaque(false);
        behind.setVisible(false);

        pane.add(front, JLayeredPane.POPUP_LAYER);
        pane.add(behind, JLayeredPane.DEFAULT_LAYER);

        centerPanel.add(pane, BorderLayout.CENTER);

        cards = new JPanel(new CardLayout());
        cards.setBackground(new Color(255, 255, 255, 0));
        cards.setOpaque(false);
        // + login panel
        loginBox = new LoginBox(this);
        behind.add(cards,BorderLayout.CENTER);
        // - login panel
        front.add(loginBox.getRightBottom(), BorderLayout.SOUTH);
        progressPanel = new NicePanel();
        progressPanel.setLayout(new BorderLayout());
        progressPanel.setBackground(new Color(255, 255, 255, 0));
        progressPanel.setVisible(false);
        progressPanel.setOpaque(false);
        progressbar1 = new JProgressBar();
        progressbar1.setForeground(new Color(255, 255, 255, 255));
        progressbar1.setBackground(new Color(255, 255, 255, 0));
        progressbar1.setBorder(new LineBorder(new Color(255, 255, 255)));
        progressbar1.setOpaque(false);
        progressbar2 = new JProgressBar();
        progressbar2.setForeground(new Color(255, 255, 255, 255));
        progressbar2.setBackground(new Color(255,255,255,0));
        progressbar2.setBorder(new LineBorder(new Color(255, 255, 255)));
        progressbar2.setOpaque(false);
        //progressPanel.add(new Label("Test"));
        //progressPanel.setBorder(new EmptyBorder(50,0,0,0));
        progressPanel.add(progressbar1,BorderLayout.NORTH);
        progressPanel.add(progressbar2,BorderLayout.SOUTH);
        front.add(progressPanel,BorderLayout.NORTH);
        // - center panel ------------------------------------------------------------

        // + bottom panel ------------------------------------------------------------
        bottomPanel = new NicePanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.setBackground(Theme.getBottomBarColor());
        bottomPanel.setPreferredSize(new Dimension(0, Theme.getBottomBarSize()));

        //inside panels
        leftPanel = new NicePanel();
        rightPanel = new NicePanel();
        leftPanel.setBackground(new Color(255, 255, 255, 0)); // transparent
        rightPanel.setBackground(new Color(255, 255, 255, 0)); // transparent

        //JLabel label = new JLabel("Debug version - only for testing purposes (GUI test)"); // TEST
        JLabel label = new JLabel("MineMediaCenter | Pozdrowienia od Kapciuszka"); // TEST
        leftPanel.add(label);

        //buttons
        loginButton = new CuteButton();
        loginButton.setPreferredSize(Theme.getLoginButtonSize());
        loginButton.setBorder(new EmptyBorder(0, 0, 0, 0));
        loginButton.setImagePath(Theme.getLoginButtonIconPath());
        loginButton.setActionCommand("loginButtonClicked");
        loginButton.addActionListener(this);
        rightPanel.add(loginButton);

        // adding components to panel
        bottomPanel.add(leftPanel, BorderLayout.CENTER);
        bottomPanel.add(rightPanel,BorderLayout.LINE_END);
        // - bottom panel ------------------------------------------------------------

        // organize interface
        mainPanel.add(upperPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        this.add(mainPanel);
    }

    public void setBorderVerticalSize(int size) {
        borderVerticalSize = size;
    }

    public void setBorderHorizontalSize(int size) {
        borderHorizontalSize = size;
    }

    public void validate() {
        behind.setBounds(0,0,getWidth()-borderHorizontalSize,this.getHeight()-Theme.getUpperBarSize()-Theme.getBottomBarSize()-borderVerticalSize);
        front.setBounds(0,0,getWidth()-borderHorizontalSize,this.getHeight()-Theme.getUpperBarSize()-Theme.getBottomBarSize()-borderVerticalSize);

        super.validate();
    }

    public void actionPerformed(ActionEvent e) {
        if("loginButtonClicked".equals(e.getActionCommand())) {
            // show or hide login box
            loginBox.changeVisibility();
        }
        String temp;
        for(Iterator<String> itr = commandTabList.iterator(); itr.hasNext(); ) {
            temp = itr.next();
            if(temp.equals(e.getActionCommand())) {
                CardLayout cl = (CardLayout)(cards.getLayout());
                cl.show(cards, temp);
                if(lastCard.equals(temp)) {
                    behind.setVisible(false);
                    lastCard = "";
                }
                else {
                    behind.setVisible(true);
                    lastCard = temp;
                }
            }
        }
    }

    public void addNewTab(String name, JPanel panel) {

        CuteButton button = new CuteButton();

        button.setText(name);
        button.setActionCommand(name);
        button.addActionListener(this);

        commandTabList.add(name);
        cards.add(panel,name);
        this.upperPanel.add(name,button);
    }

    public void setProgressBar1Value(int value) {
        progressbar1.setValue(value);
    }

    public void setProgressBar2Value(int value) {
        progressbar2.setValue(value);
    }

    public void setProgressbar1Max(int value) {
        progressbar1.setMaximum(value);
    }

    public void setProgressbar2Max(int value) {
        progressbar2.setMaximum(value);
    }

    public void showProgress() {
        progressPanel.setVisible(true);
    }

    public void hideProgress() {
        progressPanel.setVisible(false);
    }

    public LoginBox getLoginBox() {
        return loginBox;
    }
}
