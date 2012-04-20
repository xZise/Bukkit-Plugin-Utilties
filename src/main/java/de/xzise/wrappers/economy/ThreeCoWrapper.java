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

import me.ic3d.eco.ECO;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import de.xzise.MinecraftUtil;
import de.xzise.XLogger;
import de.xzise.bukkit.util.wrappers.WrapperFactory;

public class ThreeCoWrapper implements EconomyWrapper {

    private final ECO eco;

    public final class ThreeCoAccount implements AccountWrapper {

        private final String player;
        private final ECO eco;

        public ThreeCoAccount(ECO eco, String player) {
            this.player = player;
            this.eco = eco;
        }

        public Player getPlayer() {
            return Bukkit.getPlayer(this.player);
        }

        @Override
        public boolean hasEnough(double price) {
            return this.eco.hasEnough(this.getPlayer(), (int) Math.round(price));
        }

        @Override
        public double getBalance() {
            return this.eco.getMoney(this.getPlayer());
        }

        @Override
        public void add(double price) {
            this.eco.giveMoney(this.getPlayer(), (int) Math.round(price));
        }
    }

    public ThreeCoWrapper(ECO eco) {
        this.eco = eco;
    }

    @Override
    public AccountWrapper getAccount(String name) {
        return new ThreeCoAccount(this.eco, name);
    }

    @Override
    public String format(double price) {
        String value = EconomyHandler.defaultFormat(price);
        if (MinecraftUtil.equals(price, 1)) {
            return value + " " + this.eco.getSingularCurrency();
        } else {
            return value + " " + this.eco.getPluralCurrency();
        }
    }

    @Override
    public Plugin getPlugin() {
        return this.eco;
    }

    @SuppressWarnings("deprecation")
    public static class Factory implements de.xzise.wrappers.Factory<EconomyWrapper>, WrapperFactory<EconomyWrapper, Plugin> {

        @Override
        public EconomyWrapper create(Plugin plugin, XLogger logger) {
            if (plugin instanceof ECO) {
                return new ThreeCoWrapper((ECO) plugin);
            } else {
                return null;
            }
        }
    }
}
