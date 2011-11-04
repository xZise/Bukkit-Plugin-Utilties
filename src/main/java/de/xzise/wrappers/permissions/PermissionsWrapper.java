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

import de.xzise.wrappers.Wrapper;

public interface PermissionsWrapper extends Wrapper {

    Boolean has(CommandSender sender, Permission<Boolean> permission);
    
    Integer getInteger(CommandSender sender, Permission<Integer> permission);

    Double getDouble(CommandSender sender, Permission<Double> permission);

    String[] getGroup(String world, String player);

    String getString(CommandSender sender, Permission<String> permission, boolean recursive);

    String getString(String groupname, String world, Permission<String> permission);
    
}
