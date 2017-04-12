package io.github.redwallhp.slacknotify;

import io.github.redwallhp.slacknotify.listeners.KickListener;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * Manages the creation, maintenance and destruction of the plugin's listeners
 */
public class ListenerManager {


    private SlackNotify plugin;
    private Map<String, Listener> listeners;


    public ListenerManager() {
        plugin = SlackNotify.getInstance();
        listeners = new HashMap<>();
        listeners.put("kick", new KickListener());
    }


    public void load() {
        Set<String> enabled = new HashSet<>();
        for (Map.Entry<String, Listener> entry : listeners.entrySet()) {
            if (isEnabled(entry.getKey())) {
                plugin.getServer().getPluginManager().registerEvents(entry.getValue(), plugin);
                enabled.add(entry.getKey());
            }
        }
        if (!enabled.isEmpty()) {
            plugin.getLogger().info("Enabled notification listeners: " + String.join(", ", enabled));
        }
    }


    public void unload() {
        for (Map.Entry<String, Listener> entry : listeners.entrySet()) {
            HandlerList.unregisterAll(entry.getValue());
        }
    }


    private boolean isEnabled(String name) {
        return plugin.getConfig().getBoolean(String.format("listeners.%s.enabled", name), false);
    }


}
