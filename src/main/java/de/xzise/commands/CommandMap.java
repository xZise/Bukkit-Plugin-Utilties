package de.xzise.commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public interface CommandMap {

    List<SubCommand> getCommandList();
    
    SubCommand getCommand(String name);
    
    boolean executeCommand(CommandSender sender, String[] parameters);
    
    void setHelperPlugin(Plugin helper, Plugin owner);
    
}
