package de.xzise.wrappers.help;

import org.bukkit.plugin.Plugin;

/**
 * This class has no use!
 */
@Deprecated
public class TKellyHelpPlugin implements HelpAPI {

    public TKellyHelpPlugin(Object helpPlugin) {}

    @Override
    public boolean registerCommand(String command, String description, String[] fullDesc, Plugin plugin, boolean main, String... permissions) {
        return false;
    }

}
