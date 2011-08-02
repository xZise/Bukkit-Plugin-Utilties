package de.xzise.wrappers.permissions;

import org.bukkit.permissions.PermissionDefault;

public interface SuperPerm extends Permission<Boolean> {

    String getDescription();
    String getName();
    PermissionDefault getPermissionDefault();

}
