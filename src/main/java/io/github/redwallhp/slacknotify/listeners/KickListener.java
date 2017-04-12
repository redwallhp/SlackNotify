package io.github.redwallhp.slacknotify.listeners;

import io.github.redwallhp.slacknotify.SlackMessage;
import io.github.redwallhp.slacknotify.SlackNotify;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;


public class KickListener implements Listener {


    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        String server = Bukkit.getServerName();
        String rich = String.format("Player kicked from %s", server);
        String plain = String.format("Player %s kicked from %s. Reason: %s", event.getPlayer().getName(), server, event.getReason());
        SlackNotify.send(new SlackMessage()
                .setText(rich, plain)
                .addField("Player", event.getPlayer().getName(), true)
                .addField("Server", server, true)
                .addField("Reason", event.getReason(), false)
                .setColor("#FF8C00")
        );
    }


}
