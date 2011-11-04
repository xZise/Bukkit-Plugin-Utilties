/*
 * This file is part of Bukkit Plugin Utilities.
 * 
 * Bukkit Plugin Utilities is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 * 
 * Foobar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.xzise.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public abstract class CommonSubCommand implements SubCommand {

    protected final String[] commands;

    /**
     * Creates a subcommand.
     * 
     * @param commands
     *            The commands.
     * @throws IllegalArgumentException
     *             If commands is empty.
     */
    protected CommonSubCommand(String... commands) {
        this.commands = commands;
    }

    @Override
    public String[] getCommands() {
        return this.commands.clone();
    }

    @Override
    public final boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String[] params = new String[args.length + 1];
        params[0] = label;
        for (int i = 0; i < args.length; i++) {
            params[i + 1] = args[i];
        }
        return this.execute(sender, params);
    }
}
