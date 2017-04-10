package io.github.redwallhp.slacknotify;

import org.bukkit.plugin.java.JavaPlugin;


public class SlackNotify extends JavaPlugin {


    private static SlackNotify instance;
    private MessageHandler messageHandler;


    public void onEnable() {
        instance = this;
        messageHandler = new MessageHandler();
        new CommandHandler();
    }


    public void onDisable() {
        messageHandler.cancel();
    }


    public static SlackNotify getInstance() {
        return instance;
    }


    /**
     * Send a simple plain text message to Slack
     * @param text plain text message
     */
    public static void send(String text) {
        SlackMessage msg = new SlackMessage().setText(text);
        getInstance().messageHandler.getQueue().offer(msg);
    }


    /**
     * Send a message with custom formatting
     * @param msg object representing a custom Slack attachment
     */
    public static void send(SlackMessage msg) {
        getInstance().messageHandler.getQueue().offer(msg);
    }


}
