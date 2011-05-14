package de.xzise.commands;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public interface SubCommand extends CommandExecutor {

    String[] getCommands();

    boolean execute(CommandSender sender, String[] parameters);
    
}
