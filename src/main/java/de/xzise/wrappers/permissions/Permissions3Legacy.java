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

package de.xzise.wrappers.permissions;

import com.nijiko.permissions.Entry;
import com.nijiko.permissions.Entry.IntegerInfoVisitor;
import com.nijiko.permissions.Entry.DoubleInfoVisitor;
import com.nijiko.permissions.Entry.StringInfoVisitor;
import com.nijiko.permissions.Entry.EntryVisitor;

import de.xzise.XLogger;

public class Permissions3Legacy {
    
    public static EntryVisitor<Integer> getIntVisitor(String name, XLogger logger) {
        try {
            return new IntegerInfoVisitor(name);
        } catch (NoClassDefFoundError e) {
            logger.info("You are maybe using a outdated version of Permissions.");
            return new IntVisitor(name);
        }
    }
    
    public static EntryVisitor<Double> getDoubleVisitor(String name, XLogger logger) {
        try {
            return new DoubleInfoVisitor(name);
        } catch (NoClassDefFoundError e) {
            logger.info("You are maybe using a outdated version of Permissions.");
            return new DoubleVisitor(name);
        }
    }
    
    public static EntryVisitor<String> getStringVisitor(String name, XLogger logger) {
        try {
            return new StringInfoVisitor(name);
        } catch (NoClassDefFoundError e) {
            logger.info("You are maybe using a outdated version of Permissions.");
            return new StringVisitor(name);
        }
    }
    
    /**
     * A visitor class, to get the raw integer value.
     * 
     * Maybe exposed soon in Permissions 3. Until then use this one.
     */
    private static final class IntVisitor implements EntryVisitor<Integer> {

        private final String name;
        
        public IntVisitor(final String name) {
            this.name = name;
        }
        
        @Override
        public Integer value(Entry entry) {
            return entry.getRawInt(this.name);
        }
        
    }

    /**
     * A visitor class, to get the raw double value.
     * 
     * Maybe exposed soon in Permissions 3. Until then use this one.
     */
    private static final class DoubleVisitor implements EntryVisitor<Double> {

        private final String name;
        
        public DoubleVisitor(final String name) {
            this.name = name;
        }
        
        @Override
        public Double value(Entry entry) {
            return entry.getRawDouble(this.name);
        }
        
    }

    /**
     * A visitor class, to get the raw double value.
     * 
     * Maybe exposed soon in Permissions 3. Until then use this one.
     */
    private static final class StringVisitor implements EntryVisitor<String> {

        private final String name;
        
        public StringVisitor(final String name) {
            this.name = name;
        }
        
        @Override
        public String value(Entry entry) {
            return entry.getRawString(this.name);
        }
        
    }
}
