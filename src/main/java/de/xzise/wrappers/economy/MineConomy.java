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

import com.spikensbror.bukkit.mineconomy.bank.Bank;

import de.xzise.XLogger;
import de.xzise.wrappers.Factory;

public class MineConomy implements EconomyWrapper {

    private final com.spikensbror.bukkit.mineconomy.MineConomy plugin;

    public final class MineConomyAccount implements AccountWrapper {

        private final Bank bank;
        private final String name;

        public MineConomyAccount(Bank bank, String name) {
            this.bank = bank;
            this.name = name;
        }

        @Override
        public boolean hasEnough(double price) {
            return this.bank.getTotal(this.name) >= price;
        }

        @Override
        public void add(double price) {
            this.bank.add(this.name, price);
        }

        @Override
        public double getBalance() {
            return this.bank.getTotal(this.name);
        }

    }

    public MineConomy(com.spikensbror.bukkit.mineconomy.MineConomy plugin) {
        this.plugin = plugin;
    }

    @Override
    public AccountWrapper getAccount(String name) {
        return new MineConomyAccount(this.plugin.getBank(), name);
    }

    @Override
    public String format(double price) {
        return null;
    }

    @Override
    public Plugin getPlugin() {
        return this.plugin;
    }

    public static class FactoryImpl implements Factory<EconomyWrapper> {

        @Override
        public EconomyWrapper create(Plugin plugin, XLogger logger) {
            if (plugin instanceof com.spikensbror.bukkit.mineconomy.MineConomy) {
                return new MineConomy((com.spikensbror.bukkit.mineconomy.MineConomy) plugin);
            } else {
                return null;
            }
        }

    }

}
