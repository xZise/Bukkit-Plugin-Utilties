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

import com.nijikokun.register.payment.Method;
import com.nijikokun.register.payment.Method.MethodAccount;

public class MethodWrapper implements EconomyWrapper {

    private final Method method;
    private final Plugin plugin;

    public final class MethodAcc implements AccountWrapper {

        private final MethodAccount method;

        public MethodAcc(MethodAccount method) {
            this.method = method;
        }

        @Override
        public boolean hasEnough(double price) {
            return this.method.hasEnough(price);
        }

        @Override
        public void add(double price) {
            this.method.add(price);
        }

        @Override
        public double getBalance() {
            return this.method.balance();
        }

    }

    private MethodWrapper(Method method) {
        this.method = method;
        this.plugin = (Plugin) method.getPlugin();
    }

    /**
     * @deprecated Use {@link MethodWrapper#create(Method)} instead.
     */
    @Deprecated
    public MethodWrapper(Method method, Plugin plugin) {
        if (!(method.getPlugin() instanceof Plugin)) {
            throw new IllegalArgumentException("Plugin of the method isn't really a plugin.");
        }
        if (method.getPlugin() != plugin) {
            throw new IllegalArgumentException("Plugin parameter differ from the method's plugin.");
        }
        this.method = method;
        this.plugin = plugin;
    }

    public static MethodWrapper create(Method method) {
        if (method != null && method.getPlugin() instanceof Plugin) {
            return new MethodWrapper(method);
        } else {
            return null;
        }
    }

    @Override
    public AccountWrapper getAccount(String name) {
        return new MethodAcc(this.method.getAccount(name));
    }

    @Override
    public String format(double price) {
        return this.method.format(price);
    }

    @Override
    public Plugin getPlugin() {
        return this.plugin;
    }

}
