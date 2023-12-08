package com.drachenclon.dreg.Tests.Version;

import com.drachenclon.dreg.UltimateLogin;
import com.drachenclon.dreg.VersionHandler.Version;
import com.drachenclon.dreg.VersionHandler.VersionManager;
import junit.framework.Assert;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

class VersionCheckResultTest {
    @Test
    public void CheckVersionTest() {
        System.out.println(VersionManager.getCurrentVersionFromNet());
        System.out.println("2.0.1");
        Assertions.assertTrue(true);
    }

    @Test
    public void GetVersionStringFromNetAndParseJson() throws Exception {
        String json = Jsoup.connect("https://api.github.com/repos/DrachenClon22/ultimate-login-2/releases/latest")
                .ignoreContentType(true).execute().body();
        JSONParser parser = new JSONParser();

        try {

            JSONObject jsonObject = (JSONObject) parser.parse(json);

            String name = (String) jsonObject.get("tag_name");
            System.out.println(name);

        } catch (Exception e) {
            e.printStackTrace();
        }

        Assertions.assertTrue(true);
    }
}