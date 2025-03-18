package com.github.medua7.apostlesbridge.config;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.*;

public class Config {
    public static final String CONFIG_FILE_URL = "config/apostles.json";
    private static final File CONFIG_FILE = new File(CONFIG_FILE_URL);
    private static final Gson GSON = new Gson();

    private static final String[] GENERAL_MODES = {"OFF", "EVERYWHERE", "HYPIXEL ONLY"};

    private static String url = "";
    private static String token = "";
    private static String guild = "";

    private static int generalMode = 2;

    public static void loadConfig() {
        if (!CONFIG_FILE.exists()) {
            saveConfig();
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(CONFIG_FILE))) {
            JsonObject json = GSON.fromJson(reader, JsonObject.class);

            url = json.has("url") ? json.get("url").getAsString() : url;
            guild = json.has("guild") ? json.get("guild").getAsString() : guild;
            token = json.has("token") ? json.get("token").getAsString() : token;
            generalMode = json.has("generalMode") ? json.get("generalMode").getAsInt() : generalMode;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveConfig() {
        JsonObject json = new JsonObject();
        json.addProperty("url", url);
        json.addProperty("guild", guild);
        json.addProperty("token", token);
        json.addProperty("generalMode", generalMode);

        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(json, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getURL() {
        return url;
    }

    public static String getGuild() {
        return guild;
    }

    public static String getToken() {
        return token;
    }

    public static int getGeneralMode() {
        return generalMode;
    }

    public static String getGeneralModeText() {
        return GENERAL_MODES[generalMode];
    }

    public static void setURL(String newUrl) {
        url = newUrl;
    }

    public static void setGuild(String newGuild) {
        guild = newGuild;
    }

    public static void setToken(String newToken) {
        token = newToken;
    }

    public static void setGeneralMode(int newGeneralMode) {
        generalMode = newGeneralMode;
    }

    public static void nextGeneralMode() {
        generalMode = (generalMode + 1) % GENERAL_MODES.length;
    }
}
