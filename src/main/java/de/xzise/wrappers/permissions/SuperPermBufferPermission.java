package de.xzise.wrappers.permissions;

import org.bukkit.permissions.PermissionDefault;

public class SuperPermBufferPermission extends BufferPermission<Boolean> implements SuperPerm {

    public final String description;

    public SuperPermBufferPermission(String name, String description, boolean def) {
        super(name, def);
        this.description = description;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public PermissionDefault getPermissionDefault() {
        return this.def ? PermissionDefault.TRUE : PermissionDefault.OP;
    }

}
