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

import org.bukkit.command.CommandSender;

import de.xzise.wrappers.permissions.Permission;
import de.xzise.wrappers.permissions.PermissionsWrapper;

public abstract class NullaryPermissionsWrapper implements PermissionsWrapper {

    @Override
    public Boolean has(CommandSender sender, Permission<Boolean> permission) {
        return null;
    }

    @Override
    public Integer getInteger(CommandSender sender, Permission<Integer> permission) {
        return null;
    }

    @Override
    public Double getDouble(CommandSender sender, Permission<Double> permission) {
        return null;
    }

    @Override
    public String[] getGroup(String world, String player) {
        return null;
    }

    @Override
    public String getString(CommandSender sender, Permission<String> permission, boolean recursive) {
        return null;
    }

    @Override
    public String getString(String groupname, String world, Permission<String> permission) {
        return null;
    }

}
