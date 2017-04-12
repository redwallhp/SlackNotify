package io.github.redwallhp.slacknotify;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;


public class CommandHandler implements CommandExecutor {


    private SlackNotify plugin;


    public CommandHandler() {
        plugin = SlackNotify.getInstance();
        plugin.getCommand("slacknotify").setExecutor(this);
    }


    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("slacknotify")) {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.RED + "Valid subcommands: reload, send");
            }
            else if (args[0].equalsIgnoreCase("reload")) {
                reloadCommand(sender);
            }
            else if (args[0].equalsIgnoreCase("send")) {
                sendCommand(sender, args);
            }
            return true;
        }
        return false;
    }


    /**
     * Reload configuration
     */
    private void reloadCommand(CommandSender sender) {
        plugin.reloadConfig();
        plugin.reloadListeners();
        sender.sendMessage(ChatColor.LIGHT_PURPLE + "Configuration reloaded.");
    }


    /**
     * Send a basic plain text message to Slack
     */
    private void sendCommand(CommandSender sender, String[] args) {
        if (args.length == 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /slacknotify send <message>");
            sender.sendMessage(ChatColor.GRAY + "Sends a basic plain text message to Slack");
            return;
        }
        String text = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        SlackNotify.send(text);
        sender.sendMessage(ChatColor.LIGHT_PURPLE + "Message sent!");
    }


}
