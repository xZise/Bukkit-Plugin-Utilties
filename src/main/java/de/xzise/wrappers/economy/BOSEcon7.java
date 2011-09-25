package de.xzise.wrappers.economy;

import org.bukkit.plugin.Plugin;

import cosine.boseconomy.BOSEconomy;

public class BOSEcon7 implements EconomyWrapper {

    private BOSEconomy economy;

    public BOSEcon7(BOSEconomy plugin) {
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

        @Override
        public void add(double price) {
            this.economy.addPlayerMoney(this.name, price, false);
        }

        @Override
        public double getBalance() {
            return this.economy.getPlayerMoneyDouble(name);
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
