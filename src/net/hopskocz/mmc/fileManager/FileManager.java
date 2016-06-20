package net.hopskocz.mmc.fileManager;

import net.hopskocz.mmc.Logger.Logger;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FileManager {
	public static String load( String source ) throws IOException {
		String result;
		
		BufferedReader br = new BufferedReader( new FileReader( source ));
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while( line != null ) {
				sb.append( line );
				sb.append( '\n' );
				line = br.readLine();
			}
			result = sb.toString().substring(0, sb.length()-1);
		} finally {
			br.close();
		}
		
		return result;
	}

    public static ArrayList<String> loadAsList(String source) throws IOException {
        ArrayList<String> result = new ArrayList<String>();

        BufferedReader br = new BufferedReader( new FileReader( source ));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while( line != null ) {
                sb.append( line );
                sb.append( '\n' );
                line = br.readLine();
            }
            result.add(sb.toString().substring(0, sb.length()-1));
        } finally {
            br.close();
        }

        return result;
    }

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++ ) {
                boolean success = deleteDir(new File(dir, children[i]));
                if(!success)
                    return false;
            }
        }
        if(dir.delete()) {
            return true;
        }
        else
            return false;
    }

    public static boolean deleteFile(File file) {
        String name = file.getName();
        boolean success = file.delete();
        if (!success)
            Logger.warning("Somehow... file survived. ;-;");
        else
            Logger.info("Deleted "+name+" successfully!");
        return success;
    }

    public static void unpack(String source, String target) {
        File natives = new File(target);
        natives.mkdir();
        try {
            ZipFile zipFile = new ZipFile(source);
            zipFile.extractAll(target);
        } catch (ZipException e) {
            e.printStackTrace();
        }
    }
}
