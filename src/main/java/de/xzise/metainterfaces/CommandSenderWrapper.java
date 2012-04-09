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

package de.xzise.metainterfaces;

import java.util.Set;

import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

public class CommandSenderWrapper<T extends CommandSender> implements CommandSender {

    protected final T sender;

    protected CommandSenderWrapper(T sender) {
        this.sender = sender;
    }

    public T getSender() {
        return this.sender;
    }

    @Override
    public void sendMessage(String message) {
        this.sender.sendMessage(message);
    }

    @Override
    public boolean isOp() {
        return this.sender.isOp();
    }

    @Override
    public Server getServer() {
        return this.sender.getServer();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof CommandSender) {
            return o.equals(this.sender);
        } else if (o instanceof CommandSenderWrapper<?>) {
            return ((CommandSenderWrapper<?>) o).sender.equals(this.sender);
        } else {
            return false;
        }
    }

    public static CommandSender getCommandSender(CommandSender sender) {
        if (sender instanceof CommandSenderWrapper<?>) {
            return CommandSenderWrapper.getCommandSender(((CommandSenderWrapper<?>) sender).getSender());
        } else {
            return sender;
        }
    }

    @Override
    public boolean isPermissionSet(String name) {
        return this.sender.isPermissionSet(name);
    }

    @Override
    public boolean isPermissionSet(Permission perm) {
        return this.sender.isPermissionSet(perm);
    }

    @Override
    public boolean hasPermission(String name) {
        return this.sender.hasPermission(name);
    }

    @Override
    public boolean hasPermission(Permission perm) {
        return this.sender.hasPermission(perm);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        return this.sender.addAttachment(plugin, name, value);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        return this.sender.addAttachment(plugin);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
        return this.sender.addAttachment(plugin, name, value, ticks);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        return this.sender.addAttachment(plugin, ticks);
    }

    @Override
    public void removeAttachment(PermissionAttachment attachment) {
        this.sender.removeAttachment(attachment);
    }

    @Override
    public void recalculatePermissions() {
        this.sender.recalculatePermissions();
    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return this.sender.getEffectivePermissions();
    }

    @Override
    public void setOp(boolean value) {
        this.sender.setOp(value);
    }

    @Override
    public String getName() {
        return this.sender.getName();
    }

    @Override
    public void sendMessage(String[] lines) {
        this.sender.sendMessage(lines);
    }
}
