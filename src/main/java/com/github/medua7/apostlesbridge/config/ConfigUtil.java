package com.github.medua7.apostlesbridge.config;

public class ConfigUtil {

    public static String getOriginReplacement(String origin) {
        switch (origin.toLowerCase()) {
            case "dc":
                return Config.getFormattingNames().getDiscord();
            case "g1":
                return Config.getFormattingNames().getG1();
            case "g2":
                return Config.getFormattingNames().getG2();
            case "g3":
                return Config.getFormattingNames().getG3();
            default:
                return Config.getFormattingNames().getBridge();
        }
    }

    public static String convertToColor(String rawColor) {
        return rawColor.replace("§", "&");
    }
    public static String convertToRawColor(String color) {
        return color.replace("&", "§");
    }
}
