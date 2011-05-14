package de.xzise.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public abstract class CommonSubCommand implements SubCommand {

    protected final String[] commands;

    /**
     * Creates a subcommand.
     * 
     * @param commands
     *            The commands.
     * @throws IllegalArgumentException
     *             If commands is empty.
     */
    protected CommonSubCommand(String... commands) {
        this.commands = commands;
    }

    @Override
    public String[] getCommands() {
        return this.commands.clone();
    }

    @Override
    public final boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String[] params = new String[args.length + 1];
        params[0] = label;
        for (int i = 0; i < args.length; i++) {
            params[i + 1] = args[i];
        }
        return this.execute(sender, params);
    }
}
