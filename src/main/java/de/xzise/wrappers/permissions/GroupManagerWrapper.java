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

import org.anjocaido.groupmanager.GroupManager;
import org.anjocaido.groupmanager.data.Group;
import org.anjocaido.groupmanager.data.User;
import org.anjocaido.groupmanager.data.UserVariables;
import org.anjocaido.groupmanager.data.Variables;
import org.anjocaido.groupmanager.dataholder.OverloadedWorldHolder;
import org.anjocaido.groupmanager.permissions.AnjoPermissionsHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import de.xzise.MinecraftUtil;
import de.xzise.XLogger;
import de.xzise.bukkit.util.callback.Callback;
import de.xzise.wrappers.Factory;
import de.xzise.wrappers.InvalidWrapperException;

public class GroupManagerWrapper implements PermissionsWrapper {

    private final GroupManager groupManager;

    public GroupManagerWrapper(GroupManager groupManager) {
        this.groupManager = groupManager;
    }

    @Override
    public Plugin getPlugin() {
        return this.groupManager;
    }

    @Override
    public Boolean has(CommandSender sender, Permission<Boolean> permission) {
        final Player player = MinecraftUtil.cast(Player.class, sender);
        if (player != null) {
            AnjoPermissionsHandler permissions = this.groupManager.getWorldsHolder().getWorldPermissions(player);
            return permissions == null ? null : permissions.has(player, permission.getName());
        }
        return null;
    }

    private <T> T getRecursive(UserWorldHolder userWorldHolder, Callback<T, Variables> callback) {
        T t = callback.call(userWorldHolder.user.getVariables());
        if (t == null) {
            return getRecursive(userWorldHolder.user.getGroup(), userWorldHolder.worldHolder, callback);
        } else {
            return t;
        }
    }

    private <T> T getRecursive(Group group, OverloadedWorldHolder worldHolder, Callback<T, Variables> callback) {
        T t = callback.call(group.getVariables());
        if (t == null) {
            for (String inherit : group.getInherits()) {
                Group g = worldHolder.getGroup(inherit);
                if (g != null) {
                    t = getRecursive(g, worldHolder, callback);
                    if (t != null) {
                        break;
                    }
                }
            }
        }
        return t;
    }

    private UserVariables getVariables(CommandSender sender) {
        final Player player = MinecraftUtil.cast(Player.class, sender);
        if (player != null) {
            OverloadedWorldHolder permissions = this.groupManager.getWorldsHolder().getWorldData(player);
            if (permissions != null) {
                User user = permissions.getUser(player.getName());
                if (user != null) {
                    return user.getVariables();
                }
            }
        }
        return null;
    }

    private final class UserWorldHolder {
        public final User user;
        public final OverloadedWorldHolder worldHolder;
        
        public UserWorldHolder(User user, OverloadedWorldHolder worldHolder) {
            this.user = user;
            this.worldHolder = worldHolder;
        }
    }

    private UserWorldHolder getUser(CommandSender sender) {
        final Player player = MinecraftUtil.cast(Player.class, sender);
        if (player != null) {
            OverloadedWorldHolder permissions = this.groupManager.getWorldsHolder().getWorldData(player);
            if (permissions != null) {
                User u = permissions.getUser(player.getName());
                if (u != null) {
                    return new UserWorldHolder(u, permissions);
                }
            }
        }
        return null;
    }

    @Override
    public Integer getInteger(CommandSender sender, final Permission<Integer> permission) {
        UserWorldHolder u = getUser(sender);
        if (u != null) {
            return getRecursive(u, new Callback<Integer, Variables>() {

                @Override
                public Integer call(Variables parameter) {
                    return parameter.getVarInteger(permission.getName());
                }
            });
        }
        return null;
    }

    @Override
    public Double getDouble(CommandSender sender, final Permission<Double> permission) {
        UserWorldHolder u = getUser(sender);
        if (u != null) {
            return getRecursive(u, new Callback<Double, Variables>() {

                @Override
                public Double call(Variables parameter) {
                    return parameter.getVarDouble(permission.getName());
                }
            });
        }
        return null;
    }

    @Override
    public String[] getGroup(String world, String player) {
        return this.groupManager.getWorldsHolder().getWorldPermissions(world).getGroups(player);
    }

    @Override
    public String getString(CommandSender sender, final Permission<String> permission, boolean recursive) {
        if (recursive) {
        final Variables variables = getVariables(sender);
        if (variables != null) {
            return variables.getVarString(permission.getName());
        }
        } else {
            UserWorldHolder u = getUser(sender);
            if (u != null) {
                return getRecursive(u, new Callback<String, Variables>() {

                    @Override
                    public String call(Variables parameter) {
                        return parameter.getVarString(permission.getName());
                    }
                });
            }
        }
        return null;
    }

    @Override
    public String getString(String groupname, String world, final Permission<String> permission) {
        OverloadedWorldHolder owh = this.groupManager.getWorldsHolder().getWorldData(world);
        Group g = owh.getGroup(groupname);
        if (g != null) {
            return getRecursive(g, owh, new Callback<String, Variables>() {

                @Override
                public String call(Variables parameter) {
                    return parameter.getVarString(permission.getName());
                }
            });
        } else {
            return null;
        }
    }

    public static final class FactoryImpl implements Factory<PermissionsWrapper> {

        @Override
        public PermissionsWrapper create(Plugin plugin, XLogger logger) throws InvalidWrapperException {
            if (plugin instanceof GroupManager) {
                return new GroupManagerWrapper((GroupManager) plugin);
            } else {
                return null;
            }
        }
    }
}
