/*
 * This file is part of Bukkit Plugin Utilities.
 * 
 * Bukkit Plugin Utilities is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 * 
 * Foobar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.xzise.wrappers.permissions;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import de.xzise.XLogger;
import de.xzise.wrappers.Factory;
import de.xzise.wrappers.Handler;

public class PermissionsHandler extends Handler<PermissionsWrapper> {

    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    public static final Map<String, Factory<PermissionsWrapper>> FACTORIES = new HashMap<String, Factory<PermissionsWrapper>>();
    private static final PermissionsWrapper NULLARY_PERMISSIONS = new PermissionsWrapper() {

        @Override
        public Plugin getPlugin() {
            return null;
        }

        @Override
        public Boolean has(CommandSender sender, Permission<Boolean> permission) {
            return null;
        }

        @Override
        public Integer getInteger(CommandSender sender, Permission<Integer> permission) {
            return null;
        }

        @Override
        public String[] getGroup(String world, String player) {
            return null;
        }

        @Override
        public Double getDouble(CommandSender sender, Permission<Double> permission) {
            return null;
        }

        @Override
        public String getString(CommandSender sender, Permission<String> permission, boolean recursive) {
            return null;
        }

        @Override
        public String getString(String groupname, String world, Permission<String> permission) {
            return null;
        }
    };

    static {
        FACTORIES.put("Permissions", new PermissionPluginWrapperFactory());
        FACTORIES.put("PermissionsBukkit", new PermissionsBukkitWrapper.FactoryImpl());
        FACTORIES.put("bPermissions", new BPermissionsWrapper.FactoryImpl());
        FACTORIES.put("PermissionsEx", new PermissionsExWrapper.FactoryImpl());
        FACTORIES.put("GroupManager", new GroupManagerWrapper.FactoryImpl());
    }

    public PermissionsHandler(PluginManager pluginManager, String plugin, XLogger logger) {
        super(FACTORIES, NULLARY_PERMISSIONS, pluginManager, "permissions", plugin, logger);
    }

    public boolean permission(CommandSender sender, Permission<Boolean> permission) {
        Boolean result = null;
        try {
            result = this.getWrapper().has(sender, permission);
        } catch (UnsupportedOperationException e) {
            this.logger.info("PermissionsHandler permission check wasn't supported by this plugin.");
        } catch (Exception e) {
            this.logger.info("Error on calling PermissionsWrapper permission check.");
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

    private static boolean hasByDefault(CommandSender sender, Boolean def) {
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
            this.logger.info("PermissionsHandler integer getter wasn't supported by this plugin.");
        } catch (Exception e) {
            this.logger.info("Error on calling PermissionsWrapper integer getter.");
        }
        return getValue(result, permission);
    }

    public double getDouble(CommandSender sender, Permission<Double> permission) {
        Double result = null;
        try {
            result = this.getWrapper().getDouble(sender, permission);
        } catch (UnsupportedOperationException e) {
            this.logger.info("PermissionsHandler double getter wasn't supported by this plugin.");
        } catch (Exception e) {
            this.logger.info("Error on calling PermissionsWrapper double getter.");
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
            this.logger.info("PermissionsHandler string getter wasn't supported by this plugin.");
        } catch (Exception e) {
            this.logger.info("Error on calling PermissionsWrapper string getter.");
        }
        return getValue(result, permission);
    }

    public String getString(String world, String groupname, Permission<String> permission) {
        String result = null;
        try {
            result = this.getWrapper().getString(groupname, world, permission);
        } catch (UnsupportedOperationException e) {
            this.logger.info("PermissionsHandler string getter wasn't supported by this plugin.");
        } catch (Exception e) {
            this.logger.info("Error on calling PermissionsWrapper string getter.");
        }
        return getValue(result, permission);
    }

    public String[] getGroup(String world, String player) {
        String[] groups;
        try {
            groups = this.getWrapper().getGroup(world, player);
        } catch (UnsupportedOperationException e) {
            groups = null;
            this.logger.info("PermissionsManager group getter wasn't supported by this plugin.");
        }
        return groups == null ? EMPTY_STRING_ARRAY : groups;
    }

}
