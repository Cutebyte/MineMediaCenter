package net.hopskocz.mmc.launcher;

import net.hopskocz.mmc.Logger.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Tomek on 2014-09-05.
 */
public class OutputGrabber extends Thread {
    InputStream inputStream;

    OutputGrabber(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public void run() {
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = null;
            while ((line = bufferedReader.readLine()) != null)
                Logger.print(line);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
