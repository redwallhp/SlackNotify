package io.github.redwallhp.slacknotify.listeners;

import io.github.redwallhp.slacknotify.SlackMessage;
import io.github.redwallhp.slacknotify.SlackNotify;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 * Send a notification when a player uses a configured regular expression in chat or a command
 */
public class RegexListener implements ModularListener {


    private Set<Pattern> patterns;


    public void load() {
        List<String> strings = SlackNotify.getInstance().getConfig().getStringList("listeners.regex.patterns");
        patterns = new HashSet<>();
        patterns.addAll(strings.stream().map(Pattern::compile).collect(Collectors.toList()));
    }


    public void unload() {}


    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        check(event.getMessage(), event.getPlayer(), false);
    }


    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        check(event.getMessage(), event.getPlayer(), true);
    }


    private void check(String message, Player player, boolean isCommand) {
        Matcher matcher;
        for (Pattern pattern : patterns) {
            matcher = pattern.matcher(message);
            if (matcher.find()) {
                notify(message, player, isCommand);
                break;
            }
        }
    }


    private void notify(String message, Player player, boolean isCommand) {
        SlackMessage slackMessage = new SlackMessage();
        if (isCommand) {
            String plain = String.format("[Command Regex] Player %s ran command: %s", player.getName(), message);
            slackMessage.setText(message, plain)
                    .setAuthor(player.getName())
                    .setFooter("Command regex match");
        } else {
            String plain = String.format("[Chat Regex] <%s> %s", player.getName(), message);
            slackMessage.setText(message, plain)
                    .setAuthor(player.getName())
                    .setFooter("Chat regex match");
        }
        SlackNotify.send(slackMessage);
    }


}
