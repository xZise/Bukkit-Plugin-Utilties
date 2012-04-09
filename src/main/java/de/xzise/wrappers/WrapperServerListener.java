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

package de.xzise.wrappers;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import de.xzise.MinecraftUtil;

/**
 * <strong>Will be removed with final BPU 1.3.0!</strong>
 * @deprecated Use {@link de.xzise.bukkit.util.wrappers.WrapperServerListener} instead!
 */
@Deprecated
public class WrapperServerListener extends de.xzise.bukkit.util.wrappers.WrapperServerListener implements Listener {

    /**
     * Registers the listener. <strong>Not very useful anymore, and thus
     * deprecated!</strong>
     * 
     * @param plugin
     *            the plugin which owns the listener.
     * @param listener
     *            the plugin which is going to be registered
     * @deprecated <p>
     *             As it isn't needed to register for a specific event, the
     *             listener should be registered with
     *             {@link PluginManager#registerEvents(Listener, Plugin)}) or
     *             via
     *             {@link MinecraftUtil#registerMultipleListeners(Plugin, Listener...)}
     *             .
     *             </p>
     *             <p>
     *             This is the actual code: <blockquote>
     *             <code>plugin.getServer().getPluginManager().registerEvents(listener, plugin);</code>
     *             </blockquote>
     *             </p>
     */
    @Deprecated
    public static void registerEvents(Plugin plugin, Listener listener) {
        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
    }

    /**
     * Creates and registers default listener for plugin enable and disable.
     * 
     * @param plugin
     *            the plugin which owns the listener.
     * @param handlers
     *            the handlers which should be handled by this listener.
     */
    public static void createAndRegisterEvents(Plugin plugin, Handler<?>... handlers) {
        new de.xzise.bukkit.util.wrappers.WrapperServerListener(handlers).register(plugin);
    }

    public WrapperServerListener(Handler<?>... handlers) {
        super(handlers);
    }
}
