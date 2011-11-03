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

package de.xzise;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;

import de.xzise.metainterfaces.ConsoleCommandWrapper;
import de.xzise.metainterfaces.LinesCountable;
import de.xzise.metainterfaces.Nameable;
import de.xzise.metainterfaces.CommandSenderWrapper;
import de.xzise.wrappers.permissions.Permission;
import de.xzise.wrappers.permissions.SuperPerm;

public final class MinecraftUtil {

    public static final int PLAYER_LINES_COUNT = 10;
    /**
     * @deprecated Use {@link #PLAYER_LINES_COUNT} instead.
     */
    public static final int MAX_LINES_VISIBLE = PLAYER_LINES_COUNT;
    public static final int CONSOLE_LINES_COUNT = 30;

    private MinecraftUtil() {
    }

    /*
     * Minecraft specific
     */

    public static int getMaximumLines(CommandSender sender) {
        if (sender instanceof ConsoleCommandSender) {
            return CONSOLE_LINES_COUNT;
        } else if (sender instanceof LinesCountable) {
            return ((LinesCountable) sender).getMaxLinesVisible();
        } else {
            return PLAYER_LINES_COUNT;
        }
    }

    public static Player getPlayer(CommandSender sender) {
        if (sender instanceof Player) {
            return (Player) sender;
        } else if (sender instanceof CommandSenderWrapper<?>) {
            return MinecraftUtil.getPlayer(((CommandSenderWrapper<?>) sender).getSender());
        } else {
            return null;
        }
    }

    /**
     * Returns the name to a sender. If the sender has no player it returns
     * null.
     * 
     * @param sender
     *            The given sender.
     * @return Returns the name of the sender and null if the sender is no
     *         player.
     */
    public static String getPlayerName(CommandSender sender) {
        Player p = MinecraftUtil.getPlayer(sender);
        if (p != null) {
            return p.getName();
        } else {
            return null;
        }
    }

    /**
     * Returns a name in each case.
     * <ul>
     * <li>If the sender is a player it return the player name.</li>
     * <li>If the sender is the console it will return <code>[CONSOLE]</code>.</li>
     * <li>If the sender is Nameable it return this name.</li>
     * <li>In all other cases it return <code>Somebody</code>.</li>
     * </ul>
     * 
     * @param sender
     *            The name of this sender will be determined.
     * @return The name of the sender.
     */
    public static String getName(CommandSender sender) {
        return getName(sender, "Somebody");
    }

    /**
     * Returns a name in each case.
     * <ul>
     * <li>If the sender is a player it return the player name.</li>
     * <li>If the sender is the console it will return <code>[CONSOLE]</code>.</li>
     * <li>If the sender is Nameable it return this name.</li>
     * <li>In all other cases it will return the value of <code>somebody</code>.
     * </li>
     * </ul>
     * 
     * @param sender
     *            The name of this sender will be determined.
     * @param somebody
     *            The name of the sender, if it isn't a player, nameable and no
     *            console.
     * @return The name of the sender.
     */
    public static String getName(CommandSender sender, String somebody) {
        return getName(sender, ConsoleCommandWrapper.NAME, somebody);
    }

    /**
     * Returns a name in each case.
     * <ul>
     * <li>If the sender is a player it return the player name.</li>
     * <li>If the sender is the console it will return the value of
     * <code>console</code>.</li>
     * <li>If the sender is Nameable it return this name.</li>
     * <li>In all other cases it will return the value of <code>somebody</code>.
     * </li>
     * </ul>
     * 
     * @param sender
     *            The name of this sender will be determined.
     * @param console
     *            The name of the sender, if it is a console.
     * @param somebody
     *            The name of the sender, if it isn't a player, nameable and no
     *            console.
     * @return The name of the sender.
     */
    public static String getName(CommandSender sender, String console, String somebody) {
        String name = MinecraftUtil.getPlayerName(sender);
        if (name == null) {
            if (sender instanceof ConsoleCommandSender) {
                return console;
            } else if (sender instanceof Nameable) {
                return ((Nameable) sender).getName();
            } else {
                return somebody;
            }
        } else {
            return name;
        }
    }

