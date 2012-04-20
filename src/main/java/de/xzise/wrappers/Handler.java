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

import java.util.HashMap;
import java.util.Map;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;

import de.xzise.XLogger;
import de.xzise.bukkit.util.wrappers.InvalidServiceException;
import de.xzise.bukkit.util.wrappers.WrapperFactory;

public class Handler<W extends Wrapper> {

    private final Map<String, ? extends WrapperFactory<W, Plugin>> factories;
    private final Map<String, ? extends WrapperFactory<W, RegisteredServiceProvider<?>>> serviceWrapperFactories;
    private final PluginManager pluginManager;
    private final ServicesManager servicesManager;
    protected final XLogger logger;
    private final String type;
    private final W nullary;
    private String pluginName;
    private W wrapper;

    // Plugin - Nullary
    public Handler(final Map<String, ? extends WrapperFactory<W, Plugin>> factories, final PluginManager pluginManager, final String type, final String plugin, final XLogger logger) {
        // Plugin + Nullary
        this(factories, null, pluginManager, type, plugin, logger);
    }

    // Plugin + Nullary
    public Handler(final Map<String, ? extends WrapperFactory<W, Plugin>> factories, final W nullaryWrapper, final PluginManager pluginManager, String type, String plugin, XLogger logger) {
        // Both + Nullary
        this(factories, new HashMap<String, WrapperFactory<W, RegisteredServiceProvider<?>>>(0), nullaryWrapper, pluginManager, null, type, plugin, logger);
    }

    // Service - Nullary
    public Handler(final Map<String, ? extends WrapperFactory<W, RegisteredServiceProvider<?>>> factories, final PluginManager pluginManager, final ServicesManager servicesManager, final String type, final String plugin, final XLogger logger) {
        // Service + Nullary
        this(factories, null, pluginManager, servicesManager, type, plugin, logger);
    }

    // Service + Nullary
    public Handler(final Map<String, ? extends WrapperFactory<W, RegisteredServiceProvider<?>>> factories, final W nullaryWrapper, final PluginManager pluginManager, final ServicesManager servicesManager, final String type, final String plugin, final XLogger logger) {
        // Both + Nullary
        this(new HashMap<String, WrapperFactory<W, Plugin>>(0), factories, nullaryWrapper, pluginManager, servicesManager, type, plugin, logger);
    }

    // Both - Nullary
    public Handler(final Map<String, ? extends WrapperFactory<W, Plugin>> factories, final Map<String, ? extends WrapperFactory<W, RegisteredServiceProvider<?>>> serviceWrapperFactories, final PluginManager pluginManager, final ServicesManager servicesManager, final String type, final String plugin, final XLogger logger) {
        // Both + Nullary
        this(factories, serviceWrapperFactories, null, pluginManager, servicesManager, type, plugin, logger);
    }

    // Both + Nullary
    public Handler(final Map<String, ? extends WrapperFactory<W, Plugin>> factories, final Map<String, ? extends WrapperFactory<W, RegisteredServiceProvider<?>>> serviceWrapperFactories, final W nullaryWrapper, final PluginManager pluginManager, final ServicesManager servicesManager, final String type, final String plugin, final XLogger logger) {
        this.factories = factories;
        this.serviceWrapperFactories = serviceWrapperFactories;
        this.pluginManager = pluginManager;
        this.servicesManager = servicesManager;
        this.logger = logger;
        this.type = type;
        this.nullary = nullaryWrapper;
        this.pluginName = plugin;
    }

    public void setPluginName(String name) {
        this.pluginName = name;
    }

    public boolean isActive() {
        return this.wrapper != null && this.wrapper != this.nullary;
    }

    public String getWrapperName() {
        if (this.isActive()) {
            return this.wrapper.getPlugin().getDescription().getFullName();
        } else if (this.pluginManager == null && this.servicesManager == null) {
            return "Deactivated";
        } else {
            return "Not linked (yet)";
        }
    }

    public W getWrapper() {
        return this.wrapper == null ? this.nullary : this.wrapper;
    }

    public void load() {
        this.wrapper = null;
        boolean loaded = this.customLoad();
        if (loaded) {
            if (this.wrapper == null) {
                this.logger.warning("Invalid " + this.type + " system found.");
            } else {
                String pluginName = "Unknown";
                if (this.wrapper.getPlugin() != null) {
                    pluginName = this.wrapper.getPlugin().getDescription().getFullName();
                }
                this.loaded();
                this.logger.info("Linked with " + this.type + " system: " + pluginName);
            }
        } else {
            if (this.pluginManager != null) {
                for (String string : this.factories.keySet()) {
                    this.load(this.pluginManager.getPlugin(string));
                    if (this.wrapper != null) {
                        return;
                    }
                }
                if (this.servicesManager != null) {
                    for (String string : this.serviceWrapperFactories.keySet()) {
                        final Plugin plugin = this.pluginManager.getPlugin(string);
                        for (RegisteredServiceProvider<?> serviceProvider : this.servicesManager.getRegistrations(plugin)) {
                            this.load(serviceProvider);
                            if (this.wrapper != null) {
                                return;
                            }
                        }
                    }
                }
            }
            if (this.wrapper == null) {
                if (this.pluginName == null) {
                    this.logger.info("Loaded no " + this.type + " system, because it is deactivated.");
                } else {
                    this.logger.info("No " + this.type + " system found until here. A " + this.type + " plugin will be maybe activated later.");
                }
            }
        }
    }

