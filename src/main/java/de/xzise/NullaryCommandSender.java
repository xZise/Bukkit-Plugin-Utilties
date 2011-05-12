package de.xzise;

import org.bukkit.Server;
import org.bukkit.command.CommandSender;

public final class NullaryCommandSender implements CommandSender {

    /**
     * Default command sender. The server is null!
     */
    public static final NullaryCommandSender EMPTY_SENDER = new NullaryCommandSender(null);
    
    private final Server server;
    
    public NullaryCommandSender(Server server) {
        this.server = server;
    }
    
    @Override
    public void sendMessage(String message) {}

    @Override
    public boolean isOp() {
        return false;
    }

    @Override
    public Server getServer() {
        return this.server;
    }

}
