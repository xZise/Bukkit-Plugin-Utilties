package de.xzise.wrappers.economy;

import me.ashtheking.currency.Currency;
import me.ashtheking.currency.CurrencyList;

import org.bukkit.plugin.Plugin;

import de.xzise.XLogger;

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

    public static class Factory implements de.xzise.wrappers.Factory<EconomyWrapper> {

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
