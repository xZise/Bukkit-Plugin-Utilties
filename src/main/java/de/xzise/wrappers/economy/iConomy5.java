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

import com.iConomy.iConomy;
import com.iConomy.system.Account;

public class iConomy5 implements EconomyWrapper {

    public final class Account5 implements AccountWrapper {

        private Account account;

        public Account5(Account account) {
            this.account = account;
        }

        @Override
        public boolean hasEnough(double price) {
            return this.account.getHoldings().hasEnough(price);
        }

        @Override
        public void add(double price) {
            this.account.getHoldings().add(price);
        }

        @Override
        public double getBalance() {
            return this.account.getHoldings().balance();
        }

    }

    private final Plugin plugin;

    public iConomy5(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public AccountWrapper getAccount(String name) {
        return new Account5(iConomy.getAccount(name));
    }

    @Override
    public String format(double price) {
        return iConomy.format(price);
    }

    @Override
    public Plugin getPlugin() {
        return this.plugin;
    }

}
