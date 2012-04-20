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

package de.xzise.wrappers.economy;

import org.bukkit.plugin.Plugin;

import de.xzise.XLogger;
import de.xzise.bukkit.util.wrappers.WrapperFactory;

@SuppressWarnings("deprecation")
public class iConomyFactory implements de.xzise.wrappers.Factory<EconomyWrapper>, WrapperFactory<EconomyWrapper, Plugin> {

    @Override
    public EconomyWrapper create(Plugin plugin, XLogger logger) {
        try {
            if (plugin instanceof com.iConomy.iConomy) {
                return new iConomy5(plugin);
            } else {
                return null;
            }
        } catch (NoClassDefFoundError e) {
            logger.info("The plugin \"" + plugin.getDescription().getFullName() + "\" is not iConomy 5 compatible.");
        }

        try {
            if (plugin instanceof com.nijiko.coelho.iConomy.iConomy) {
                return new iConomy4(plugin);
            } else {
                return null;
            }
        } catch (NoClassDefFoundError e) {
            logger.info("The plugin \"" + plugin.getDescription().getFullName() + "\" is not iConomy 4 compatible.");
        }
        return null;
    }

}
