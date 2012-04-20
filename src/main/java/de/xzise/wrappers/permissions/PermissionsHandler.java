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

package de.xzise.wrappers.permissions;

import java.util.Collection;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;

import com.google.common.collect.ImmutableMap;

import de.xzise.XLogger;
import de.xzise.bukkit.util.wrappers.WrapperFactory;
import de.xzise.bukkit.util.wrappers.permissions.NullaryPermissionsWrapper;
import de.xzise.bukkit.util.wrappers.permissions.VaultPermissionsWrapper;
import de.xzise.wrappers.Handler;

public class PermissionsHandler extends Handler<PermissionsWrapper> {

    public static final ImmutableMap<String, WrapperFactory<PermissionsWrapper, Plugin>> FACTORIES;
    public static final ImmutableMap<String, WrapperFactory<PermissionsWrapper, RegisteredServiceProvider<?>>> SERVICE_FACTORIES;

    private static final PermissionsWrapper NULLARY_PERMISSIONS = new NullaryPermissionsWrapper() {
        @Override
        public Plugin getPlugin() {
            return null;
        }
    };

    static {
        FACTORIES = ImmutableMap.<String, WrapperFactory<PermissionsWrapper, Plugin>>builder()
                .put("Permissions", new PermissionPluginWrapperFactory())
                .put("PermissionsBukkit", new PermissionsBukkitWrapper.FactoryImpl())
                .put("bPermissions", new BPermissionsWrapper.FactoryImpl())
                .put("PermissionsEx", new PermissionsExWrapper.FactoryImpl())
                .put("GroupManager", new GroupManagerWrapper.FactoryImpl())
                .build();

        SERVICE_FACTORIES = ImmutableMap.<String, WrapperFactory<PermissionsWrapper, RegisteredServiceProvider<?>>>builder()
                .put("Vault", VaultPermissionsWrapper.FACTORY)
                .build();
    }

    private boolean logUnsupported = false;

    @Deprecated
    public PermissionsHandler(final PluginManager pluginManager, final String plugin, final XLogger logger) {
        this(pluginManager, null, plugin, logger);
    }

    public PermissionsHandler(final PluginManager pluginManager, final ServicesManager servicesManager, final String plugin, final XLogger logger) {
        super(FACTORIES, SERVICE_FACTORIES, NULLARY_PERMISSIONS, pluginManager, servicesManager, "permissions", plugin, logger);
    }

    public void setLogUnsupported(final boolean logUnsupported) {
        this.logUnsupported = logUnsupported;
    }

    public boolean getLogUnsupported() {
        return this.logUnsupported;
    }

    private void printUnsupported(final String name) {
        if (this.logUnsupported) {
            this.logger.info("PermissionsManager '" + name + "' wasn't supported by this plugin.");
        }
    }

    private void printException(final String name) {
        this.logger.info("Error on calling PermissionsWrapper '" + name + "'.");
    }

    public boolean permission(CommandSender sender, Permission<Boolean> permission) {
        Boolean result = null;
        try {
            result = this.getWrapper().has(sender, permission);
        } catch (UnsupportedOperationException e) {
            this.printUnsupported("permission check");
        } catch (Exception e) {
            this.printException("permission check");
        }
        if (result != null) {
            return result;
        } else {
            try {
                return sender.hasPermission(permission.getName());
            } catch (NoSuchMethodError e) {
                return hasByDefault(sender, permission.getDefault());
            }
        }
    }

    private static boolean hasByDefault(final CommandSender sender, final Boolean def) {
        if (def != null && def == true) {
            return true;
        } else {
            return sender.isOp();
        }
    }

    // To prevent the unchecked warning
    public boolean permissionOr(CommandSender sender, Permission<Boolean> p1, Permission<Boolean> p2) {
        return this.permission(sender, p1) || this.permission(sender, p2);
    }

    public boolean permissionOr(CommandSender sender, Collection<? extends Permission<Boolean>> permissions) {
        for (Permission<Boolean> permission : permissions) {
            if (this.permission(sender, permission)) {
                return true;
            }
        }
        return false;
    }

    private static <T> T getValue(T t, Permission<T> permission) {
        return t == null ? permission.getDefault() : t;
    }

    public int getInteger(CommandSender sender, Permission<Integer> permission) {
        Integer result = null;
        try {
            result = this.getWrapper().getInteger(sender, permission);
        } catch (UnsupportedOperationException e) {
            this.printUnsupported("integer getter");
        } catch (Exception e) {
            this.printException("integer getter");
        }
        return getValue(result, permission);
    }

    public double getDouble(CommandSender sender, Permission<Double> permission) {
        Double result = null;
        try {
            result = this.getWrapper().getDouble(sender, permission);
        } catch (UnsupportedOperationException e) {
            this.printUnsupported("double getter");
        } catch (Exception e) {
            this.printException("double getter");
        }
        return getValue(result, permission);
    }

    public String getString(CommandSender sender, Permission<String> permission) {
        return this.getString(sender, permission, true);
    }

    public String getUserString(CommandSender sender, Permission<String> permission) {
        return this.getString(sender, permission, false);
    }

    private String getString(CommandSender sender, Permission<String> permission, boolean recursive) {
        String result = null;
        try {
            result = this.getWrapper().getString(sender, permission, recursive);
        } catch (UnsupportedOperationException e) {
            this.printUnsupported("string getter");
        } catch (Exception e) {
            this.printException("string getter");
        }
        return getValue(result, permission);
    }

    public String getString(String world, String groupname, Permission<String> permission) {
        String result = null;
        try {
            result = this.getWrapper().getString(groupname, world, permission);
        } catch (UnsupportedOperationException e) {
            this.printUnsupported("string getter");
        } catch (Exception e) {
            this.printException("string getter");
        }
        return getValue(result, permission);
    }

    public String[] getGroup(String world, String player) {
        String[] groups = null;
        try {
            groups = this.getWrapper().getGroup(world, player);
        } catch (UnsupportedOperationException e) {
            this.printUnsupported("group getter");
        } catch (Exception e) {
            this.printException("group getter");
        }
        return groups == null ? new String[0] : groups;
    }

    @Override
    protected boolean customLoad(RegisteredServiceProvider<?> provider) {
        if (this.getWrapper() instanceof VaultPermissionsWrapper) {
            ((VaultPermissionsWrapper) this.getWrapper()).setProvider(provider);
            return true;
        } else {
            return super.customLoad(provider);
        }
    }
}
