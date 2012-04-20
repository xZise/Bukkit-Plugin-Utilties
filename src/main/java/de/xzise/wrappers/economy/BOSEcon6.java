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

public class BOSEcon6 implements EconomyWrapper {

    private BOSEconomy economy;

    public BOSEcon6(BOSEconomy plugin) {
        this.economy = plugin;
    }

    public final class BOSEAccount implements AccountWrapper {

        private final BOSEconomy economy;
        private final String name;

        public BOSEAccount(BOSEconomy economy, String name) {
            this.economy = economy;
            this.name = name;
        }

        @Override
        public boolean hasEnough(double price) {
            return this.getBalance() >= price;
        }

        @SuppressWarnings("deprecation")
        @Override
        public void add(double price) {
            this.economy.addPlayerMoney(this.name, (int) Math.round(price), false);
        }

        @SuppressWarnings("deprecation")
        @Override
        public double getBalance() {
            return this.economy.getPlayerMoney(this.name);
        }

    }

    @Override
    public AccountWrapper getAccount(String name) {
        return new BOSEAccount(this.economy, name);
    }

    @Override
    public String format(double price) {
        return null;
    }

    @Override
    public Plugin getPlugin() {
        return this.economy;
    }
}
