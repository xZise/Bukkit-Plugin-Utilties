package de.xzise.wrappers;

import org.bukkit.event.Event;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListener;
import org.bukkit.plugin.Plugin;

public class WrapperServerListener extends ServerListener {

    private final Handler<?>[] handlers;

    public static void registerEvents(Plugin plugin, ServerListener listener) {
        plugin.getServer().getPluginManager().registerEvent(Event.Type.PLUGIN_ENABLE, listener, Event.Priority.Monitor, plugin);
        plugin.getServer().getPluginManager().registerEvent(Event.Type.PLUGIN_DISABLE, listener, Event.Priority.Monitor, plugin);
    }

    public static void createAndRegisterEvents(Plugin plugin, Handler<?>... handlers) {
        registerEvents(plugin, new WrapperServerListener(handlers));
    }

    public WrapperServerListener(Handler<?>... handlers) {
        this.handlers = handlers;
    }

    @Override
    public void onPluginEnable(PluginEnableEvent event) {
        for (Handler<?> handler : this.handlers) {
            handler.load(event.getPlugin());
        }
    }

    @Override
    public void onPluginDisable(PluginDisableEvent event) {
        for (Handler<?> handler : this.handlers) {
            handler.reload(event.getPlugin());
        }
    }
}
