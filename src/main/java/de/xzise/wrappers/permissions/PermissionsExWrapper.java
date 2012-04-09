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

package de.xzise.wrappers.permissions;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import de.xzise.MinecraftUtil;
import de.xzise.XLogger;
import de.xzise.wrappers.Factory;
import de.xzise.wrappers.InvalidWrapperException;

public class PermissionsExWrapper implements PermissionsWrapper {

    private final PermissionsEx permissions;

    private static String getWorld(CommandSender sender) {
        return sender instanceof Player ? ((Player) sender).getWorld().getName() : "";
    }

    private static PermissionUser getUser(CommandSender sender) {
        final String username = MinecraftUtil.getPlayerName(sender);
        if (username != null) {
            return PermissionsEx.getPermissionManager().getUser(username);
        } else {
            return null;
        }
    }

    @Override
    public Plugin getPlugin() {
        return this.permissions;
    }

    @Override
    public Boolean has(CommandSender sender, Permission<Boolean> permission) {
        PermissionUser user = getUser(sender);
        if (user != null) {
            return user.has(permission.getName());
        } else {
            return null;
        }
    }

    @Override
    public Integer getInteger(CommandSender sender, Permission<Integer> permission) {
        final PermissionUser user = getUser(sender);
        if (user != null) {
            return user.getOptionInteger(permission.getName(), getWorld(sender), permission.getDefault());
        } else {
            return null;
        }
    }

    @Override
    public Double getDouble(CommandSender sender, Permission<Double> permission) {
        final PermissionUser user = getUser(sender);
        if (user != null) {
            return user.getOptionDouble(permission.getName(), getWorld(sender), permission.getDefault());
        } else {
            return null;
        }
    }

    @Override
    public String[] getGroup(String world, String player) {
        PermissionUser user = PermissionsEx.getPermissionManager().getUser(player);
        return user == null ? new String[0] : user.getGroupsNames(world);
    }

    @Override
    public String getString(CommandSender sender, Permission<String> permission, boolean recursive) {
        final PermissionUser user = getUser(sender);
        if (user != null) {
            if (recursive) {
                return user.getOption(permission.getName(), getWorld(sender), permission.getDefault());
            } else {
                return user.getOwnOption(permission.getName(), getWorld(sender), permission.getDefault());
            }
        } else {
            return null;
        }
    }

    @Override
    public String getString(String groupname, String world, Permission<String> permission) {
        PermissionGroup group = PermissionsEx.getPermissionManager().getGroup(groupname);
        return group == null ? null : group.getOption(permission.getName(), world, permission.getDefault());
    }

    public PermissionsExWrapper(PermissionsEx permissions) {
        this.permissions = permissions;
    }

    public static final class FactoryImpl implements Factory<PermissionsWrapper> {

        @Override
        public PermissionsWrapper create(Plugin plugin, XLogger logger) throws InvalidWrapperException {
            if (plugin instanceof PermissionsEx) {
                return new PermissionsExWrapper((PermissionsEx) plugin);
            } else {
                return null;
            }
        }
    }
}
