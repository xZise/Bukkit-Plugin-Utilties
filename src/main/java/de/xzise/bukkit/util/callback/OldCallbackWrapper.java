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

package de.xzise.bukkit.util.callback;

/**
 * <p>
 * <strong>Do not use it externally!</strong>
 * </p>
 * <p>
 * This wrapper will be removed with the {@link de.xzise.Callback} interface.
 * </p>
 * 
 * @deprecated You may should use {@link Callback}!
 */
@Deprecated
public class OldCallbackWrapper<R, P> implements Callback<R, P> {

    private final de.xzise.Callback<R, ? super P> c;

    public OldCallbackWrapper(final de.xzise.Callback<R, ? super P> c) {
        this.c = c;
    }

    @Override
    public R call(P parameter) {
        return this.c.call(parameter);
    }

}
