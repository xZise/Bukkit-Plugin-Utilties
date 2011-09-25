package de.xzise.wrappers.economy;

import me.ic3d.eco.ECO;

import org.bukkit.plugin.Plugin;

import de.xzise.MinecraftUtil;
import de.xzise.XLogger;

public class ThreeCoWrapper implements EconomyWrapper {

    private final ECO eco;

    public final class ThreeCoAccount implements AccountWrapper {

        private final String player;
        private final ECO eco;

        public ThreeCoAccount(ECO eco, String player) {
            this.player = player;
            this.eco = eco;
        }

        @Override
        public boolean hasEnough(double price) {
            return this.eco.hasEnough(this.player, (int) Math.round(price));
        }

        @Override
        public double getBalance() {
            return this.eco.getMoney(this.player);
        }

        @Override
        public void add(double price) {
            this.eco.giveMoney(this.player, (int) Math.round(price));
        }
    }

    public ThreeCoWrapper(ECO eco) {
        this.eco = eco;
    }

    @Override
    public AccountWrapper getAccount(String name) {
        return new ThreeCoAccount(this.eco, name);
    }

    @Override
    public String format(double price) {
        String value = EconomyHandler.defaultFormat(price);
        if (MinecraftUtil.equals(price, 1)) {
            return value + " " + this.eco.getSingularCurrency();
        } else {
            return value + " " + this.eco.getPluralCurrency();
        }
    }

    @Override
    public Plugin getPlugin() {
        return this.eco;
    }

    public static class Factory implements de.xzise.wrappers.Factory<EconomyWrapper> {

        @Override
        public EconomyWrapper create(Plugin plugin, XLogger logger) {
            if (plugin instanceof ECO) {
                return new ThreeCoWrapper((ECO) plugin);
            } else {
                return null;
            }
        }
    }
}
