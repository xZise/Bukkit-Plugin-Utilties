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

package de.xzise.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import de.xzise.wrappers.help.HelpAPI;

public interface SmallHelpable extends SubCommand {

    String getSmallHelpText();

    String getCommand();

    /**
     * Returns if the sender could use this command and so if list the command
     * in the help.
     * 
     * @param sender
     *            The user of the command.
     * @return If the user could access this command.
     */
    boolean listHelp(CommandSender sender);

    // Help API methods
    boolean showInMain();

    String[] permissionsNeeded();

    boolean register(HelpAPI api, Plugin plugin);
}
