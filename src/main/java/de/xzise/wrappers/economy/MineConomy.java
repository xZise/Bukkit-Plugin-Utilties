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

import me.mjolnir.mineconomy.Accounting;

import org.bukkit.plugin.Plugin;

import de.xzise.XLogger;

public class MineConomy implements EconomyWrapper {

    private final me.mjolnir.mineconomy.MineConomy plugin;

    public final class MineConomyAccount implements AccountWrapper {

        private final String name;

        public MineConomyAccount(String name) {
            this.name = name;
        }

        @Override
        public boolean hasEnough(double price) {
            return this.getBalance() >= price;
        }

        @Override
        public void add(double price) {
            Accounting.setBalance(this.name, this.getBalance() + price, me.mjolnir.mineconomy.MineConomy.accounts);
        }

        @Override
        public double getBalance() {
            return Accounting.getBalance(name, me.mjolnir.mineconomy.MineConomy.accounts);
        }

    }

    public MineConomy(me.mjolnir.mineconomy.MineConomy plugin) {
        this.plugin = plugin;
    }

    @Override
    public AccountWrapper getAccount(String name) {
        return new MineConomyAccount(name);
    }

    @Override
    public String format(double price) {
        return null;
    }

    @Override
    public Plugin getPlugin() {
        return this.plugin;
    }

    public static class Factory implements de.xzise.wrappers.Factory<EconomyWrapper> {

        @Override
        public EconomyWrapper create(Plugin plugin, XLogger logger) {
            if (plugin instanceof me.mjolnir.mineconomy.MineConomy) {
                return new MineConomy((me.mjolnir.mineconomy.MineConomy) plugin);
            } else {
                return null;
            }
        }
    }
}
