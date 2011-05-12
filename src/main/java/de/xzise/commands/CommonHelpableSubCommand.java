package de.xzise.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import de.xzise.wrappers.help.HelpAPI;

public abstract class CommonHelpableSubCommand extends CommonSubCommand implements FullHelpable, CommandExecutor {

    protected CommonHelpableSubCommand(String... commands) {
        super(commands);
    }

    @Override
    public boolean listHelp(CommandSender sender) {
        return true;
    }

    @Override
    public final boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return this.execute(sender, args);
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
