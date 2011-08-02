package de.xzise;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

public final class NullaryCommandSender implements CommandSender {

    /**
     * Default command sender. The server is null!
     */
    public static final NullaryCommandSender EMPTY_SENDER = new NullaryCommandSender(Bukkit.getServer());
    
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

    @Override
    public boolean isPermissionSet(String name) {
        return false;
    }

    @Override
    public boolean isPermissionSet(Permission perm) {
        return false;
    }

    @Override
    public boolean hasPermission(String name) {
        return false;
    }

    @Override
    public boolean hasPermission(Permission perm) {
        return false;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        return null;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        return null;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
        return null;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        return null;
    }

    @Override
    public void removeAttachment(PermissionAttachment attachment) {}

    @Override
    public void recalculatePermissions() {}

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return null;
    }

    @Override
    public void setOp(boolean value) {}

}
