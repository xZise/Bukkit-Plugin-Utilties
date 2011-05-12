package de.xzise.commands;

import org.bukkit.command.CommandSender;

public interface SubCommand {

    String[] getCommands();

    boolean execute(CommandSender sender, String[] parameters);
    
}
