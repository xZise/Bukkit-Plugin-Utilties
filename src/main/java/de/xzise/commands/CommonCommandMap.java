/*
 * This file is part of Bukkit Plugin Utilities.
 * 
 * Bukkit Plugin Utilities is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 * 
 * Bukkit Plugin Utilities is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Bukkit Plugin Utilities.
 * If not, see <http://www.gnu.org/licenses/>.
 */

package de.xzise.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import de.xzise.MinecraftUtil;

public class CommonCommandMap implements CommandMap, CommandExecutor {

    private Map<String, SubCommand> subCommandsMap;
    private List<SubCommand> subCommandsList;
    private HelpCommand helper;
    private SubCommand defaultCommand;

    public CommonCommandMap() {
        this.subCommandsMap = new HashMap<String, SubCommand>();
        this.subCommandsList = new ArrayList<SubCommand>();
    }

    public CommonCommandMap(List<SubCommand> subCommands) {
        this();
    }

    public void clear() {
        this.subCommandsMap.clear();
        this.helper = null;
    }

    public void populate(Collection<SubCommand> subCommands) {
        this.subCommandsList.addAll(subCommands);
        for (SubCommand subCommand : subCommands) {
            for (String text : subCommand.getCommands()) {
                if (this.subCommandsMap.put(text, subCommand) != null) {
                    throw new IllegalArgumentException("Command was already registered!");
                }
            }
        }
    }

    public void setHelper(HelpCommand helpCommand) {
        this.helper = helpCommand;
        this.helper.setCommandMap(this);
    }

    public void setDefault(SubCommand defaultCommand) {
        this.defaultCommand = defaultCommand;
    }

    private boolean executeDefault(CommandSender sender, String[] parameters) {
        if (this.defaultCommand != null && this.defaultCommand.execute(sender, parameters)) {
            return true;
        } else {
            return this.helper.execute(sender, parameters);
        }
    }

    public boolean executeCommand(CommandSender sender, String[] parameters) {
        if (parameters.length == 0) {
            return this.executeDefault(sender, parameters);
        } else {
            SubCommand command = this.subCommandsMap.get(parameters[0]);
            if (command != null) {
                if (command.execute(sender, parameters)) {
                    return true;
                } else if (command instanceof FullHelpable) {
                    this.helper.showCommandHelp(sender, (FullHelpable) command);
                    return true;
                } else {
                    return false;
                }
            } else {
                return this.executeDefault(sender, parameters);
            }
        }
    }

    @Override
    public List<SubCommand> getCommandList() {
        return this.subCommandsList;
    }

    @Override
    public SubCommand getCommand(String name) {
        if (MinecraftUtil.isSet(name)) {
            return this.subCommandsMap.get(name);
        } else {
            return this.defaultCommand;
        }
    }

    @Override
    public void setHelperPlugin(Plugin helperPlugin, Plugin owner) {
//        HelpAPI api = null;
//        if (helperPlugin instanceof Help) {
//            api = new TKellyHelpPlugin((Help) helperPlugin);
//        }
//
//        if (api != null) {
//            for (SubCommand command : this.subCommandsList) {
//                if (command instanceof SmallHelpable) {
//                    ((SmallHelpable) command).register(api, owner);
//                }
//            }
//        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        StringBuilder line = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            line.append(arg);
            if (i < args.length - 1) {
                line.append(' ');
            }
        }

        return this.executeCommand(sender, MinecraftUtil.parseLine(line.toString()));
    }

}
