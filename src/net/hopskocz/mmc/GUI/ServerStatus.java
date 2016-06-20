package net.hopskocz.mmc.GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;

/**
 * Created by Tomek on 2014-09-11.
 */
public class ServerStatus extends Thread {
    private Callable<Void> callable;

    public ServerStatus(Callable<Void> callable) {
        this.callable = callable;
    }

    public void run() {
        while(true) {
            try {
                callable.call();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
