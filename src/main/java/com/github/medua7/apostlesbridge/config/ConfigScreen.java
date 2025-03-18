package com.github.medua7.apostlesbridge.config;

import com.github.medua7.apostlesbridge.ApostlesBridge;
import com.github.medua7.apostlesbridge.handler.MessageHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class ConfigScreen extends GuiScreen {
    private final ApostlesBridge apostlesBridge;

    private GuiTextField urlField;
    private GuiTextField tokenField;
    private GuiTextField guildField;
    private GuiButton generalToggleButton;

    private int totalHeight = 0;
    private final int headerHeight = 10;
    private final int headerMarginBottom = 10;

    private int fieldTotalHeight = 0;
    private final int fieldSpacingHeight = 10;
    private final int fieldTextHeight = 15;
    private final int fieldTextboxHeight = 20;

    private final int generalToggleButtonHeight = 20;

    public ConfigScreen(ApostlesBridge apostlesBridge) {
        this.apostlesBridge = apostlesBridge;

        fieldTotalHeight = 0;
        fieldTotalHeight += fieldSpacingHeight; // spacing
        fieldTotalHeight += fieldTextHeight; // text
        fieldTotalHeight += fieldTextboxHeight; // field

        totalHeight += headerHeight; // header
        totalHeight += headerMarginBottom;
        int totalFields = 3;
        for (int i = 0; i < totalFields; i++) {
            totalHeight += fieldTotalHeight;
        }
        totalHeight += fieldSpacingHeight; // spacing
        totalHeight += generalToggleButtonHeight; // button
    }

    @Override
    public void initGui() {
        super.initGui();

        Keyboard.enableRepeatEvents(true);

        int centerX = width / 2;
        int centerY = height / 2;

        int fieldPosition = centerY - totalHeight / 2 + headerMarginBottom + fieldSpacingHeight + fieldTextHeight;
        urlField = new GuiTextField(0, fontRendererObj, centerX - 100, fieldPosition, 200, fieldTextboxHeight);
        fieldPosition += fieldTotalHeight;
        tokenField = new GuiTextField(1, fontRendererObj, centerX - 100, fieldPosition, 200, fieldTextboxHeight);
        fieldPosition += fieldTotalHeight;
        guildField = new GuiTextField(2, fontRendererObj, centerX - 100, fieldPosition, 200, fieldTextboxHeight);
        fieldPosition += fieldTextboxHeight;

        urlField.setMaxStringLength(256);
        tokenField.setMaxStringLength(128);
        guildField.setMaxStringLength(64);

        generalToggleButton = new GuiButton(3, centerX - 100, fieldPosition + fieldSpacingHeight, 200, generalToggleButtonHeight, getModeButtonText());
        buttonList.add(generalToggleButton);

        urlField.setText(Config.getURL());
        tokenField.setText(Config.getToken());
        guildField.setText(Config.getGuild());
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        drawCenteredString(fontRendererObj, "Apostles Settings", width / 2, height / 2 - totalHeight / 2, 0xFFFFFF);

        int textPosition = height / 2 - totalHeight / 2 + headerMarginBottom + fieldSpacingHeight;
        drawString(fontRendererObj, "WebSocket-URL:", width / 2 - 110, textPosition, 0xA0A0A0);
        textPosition += fieldTotalHeight;
        drawString(fontRendererObj, "WebSocket-Token:", width / 2 - 110, textPosition, 0xA0A0A0);
        textPosition += fieldTotalHeight;
        drawString(fontRendererObj, "Your Guild:", width / 2 - 110, textPosition, 0xA0A0A0);

        urlField.drawTextBox();
        guildField.drawTextBox();
        tokenField.drawTextBox();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 3) {
            Config.nextGeneralMode();
            generalToggleButton.displayString = getModeButtonText();
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        urlField.textboxKeyTyped(typedChar, keyCode);
        guildField.textboxKeyTyped(typedChar, keyCode);
        tokenField.textboxKeyTyped(typedChar, keyCode);

        if (keyCode == Keyboard.KEY_ESCAPE) {
            saveAndExit();
        }
    }

    private String getModeButtonText() {
        return "Mode: " + Config.getGeneralModeText();
    }

    private void saveAndExit() {
        Config.setURL(urlField.getText());
        Config.setGuild(guildField.getText());
        Config.setToken(tokenField.getText());
        Config.saveConfig();

        mc.displayGuiScreen(null);
        MessageHandler.sendMessage("Config has been saved");

        this.apostlesBridge.getWebSocketHandler().restartWebSocket(true);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        try {
            super.mouseClicked(mouseX, mouseY, mouseButton);

            urlField.mouseClicked(mouseX, mouseY, mouseButton);
            guildField.mouseClicked(mouseX, mouseY, mouseButton);
            tokenField.mouseClicked(mouseX, mouseY, mouseButton);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateScreen() {
        urlField.updateCursorCounter();
        guildField.updateCursorCounter();
        tokenField.updateCursorCounter();
    }
}
