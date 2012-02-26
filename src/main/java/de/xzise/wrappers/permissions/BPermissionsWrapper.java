package de.xzise.wrappers.permissions;

import java.util.List;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import de.bananaco.permissions.Permissions;
import de.bananaco.permissions.info.InfoReader;
import de.bananaco.permissions.worlds.WorldPermissionsManager;
import de.xzise.XLogger;
import de.xzise.wrappers.Factory;
import de.xzise.wrappers.InvalidWrapperException;

public class BPermissionsWrapper implements PermissionsWrapper {
    
    private final InfoReader infoReader;
    private final WorldPermissionsManager worldPermissionsManager;
    private final Plugin plugin;

    public BPermissionsWrapper(Permissions plugin) {
        this.plugin = plugin;
        this.infoReader = Permissions.getInfoReader();
        this.worldPermissionsManager = Permissions.getWorldPermissionsManager();
    }

    @Override
    public Plugin getPlugin() {
        return this.plugin;
    }

    @Override
    public Boolean has(CommandSender sender, Permission<Boolean> permission) {
        if (sender instanceof Player) {
            World world = ((Player) sender).getWorld();
            return worldPermissionsManager.getPermissionSet(world).has((Player) sender, permission.getName());
        } else {
            return null;
        }
    }
    
    private static String makeCompatible(String value) {
        return value.replace('.', '-');
    }
    
    private String getValue(CommandSender sender, String name) {
        if (sender instanceof Player) {
            return makeCompatible(this.infoReader.getValue((Player) sender, name));
        } else {
            return null;
        }
    }

    @Override
    public Integer getInteger(CommandSender sender, Permission<Integer> permission) {
        String value = getValue(sender, permission.getName());
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException nfe) {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public Double getDouble(CommandSender sender, Permission<Double> permission) {
        String value = getValue(sender, permission.getName());
        if (value != null) {
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException nfe) {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public String[] getGroup(String world, String player) {
        List<String> groups = this.worldPermissionsManager.getPermissionSet(world).getGroups(player);
        return groups == null ? new String[0] : groups.toArray(new String[0]);
    }

    @Override
    public String getString(CommandSender sender, Permission<String> permission, boolean recursive) {
        return this.getValue(sender, permission.getName());
    }

    @Override
    public String getString(String groupname, String world, Permission<String> permission) {
        /* No specific group string info */
        return null;
    }

    public static final class FactoryImpl implements Factory<PermissionsWrapper> {

        @Override
        public PermissionsWrapper create(Plugin plugin, XLogger logger) throws InvalidWrapperException {
            if (plugin instanceof Permissions) {
                return new BPermissionsWrapper((Permissions) plugin);
            } else {
                return null;
            }
        }
    }
}