    /**
     * Expands the name, so it match a player (if possible).
     * 
     * @param name
     *            The primitive name.
     * @param server
     *            The server where the player is searched.
     * @return The name of a player on the server, if the name matches anything.
     *         Otherwise the inputed name.
     * @see Uses {@link Server#getPlayer(String)} to determine the player object
     *      to the name.
     */
    public static String expandName(String name, Server server) {
        Player player = server.getPlayer(name);
        return player == null ? name : player.getName();
    }

    /**
     * Expands the name, so it match a player (if possible).
     * 
     * @param name
     *            The primitive name.
     * @return The name of a player on the server, if the name matches anything.
     *         Otherwise the inputed name.
     * @see Call {@link #expandName(String, Server)} where the server is {@link Bukkit#getServer()}.
     */
    public static String expandName(String name) {
        return expandName(name, Bukkit.getServer());
    }

    public static <T> T cast(Class<T> clazz, Object o, T def) {
        try {
            return clazz.cast(o);
        } catch (ClassCastException e) {
            return def;
        }
    }
    
    public static <T> T cast(Class<T> clazz, Object o) {
        return cast(clazz, o, null);
    }

    //TODO: Support logger
    public static void register(PluginManager pluginManager, XLogger logger, SuperPerm... perms) {
        register(pluginManager, logger, new SuperPerm[][] { perms });
    }

    //TODO: Support logger
    public static void register(PluginManager pluginManager, XLogger logger, SuperPerm[]... perms) {
        try {
            for (SuperPerm[] superPerms : perms) {
                for (SuperPerm superPerm : superPerms) {
                    try {
                        pluginManager.addPermission(new org.bukkit.permissions.Permission(superPerm.getName(), superPerm.getDescription(), superPerm.getPermissionDefault()));
                    } catch (IllegalArgumentException e) {
                        logger.warning("Couldn't register the permission '" + superPerm.getName() + "'!", e);
                    }
                }
            }
        } catch (Exception e) {
            logger.warning("SuperPerms not found, didn't register permissions.", e);
        }
    }

    public static <T> ImmutableSet<Permission<T>> getByDefault(T def, Permission<T>... permissions) {
        Builder<Permission<T>> builder = ImmutableSet.builder();
        for (Permission<T> permission : permissions) {
            if (equals(permission.getDefault(), def)) {
                builder.add(permission);
            }
        }
        return builder.build();
    }

    public static boolean equals(Object o, Object p) {
        return o == null ? p == null : o.equals(p);
    }
    
    public static String[] parseLine(String line) {
        return MinecraftUtil.parseLine(line, ' ');
    }

