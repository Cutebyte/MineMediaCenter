package net.hopskocz.mmc.launcher;

import net.hopskocz.mmc.GUI.MMCFrame;
import net.hopskocz.mmc.Logger.Logger;
import net.hopskocz.mmc.Options.Options;
import net.hopskocz.mmc.fileManager.FileManager;
import net.hopskocz.mmc.launcher.downloader.DownloadJob;
import net.hopskocz.mmc.launcher.downloader.Downloader;
import net.hopskocz.mmc.launcher.downloader.Parser;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the main class for launcher
 * Why isn't it inside MMC?
 * I want to make possible to choose which Launcher package you want to use.
 * Differencies? I'll try to make possible to use Mojang launcher inside MMC.
 * Why? I'll be safer, than running custom launcher written by someone (especially me xD)
 * I plan to create 2 versions of launcher package. Online and offline. Why? You know best.
 * <p/>
 * Launcher command line parameters:
 * username password version mode path
 * should look like:
 * launcher --username USERNAME --password PASSWORD --version VERSION --mode MODE:binary[0,1]:optional --path /PATH/PATH/PATH/
 */
public class BetterLauncher extends Thread {
    private String username;
    private String password;
    private String version;
    private String asset;
    private boolean mode;
    private String path;

    private MMCFrame frame;

    public BetterLauncher() {

    }

    public void setup(String username, String password, String version, boolean mode, String path, MMCFrame frame) {
        this.username = username;
        this.password = password;
        this.version = version;
        this.mode = mode;
        this.path = path;
        this.frame = frame;
    }

    public void test() {
        frame.getLoginBox().lock(true);
        frame.showProgress();

        Logger.info("Downloading files...");
        //downloading and shit goes here
        downloadFiles(prepareDownload());
        Logger.info("Downloading finished!");
        Logger.info("Preparing to launch Minecraft "+version);
        List<String> params = prepareLaunch();
        //finnaly launch that bastard
        launch(params);

        frame.getLoginBox().lock(false);
        frame.hideProgress();
        resetProgressbars();
    }

    public void resetProgressbars() {
        frame.setProgressBar1Value(0);
        frame.setProgressBar2Value(0);
    }

    public ArrayList<DownloadJob> prepareDownload() {

        ArrayList<DownloadJob> jobList = new ArrayList<DownloadJob>();
        Downloader downloader = new Downloader();

        downloader.addJob(new DownloadJob("http://s3.amazonaws.com/Minecraft.Download/versions/" + version + "/" + version + ".json", path + "/versions/" + version + "/" + version + ".json"));
        downloader.run();

        try {
            jobList.addAll(Parser.parseVersionFile(version, path));
        } catch (IOException e) {
            e.printStackTrace();
        }

        asset = loadAssetVersion();

        downloader.addJob(new DownloadJob("https://s3.amazonaws.com/Minecraft.Download/indexes/" + asset + ".json", path + "/assets/indexes/" + asset + ".json"));
        downloader.run();

        try {
            jobList.addAll(Parser.parseAssets(version, path, asset));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jobList;
    }

    public void downloadFiles(ArrayList<DownloadJob> jobList) {
        Downloader downloader = new Downloader(frame);
        downloader.addRange(jobList);
        downloader.run();
    }

    public List<String> prepareLaunch() {
        String arguments = null, libraries = null, mainClass = null;
        String command = null;
        List<String> params = new ArrayList<String>();

        try {
            arguments = FileManager.load(path + "/versions/" + version
                    + "/arguments.txt");
            libraries = FileManager.load(path + "/versions/" + version
                    + "/libraries.txt");
            mainClass = FileManager.load(path + "/versions/" + version
                    + "/mainClass.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        arguments = arguments.replace("${auth_player_name}", username);
        arguments = arguments.replace("${auth_session}", "token:1:1");
        arguments = arguments.replace("${auth_uuid}", Options.getFakeUUID()); //temporary solution
        arguments = arguments.replace("${version_name}", version);
        arguments = arguments.replace("${game_directory}", "\"" + path + "\"");
        arguments = arguments.replace("${game_assets}", "\"" + path
                + "/assets\"");
        arguments = arguments.replace("${auth_access_token}", "1");
        arguments = arguments.replace("${assets_root}", path + "/assets");
        arguments = arguments.replace("${assets_index_name}", asset);
        arguments = arguments.replace("${user_properties}", "{}");
        arguments = arguments.replace("${user_type}", "legacy");

        params.add(getJavaDir());
        //params.add("javaw");

        params.addAll(Options.getJavaArgs());

        params.add("-Djava.library.path=" + path + "/versions/" + version + "/natives");
        params.add("-cp");
        params.add(libraries);

        params.add(mainClass);

        String[] temp = arguments.split(" ");

        for (int i = 0; i < temp.length; i++) {
            if (temp[i].contains("--")) {
                if (temp[i + 1].contains("/")) {
                    params.add(temp[i] + " " + temp[i + 1]);
                    i++;
                } else {
                    params.add(temp[i]);
                    params.add(temp[i + 1]);
                    i++;
                }
            } else
                params.add(temp[i]);
        }

        //extract natives
        String nativesList = "";
        try {
            nativesList = FileManager.load(path + "/versions/" + version + "/natives.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] natives = nativesList.split("\n");

        Logger.info("Unpacking natives...");

        for (int i = 0; i < natives.length; i++) {
            //UnZipJar.unzipJar(natives[i],path+"/natives/");
            FileManager.unpack(natives[i], path + "/versions/" + version + "/natives/");
        }

        return params;
    }

    public String loadAssetVersion() {
        String asset = "";
        try {
            asset = FileManager.load(path + "/versions/" + version + "/assets.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return asset;
    }

    public void launch(List<String> params) {

        Logger.info("Command: "+params);

        ProcessBuilder pb = new ProcessBuilder(params);
        pb.directory(new File(path));
        Process pr = null;
        try {
            Logger.info("Launching Minecraft "+version+"...");
            pr = pb.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        OutputGrabber errorGrabber = new OutputGrabber(pr.getErrorStream());
        OutputGrabber outputGrabber = new OutputGrabber(pr.getInputStream());
        errorGrabber.start();
        outputGrabber.start();
        try {
            pr.waitFor();
            Logger.info("Deleting natives...");
            FileManager.deleteDir(new File(path + "/versions/" + version + "/natives/"));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    public String getJavaDir() {
        String separator = System.getProperty("file.separator");
        String path = (new StringBuilder()).append(System.getProperty("java.home")).append(separator).append("bin").append(separator).toString();
        Logger.info("Your java dir: "+path);
        if (System.getProperty("os.name").toLowerCase().contains("windows")
                && (new File((new StringBuilder()).append(path).append("javaw.exe").toString())).isFile())
            return (new StringBuilder()).append(path).append("javaw.exe").toString();
        else
            return (new StringBuilder()).append(path).append("java").toString();
    }

    public String readInputStream(InputStream inputStream)
            throws IOException {
        StringBuilder sb = new StringBuilder();
        byte[] buffer = new byte[8192];
        Charset cs = Charset.defaultCharset(); // or something different
        while (true) {
            int readCount = inputStream.read(buffer);
            if (-1 == readCount) {
                break;
            }
            String x = new String(buffer, 0, readCount, cs);
            sb.append(x);
        }
        String y = sb.toString();
        return y;
    }

    @Override
    public void run() {
        test();
    }
}
