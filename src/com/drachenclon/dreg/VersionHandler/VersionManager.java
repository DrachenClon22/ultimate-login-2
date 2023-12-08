package com.drachenclon.dreg.VersionHandler;

import com.drachenclon.dreg.UltimateLogin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;

import java.io.IOException;

public final class VersionManager {
    private static UltimateLogin _plugin;
    public static void init(UltimateLogin plugin) {
        _plugin = plugin;
    }

    public static boolean checkVersion() {
        Version pluginYaml = getCurrentVersionFromFile();
        Version net = getCurrentVersionFromNet();

        return pluginYaml.compareTo(net) >= 0;
    }
    public static Version getCurrentVersionFromFile() {
        PluginDescriptionFile pdf = _plugin.getDescription();
        return new Version(pdf.getVersion());
    }
    public static Version getCurrentVersionFromNet() {
        String result = "0.0.0";
        try {
            String json = Jsoup.connect("https://api.github.com/repos/DrachenClon22/ultimate-login-2/releases/latest")
                    .ignoreContentType(true).execute().body();
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(json);

            result = (String) jsonObject.get("tag_name");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Version(result);
    }
}