    /**
     * Parses a string line using quoting and escaping. It will split the line
     * where a space is, but ignores quoted or escaped spaces.
     * 
     * Examples: <blockquote>
     * <table>
     * <tr>
     * <th>Input</th>
     * <th>Output</th>
     * </tr>
     * <tr>
     * <td>Hello World</td>
     * <td><tt>{ Hello, World }</tt></td>
     * </tr>
     * <tr>
     * <td>"Hello World"</td>
     * <td><tt>{ Hello World }</tt></td>
     * </tr>
     * <tr>
     * <td>Hello\ World</td>
     * <td><tt>{ Hello World }</tt></td>
     * </tr>
     * <tr>
     * <td>"Hello World \"Bukkit\""</td>
     * <td><tt>{ Hello World "Bukkit" }</tt></td>
     * </tr>
     * </table>
     * </blockquote>
     * 
     * <b>Notice</b>: This method ignores illegal escapes.
     * 
     * @param line
     *            The command line.
     * @return The parsed segments.
     */
    public static String[] parseLine(String line, char delimiter) {
        boolean quoted = false;
        boolean escaped = false;
        int lastStart = 0;
        char[] word = new char[line.length()];
        int wordIndex = 0;
        List<String> values = new ArrayList<String>(2);
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (escaped) {
                word[wordIndex] = c;
                wordIndex++;
                escaped = false;
            } else {
                switch (c) {
                case '"':
                    quoted = !quoted;
                    break;
                case '\\':
                    escaped = true;
                    break;
                default:
                    if (delimiter == c && !quoted) {
                        if (lastStart < i) {
                            values.add(String.copyValueOf(word, 0, wordIndex));
                            word = new char[line.length() - i];
                            wordIndex = 0;
                        }
                        lastStart = i + 1;
                    } else {
                        word[wordIndex] = c;
                        wordIndex++;
                    }
                    break;
                }
            }
        }
        if (wordIndex > 0) {
            values.add(String.copyValueOf(word, 0, wordIndex));
        }
        return values.toArray(new String[0]);
    }

    /*
     * Java specific
     */

    public static <T> T[] concat(T t, T... ts) {
        int newLength = ts.length + 1;
        @SuppressWarnings("unchecked")
        T[] completed = ((Object) ts.getClass() == (Object) Object[].class) ? (T[]) new Object[newLength] : (T[]) Array.newInstance(ts.getClass().getComponentType(), newLength);
        completed[0] = t;
        for (int i = 0; i < ts.length; i++) {
            completed[i + 1] = ts[i];
        }
        return completed;
    }

    /**
     * Checks if an object is set. Set mean at least “not null”. Following
     * objects will be checked separate:
     * 
     * <blockquote>
     * <table>
     * <tr>
     * <th>Type</th>
     * <th>Tests</th>
     * </tr>
     * <tr>
     * <td>String</td>
     * <td>not <tt>{@link String#isEmpty()}</tt></td>
     * </tr>
     * <tr>
     * <td>Collection&lt;?&gt;</td>
     * <td>not <tt>{@link Collection#isEmpty()}</tt></td>
     * </tr>
     * <tr>
     * <td>Map&lt;?, ?&gt;</td>
     * <td>not <tt>{@link Map#isEmpty()}</tt></td>
     * </tr>
     * <tr>
     * <td>Any array</td>
     * <td>Arraylength is positive</td>
     * </tr>
     * </table>
     * </blockquote>
     * 
     * @param o
     *            The tested object.
     * @return If the object is not empty.
     */
    public static boolean isSet(Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof String) {
            return !((String) o).isEmpty();
        } else if (o instanceof Collection<?>) {
            return !((Collection<?>) o).isEmpty();
        } else if (o instanceof Map<?, ?>) {
            return !((Map<?, ?>) o).isEmpty();
        } else if (o.getClass().isArray()) {
            return java.lang.reflect.Array.getLength(o) > 0;
        } else {
            return true;
        }
    }

    public static String getOrdinal(int value) {
        String ordinal = "";
        if ((value % 100) / 10 == 1) {
            ordinal = "th";
        } else {
            switch (value % 10) {
            case (1):
                ordinal = "st";
                break;
            case (2):
                ordinal = "nd";
                break;
            case (3):
                ordinal = "rd";
                break;
            default:
                ordinal = "th";
            }
        }
        return value + ordinal;
    }

    public static String scramble(String word) {
        Random rand = new Random();
        char[] input = word.toCharArray();
        char[] result = new char[word.length()];
        boolean[] used = new boolean[word.length()];
        int i = 0;
        int length = word.length();
        while (i < length) {
            int newIdx = rand.nextInt(length);
            if (!used[newIdx]) {
                used[newIdx] = true;
                result[newIdx] = input[i];
                i++;
            }
        }
        return new String(result);
    }

    public static int getWidth(int number, int base) {
        int width = 1;
        while (number >= base) {
            number /= base;
            width++;
        }
        return width;
    }

    public static <T> boolean toogleEntry(T entry, List<T> list) {
        if (list.remove(entry)) {
            return false;
        } else {
            list.add(entry);
            return true;
        }
    }

    /**
     * Tries to convert a string into an integer. If the string is invalid it
     * returns <code>null</code>.
     * 
     * @param string
     *            The string to be parsed.
     * @return The value if the string is valid, otherwise <code>null</code>.
     */
    public static Integer tryAndGetInteger(String string) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Tries to convert a string into a short. If the string is invalid it
     * returns <code>null</code>.
     * 
     * @param string
     *            The string to be parsed.
     * @return The value if the string is valid, otherwise <code>null</code>.
     */
    public static Short tryAndGetShort(String string) {
        try {
            return Short.parseShort(string);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Tries to convert a string into a byte. If the string is invalid it
     * returns <code>null</code>.
     * 
     * @param string
     *            The string to be parsed.
     * @return The value if the string is valid, otherwise <code>null</code>.
     */
    public static Byte tryAndGetByte(String string) {
        try {
            return Byte.parseByte(string);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    /**
     * Tries to convert a string into a short. If the string is invalid it
     * returns <code>null</code>.
     * 
     * @param string
     *            The string to be parsed.
     * @return The value if the string is valid, otherwise <code>null</code>.
     */
    public static Double tryAndGetDouble(String string) {
        try {
            return Double.parseDouble(string);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    public static long trunc(double value) {
        return (long) value;
    }
    
    public static double getDecimalPlaces(double value) {
        return Math.abs(value - trunc(value));
    }

    /**
     * Tests where the first object is inside the array.
     * 
     * @param o
     *            Searched object.
     * @param a
     *            Searched array.
     * @return the first position found.
     */
    public static <T> int indexOf(T o, T[] a) {
        int idx = 0;
        for (T t : a) {
            if (t != null && t.equals(o)) {
                return idx;
            }
            idx++;
        }
        return -1;
    }

    public static <T> boolean contains(T o, T[] a) {
        return MinecraftUtil.indexOf(o, a) >= 0;
    }

    /**
     * Returns a random value of the list. If the list is not set (
     * {@link #isSet(Object)}) it will return null.
     * 
     * @param list
     *            The given list.
     * @return a random element of the list.
     */
    public static <T> T getRandom(List<T> list) {
        if (MinecraftUtil.isSet(list)) {
            return list.get(new Random().nextInt(list.size()));
        } else {
            return null;
        }
    }

    /**
     * Returns a random value of the array. If the array is not set (
     * {@link #isSet(Object)}) it will return null.
     * 
     * @param list
     *            The given list.
     * @return a random element of the list.
     */
    public static <T> T getRandom(T[] array) {
        if (MinecraftUtil.isSet(array)) {
            return array[new Random().nextInt(array.length)];
        } else {
            return null;
        }
    }

    public static void copyFile(File source, File destination) throws IOException {
        FileReader in = new FileReader(source);
        if (!destination.exists()) {
            destination.createNewFile();
        }
        FileWriter out = new FileWriter(destination);
        int c;

        while ((c = in.read()) != -1)
            out.write(c);

        in.close();
        out.close();
    }

    public static void copy(File fromFile, File toFile) throws IOException {

        // if (!fromFile.exists())
        // throw new IOException("FileCopy: " + "no such source file: "
        // + fromFileName);
        // if (!fromFile.isFile())
        // throw new IOException("FileCopy: " + "can't copy directory: "
        // + fromFileName);
        // if (!fromFile.canRead())
        // throw new IOException("FileCopy: " + "source file is unreadable: "
        // + fromFileName);
        //
        // if (toFile.isDirectory())
        // toFile = new File(toFile, fromFile.getName());
        //
        if (toFile.exists()) {
            if (!toFile.canWrite())
                throw new IOException("FileCopy: " + "destination file is unwriteable: " + toFile.getName());
        } else {
            File parent = toFile.getParentFile();
            if (parent == null)
                parent = new File(System.getProperty("user.dir"));
            if (!parent.exists())
                throw new IOException("FileCopy: " + "destination directory doesn't exist: " + parent);
            if (parent.isFile())
                throw new IOException("FileCopy: " + "destination is not a directory: " + parent);
            if (!parent.canWrite())
                throw new IOException("FileCopy: " + "destination directory is unwriteable: " + parent);
        }

        FileInputStream from = null;
        FileOutputStream to = null;
        try {
            from = new FileInputStream(fromFile);
            to = new FileOutputStream(toFile);
            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = from.read(buffer)) != -1)
                to.write(buffer, 0, bytesRead); // write
        } finally {
            if (from != null)
                try {
                    from.close();
                } catch (IOException e) {
                    ;
                }
            if (to != null)
                try {
                    to.close();
                } catch (IOException e) {
                    ;
                }
        }
    }

    public static boolean isInteger(String string) {
        return MinecraftUtil.tryAndGetInteger(string) != null;
    }

    public static <T> T[] subArray(T[] t, int start, int len) {
        if (start == 0) {
            return t.clone();
        } else if (start < 0 || start >= t.length) {
            throw new IndexOutOfBoundsException("Start index is out of the bount (" + start + ").");
        } else {
            @SuppressWarnings("unchecked")
            T[] newArray = (T[]) new Object[len];

            System.arraycopy(t, start, newArray, 0, Math.min(t.length, newArray.length));
            
            return newArray;
        }
    }
    
    public static <T> T[] subArray(T[] t, int start) {
        return subArray(t, start, t.length - start);
    }

    /**
     * Creates an map, mapping an identifier to all values of an enum.
     * @param enumClass The class of the enum.
     * @param keys The callback class defining a name for each enum.
     * @return A map mapping an identifier to an enum.
     * @deprecated Use {@link #createReverseEnumMap(Class, Callback)} instead.
     */
    @Deprecated
    public static <K, V extends Enum<?>> Map<K, V> createEnumMap(Class<V> enumClass, Callback<K, ? super V> keys) {
        return createReverseEnumMap(enumClass, keys);
    }

    /**
     * Creates an map, mapping an identifier to all values of an enum.
     * @param enumClass The class of the enum.
     * @param keys The callback class defining a name for each enum.
     * @return A map mapping an identifier to an enum.
     */
    public static <K, V extends Enum<?>> ImmutableMap<K, V> createReverseEnumMap(Class<V> enumClass, Callback<K, ? super V> keys) {
        com.google.common.collect.ImmutableMap.Builder<K, V> builder = ImmutableMap.builder();
        
        for (V enumValue : enumClass.getEnumConstants()) {
            builder.put(keys.call(enumValue), enumValue);
        }
        return builder.build();
    }

    public static <K, V extends Enum<?>> Map<K, V> createReverseMultiEnumMap(Class<V> enumClass, Callback<K[], ? super V> keys) {
        Map<K, V> m = new HashMap<K, V>();
        
        for (V enumValue : enumClass.getEnumConstants()) {
            for (K key : keys.call(enumValue)) {
                m.put(key, enumValue);
            }
        }
        return m;
    }

    /* 
     * Shorten List/Set/Map constructors (without generic type duplication).
     * This is basically inspired by Google Guava.
     * 
     * See also: http://code.google.com/p/guava-libraries/source/browse/trunk/guava/src/com/google/common/collect/Lists.java
     */
    public static <K, V> HashMap<K, V> createHashMap() {
        return new HashMap<K, V>();
    }

    public static <K extends Enum<K>, V> EnumMap<K, V> createEnumMap(Class<K> keyType) {
        return new EnumMap<K, V>(keyType);
    }
    
    public static <K extends Enum<K>, V> EnumMap<K, V> createEnumMap(Map<K, V> map, Class<K> keyType) {
        if (map == null) {
            return new EnumMap<K, V>(keyType);
        } else {
            return new EnumMap<K, V>(map);
        }
    }
}
