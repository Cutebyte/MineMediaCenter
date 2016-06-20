package net.hopskocz.mmc.launcher.downloader;

import net.hopskocz.mmc.GUI.MMCFrame;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by hopskocz on 03.05.14.
 */
public class Downloader extends Thread {

    private Queue<DownloadJob> jobs;
    private boolean running;
    private MMCFrame frame;

    public Downloader() {
        jobs = new LinkedList<DownloadJob>();
    }

    public Downloader(MMCFrame frame) {
        jobs = new LinkedList<DownloadJob>();
        this.frame = frame;
    }

    @Override
    public void run() {
        DownloadJob current;
        int counter = 0;
        int currentNumber = 1;
        running = true;
        if(frame != null) {
            frame.showProgress();
            //System.out.println(jobs.size());
            frame.setProgressbar1Max(jobs.size());
        }

        while(running) {
            //System.out.println(jobs);
            //get job from queue -> download it -> run next job
            if(jobs.peek() != null) {
                current = jobs.peek();
                if(frame != null)
                    frame.setProgressBar1Value(currentNumber++);
                if(current.download(frame))
                    jobs.poll();
                else
                    if(counter<2) counter++;
                    else {
                        JOptionPane.showMessageDialog(null, "Coś nie tak z pobieraniem. Nie zostało ukończone.");
                        running = false;
                    }
            }
            else running = false;
        }
        if(frame != null)
            frame.hideProgress();
        jobs.clear();
    }

    public void addJob(DownloadJob job) {
        jobs.offer(job);
    }

    public void addRange(ArrayList<DownloadJob> jobsList) {
        for(Iterator<DownloadJob> itr = jobsList.iterator(); itr.hasNext(); ) {
            jobs.offer(itr.next());
        }
    }

    public boolean isFinished() {
        return !running;
    }

    public void setFrame(MMCFrame frame) {
        this.frame = frame;
    }
}
