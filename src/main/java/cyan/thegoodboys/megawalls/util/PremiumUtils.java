/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.JsonObject
 */
package cyan.thegoodboys.megawalls.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;

public class PremiumUtils {
    private static final Gson GSON = new Gson();
    private static final String BASE_URL = "http://localhost:8080/plugin?key=";

    public static boolean validate(String key) {
        try {
            int cp;
            BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(BASE_URL + key).openStream(), Charset.forName("UTF-8")));
            StringBuilder sb = new StringBuilder();
            while ((cp = reader.read()) != -1) {
                sb.append((char) cp);
            }
            reader.close();
            JsonObject jsonObject = (JsonObject) GSON.fromJson(sb.toString(), JsonObject.class);
            return jsonObject.get("success").getAsBoolean();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        System.out.println("success: " + PremiumUtils.validate("a"));
    }
}

