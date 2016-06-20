package net.hopskocz.mmc.launcher;

import net.hopskocz.mmc.launcher.downloader.DownloadJob;
import net.hopskocz.mmc.launcher.downloader.Downloader;
import net.hopskocz.mmc.launcher.downloader.Parser;
import net.hopskocz.mmc.GUI.MMCFrame;
import net.hopskocz.mmc.fileManager.FileManager;
import net.hopskocz.mmc.unzipjar.UnZipJar;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.prefs.Preferences;

/**
 * This is the main class for launcher
 * Why isn't it inside MMC?
 * I want to make possible to choose which Launcher package you want to use.
 * Differencies? I'll try to make possible to use Mojang launcher inside MMC.
 * Why? I'll be safer, than running custom launcher written by someone (especially me xD)
 * I plan to create 2 versions of launcher package. Online and offline. Why? You know best.
 *
 * Launcher command line parameters:
 * username password version mode path
 * should look like:
 * launcher --username USERNAME --password PASSWORD --version VERSION --mode MODE:binary[0,1]:optional --path /PATH/PATH/PATH/
 */
public class Launcher implements Runnable {
    private static String username;
    private static String password;
    private static String version;
    private static boolean mode;
    private static String path;
    private static boolean lock = false;

    public static void main(String[] args) {
        System.out.println(System.getProperty("os.name"));

        for(int i=0;i<args.length;i+=2) {
            if(args[i].equals("--username")) {
                username=args[i+1];
            }
            if(args[i].equals("--password")) {
                password=args[i+1];
            }
            if(args[i].equals("--version")) {
                version="1.7.4";//args[i+1];
            }
            if(args[i].equals("--mode")) {
                mode=Boolean.parseBoolean(args[i+1]);
            }
            if(args[i].equals("--path")) {
                path=args[i+1];
            }
        }
        System.out.println("Welcome to MMC - text mode");
        System.out.println("username="+username+"\npassword="+password+"\nversion="+version+"\nmode="+mode+"\npath="+path);
        System.out.println("Here it goes!!!");

        System.out.println(System.getProperty("user.dir"));

        //first download version list
        Downloader downloader = new Downloader();
        downloader.addJob(new DownloadJob("http://s3.amazonaws.com/Minecraft.Download/versions/versions.json",path+"/versions/versions.json"));
        System.out.println("http://s3.amazonaws.com/Minecraft.Download/versions/versions.json "+path+"/versions/versions.json");
        // Okay shit is working so far...
        //Lets download version json!!!
        //"http://s3.amazonaws.com/Minecraft.Download/versions/" + version + "/" + version + ".json;"
        downloader.addJob(new DownloadJob("http://s3.amazonaws.com/Minecraft.Download/versions/" + version + "/" + version + ".json", path + "/versions/" + version + "/" + version + ".json"));
        System.out.println("http://s3.amazonaws.com/Minecraft.Download/versions/" + version + "/" + version + ".json "+path+"/versions/"+version+"/"+version+".json");

        //downloader.addJob(new DownloadJob("https://s3.amazonaws.com/Minecraft.Download/indexes/1.7.4.json", path + "/assets/indexes/1.7.4.json"));

        downloader.run();

        //version list - needed by launcher to load into combobox
        try {
            System.out.println(Parser.parseVersions(path + "/versions/versions.json"));
            downloader.addRange(Parser.parseVersionFile(version, path));
        } catch (IOException e) {
            e.printStackTrace();
        }
/*
        try {
            Parser.parseJson(version,path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
        //assets
        String asset ="";
        try {
            asset = FileManager.load(path
                    + "/versions/" + version + "/assets.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("https://s3.amazonaws.com/Minecraft.Download/indexes/"+asset+".json"+" "+ path + "/assets/indexes/"+asset+".json");
        downloader.addJob(new DownloadJob("https://s3.amazonaws.com/Minecraft.Download/indexes/" + asset + ".json", path + "/assets/indexes/" + asset + ".json"));

        downloader.run();


        //files - libs and game itself

        try {
            downloader.addRange(Parser.parseAssets(version, path, asset));
        } catch (IOException e) {
            e.printStackTrace();
        }


        downloader.run();

        //extract natives
        String nativesList ="";
        try {
            nativesList = FileManager.load(path + "/versions/" + version + "/natives.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] natives = nativesList.split("\n");

        for(int i = 0; i<natives.length; i++) {
            //UnZipJar.unzipJar(natives[i],path+"/natives/");
            FileManager.unpack(natives[i], path + "/versions/" + version + "/natives/");
        }
        System.out.println("Whoa");

        // launch game
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //wait for finish

        //delete natives

    }

    public static void launch(String username, String password, String version, boolean mode, String path, MMCFrame frame) {
        //lock = true;
        Downloader downloader = new Downloader(frame);
        System.out.println("Launching... and here's what i got: "+username+" "+password+" "+version+" "+mode+" "+path);
        //download exact version
        // Okay shit is working so far...
        //Lets download version json!!!
        //"http://s3.amazonaws.com/Minecraft.Download/versions/" + version + "/" + version + ".json;"
        downloader.addJob(new DownloadJob("http://s3.amazonaws.com/Minecraft.Download/versions/" + version + "/" + version + ".json", path + "/versions/" + version + "/" + version + ".json"));
        System.out.println("http://s3.amazonaws.com/Minecraft.Download/versions/" + version + "/" + version + ".json "+path+"/versions/"+version+"/"+version+".json");

        //downloader.addJob(new DownloadJob("https://s3.amazonaws.com/Minecraft.Download/indexes/1.7.4.json", path + "/assets/indexes/1.7.4.json"));

        downloader.run();

        //version list - needed by launcher to load into combobox
        ArrayList<DownloadJob> libList = null;
        try {
            libList = Parser.parseVersionFile(version, path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String asset ="";
        try {
            asset = FileManager.load(path
                    + "/versions/" + version + "/assets.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("https://s3.amazonaws.com/Minecraft.Download/indexes/"+asset+".json"+" "+ path + "/assets/indexes/"+asset+".json");
        downloader.addJob(new DownloadJob("https://s3.amazonaws.com/Minecraft.Download/indexes/" + asset + ".json", path + "/assets/indexes/" + asset + ".json"));
/*
        try {
            Parser.parseJson(version,path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
        //assets

        downloader.run();


        //files - libs and game itself

        try {
            downloader.addRange(Parser.parseAssets(version, path, asset));
        } catch (IOException e) {
            e.printStackTrace();
        }
        downloader.addRange(libList);

        downloader.start();
        System.out.println("Let's go...");
        //lock = false;


        String arguments = null, libraries = null, mainClass = null;
        String command = null;
        List<String> params = new ArrayList<String>();

        try {
            arguments = FileManager.load( path + "/versions/" + version
                    + "/arguments.txt" );
            libraries = FileManager.load( path + "/versions/" + version
                    + "/libraries.txt" );
            mainClass = FileManager.load( path + "/versions/" + version
                    + "/mainClass.txt" );
        } catch( IOException e ) {
            e.printStackTrace();
        }

        arguments = arguments.replace( "${auth_player_name}", username );
        arguments = arguments.replace( "${auth_session}", "token:1:1" );
        arguments = arguments.replace( "${version_name}", version );
        arguments = arguments.replace( "${game_directory}", "\"" + path + "\"" );
        arguments = arguments.replace( "${game_assets}", "\"" + path
                + "/assets\"" );
        arguments = arguments.replace( "${auth_access_token}", "1" );
        arguments = arguments.replace( "${assets_root}", path + "/assets" );
        arguments = arguments.replace( "${assets_index_name}", asset );
        arguments = arguments.replace( "${user_properties}", "{}" );
        arguments = arguments.replace( "${user_type}", "${user_type}" );
        // ${auth_access_token} same as token:accesstoken:1
        // ${auth_uuid} same as token:1:userid

        //arguments = arguments.substring( 0, arguments.length() - 1 );
        //mainClass = mainClass.substring( 0, mainClass.length() - 1 );
        //libraries = libraries.substring( 0, libraries.length() - 1 ).replaceAll( ":", File.pathSeparator ).replaceAll( "\"", "" );

        //if( prefs.get( "Prefix", null ) != null ) params.add( prefs.get( "Prefix", null ) );

        params.add( getJavaDir() );

        params.add( "-Djava.library.path=" + path + "/versions/"+version+"/natives" );
        params.add( "-cp" );
        params.add( libraries );

        params.add( mainClass );

        String[] temp = arguments.split( " " );

        for( int i = 0; i < temp.length; i++ ) {
            if( temp[i].contains( "--" ) ) {
                if( temp[i + 1].contains( "/" ) ) {
                    params.add( temp[i] + " " + temp[i + 1] );
                    i++;
                } else {
                    params.add( temp[i] );
                    params.add( temp[i + 1] );
                    i++;
                }
            } else
                params.add( temp[i] );
        }



        while(!downloader.isFinished()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //extract natives
        String nativesList ="";
        try {
            nativesList = FileManager.load(path + "/versions/" + version + "/natives.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] natives = nativesList.split("\n");

        for(int i = 0; i<natives.length; i++) {
            //UnZipJar.unzipJar(natives[i],path+"/natives/");
            FileManager.unpack(natives[i], path + "/versions/" + version + "/natives/");
        }

        System.out.println("Here we go!");

        System.out.println( command );
        System.out.println( params );

        ProcessBuilder pb = new ProcessBuilder( params );
        pb.directory( new File( path ) );
        Process pr = null;
        try {
            pr = pb.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // pb.start();
        try {
            System.out.println( readInputStream( pr.getErrorStream() ) );
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            pr.waitFor();
            FileManager.deleteDir(new File(path + "/versions/" + version + "/natives/"));
        } catch( Exception e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static String getJavaDir() {
        String separator = System.getProperty( "file.separator" );
        String path = ( new StringBuilder() ).append( System.getProperty( "java.home" ) ).append( separator ).append( "bin" ).append( separator ).toString();
        if( System.getProperty( "os.name" ).toLowerCase().contains("windows")
                && ( new File( ( new StringBuilder() ).append( path ).append( "javaw.exe" ).toString() ) ).isFile() )
            return ( new StringBuilder() ).append( path ).append( "javaw.exe" ).toString();
        else
            return ( new StringBuilder() ).append( path ).append( "java" ).toString();
    }

    public static String readInputStream( InputStream inputStream )
            throws IOException {
        StringBuilder sb = new StringBuilder();
        byte[] buffer = new byte[8192];
        Charset cs = Charset.defaultCharset(); // or something different
        while( true ) {
            int readCount = inputStream.read( buffer );
            if( -1 == readCount ) {
                break;
            }
            String x = new String( buffer, 0, readCount, cs );
            sb.append( x );
        }
        String y = sb.toString();
        return y;
    }

    public static boolean getLock() {
        return lock;
    }

    @Override
    public void run() {

    }
}
