package io.github.redwallhp.slacknotify;

import io.github.redwallhp.slacknotify.listeners.KickListener;
import io.github.redwallhp.slacknotify.listeners.ModularListener;
import io.github.redwallhp.slacknotify.listeners.RegexListener;
import org.bukkit.event.HandlerList;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * Manages the creation, maintenance and destruction of the plugin's listeners
 */
public class ListenerManager {


    private SlackNotify plugin;
    private Map<String, ModularListener> listeners;


    public ListenerManager() {
        plugin = SlackNotify.getInstance();
        listeners = new HashMap<>();
        listeners.put("kick", new KickListener());
        listeners.put("regex", new RegexListener());
    }


    public void load() {
        Set<String> enabled = new HashSet<>();
        for (Map.Entry<String, ModularListener> entry : listeners.entrySet()) {
            if (isEnabled(entry.getKey())) {
                entry.getValue().load();
                plugin.getServer().getPluginManager().registerEvents(entry.getValue(), plugin);
                enabled.add(entry.getKey());
            }
        }
        if (!enabled.isEmpty()) {
            plugin.getLogger().info("Enabled notification listeners: " + String.join(", ", enabled));
        }
    }


    public void unload() {
        for (Map.Entry<String, ModularListener> entry : listeners.entrySet()) {
            HandlerList.unregisterAll(entry.getValue());
            entry.getValue().unload();
        }
    }


    private boolean isEnabled(String name) {
        return plugin.getConfig().getBoolean(String.format("listeners.%s.enabled", name), false);
    }


}
