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
        if (this.commands.length > 1) {
            String aliases = "Aliases: ";
            for (int i = 1; i < helpable.getCommands().length; i++) {
                aliases += ChatColor.GREEN + this.commands[i];
                if (i < this.commands.length - 1) {
                    aliases += ChatColor.WHITE + ", ";
                }
            }
            lines.add(aliases);
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
