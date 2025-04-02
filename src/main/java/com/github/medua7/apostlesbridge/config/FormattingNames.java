package com.github.medua7.apostlesbridge.config;

public class FormattingNames {
    String bridge;
    String discord;
    String g1;
    String g2;
    String g3;

    public FormattingNames () {
        this("Bridge", "Discord","Guild 1","Guild 2", "Guild 3");
    }
    public FormattingNames (String bridge, String discord, String g1, String g2, String g3) {
        this.bridge = bridge;
        this.discord = discord;
        this.g1 = g1;
        this.g2 = g2;
        this.g3 = g3;
    }

    public String getBridge() {
        return bridge;
    }

    public void setBridge(String bridge) {
        this.bridge = bridge;
    }

    public String getDiscord() {
        return discord;
    }

    public void setDiscord(String discord) {
        this.discord = discord;
    }

    public String getG1() {
        return g1;
    }

    public void setG1(String g1) {
        this.g1 = g1;
    }

    public String getG2() {
        return g2;
    }

    public void setG2(String g2) {
        this.g2 = g2;
    }

    public String getG3() {
        return g3;
    }

    public void setG3(String g3) {
        this.g3 = g3;
    }
}
