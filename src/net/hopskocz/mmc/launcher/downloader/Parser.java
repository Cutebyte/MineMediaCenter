package net.hopskocz.mmc.launcher.downloader;

import com.google.gson.*;
import com.google.gson.internal.LinkedTreeMap;

import java.io.*;
import java.util.*;

public class Parser {

    public static ArrayList<String> parseVersions(String path)
            throws IOException {
        ArrayList<String> list = new ArrayList<String>();

        String json = "";

        BufferedReader br = new BufferedReader(new FileReader(path));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append('\n');
                line = br.readLine();
            }
            json = sb.toString();
        } finally {
            br.close();
        }

        JsonElement jelement = new JsonParser().parse(json);
        JsonObject jobject = jelement.getAsJsonObject();
        JsonArray jarray = jobject.getAsJsonArray("versions");

        for (JsonElement temp : jarray) {
            jobject = temp.getAsJsonObject();
            if (jobject.get("type").getAsString().equals("release") || jobject.get("type").getAsString().equals("snapshot")
                    || jobject.get("type").getAsString().equals("old_beta") || jobject.get("type").getAsString().equals("old_alpha")) {
                list.add(jobject.get("type").getAsString() + ";" + jobject.get("id").getAsString());
            }
        }

        return list;
    }

    public static ArrayList<DownloadJob> parseAssets(String version, String path, String assetVersion) throws IOException {
        ArrayList<DownloadJob> list = new ArrayList<DownloadJob>();
        String json = "";

        BufferedReader br = new BufferedReader(new FileReader(path
                + "/assets/indexes/" + assetVersion + ".json"));

        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append('\n');
                line = br.readLine();
            }
            json = sb.toString();
        } finally {
            br.close();
        }

        Gson gson = new Gson();

        Map<String, Map<String, Object>> map = new HashMap<String, Map<String, Object>>();
        map = (Map<String, Map<String, Object>>) gson.fromJson(json, map.getClass());

        Map<String, Object> mapp = map.get("objects");
        Set<Map.Entry<String, Object>> mapSet = mapp.entrySet();

        for (Map.Entry<String, Object> entry : mapSet) {
            LinkedTreeMap<String, String> linked = (LinkedTreeMap<String, String>) entry.getValue();

            if (!assetVersion.equals("legacy"))
                list.add(new DownloadJob("http://resources.download.minecraft.net/" + linked.get("hash").toString().substring(0, 2) + "/" + linked.get("hash").toString(), path + "/assets/objects/" + linked.get("hash").toString().substring(0, 2) + "/" + linked.get("hash").toString()));
            else
                list.add(new DownloadJob("http://resources.download.minecraft.net/" + linked.get("hash").toString().substring(0, 2) + "/" + linked.get("hash").toString(), path + "/assets/virtual/legacy/" + entry.getKey().toString()));
        }
        return list;
    }

    public static ArrayList<DownloadJob> parseVersionFile(String version,
                                                          String path) throws IOException {

        ArrayList<DownloadJob> list = new ArrayList<DownloadJob>();
        String json = "";

        BufferedReader br = new BufferedReader(new FileReader(path
                + "/versions/" + version + "/" + version + ".json"));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append('\n');
                line = br.readLine();
            }
            json = sb.toString();
        } finally {
            br.close();
        }

        JsonElement jelement = new JsonParser().parse(json);
        JsonObject jobject = jelement.getAsJsonObject();
        String arguments = jobject.get("minecraftArguments").getAsString();
        String mainClass = jobject.get("mainClass").getAsString();
        String assets;
        String natives = "";
        if (jobject.get("assets") == null)
            assets = "legacy";
        else
            assets = jobject.get("assets").getAsString();

        // System.out.println( arguments + " - " + mainClass );

        PrintWriter writer = new PrintWriter(path + "/versions/" + version
                + "/arguments.txt", "UTF-8");
        writer.println(arguments);
        writer.close();
        writer = new PrintWriter(path + "/versions/" + version
                + "/mainClass.txt", "UTF-8");
        writer.println(mainClass);
        writer.close();
        writer = new PrintWriter(path + "/versions/" + version
                + "/assets.txt", "UTF-8");
        writer.println(assets);
        writer.close();

        JsonArray jarray = jobject.getAsJsonArray("libraries");

        String source;
        String libs = "";
        String target;

        for (JsonElement temp : jarray) {
            jobject = temp.getAsJsonObject();
            String tempStr = jobject.get("name").getAsString();

            if (tempStr.contains("nightly")) {
                //continue;
            }
            // System.out.println( tempStr );

            String[] tempList = tempStr.split(":");
            target = path + "/libraries/" + tempList[0].replace(".", "/") + "/";
            source = "https://libraries.minecraft.net/" + tempList[0].replace(".", "/") + "/";
            for (int i = 1; i < tempList.length; i++) {
                target += tempList[i] + "/";
                source += tempList[i] + "/";
            }
            target += tempList[tempList.length - 2];
            target += "-" + tempList[tempList.length - 1];

            source += tempList[tempList.length - 2];
            source += "-" + tempList[tempList.length - 1];
            if (jobject.has("natives")) {
                JsonObject nativesObject = jobject.get("natives").getAsJsonObject();
                if (System.getProperty("os.name").toLowerCase().contains("linux") && nativesObject.has("linux")) {
                    //fileDestination += "-natives-linux.jar\":";
                    target += "-" + nativesObject.get("linux").getAsString().replace("${arch}", System.getProperty("sun.arch.data.model")) + ".jar";
                    source += "-" + nativesObject.get("linux").getAsString().replace("${arch}", System.getProperty("sun.arch.data.model")) + ".jar";
                    //downloadSource += "-natives-linux.jar";
                    natives += target + "\n";
                }
                if (System.getProperty("os.name").toLowerCase().contains("windows") && nativesObject.has("windows")) {
                    //fileDestination += "-natives-windows.jar\":";
                    target += "-" + nativesObject.get("windows").getAsString().replace("${arch}", System.getProperty("sun.arch.data.model")) + ".jar";
                    source += "-" + nativesObject.get("windows").getAsString().replace("${arch}", System.getProperty("sun.arch.data.model")) + ".jar";
                    //downloadSource += "-natives-windows.jar";
                    natives += target + "\n";
                }
            } else {
                target += ".jar";
                source += ".jar";
            }

            // System.out.println( tempS );
            // System.out.println( result );

            libs += target+";";

            //list.add( tempS + ";"+ result.replace( "\"", "" ).replace( ":", "" ) );
            list.add(new DownloadJob(source, target));
        }

        writer = new PrintWriter(path + "/versions/" + version + "/natives.txt", "UTF-8");
        writer.println(natives);
        writer.close();

        source = "http://s3.amazonaws.com/Minecraft.Download/versions/"
                + version + "/" + version + ".jar";

        target = path + "/versions/" + version + "/" + version
                + ".jar";

        libs += target;

        //list.add( downloadSource + ";" + fileDestination.replace( "\"", "" ).replace( ":", "" ) );
        list.add(new DownloadJob(source, target));

        writer = new PrintWriter(path + "/versions/" + version
                + "/libraries.txt", "UTF-8");
        writer.println(libs);
        writer.close();

        // System.out.println( list );

        // System.out.println( result );

        return list;
    }
}
