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

import com.nijiko.coelho.iConomy.iConomy;
import com.nijiko.coelho.iConomy.system.Account;
import com.nijiko.coelho.iConomy.system.Bank;

public class iConomy4 implements EconomyWrapper {

    private final Bank bank;
    private final Plugin plugin;
    
    public final class Account4 implements AccountWrapper {

        private final Account account;
        
        public Account4(Account account) {
            this.account = account;
        }
        
        @Override
        public boolean hasEnough(double price) {
            return this.account.hasEnough(price);
        }

        @Override
        public void add(double price) {
            this.account.add(price);
        }

        @Override
        public double getBalance() {
            return this.account.getBalance();
        }
        
    }
    
    public iConomy4(Plugin plugin) {
        this.bank = iConomy.getBank();
        this.plugin = plugin;
    }
    
    @Override
    public AccountWrapper getAccount(String name) {
        if (!this.bank.hasAccount(name)) {
            this.bank.addAccount(name);
        }
        return new Account4(this.bank.getAccount(name));
    }

    @Override
    public String format(double price) {
        return this.bank.format(price);
    }

    @Override
    public Plugin getPlugin() {
        return this.plugin;
    }

}
