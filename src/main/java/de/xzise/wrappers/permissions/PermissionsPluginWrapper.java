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

package de.xzise.wrappers.permissions;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

import de.xzise.MinecraftUtil;

public class PermissionsPluginWrapper implements PermissionsWrapper {

    private PermissionHandler handler;
    private Plugin plugin;
    
    @Override
    public Boolean has(CommandSender sender, Permission<Boolean> permission) {
        Player player = MinecraftUtil.getPlayer(sender);
        if (player != null) {
            return this.handler.has(player, permission.getName());
        } else {
            return null;
        }
    }

    @Override
    public Integer getInteger(CommandSender sender, Permission<Integer> permission) {
        Player player = MinecraftUtil.getPlayer(sender);
        if (player != null) {
            @SuppressWarnings("deprecation")
            int i = this.handler.getPermissionInteger(player.getWorld().getName(), player.getName(), permission.getName());
            if (i < 0) {
                return null;
            } else {
                return i;
            }
        } else {
            return null;
        }
    }
    
    @Override
    public Double getDouble(CommandSender sender, Permission<Double> permission) {
        Player player = MinecraftUtil.getPlayer(sender);
        if (player != null) {
            @SuppressWarnings("deprecation")
            double i = this.handler.getPermissionDouble(player.getWorld().getName(), player.getName(), permission.getName());
            if (i < 0) {
                return null;
            } else {
                return i;
            }
        } else {
            return null;
        }
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public String[] getGroup(String world, String player) {
        return new String[] { this.handler.getGroup(world, player) };
    }
    
    public PermissionsPluginWrapper(Permissions plugin) {
        this.handler = plugin.getHandler();
    }

    @Override
    public Plugin getPlugin() {
        return this.plugin;
    }

    @SuppressWarnings("deprecation")
    @Override
    public String getString(CommandSender sender, Permission<String> permission, boolean recursive) {
        Player player = MinecraftUtil.getPlayer(sender);
        if (player != null) {
            if (recursive) {
                return this.handler.getUserPermissionString(player.getWorld().getName(), player.getName(), permission.getName());
            } else {
                return this.handler.getPermissionString(player.getWorld().getName(), player.getName(), permission.getName());
            }
        } else {
            return null;
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public String getString(String groupname, String world, Permission<String> permission) {
        return this.handler.getGroupPermissionString(world, groupname, permission.getName());
    }
}
