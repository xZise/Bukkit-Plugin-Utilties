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
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public License
* along with Bukkit Plugin Utilities.
* If not, see <http://www.gnu.org/licenses/>.
*/

package de.xzise.bukkit.util.wrappers.permissions;

import net.milkbowl.vault.chat.Chat;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import de.xzise.XLogger;
import de.xzise.bukkit.util.wrappers.WrapperFactory;
import de.xzise.wrappers.InvalidWrapperException;
import de.xzise.wrappers.permissions.Permission;
import de.xzise.wrappers.permissions.PermissionsWrapper;

public class VaultPermissionsWrapper implements PermissionsWrapper {

    private net.milkbowl.vault.permission.Permission permission;
    private Chat chat;
    private final Plugin plugin;

    public VaultPermissionsWrapper(final Plugin plugin) {
        this.permission = null;
        this.chat = null;
        this.plugin = plugin;
    }

    @Override
    public Plugin getPlugin() {
        return this.plugin;
    }

    @Override
    public Boolean has(CommandSender sender, Permission<Boolean> permission) {
        return this.permission != null ? this.permission.has(sender, permission.getName()) : null;
    }

    @Override
    public String[] getGroup(String world, String player) {
        return this.permission != null ? this.permission.getPlayerGroups(world, player) : null;
    }

    @Override
    public Integer getInteger(CommandSender sender, Permission<Integer> permission) {
        if (sender instanceof Player && this.chat != null) {
            return this.chat.getPlayerInfoInteger((Player) sender, permission.getName(), permission.getDefault());
        } else {
            return null;
        }
    }

    @Override
    public Double getDouble(CommandSender sender, Permission<Double> permission) {
        if (sender instanceof Player && this.chat != null) {
            return this.chat.getPlayerInfoDouble((Player) sender, permission.getName(), permission.getDefault());
        } else {
            return null;
        }
    }

    @Override
    public String getString(CommandSender sender, Permission<String> permission, boolean recursive) {
        if (sender instanceof Player && this.chat != null) {
            return this.chat.getPlayerInfoString((Player) sender, permission.getName(), permission.getDefault());
        } else {
            return null;
        }
    }

    @Override
    public String getString(String groupname, String world, Permission<String> permission) {
        if (this.chat != null) {
            return this.chat.getGroupInfoString(world, groupname, permission.getName(), permission.getDefault());
        } else {
            return null;
        }
    }

    public boolean setProvider(RegisteredServiceProvider<?> serviceProvider) {
        Object o = serviceProvider.getProvider();
        if (o instanceof net.milkbowl.vault.permission.Permission) {
            this.permission = (net.milkbowl.vault.permission.Permission) o;
            return true;
        } else if (o instanceof Chat) {
            this.chat = (Chat) o;
            return true;
        }
        return false;
    }

    public static final WrapperFactory<PermissionsWrapper, RegisteredServiceProvider<?>> FACTORY = new WrapperFactory<PermissionsWrapper, RegisteredServiceProvider<?>>() {
        @Override
        public PermissionsWrapper create(RegisteredServiceProvider<?> service, XLogger logger) throws InvalidWrapperException {
            final VaultPermissionsWrapper wrapper = new VaultPermissionsWrapper(service.getPlugin());
            if (wrapper.setProvider(service)) {
                return wrapper;
            } else {
                return null;
            }
        }
    };
}
