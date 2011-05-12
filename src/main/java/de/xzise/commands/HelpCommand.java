package de.xzise.commands;

import org.bukkit.command.CommandSender;

public interface HelpCommand extends SubCommand {

    void setCommandMap(CommandMap map);
    
    void showCommandHelp(CommandSender sender, FullHelpable command);
    
}
