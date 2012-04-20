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

package de.xzise.wrappers.economy;

import org.bukkit.plugin.Plugin;

import cosine.boseconomy.BOSEconomy;

import de.xzise.MinecraftUtil;
import de.xzise.XLogger;
import de.xzise.bukkit.util.wrappers.WrapperFactory;
import de.xzise.wrappers.InvalidWrapperException;

@SuppressWarnings("deprecation")
public class BOSEconFactory implements de.xzise.wrappers.Factory<EconomyWrapper>, WrapperFactory<EconomyWrapper, Plugin> {

    @Override
    public EconomyWrapper create(Plugin plugin, XLogger logger) throws InvalidWrapperException {
        if (plugin instanceof BOSEconomy) {
            String[] versionparts = plugin.getDescription().getVersion().split("\\.");
            if (versionparts.length >= 2 && MinecraftUtil.isInteger(versionparts[1])) {
                int minor = Integer.parseInt(versionparts[1]);
                switch (minor) {
                case 6 : return new BOSEcon6((BOSEconomy) plugin);
                case 7 : return new BOSEcon7((BOSEconomy) plugin);
                }
            }
        }
        return null;
    }
}
