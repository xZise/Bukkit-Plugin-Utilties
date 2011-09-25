package de.xzise.wrappers.economy;

import org.bukkit.plugin.Plugin;

import cosine.boseconomy.BOSEconomy;

import de.xzise.MinecraftUtil;
import de.xzise.XLogger;
import de.xzise.wrappers.Factory;
import de.xzise.wrappers.InvalidWrapperException;

public class BOSEconFactory implements Factory<EconomyWrapper> {

    @Override
    public EconomyWrapper create(Plugin plugin, XLogger logger) throws InvalidWrapperException {
        if (plugin instanceof BOSEconomy) {
            String[] versionparts = plugin.getDescription().getVersion().split("\\.");
            if (versionparts.length >= 2 && MinecraftUtil.isInteger(versionparts[1])) {
                int minor = Integer.parseInt(versionparts[1]);
                switch (minor) {
                case 6 : return new BOSEcon6((BOSEconomy) plugin);
                case 7 : return new BOSEcon7((BOSEconomy) plugin);
                }
            }
        }
        return null;
    }
}
