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

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import de.bananaco.bpermissions.api.ApiLayer;
import de.bananaco.bpermissions.api.util.CalculableType;
import de.bananaco.permissions.Permissions;
import de.bananaco.permissions.info.InfoReader;
import de.bananaco.permissions.worlds.WorldPermissionsManager;
import de.xzise.XLogger;
import de.xzise.wrappers.Factory;
import de.xzise.wrappers.InvalidWrapperException;

public class BPermissionsWrapper implements PermissionsWrapper {

    private final InfoReader infoReader;
    private final WorldPermissionsManager worldPermissionsManager;
    private final Plugin plugin;

    public BPermissionsWrapper(Permissions plugin) {
        this.plugin = plugin;
        this.infoReader = Permissions.getInfoReader();
        this.worldPermissionsManager = Permissions.getWorldPermissionsManager();
    }

    @Override
    public Plugin getPlugin() {
        return this.plugin;
    }

    @Override
    public Boolean has(CommandSender sender, Permission<Boolean> permission) {
        if (sender instanceof Player) {
            return ApiLayer.hasPermission(((Player) sender).getWorld().getName(), CalculableType.USER, sender.getName(), permission.getName());
        } else {
            return null;
        }
    }

    private static String makeCompatible(String value) {
        return value.replace('.', '-');
    }

    private String getValue(CommandSender sender, String name) {
        if (sender instanceof Player) {
            return makeCompatible(this.infoReader.getValue((Player) sender, name));
        } else {
            return null;
        }
    }

    @Override
    public Integer getInteger(CommandSender sender, Permission<Integer> permission) {
        String value = getValue(sender, permission.getName());
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException nfe) {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public Double getDouble(CommandSender sender, Permission<Double> permission) {
        String value = getValue(sender, permission.getName());
        if (value != null) {
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException nfe) {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public String[] getGroup(String world, String player) {
        List<String> groups = this.worldPermissionsManager.getPermissionSet(world).getGroups(player);
        return groups == null ? new String[0] : groups.toArray(new String[0]);
    }

    @Override
    public String getString(CommandSender sender, Permission<String> permission, boolean recursive) {
        return this.getValue(sender, permission.getName());
    }

    @Override
    public String getString(String groupname, String world, Permission<String> permission) {
        /* No specific group string info */
        return null;
    }

    public static final class FactoryImpl implements Factory<PermissionsWrapper> {

        @Override
        public PermissionsWrapper create(Plugin plugin, XLogger logger) throws InvalidWrapperException {
            if (plugin instanceof Permissions) {
                return new BPermissionsWrapper((Permissions) plugin);
            } else {
                return null;
            }
        }
    }
}
