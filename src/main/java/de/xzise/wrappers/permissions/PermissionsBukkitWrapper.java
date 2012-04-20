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

package de.xzise.wrappers.permissions;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import com.platymuus.bukkit.permissions.Group;
import com.platymuus.bukkit.permissions.PermissionsPlugin;

import de.xzise.XLogger;
import de.xzise.bukkit.util.wrappers.WrapperFactory;
import de.xzise.bukkit.util.wrappers.permissions.NullaryPermissionsWrapper;
import de.xzise.wrappers.InvalidWrapperException;

public class PermissionsBukkitWrapper extends NullaryPermissionsWrapper {

    private final PermissionsPlugin plugin;

    public PermissionsBukkitWrapper(PermissionsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Plugin getPlugin() {
        return this.plugin;
    }

    @Override
    public Boolean has(CommandSender sender, Permission<Boolean> permission) {
        return sender.hasPermission(permission.getName());
    }

    @Override
    public String[] getGroup(String world, String player) {
        List<Group> groups = this.plugin.getGroups(player);
        String[] groupNames = new String[groups.size()];
        int i = 0;
        for (Group group : groups) {
            groupNames[i++] = group.getName();
        }
        return groupNames;
    }

    @SuppressWarnings("deprecation")
    public static final class FactoryImpl implements de.xzise.wrappers.Factory<PermissionsWrapper>, WrapperFactory<PermissionsWrapper, Plugin> {

        @Override
        public PermissionsWrapper create(Plugin plugin, XLogger logger) throws InvalidWrapperException {
            if (plugin instanceof PermissionsPlugin) {
                return new PermissionsBukkitWrapper((PermissionsPlugin) plugin);
            } else {
                return null;
            }
        }
    }

}
