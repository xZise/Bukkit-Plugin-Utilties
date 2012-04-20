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

package de.xzise;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.bukkit.plugin.Plugin;

public class XLogger extends Logger {

    private final Logger logger;
    private final String pluginName;
    private final String version;

    public XLogger(String loggerName, String pluginName) {
        this(Logger.getLogger(loggerName), pluginName, "");
    }

    public XLogger(String pluginName) {
        this("Minecraft", pluginName);
    }

    private XLogger(Logger logger, String pluginName, String version) {
        super(logger.getName(), logger.getResourceBundleName());
        this.logger = logger;
        this.pluginName = pluginName;
        this.version = version;
    }

    public XLogger(Plugin plugin) {
        this(plugin.getServer().getLogger(), plugin.getDescription().getName(), plugin.getDescription().getVersion());
    }

    private String formatMessage(String message) {
        return "[" + this.pluginName + "]: " + message;
    }

//    public void fine(String msg) {
//        this.log(Level.FINE, msg);
//    }
//
//    public void info(String msg) {
//        this.log(Level.INFO, msg);
//    }
//
//    public void warning(String msg) {
//        this.log(Level.WARNING, msg);
//    }
//
    public void warning(String msg, Throwable exception) {
        this.log(Level.WARNING, msg, exception);
    }
//
//    public void severe(String msg) {
//        this.log(Level.SEVERE, msg);
//    }
//
    public void severe(String msg, Throwable exception) {
        this.log(Level.SEVERE, msg, exception);
    }
//
//    public void log(Level level, String msg, Throwable exception) {
//        this.logger.log(level, this.formatMessage(msg), exception);
//    }
//
//    public void log(Level level, String msg) {
//        this.logger.log(level, this.formatMessage(msg));
//    }

    public void logVariable(Level level, String name, Object value) {
        this.log(level, "Variable '" + name + "' = " + (value == null ? "null" : value.toString()));
    }

    /**
     * Prints out a version message with the text {@code enabled}.
     * 
     * @see XLogger#versionMsg(String)
     */
    public void enableMsg() {
        this.versionMsg("enabled");
    }

    /**
     * Prints out a version message with the text {@code disabled}.
     * 
     * @see XLogger#versionMsg(String)
     */
    public void disableMsg() {
        this.versionMsg("disabled");
    }

    /**
     * Prints out a version message with the text {@code loaded}.
     * 
     * @see XLogger#versionMsg(String)
     */
    public void loadMsg() {
        this.versionMsg("loaded");
    }

    /**
     * Prints out a message with the level {@link Level#INFO} and following
     * format: <blockquote>{@code <plugin> <version> <msg>}</blockquote>
     * 
     * @param msg
     *            Message of the log info.
     */
    public void versionMsg(String msg) {
        this.info(this.pluginName + (MinecraftUtil.isSet(this.version) ? " " : "") + this.version + (MinecraftUtil.isSet(msg) ? " " + msg : ""));
    }

    @Override
    public void log(LogRecord record) {
        record.setMessage(this.formatMessage(record.getMessage()));
        this.logger.log(record);
    }
}
