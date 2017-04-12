package io.github.redwallhp.slacknotify.listeners;

import org.bukkit.event.Listener;


public interface ModularListener extends Listener {

    public void load();

    public void unload();

}
