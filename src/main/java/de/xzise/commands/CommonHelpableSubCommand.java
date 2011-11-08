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

public abstract class CommonHelpableSubCommand extends CommonSubCommand implements FullHelpable {

    protected CommonHelpableSubCommand(String... commands) {
        super(commands);
    }

    @Override
    public boolean listHelp(CommandSender sender) {
        return true;
    }

    @Override
    public boolean showInMain() {
        return true;
    }

    @Override
    public String[] permissionsNeeded() {
        return new String[0];
    }

    @Override
    public boolean register(HelpAPI api, Plugin plugin) {
        return api.registerCommand(this.getCommand(), this.getSmallHelpText(), this.getFullHelpText(), plugin, this.showInMain(), this.permissionsNeeded());
    }
}
