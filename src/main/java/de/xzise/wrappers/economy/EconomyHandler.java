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

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import com.nijikokun.register.payment.Methods;

import de.xzise.MinecraftUtil;
import de.xzise.XLogger;
import de.xzise.wrappers.Factory;
import de.xzise.wrappers.Handler;

public class EconomyHandler extends Handler<EconomyWrapper> {

    public enum PayResult {
        /** The price was paid. */
        PAID,
        /** The price couldn't paid, but not because the player hasn't enough. */
        UNABLE,
        /** The price couldn't paid, because the player hasn't enough. */
        NOT_ENOUGH;
    }

    public static final Map<String, Factory<EconomyWrapper>> FACTORIES = new HashMap<String, Factory<EconomyWrapper>>();

    static {
        FACTORIES.put("BOSEconomy", new BOSEcon0.FactoryImpl());
        FACTORIES.put("iConomy", new iConomyFactory());
        FACTORIES.put("Essentials", new Essentials.FactoryImpl());
        FACTORIES.put("MineConomy", new MineConomy.FactoryImpl());
    }

    public static final AccountWrapper NULLARY_ACCOUNT = new AccountWrapper() {

        @Override
        public boolean hasEnough(double price) {
            return false;
        }

        @Override
        public void add(double price) {
        }

        @Override
        public double getBalance() {
            return 0;
        }
    };

    private AccountWrapper tax = NULLARY_ACCOUNT;
    private final boolean useMethods;
    private String economyBaseName;
    private final PluginManager pluginManager;

    public EconomyHandler(PluginManager pluginManager, String economyPluginName, String economyBaseName, XLogger logger) {
        super(FACTORIES, pluginManager, "economy", economyPluginName, logger);
        this.economyBaseName = economyBaseName;
        this.pluginManager = pluginManager;
        Methods m = null;
        try {
            Methods.hasMethod();
            m = new Methods();
        } catch (NoClassDefFoundError e) {
            this.logger.info("No Register found. Deactivating Register support.");
        } catch (IncompatibleClassChangeError e) {
            this.logger.info("Outdated Register found. Deactivating Register support.");
        }
        this.useMethods = m != null;
        this.setBaseAccount();
    }

    /**
     * Pays for an action if the sender has enough money. If the sender is not a
     * player no money will be transfered.
     * 
     * @param sender
     *            The paying sender.
     * @param reciever
     *            Optional reciever of the price. If null only the basic price
     *            is to pay.
     * @param price
     *            The amount of money which the reciever get.
     * @param basic
     *            The basic price like an tax.
     * @return If the price could be paid or if there was nothing to pay.
     */
    public PayResult pay(CommandSender sender, String reciever, double price, double basic) {
        if (this.getWrapper() != null) {
            Player player = MinecraftUtil.getPlayer(sender);
            if (player != null) {
                AccountWrapper executor = this.getAccount(player.getName());
                // Not negative
                // TODO: Add option if allow not negatives
                if (executor.hasEnough(price + basic)) {
                    executor.add(-price - basic);
                    this.tax.add(basic);
                    if (MinecraftUtil.isSet(reciever)) {
                        AccountWrapper owner = this.getAccount(reciever);
                        owner.add(price);
                    }
                    return PayResult.PAID;
                } else {
                    return PayResult.NOT_ENOUGH;
                }
            } else {
                this.logger.info("Couldn't pay action, because the executor is not a player.");
            }
        } else if (price > 0) {
            sender.sendMessage(ChatColor.RED + "You should pay for this warp. But no economy plugin found.");
        }
        return PayResult.UNABLE;
    }

    private final AccountWrapper getAccount(String name) {
        return this.getWrapper().getAccount(name);
    }

    public double getBalance(String name) {
        AccountWrapper acc = getAccount(name);
        return acc == null ? 0 : acc.getBalance();
    }

    public PayResult pay(CommandSender sender, double basic) {
        return this.pay(sender, null, 0, basic);
    }

    public String format(double price) {
        String result = null;
        if (this.isActive()) {
            result = this.getWrapper().format(price);
        }

        if (result == null) {
            DecimalFormat fakeForm = new DecimalFormat("#,##0.##");
            String fakeFormed = fakeForm.format(price);
            if (fakeFormed.endsWith(".")) {
                fakeFormed = fakeFormed.substring(0, fakeFormed.length() - 1);
            }

            return fakeFormed;
        }
        return result;
    }

    public void reloadConfig(String economyPluginName, String economyBaseName) {
        this.economyBaseName = economyBaseName;
        this.setPluginName(economyPluginName);
        this.load();
        this.setBaseAccount();
    }

    private void setBaseAccount() {
        if (MinecraftUtil.isSet(this.economyBaseName) && this.isActive()) {
            this.tax = this.getWrapper().getAccount(this.economyBaseName);
        } else {
            this.tax = NULLARY_ACCOUNT;
        }
    }

    @Override
    protected void loaded() {
        this.setBaseAccount();
    }

    @Override
    protected boolean customLoad(Plugin plugin) {
        if (this.useMethods && !Methods.hasMethod() && Methods.setMethod(this.pluginManager)) {
            this.setWrapper(MethodWrapper.create(Methods.getMethod()));
            return true;
        } else {
            return false;
        }
    }
}
