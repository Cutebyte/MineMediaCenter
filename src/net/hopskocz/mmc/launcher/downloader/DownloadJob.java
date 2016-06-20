package net.hopskocz.mmc.launcher.downloader;

import net.hopskocz.mmc.GUI.MMCFrame;
import net.hopskocz.mmc.fileManager.FileManager;

import javax.swing.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by hopskocz on 03.05.14.
 */
public class DownloadJob {

    private boolean finished;
    private String target, source;
    private int size, progress;

    private boolean extract;

    public DownloadJob(String source, String target) {
        finished = false;
        this.source = source;
        this.target = target;
    }

    public boolean download(MMCFrame frame) {

        boolean result = true;
        File file = new File(target);

        if (!file.exists()) {


            progress = 0;

            System.out.println("Currently downloading: " + source + " -> " + target);


            URL url = null;
            try {
                url = new URL(source);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection connect = null;
            try {
                connect = (HttpURLConnection) (url != null ? url.openConnection() : null);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //int size;
            if ((connect != null ? connect.getHeaderFields().get("Content-Length") : null) != null) {
                size = Integer.parseInt(connect.getHeaderFields().get("Content-Length").toString().replace("[", "").replace("]", ""));
                if (frame != null)
                    frame.setProgressbar2Max(size);
            } else {
                size = 0;
            }


            try {
                InputStream input = connect.getInputStream();
                byte[] buffer = new byte[4096];
                int portionSize;
                file.getParentFile().mkdirs();
                OutputStream output = new FileOutputStream(file);
                //int progress = 0;

                while ((portionSize = input.read(buffer)) != -1) {
                    output.write(buffer, 0, portionSize);
                    progress += portionSize;
                    if (frame != null)
                        frame.setProgressBar2Value(progress);
                    //System.out.println(progress + "/" + size + " - " + (int) (((float) progress / (float) size) * 100));
                }

                if (progress == size || size == 0) {
                    result = true;
                    // System.out.println("Gufno");
                } else {
                    result = false;
                    FileManager.deleteFile(file);
                    //System.out.println("To tutaj! - " + progress + " = " + size);
                }

            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Błąd wejścia/wyjścia. Plik nie został pobrany i kaszanka.");
            }

        } else
            result = true;
        return result;
    }

    public int getFileProgress() {
        return progress;
    }

    public int getFileSize() {
        return size;
    }

    public boolean isFinished() {
        return finished;
    }
}
