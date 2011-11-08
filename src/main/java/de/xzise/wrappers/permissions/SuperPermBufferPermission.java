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

import org.bukkit.permissions.PermissionDefault;

public class SuperPermBufferPermission extends BufferPermission<Boolean> implements SuperPerm {

    public final String description;

    public SuperPermBufferPermission(String name, String description, boolean def) {
        super(name, def);
        this.description = description;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public PermissionDefault getPermissionDefault() {
        return this.def ? PermissionDefault.TRUE : PermissionDefault.OP;
    }

}
