/*
 * This file is part of Bukkit Plugin Utilities.
 * 
 * Bukkit Plugin Utilities is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 * 
 * Foobar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.xzise.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import de.xzise.MinecraftUtil;
import de.xzise.commands.CommandMap;
import de.xzise.commands.CommonHelpableSubCommand;
import de.xzise.commands.FullHelpable;
import de.xzise.commands.HelpCommand;
import de.xzise.commands.SmallHelpable;
import de.xzise.commands.SubCommand;

public class CommonHelpCommand extends CommonHelpableSubCommand implements HelpCommand {

    private CommandMap map;
    private final String name;

    public CommonHelpCommand(String name) {
        super("help", "?");
        this.name = name;
    }

    private String[] getFullHelp(FullHelpable helpable) {
        List<String> lines = new ArrayList<String>();
        lines.add(this.name + " help: " + ChatColor.GREEN + helpable.getCommand());
        for (String string : helpable.getFullHelpText()) {
            lines.add(string);
        }
        String[] aliases = helpable.getCommands();
        if (aliases.length > 1) {
            String aliasString = "Aliases: ";
            for (int i = 1; i < aliases.length; i++) {
                aliasString += ChatColor.GREEN + aliases[i];
                if (i < aliases.length - 1) {
                    aliasString += ChatColor.WHITE + ", ";
                }
            }
            lines.add(aliasString);
        }
        return lines.toArray(new String[0]);
    }

    private final String getSmallHelp(SmallHelpable helpable) {
        return ChatColor.GREEN + helpable.getCommand() + ChatColor.WHITE + " - " + helpable.getSmallHelpText();
    }

    @Override
    public boolean execute(CommandSender sender, String[] parameters) {
        if (parameters.length > 2) {
            return false;
        }

        List<SubCommand> commands = this.map.getCommandList();

        // First get all commands:
        List<String> lines = new ArrayList<String>(commands.size());
        for (SubCommand command : commands) {
            if (command instanceof SmallHelpable && ((SmallHelpable) command).listHelp(sender)) {
                lines.add(this.getSmallHelp((SmallHelpable) command));
            }
        }

        Integer page = null;
        int linesPerPage = MinecraftUtil.getMaximumLines(sender);
        int maxPage = lines.size() / (linesPerPage - 1);
        if (parameters.length == 2) {
            if ((page = MinecraftUtil.tryAndGetInteger(parameters[1])) != null) {
                if (page < 1) {
                    sender.sendMessage(ChatColor.RED + "Page number can't be below 1.");
                    return true;
                } else if (page > maxPage) {
                    sender.sendMessage(ChatColor.RED + "There are only 2 pages of help");
                    return true;
                }
            } else {
                SubCommand command = this.map.getCommand(parameters[1]);
                if (command instanceof FullHelpable) {
                    this.showCommandHelp(sender, (FullHelpable) command);
                } else {
                    sender.sendMessage(ChatColor.RED + "Please input a valid number/command");
                }
                return true;
            }
        }
        if (page == null) {
            page = 1;
        }
        sender.sendMessage(ChatColor.WHITE + "------------------ " + ChatColor.GREEN + this.name + " Help " + page + "/" + maxPage + ChatColor.WHITE + " ------------------");
        for (int i = (page - 1) * (linesPerPage - 1); i < lines.size() && i < page * (linesPerPage - 1); i++) {
            sender.sendMessage(lines.get(i));
        }
        return true;
    }

    @Override
    public void showCommandHelp(CommandSender sender, FullHelpable command) {
        for (String line : this.getFullHelp(command)) {
            sender.sendMessage(line);
        }
    }

    @Override
    public String[] getFullHelpText() {
        return new String[] { "Shows the selected help page." };
    }

    @Override
    public String getSmallHelpText() {
        return "Shows the help";
    }

    @Override
    public String getCommand() {
        return this.name + " help [#page]";
    }

    @Override
    public void setCommandMap(CommandMap map) {
        this.map = map;
    }

}
