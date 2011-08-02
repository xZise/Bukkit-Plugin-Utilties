package de.xzise.wrappers.economy;

import org.bukkit.plugin.Plugin;

import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;

import de.xzise.XLogger;

public class Essentials0 implements EconomyWrapper {

    private final Plugin economy;
    private final XLogger logger;

    public final class EssentialsAccount implements AccountWrapper {
        private final String name;

        public EssentialsAccount(String name, XLogger logger) {
            this.name = name;
//            if (!Economy.playerExists(name)) {
//                if (!Economy.(name)) {
//                    logger.warning("EssentialsAccount: Couldn't create a new account named \"" + name + "\"!");
//                }
//            }
        }

        @Override
        public boolean hasEnough(double price) {
            try {
                return Economy.hasEnough(this.name, price);
            } catch (UserDoesNotExistException e) {
                System.out.println("udnee has");
                // eat
                return false;
            }
        }

        @Override
        public void add(double price) {
            try {
                Economy.add(this.name, price);
            } catch (UserDoesNotExistException e) {
                System.out.println("udnee add");
                // eat
            } catch (NoLoanPermittedException e) {
                System.out.println("nlpe add");
                // eat
            }
        }
    }

    public Essentials0(Plugin plugin, XLogger logger) {
        this.economy = plugin;
        this.logger = logger;
    }

    @Override
    public AccountWrapper getAccount(String name) {
        return new EssentialsAccount(name, this.logger);
    }

    @Override
    public String format(double price) {
        return Economy.format(price);
    }

    @Override
    public Plugin getPlugin() {
        return this.economy;
    }

    public static class Factory implements EconomyWrapperFactory {

        @Override
        public EconomyWrapper create(Plugin plugin, XLogger logger) {
            if (plugin instanceof com.earth2me.essentials.Essentials) {
                Essentials0 buf = new Essentials0(plugin, logger);
                try {
                    buf.format(0);
                    return buf;
                } catch (NoClassDefFoundError e) {
                    logger.info("Essentials plugin found, but without Economy API. Should be there since Essentials 2.2.13");
                    return null;
                }
            } else {
                return null;
            }
        }

    }

}
