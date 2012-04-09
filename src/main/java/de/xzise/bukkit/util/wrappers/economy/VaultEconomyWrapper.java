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

package de.xzise.bukkit.util.wrappers.economy;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import de.xzise.XLogger;
import de.xzise.bukkit.util.wrappers.ServiceWrapperFactory;
import de.xzise.wrappers.InvalidWrapperException;
import de.xzise.wrappers.economy.AccountWrapper;
import de.xzise.wrappers.economy.EconomyWrapper;

public class VaultEconomyWrapper implements EconomyWrapper {

    public static final class VaultEconomyAccount implements AccountWrapper {

        private final Economy economy;
        private final String player;

        public VaultEconomyAccount(final Economy economy, final String player) {
            this.economy = economy;
            this.player = player;
        }

        @Override
        public boolean hasEnough(double price) {
            return this.economy.has(player, price);
        }

        @Override
        public double getBalance() {
            return this.economy.getBalance(player);
        }

        @Override
        public void add(double price) {
            this.economy.depositPlayer(player, price);
        }
    }

    private final Economy economy;
    private final Plugin plugin;

    public VaultEconomyWrapper(final Economy economy, final Plugin plugin) {
        this.economy = economy;
        this.plugin = plugin;
    }

    @Override
    public AccountWrapper getAccount(String name) {
        return new VaultEconomyAccount(this.economy, name);
    }

    @Override
    public String format(double price) {
        return this.economy.format(price);
    }

    @Override
    public Plugin getPlugin() {
        return this.plugin;
    }

    public static final ServiceWrapperFactory<EconomyWrapper> FACTORY = new ServiceWrapperFactory<EconomyWrapper>() {
        @Override
        public EconomyWrapper create(RegisteredServiceProvider<?> service, XLogger logger) throws InvalidWrapperException {
            if (service.getProvider() instanceof Economy) {
                return new VaultEconomyWrapper((Economy) service.getProvider(), service.getPlugin());
            } else {
                return null;
            }
        }
    };
}
