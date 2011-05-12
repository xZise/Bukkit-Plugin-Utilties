package de.xzise.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import de.xzise.wrappers.help.HelpAPI;

public interface SmallHelpable extends SubCommand {

    String getSmallHelpText();

    String getCommand();

    /**
     * Returns if the sender could use this command and so if list the command
     * in the help.
     * 
     * @param sender
     *            The user of the command.
     * @return If the user could access this command.
     */
    boolean listHelp(CommandSender sender);
    
    // Help API methods
    boolean showInMain();
    
    String[] permissionsNeeded();
    
    boolean register(HelpAPI api, Plugin plugin);
}
