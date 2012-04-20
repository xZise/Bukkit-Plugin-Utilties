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

/**
 * Simple callback interface. If you require version 1.3 use
 * {@link de.xzise.bukkit.util.callback.Callback} instead.
 * 
 * @since 1.0
 * @deprecated Use {@link de.xzise.bukkit.util.callback.Callback} instead.
 */
@Deprecated
public interface Callback<Result, Parameter> {
    Result call(Parameter parameter);
}