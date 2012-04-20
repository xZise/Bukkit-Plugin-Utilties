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

import me.ashtheking.currency.Currency;
import me.ashtheking.currency.CurrencyList;

import org.bukkit.plugin.Plugin;

import de.xzise.XLogger;
import de.xzise.bukkit.util.wrappers.WrapperFactory;

public class MultiCurrencyWrapper implements EconomyWrapper {

    private final Plugin plugin;

    public final class MultiCurrencyAccount implements AccountWrapper {

        private final String name;

        public MultiCurrencyAccount(String name) {
            this.name = name;
        }

        @Override
        public boolean hasEnough(double price) {
            return CurrencyList.hasEnough(this.name, price);
        }

        @Override
        public double getBalance() {
            return CurrencyList.getValue((String) CurrencyList.maxCurrency(this.name)[0], this.name);
        }

        @Override
        public void add(double price) {
            CurrencyList.add(this.name, price);
        }
    }

    public MultiCurrencyWrapper(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public AccountWrapper getAccount(String name) {
        return new MultiCurrencyAccount(name);
    }

    @Override
    public String format(double price) {
        return null;
    }

    @Override
    public Plugin getPlugin() {
        return this.plugin;
    }

    @SuppressWarnings("deprecation")
    public static class Factory implements de.xzise.wrappers.Factory<EconomyWrapper>, WrapperFactory<EconomyWrapper, Plugin> {

        @Override
        public EconomyWrapper create(Plugin plugin, XLogger logger) {
            if (plugin instanceof Currency) {
                return new MultiCurrencyWrapper(plugin);
            } else {
                return null;
            }
        }
    }
}
