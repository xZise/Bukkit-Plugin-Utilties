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

package de.xzise.bukkit.util.wrappers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServiceRegisterEvent;
import org.bukkit.event.server.ServiceUnregisterEvent;
import org.bukkit.plugin.Plugin;

import de.xzise.wrappers.Handler;

public class WrapperServerListener {

    public static final class PluginListener implements Listener {

        private final WrapperServerListener serverListener;

        public PluginListener(final WrapperServerListener serverListener) {
            this.serverListener = serverListener;
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onPluginEnable(PluginEnableEvent event) {
            this.serverListener.onPluginEnable(event);
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onPluginDisable(PluginDisableEvent event) {
            this.serverListener.onPluginDisable(event);
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onServiceRegistration(ServiceRegisterEvent event) {
            this.serverListener.onServiceRegistration(event);
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onServiceUnregistration(ServiceUnregisterEvent event) {
            this.serverListener.onServiceUnregistration(event);
        }
    }

    private final Handler<?>[] handlers;
    private final PluginListener listener;

    /**
     * Creates and registers default listener for plugin enable and disable.
     * 
     * @param plugin
     *            the plugin which owns the listener.
     * @param handlers
     *            the handlers which should be handled by this listener.
     */
    public static void createAndRegisterEvents(Plugin plugin, Handler<?>... handlers) {
        new WrapperServerListener(handlers).register(plugin);
    }

    public WrapperServerListener(Handler<?>... handlers) {
        this.handlers = handlers;
        this.listener = new PluginListener(this);
    }

    public void onPluginEnable(PluginEnableEvent event) {
        for (Handler<?> handler : this.handlers) {
            handler.load(event.getPlugin());
        }
        System.out.println("p enable!");
    }

    public void onPluginDisable(PluginDisableEvent event) {
        for (Handler<?> handler : this.handlers) {
            handler.reload(event.getPlugin());
        }
    }

    public void onServiceRegistration(ServiceRegisterEvent event) {
        for (Handler<?> handler : this.handlers) {
            handler.load(event.getProvider());
        }
    }

    public void onServiceUnregistration(ServiceUnregisterEvent event) {
        for (Handler<?> handler : this.handlers) {
            handler.reload(event.getProvider());
        }
    }

    public void load() {
        for (Handler<?> handler : this.handlers) {
            handler.load();
        }
    }

    public void register(final Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this.listener, plugin);
    }
}
