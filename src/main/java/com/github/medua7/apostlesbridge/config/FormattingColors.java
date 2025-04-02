package com.github.medua7.apostlesbridge.config;

public class FormattingColors {
    String originColor;
    String userColor;
    String messageColor;

    public FormattingColors () {
        this("§2","§b","§r");
    }
    public FormattingColors (String originColor, String userColor, String messageColor) {
        this.originColor = originColor;
        this.userColor = userColor;
        this.messageColor = messageColor;
    }

    public String getOriginColor() {
        return originColor;
    }

    public void setOriginColor(String originColor) {
        this.originColor = originColor;
    }

    public String getUserColor() {
        return userColor;
    }

    public void setUserColor(String userColor) {
        this.userColor = userColor;
    }

    public String getMessageColor() {
        return messageColor;
    }

    public void setMessageColor(String messageColor) {
        this.messageColor = messageColor;
    }
}
