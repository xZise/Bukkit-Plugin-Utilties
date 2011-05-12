package de.xzise.commands;

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
}
