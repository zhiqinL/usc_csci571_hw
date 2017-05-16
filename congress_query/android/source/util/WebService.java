package csci571.zhiqinliao.hw9.util;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Internet service class which actually request informration from SunLight API
 */
public class WebService {
    final static private String getAllLegInfo = "http://104.198.0.197:8080/legislators?per_page=all&order=state__asc";
    final static private String getAllHouseLegInfo = "http://104.198.0.197:8080/legislators?per_page=all&order=last_name__asc&chamber=house";
    final static private String getAllSenateLegInfo = "http://104.198.0.197:8080/legislators?per_page=all&order=last_name__asc&chamber=senate";
    final static private String getActiveBillInfo = "http://104.198.0.197:8080/bills?history.active=true&per_page=50&order=introduced_on__desc";
    final static private String getNewBillInfo = "http://104.198.0.197:8080/bills?history.active=false&per_page=50&order=introduced_on__desc";
    final static private String getHouseComInfo = "http://104.198.0.197:8080/committees?per_page=all&chamber=house&order=name__asc";
    final static private String getSenateComInfo = "http://104.198.0.197:8080/committees?per_page=all&chamber=senate&order=name__asc";
    final static private String getJointComInfo = "http://104.198.0.197:8080/committees?per_page=all&chamber=joint&order=name__asc";

    public static JsonArray getLegInfo(String type) {
        String legInfoURL = "";

        if(type == "state")
            legInfoURL = getAllLegInfo;
        else if(type == "house")
            legInfoURL = getAllHouseLegInfo;
        else if(type == "senate")
            legInfoURL = getAllSenateLegInfo;

        try {

            URL url= new URL(legInfoURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(response.toString()).getAsJsonObject();
            JsonArray legInfo = jsonObject.get("results").getAsJsonArray();
            return legInfo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JsonArray getBillInfo(String type) {
        String billInfoURL = "";

        if(type == "active")
            billInfoURL = getActiveBillInfo;
        else if(type == "new")
            billInfoURL = getNewBillInfo;

        try {

            URL url = new URL(billInfoURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(response.toString()).getAsJsonObject();
            JsonArray billInfo = jsonObject.get("results").getAsJsonArray();
            return billInfo;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JsonArray getComInfo(String type) {
        String comInfoURL = "";

        if(type == "house")
            comInfoURL = getHouseComInfo;
        else if(type == "senate")
            comInfoURL = getSenateComInfo;
        else if(type == "joint")
            comInfoURL = getJointComInfo;

        try {

            URL url = new URL(comInfoURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(response.toString()).getAsJsonObject();
            JsonArray comInfo = jsonObject.get("results").getAsJsonArray();
            return comInfo;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
