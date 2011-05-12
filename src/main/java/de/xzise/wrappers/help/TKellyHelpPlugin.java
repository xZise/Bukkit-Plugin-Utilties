package de.xzise.wrappers.help;

import me.taylorkelly.help.Help;

import org.bukkit.plugin.Plugin;

public class TKellyHelpPlugin implements HelpAPI {
    
    private final Help plugin;
    
    public TKellyHelpPlugin(Help helpPlugin) {
        this.plugin = helpPlugin;
    }

    @Override
    public boolean registerCommand(String command, String description, String[] fullDesc, Plugin plugin, boolean main, String... permissions) {
        return this.plugin.registerCommand(command, description, plugin, main, permissions);
    }

}
