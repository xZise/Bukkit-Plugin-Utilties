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
        if (player == null) {
            AnjoPermissionsHandler permissions = this.groupManager.getWorldsHolder().getWorldPermissions(player);
            return permissions == null ? null : permissions.has(player, permission.getName());
        }
        return null;
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

    @Override
    public Integer getInteger(CommandSender sender, Permission<Integer> permission) {
        final UserVariables variables = getVariables(sender);
        if (variables != null) {
            return variables.getVarInteger(permission.getName());
        }
        return null;
    }

    @Override
    public Double getDouble(CommandSender sender, Permission<Double> permission) {
        final UserVariables variables = getVariables(sender);
        if (variables != null) {
            return variables.getVarDouble(permission.getName());
        }
        return null;
    }

    @Override
    public String[] getGroup(String world, String player) {
        return this.groupManager.getWorldsHolder().getWorldPermissions(world).getGroups(player);
    }

    @Override
    public String getString(CommandSender sender, Permission<String> permission, boolean recursive) {
        final Variables variables = getVariables(sender);
        if (variables != null) {
            return variables.getVarString(permission.getName());
        }
        return null;
    }

    @Override
    public String getString(String groupname, String world, Permission<String> permission) {
        OverloadedWorldHolder owh = this.groupManager.getWorldsHolder().getWorldData(world);
        Group g = owh.getGroup(groupname);
        if (g != null) {
            return g.getVariables().getVarString(permission.getName());
        } else {
            return null;
        }
    }

}
