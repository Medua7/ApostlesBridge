package com.github.medua7.apostlesbridge.config;

import com.github.medua7.apostlesbridge.ApostlesBridge;
import com.github.medua7.apostlesbridge.handler.MessageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class ConfigFormattingScreen extends GuiScreen {
    private final ApostlesBridge apostlesBridge;

    // COLOR FIELDS
    private GuiTextField originColorField;
    private GuiTextField userColorField;
    private GuiTextField messageColorField;
    // NAMES FIELDS
    private GuiTextField bridgeField;
    private GuiTextField discordField;
    private GuiTextField g1Field;
    private GuiTextField g2Field;
    private GuiTextField g3Field;
    // BUTTONS
    private GuiButton backButton;

    private int totalHeight = 0;
    private final int headerHeight = 10;
    private final int headerMarginBottom = 10;

    private int fieldTotalHeight;
    private final int fieldSpacingHeight = 10;
    private final int fieldTextHeight = 15;
    private final int fieldTextboxHeight = 20;

    private final int backButtonHeight = 20;

    public ConfigFormattingScreen(ApostlesBridge apostlesBridge) {
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
    }

    @Override
    public void initGui() {
        super.initGui();

        Keyboard.enableRepeatEvents(true);

        int centerX = width / 2;
        int centerY = height / 2;

        int fieldPosition = centerY - totalHeight / 2 + headerMarginBottom + fieldSpacingHeight + fieldTextHeight;
        originColorField = new GuiTextField(0, fontRendererObj, centerX - 100 + 40 - 5, fieldPosition, 20, fieldTextboxHeight);
        userColorField = new GuiTextField(1, fontRendererObj, centerX - 30 + 40 - 10 - 2, fieldPosition, 20, fieldTextboxHeight);
        messageColorField = new GuiTextField(2, fontRendererObj, centerX + 40 + 40, fieldPosition, 20, fieldTextboxHeight);
        fieldPosition += fieldTotalHeight + fieldSpacingHeight;
        bridgeField = new GuiTextField(0, fontRendererObj, centerX - 100, fieldPosition, 95, fieldTextboxHeight);
        discordField = new GuiTextField(1, fontRendererObj, centerX + 5, fieldPosition, 95, fieldTextboxHeight);
        fieldPosition += fieldSpacingHeight + fieldTextboxHeight + fieldSpacingHeight - 5;
        g1Field = new GuiTextField(2, fontRendererObj, centerX - 100, fieldPosition, 60, fieldTextboxHeight);
        g2Field = new GuiTextField(2, fontRendererObj, centerX - 30, fieldPosition, 60, fieldTextboxHeight);
        g3Field = new GuiTextField(2, fontRendererObj, centerX + 40, fieldPosition, 60, fieldTextboxHeight);

        originColorField.setMaxStringLength(2);
        userColorField.setMaxStringLength(2);
        messageColorField.setMaxStringLength(2);

        bridgeField.setMaxStringLength(64);
        discordField.setMaxStringLength(64);
        g1Field.setMaxStringLength(64);
        g2Field.setMaxStringLength(64);
        g3Field.setMaxStringLength(64);

        backButton = new GuiButton(3, 10, 10, 40, backButtonHeight, "Back");
        buttonList.add(backButton);

        originColorField.setText(ConfigUtil.convertToColor(Config.getFormattingColors().getOriginColor()));
        userColorField.setText(ConfigUtil.convertToColor(Config.getFormattingColors().getUserColor()));
        messageColorField.setText(ConfigUtil.convertToColor(Config.getFormattingColors().getMessageColor()));

        bridgeField.setText(Config.getFormattingNames().getBridge());
        discordField.setText(Config.getFormattingNames().getDiscord());
        g1Field.setText(Config.getFormattingNames().getG1());
        g2Field.setText(Config.getFormattingNames().getG2());
        g3Field.setText(Config.getFormattingNames().getG3());
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        drawCenteredString(fontRendererObj, "Apostles Settings", width / 2, height / 2 - totalHeight / 2, 0xFFFFFF);

        GlStateManager.pushMatrix();

        float tinyHeaderScale = 0.5f;
        float tinyHeaderScaleMultiplier = 1f / 0.5f;
        GlStateManager.scale(tinyHeaderScale, tinyHeaderScale, tinyHeaderScale);
        drawCenteredString(fontRendererObj, "> Formatting", (int) ((float) width / 2 * tinyHeaderScaleMultiplier), (int) (((float) height / 2 - (float) totalHeight / 2 + 10) * tinyHeaderScaleMultiplier), 0xA0A0A0);

        GlStateManager.popMatrix();

        int textPosition = height / 2 - totalHeight / 2 + headerMarginBottom + fieldSpacingHeight;
        drawString(fontRendererObj, "Colors:", width / 2 - 110, textPosition, 0xA0A0A0);
        textPosition += fieldSpacingHeight + fieldTextHeight - 5 + 2;
        drawString(fontRendererObj, "Origin:", width / 2 - 100, textPosition, 0xA0A0A0);
        drawString(fontRendererObj, "User:", width / 2 - 30 - 5 + 2, textPosition, 0xA0A0A0);
        drawString(fontRendererObj, "Message:", width / 2 + 40 - 10 + 1, textPosition, 0xA0A0A0);
        textPosition += fieldSpacingHeight + fieldTextHeight - 2;
        drawString(fontRendererObj, "Names:", width / 2 - 110, textPosition, 0xA0A0A0);
        textPosition += fieldTextHeight;
        drawString(fontRendererObj, "Bridge", width / 2 - 100, textPosition, 0xA0A0A0);
        drawString(fontRendererObj, "Discord", width / 2 + 5, textPosition, 0xA0A0A0);
        textPosition += fieldTotalHeight - fieldSpacingHeight;
        drawString(fontRendererObj, "Guild 1", width / 2 - 100, textPosition, 0xA0A0A0);
        drawString(fontRendererObj, "Guild 2", width / 2 - 30, textPosition, 0xA0A0A0);
        drawString(fontRendererObj, "Guild 3", width / 2 + 40, textPosition, 0xA0A0A0);

        originColorField.drawTextBox();
        userColorField.drawTextBox();
        messageColorField.drawTextBox();

        bridgeField.drawTextBox();
        discordField.drawTextBox();
        g1Field.drawTextBox();
        g2Field.drawTextBox();
        g3Field.drawTextBox();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 3) {
            saveSettings();
            Minecraft.getMinecraft().displayGuiScreen(new ConfigScreen(this.apostlesBridge));
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        originColorField.textboxKeyTyped(typedChar, keyCode);
        userColorField.textboxKeyTyped(typedChar, keyCode);
        messageColorField.textboxKeyTyped(typedChar, keyCode);

        bridgeField.textboxKeyTyped(typedChar, keyCode);
        discordField.textboxKeyTyped(typedChar, keyCode);
        g1Field.textboxKeyTyped(typedChar, keyCode);
        g2Field.textboxKeyTyped(typedChar, keyCode);
        g3Field.textboxKeyTyped(typedChar, keyCode);

        if (keyCode == Keyboard.KEY_ESCAPE) {
            saveAndExit();
        }
    }

    private void saveSettings() {
        Config.getFormattingColors().setOriginColor(ConfigUtil.convertToRawColor(originColorField.getText()));
        Config.getFormattingColors().setUserColor(ConfigUtil.convertToRawColor(userColorField.getText()));
        Config.getFormattingColors().setMessageColor(ConfigUtil.convertToRawColor(messageColorField.getText()));

        Config.getFormattingNames().setBridge(bridgeField.getText());
        Config.getFormattingNames().setDiscord(discordField.getText());
        Config.getFormattingNames().setG1(g1Field.getText());
        Config.getFormattingNames().setG2(g2Field.getText());
        Config.getFormattingNames().setG3(g3Field.getText());
        Config.saveConfig();

        MessageHandler.sendMessage("Config has been saved");
    }

    private void saveAndExit() {
        saveSettings();

        mc.displayGuiScreen(null);

        this.apostlesBridge.getWebSocketHandler().restartWebSocket(true);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        try {
            super.mouseClicked(mouseX, mouseY, mouseButton);

            originColorField.mouseClicked(mouseX, mouseY, mouseButton);
            userColorField.mouseClicked(mouseX, mouseY, mouseButton);
            messageColorField.mouseClicked(mouseX, mouseY, mouseButton);

            bridgeField.mouseClicked(mouseX, mouseY, mouseButton);
            discordField.mouseClicked(mouseX, mouseY, mouseButton);
            g1Field.mouseClicked(mouseX, mouseY, mouseButton);
            g2Field.mouseClicked(mouseX, mouseY, mouseButton);
            g3Field.mouseClicked(mouseX, mouseY, mouseButton);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateScreen() {
        originColorField.updateCursorCounter();
        userColorField.updateCursorCounter();
        messageColorField.updateCursorCounter();

        bridgeField.updateCursorCounter();
        discordField.updateCursorCounter();
        g1Field.updateCursorCounter();
        g2Field.updateCursorCounter();
        g3Field.updateCursorCounter();
    }
}