    protected void loaded() {
    }

    protected void setWrapper(W wrapper) {
        this.wrapper = wrapper;
    }

    protected boolean customLoad(Plugin plugin) {
        return false;
    }

    protected boolean customLoad(RegisteredServiceProvider<?> provider) {
        return false;
    }

    protected boolean customLoad() {
        return false;
    }

    private abstract class Loader<T> {
        protected final T t;
        protected final Handler<W> handler;

        public Loader(final T t, final Handler<W> handler) {
            this.t = t;
            this.handler = handler;
        }

        public abstract boolean customLoad();

        public boolean canCreate() {
            return this.t != null && this.getFactory() != null;
        }

        public W create(final XLogger logger) throws InvalidWrapperException, InvalidServiceException {
            final WrapperFactory<W, T> factory = this.getFactory();
            if (factory == null) {
                return null;
            } else {
                return factory.create(this.t, logger);
            }
        }

        protected abstract Plugin getPlugin();

        public abstract WrapperFactory<W, T> getFactory();

        public final String getName() {
            return this.getPlugin().getDescription().getName();
        }

        public String getFullName() {
            return this.getPlugin().getDescription().getFullName();
        }
    }

    private class PluginLoader extends Loader<Plugin> {

        public PluginLoader(final Plugin plugin, final Handler<W> handler) {
            super(plugin, handler);
        }

        @Override
        public boolean customLoad() {
            return this.handler.customLoad(this.t);
        }

        @Override
        public WrapperFactory<W, Plugin> getFactory() {
            return this.handler.getPluginFactory(this.getName());
        }

        @Override
        protected Plugin getPlugin() {
            return this.t;
        }
    }

    private class ServiceLoader extends Loader<RegisteredServiceProvider<?>> {

        public ServiceLoader(final RegisteredServiceProvider<?> serviceProvider, final Handler<W> handler) {
            super(serviceProvider, handler);
        }

        @Override
        public boolean customLoad() {
            return this.handler.customLoad(this.t);
        }

        @Override
        public WrapperFactory<W, RegisteredServiceProvider<?>> getFactory() {
            return this.handler.getServiceFactory(this.getName());
        }

        @Override
        protected Plugin getPlugin() {
            return this.t.getPlugin();
        }

        @Override
        public W create(XLogger logger) throws InvalidWrapperException, InvalidServiceException {
            final W wrapper = super.create(logger);
            if (wrapper != null) {
                return wrapper;
            } else {
                throw new InvalidServiceException();
            }
        }
    }

    public WrapperFactory<W, Plugin> getPluginFactory(final String name) {
        return this.factories.get(name);
    }

    public WrapperFactory<W, RegisteredServiceProvider<?>> getServiceFactory(final String name) {
        return this.serviceWrapperFactories.get(name);
    }

    public <T> void load(final Loader<T> loader) {
        if (loader != null && loader.canCreate() && this.wrapper == null && this.pluginName != null) {
            if (this.pluginName.isEmpty() || (loader.getName().equalsIgnoreCase(this.pluginName))) {
                boolean loaded = loader.customLoad();

                if (!loaded) {
                    if (loader.getPlugin().isEnabled()) {
                        try {
                            this.wrapper = loader.create(this.logger);
                            loaded = true;
                        } catch (InvalidServiceException e) {
                            // Nothing tooo bad here...
                            loaded = false;
                            this.wrapper = null;
                        } catch (InvalidWrapperException e) {
                            this.logger.warning("Error while loading the plugin " + loader.getFullName() + " into " + this.type + " system.");
                            this.logger.warning("Error message: " + e.getMessage());
                            this.wrapper = null;
                        } catch (Throwable e) {
                            this.logger.warning("Unspecified error while loading the plugin " + loader.getFullName() + " into " + this.type + " system.");
                            this.logger.warning("Error message: '" + e.getMessage() + "' of '" + e.getClass().getSimpleName() + "'");
                            this.wrapper = null;
                        }
                    } else {
                        this.logger.warning("Skiped disabled " + this.type + " system: " + loader.getFullName());
                    }
                }

                if (loaded) {
                    if (this.wrapper == null) {
                        this.logger.warning("Invalid " + this.type + " system found: " + loader.getFullName());
                    } else {
                        this.loaded();
                        this.logger.info("Linked with " + this.type + " system: " + loader.getFullName());
                    }
                }
            }
        }
    }

    public void load(RegisteredServiceProvider<?> provider) {
        this.load(new ServiceLoader(provider, this));
    }

    public void load(Plugin plugin) {
        this.load(new PluginLoader(plugin, this));
    }

    private boolean unload(final boolean unload) {
        if (unload) {
            this.wrapper = null;
            this.logger.info("Deactivated " + this.type + " system.");
            return true;
        } else {
            return false;
        }
    }

    public boolean unload(Plugin plugin) {
        return this.unload(this.wrapper != null && plugin == this.wrapper.getPlugin());
    }

    public boolean unload(RegisteredServiceProvider<?> provider) {
        return this.unload(this.wrapper != null && provider != null && provider.getPlugin() == this.wrapper.getPlugin());
    }

    public void reload(Plugin plugin) {
        if (this.unload(plugin)) {
            this.load();
        }
    }

    public void reload(RegisteredServiceProvider<?> provider) {
        if (this.unload(provider)) {
            this.load();
        }
    }
}
