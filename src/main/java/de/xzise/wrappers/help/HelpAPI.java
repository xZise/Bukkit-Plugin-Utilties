package de.xzise.wrappers.help;

import org.bukkit.plugin.Plugin;

public interface HelpAPI {

    boolean registerCommand(String command, String description, String[] fullDescription, Plugin plugin, boolean main, String... permissions);
    
}
