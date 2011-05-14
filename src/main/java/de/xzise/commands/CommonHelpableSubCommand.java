package de.xzise.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import de.xzise.wrappers.help.HelpAPI;

public abstract class CommonHelpableSubCommand extends CommonSubCommand implements FullHelpable {

    protected CommonHelpableSubCommand(String... commands) {
        super(commands);
    }

    @Override
    public boolean listHelp(CommandSender sender) {
        return true;
    }
    
    @Override
    public boolean showInMain() {
        return true;
    }
    
    @Override
    public String[] permissionsNeeded() {
        return new String[0];
    }
    
    @Override
    public boolean register(HelpAPI api, Plugin plugin) {
        return api.registerCommand(this.getCommand(), this.getSmallHelpText(), this.getFullHelpText(), plugin, this.showInMain(), this.permissionsNeeded());
    }
}
