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

import org.bukkit.plugin.Plugin;

import com.nijikokun.bukkit.Permissions.Permissions;

import de.xzise.XLogger;
import de.xzise.bukkit.util.wrappers.WrapperFactory;
import de.xzise.wrappers.InvalidWrapperException;

@SuppressWarnings("deprecation")
public class PermissionPluginWrapperFactory implements de.xzise.wrappers.Factory<PermissionsWrapper>, WrapperFactory<PermissionsWrapper, Plugin> {

    @Override
    public PermissionsWrapper create(Plugin plugin, XLogger logger) throws InvalidWrapperException {
        if (plugin instanceof Permissions) {
            String compVersion = plugin.getDescription().getVersion();
            String[] versionElements = compVersion.split("\\.");
            if (versionElements.length > 0) {
                int majorVersion;
                try {
                    majorVersion = Integer.parseInt(versionElements[0]);
                } catch (NumberFormatException e) {
                    majorVersion = -1;
                }
                switch (majorVersion) {
                case 3:
                    return new Permissions3Wrapper((Permissions) plugin, logger);
                case 2:
                case 1: // Guess, that the Permissions 1 work with the
                        // Permissions 2 wrapper (e.g. for PermissionsEx)
                    return new PermissionsPluginWrapper((Permissions) plugin);
                default:
                    throw new InvalidWrapperException("Unknown Permissions version. (" + compVersion + ")");
                }
            } else {
                throw new InvalidWrapperException("Unknown Permissions version. (" + compVersion + ")");
            }
        }
        return null;
    }

}
