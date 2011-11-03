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
import de.xzise.wrappers.Factory;
import de.xzise.wrappers.InvalidWrapperException;

public class PermissionsBukkitWrapper implements PermissionsWrapper {

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
    public Integer getInteger(CommandSender sender, Permission<Integer> permission) {
        // Not supported
        return null;
    }

    @Override
    public Double getDouble(CommandSender sender, Permission<Double> permission) {
        // Not supported
        return null;
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

    @Override
    public String getString(CommandSender sender, Permission<String> permission, boolean recursive) {
        // Not supported
        return null;
    }

    @Override
    public String getString(String groupname, String world, Permission<String> permission) {
        // Not supported
        return null;
    }
    
    public static final class FactoryImpl implements Factory<PermissionsWrapper> {

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
