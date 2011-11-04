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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import de.xzise.collections.ArrayReferenceList;
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
    //TODO: Remove in BPU 2
    public static final int MAX_LINES_VISIBLE = PLAYER_LINES_COUNT;
    public static final int CONSOLE_LINES_COUNT = 30;

    /** Default tolerance */
    public static final double EPSILON = 0.0000001;

    /** Version information */
    private static final int[] VERSION = new int[] { 1, 3, 0 };
    public static final boolean OFFICAL = false;

    private MinecraftUtil() {
    }

    /*
     * BPU specific
     */

    /**
     * Returns the current version of BPU.
     * @return the current version of BPU.
     * @since 1.3
     */
    public static int[] getVersion() {
        return VERSION.clone();
    }

    /**
     * Returns if the currently using version of BPU needs to be updated to fit
     * the specifications of the plugin.
     * @param primary Primary number of version.
     * @param secondary Secondary number of version.
     * @return if BPU needs to be updated.
     * @since 1.3
     */
    public static boolean needUpdate(int primary, int secondary) {
        if (primary > VERSION[0]) {
            return true;
        } else if (primary == VERSION[0]) {
            return secondary > VERSION[1];
        } else {
            return false;
        }
    }

    /*
     * Minecraft specific
     */

    /**
     * Get maximum lines of the command sender.
     * <ul><li>{@link ConsoleCommandSender}: {@link #CONSOLE_LINES_COUNT}</li>
     * <li>{@link LinesCountable}: {@link LinesCountable#getMaxLinesVisible()}</li>
     * <li>Everything else (also players): {@link #PLAYER_LINES_COUNT}</li>
     * </ul>
     * @param sender tested sender.
     * @return maximum lines for the command sender.
     * @since 1.0
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

    /**
     * Get the online player instance for the offline player. If the player isn't
     * online it will return <code>null</code>.
     * @param player the offline player instance.
     * @return the online player instance.
     * @since 1.3
     */
    public static Player getFromOfflinePlayer(OfflinePlayer player) {
        return player.isOnline() ? Bukkit.getServer().getPlayerExact(player.getName()) : null;
    }

    /**
     * Returns the player instance for a command sender. If there is no way to
     * do this it will return <code>null</code>. It will unwrap the
     * {@link CommandSenderWrapper} chain.
     * @param sender the tested command sender instance.
     * @return the player instance.
     * @since 1.0
     */
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
     * Returns the name to a sender. If possible it will try to call
     * {@link CommandSender#getName()}. If the Bukkit version doesn't support
     * this call it try to interpret the sender as player with
     * {@link #getPlayer(CommandSender)}. If this returns <code>null</code> this
     * will return <code>null</code> otherwise the name of the player.
     * 
     * @param sender
     *            The given sender.
     * @return Returns the name of the sender and null if the sender is no
     *         player.
     * @since 1.0
     */
    public static String getPlayerName(CommandSender sender) {
        try {
            return sender.getName();
        } catch (NoSuchMethodError e) {
            Player p = MinecraftUtil.getPlayer(sender);
            if (p != null) {
                return p.getName();
            } else {
                return null;
            }
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
     * @since 1.0
     */
    public static String getName(CommandSender sender) {
        return getName(sender, "Somebody");
    }

    /**
     * Returns a name in each case.
     * <ul>
     * <li>If the sender is a player it return the player name.</li>
     * <li>If the sender is the console it will return {@link ConsoleCommandWrapper#NAME} (<code>[CONSOLE]</code>).</li>
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
     * @since 1.0
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
     * @since 1.0
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
     * @since 1.0
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
     * @see Call {@link #expandName(String, Server)} where the server is
     *      {@link Bukkit#getServer()}.
     * @since 1.0
     */
    public static String expandName(String name) {
        return expandName(name, Bukkit.getServer());
    }

    // Since 1.0
    // TODO: Support logger
    public static void register(PluginManager pluginManager, XLogger logger, SuperPerm... perms) {
        register(pluginManager, logger, new SuperPerm[][] { perms });
    }

    // Since 1.0
    // TODO: Support logger
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

    private static void generate(Set<org.bukkit.permissions.Permission> permissions, Set<String> children, String prefix, String name) {
        if (children.isEmpty()) {
            Map<String, Boolean> childrenMap = Maps.newHashMapWithExpectedSize(children.size());
            for (String child : children) {
                childrenMap.put(child, true);
            }
            permissions.add(new org.bukkit.permissions.Permission(prefix + (prefix.isEmpty() ? "" : ".") + name, childrenMap));
        }
    }

    public static org.bukkit.permissions.Permission[] createPermissionPools(SuperPerm[]... permissions) {
        class Node {

            private final Map<String, Node> nodes = Maps.newHashMap();
            private final String name;
            private final SuperPerm endNode;

            public Node(String name, SuperPerm endNode) {
                this.name = name;
                this.endNode = endNode;
            }

            public Node() {
                this("", null);
            }

            public Node putAndGet(String name) {
                return this.putAndGet(name, null);
            }

            public Node putAndGet(String name, SuperPerm endNode) {
                Node result = this.nodes.get(name);
                if (result == null) {
                    result = new Node(name, endNode);
                    this.nodes.put(name, result);
                }
                return result;
            }

            private void create(String prefix, Set<String> trueNodes, Set<String> nonOpNodes, Set<String> opNodes, Set<String> falseNodes, Set<String> allNodes, Set<org.bukkit.permissions.Permission> permissions) {
                Set<String> trueSubNodes = Sets.newHashSet();
                Set<String> nonOpSubNodes = Sets.newHashSet();
                Set<String> opSubNodes = Sets.newHashSet();
                Set<String> falseSubNodes = Sets.newHashSet();
                Set<String> allSubNodes = Sets.newHashSet();
                final String fullName = prefix + (prefix.isEmpty() ? "" : ".") + this.name;

                for (Node node : nodes.values()) {
                    node.create(fullName, trueSubNodes, nonOpSubNodes, opSubNodes, falseSubNodes, allSubNodes, permissions);
                }

                generate(permissions, trueSubNodes, fullName, "true");
                generate(permissions, nonOpSubNodes, fullName, "notop");
                generate(permissions, opSubNodes, fullName, "op");
                generate(permissions, falseSubNodes, fullName, "false");
                generate(permissions, allSubNodes, fullName, "all");

                if (this.endNode != null) {
                    switch (this.endNode.getPermissionDefault()) {
                    case TRUE :
                        trueNodes.add(fullName);
                        break;
                    case NOT_OP :
                        nonOpNodes.add(fullName);
                        break;
                    case OP :
                        opNodes.add(fullName);
                        break;
                    case FALSE :
                        falseNodes.add(fullName);
                        break;
                    }
                    allNodes.add(fullName);
                }
            }

            public org.bukkit.permissions.Permission[] create() {
                Set<org.bukkit.permissions.Permission> permissions = Sets.newHashSet();
                this.create("", new HashSet<String>(), new HashSet<String>(), new HashSet<String>(), new HashSet<String>(), new HashSet<String>(), permissions);
                return permissions.toArray(new org.bukkit.permissions.Permission[0]);
            }
        }

        final Node root = new Node();

        // Fill node map
        for (SuperPerm[] superPerms : permissions) {
            for (SuperPerm superPerm : superPerms) {
                String[] segments = superPerm.getName().split("\\.");
                Node parent = root;
                for (int i = 0; i < segments.length; i++) {
                    if (i == segments.length - 1) {
                        parent = parent.putAndGet(segments[i], superPerm);
                    } else {
                        parent = parent.putAndGet(segments[i]);
                    }
                }
            }
        }

        // Search groups
        return root.create();
    }

    /**
     * <p>
     * Test if the player has ever joined on the given worlds.
     * </p>
     * <p>
     * <b>Warning</b>: This method is using special behavior of
     * CraftBukkit/Minecraft.
     * </p>
     * 
     * @param name
     *            Name of the player.
     * @param worlds
     *            Worlds the player has to be. If there are no worlds set, it
     *            will test all worlds.
     * @return If the player has ever joined on the given worlds.
     */
    public static boolean playerHasJoined(String name, World... worlds) {
        if (worlds == null || worlds.length == 0) {
            worlds = Bukkit.getServer().getWorlds().toArray(new World[0]);
        }
        for (World world : worlds) {
            File playerDirectory = new File(world.getName(), "players");
            for (String playerName : playerDirectory.list()) {
                if (playerName.equals(name + ".dat")) {
                    return true;
                }
            }
        }
        return false;
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

    @SuppressWarnings("unchecked")
    public static <T> List<T> getOneElementList(T element) {
        return element == null ? Lists.<T>newArrayListWithCapacity(0) : Lists.newArrayList(element);
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
        return parseLine(line, delimiter, '"', '\\', null, null, false);
    }

    /**
     * Parses a string line using quoting and escaping. It will split the line
     * where a delimiter is, but ignores quoted or escaped delimiter.
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
     * @param delimiter
     *            The character which delimits the line.
     * @param quote
     *            The character which acts as quotes.
     * @param escape
     *            The character which acts as escape character.
     * @param trimQuotes
     *            If set to true, everything before and after the quote character will be removed. 
     * @return The parsed segments.
     */
    //TODO: Remove everything after the quote!
    public static String[] parseLine(String line, char delimiter, char quote, char escape, Character bracketStart, Character bracketEnd, boolean trimQuotes) {
        boolean quoted = false;
        boolean escaped = false;
        int bracketLevel = 0;
        int lastStart = 0;
        char[] word = new char[line.length()];
        int wordIndex = 0; // Word length
        int quotedWordLength = -1;
        int quotedLastStart = -1;
        List<String> values = new ArrayList<String>();
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (escaped) {
                word[wordIndex] = c;
                wordIndex++;
                escaped = false;
            } else {
                if (c == quote && bracketLevel <= 0) {
                    if (trimQuotes) {
                        if (quoted) {
                            quotedWordLength = wordIndex;
                        } else if (quotedLastStart < 0) {
                            quotedLastStart = wordIndex;
                        }
                    }
                    quoted = !quoted;
                } else if (c == escape) {
                    escaped = true;
                } else if (c == delimiter && !quoted && bracketLevel <= 0) {
                    if (lastStart < i) {
                        final int first = quotedLastStart < 0 ? 0 : quotedLastStart;
                        final int last = quotedWordLength < 0 ? wordIndex : (quotedWordLength - first);
                        values.add(String.copyValueOf(word, first, last));
                        word = new char[line.length() - i];
                        wordIndex = 0;
                        quotedWordLength = -1;
                    }
                    lastStart = i + 1;
                    quotedLastStart = -1;
                } else {
                    if (equalObjects(c, bracketStart)) {
                        bracketLevel++;
                    } else if (equalObjects(c, bracketEnd)) {
                        bracketLevel = Math.max(bracketLevel - 1, 0);
                    }
                    word[wordIndex] = c;
                    wordIndex++;
                }
            }
        }
        if (wordIndex > 0) {
            final int first = quotedLastStart < 0 ? 0 : quotedLastStart;
            final int last = quotedWordLength < 0 ? wordIndex : (quotedWordLength - first);
            values.add(String.copyValueOf(word, first, last));
        }
        return values.toArray(new String[0]);
    }

    /*
     * Java specific
     */

    /**
     * Returns the binary prefix for a specific value. Each step is 1024 units
     * bigger than the previous. The largest (defined) prefix is
     * <code>Yobi/Yi</code> where 1 YiB is 1*2^80 Bytes.
     * 
     * @param value
     *            the prefixed value.
     * @return value and binary prefix (e.g. 2 KiB), where the value is lower
     *         than 1024 if the parameter is lower than 2^90.
     * @since 1.3
     */
    public static String getBinaryPrefixValue(long value) {
        return getBinaryPrefixValue(value, 0);
    }

    /**
     * Returns the binary prefix for a specific value. Each step is 1024 units
     * bigger than the previous. The largest (defined) prefix is
     * <code>Yobi/Yi</code> where 1 YiB is 1*2^80 Bytes.
     * 
     * @param value
     *            the prefixed value.
     * @param decimalPlaces
     *            the decimal places in the result.
     * @return value and binary prefix (e.g. 2 KiB), where the value is lower
     *         than 1024 if the parameter is lower than 2^90.
     * @since 1.3
     */
    public static String getBinaryPrefixValue(long value, int decimalPlaces) {
        final int ITERATION_WIDTH = 10;
        final int ONE_ITERATION = 1 << ITERATION_WIDTH; // 2¹⁰

        int iterations = 0;
        if (Math.abs(value) >= ONE_ITERATION) {
            final String[] PREFIXES = new String[] { "", "Ki", "Mi", "Gi", "Ti", "Pi", "Ei", "Zi", "Yi" };
            final int TWO_ITERATIONS = ONE_ITERATION << ITERATION_WIDTH;
            while (Math.abs(value) >= TWO_ITERATIONS && iterations < PREFIXES.length - 2) {
                value >>= ITERATION_WIDTH;
                iterations++;
            }

            return round(((double) value) / ONE_ITERATION, decimalPlaces) + " " + PREFIXES[iterations + 1];
        } else {
            return value + " ";
        }
    }

    /**
     * Returns the SI prefix for a specific value. Each step is 1000 units
     * bigger/smaller than the previous. The largest/smallest (defined) prefix
     * is <code>Yotta/Yokto</code>. For example 1 Yottameter are 1*1000^8 meter.
     * 
     * @param value
     *            the prefixed value.
     * @return value and SI prefix (e.g. 2 kg), where the value is lower than
     *         1000 if the parameter is lower than 1000^9.
     * @since 1.3
     */
    public static String getSIPrefixValue(double value) {
        return getSIPrefixValue(value, 0);
    }

    /**
     * Returns the SI prefix for a specific value. Each step is 1000 units
     * bigger/smaller than the previous. The largest/smallest (defined) prefix
     * is <code>Yotta/Yokto</code>. For example 1 Yottameter are 1*1000^8 meter.
     * 
     * @param value
     *            the prefixed value.
     * @param decimalPlaces
     *            the decimal places in the result.
     * @return value and SI prefix (e.g. 2 kg), where the value is lower than
     *         1000 if the parameter is lower than 1000^9.
     * @since 1.3
     */
    public static String getSIPrefixValue(double value, int decimalPlaces) {
        final int ONE_ITERATION = 1000;
        if (decimalPlaces < 0) {
            throw new IllegalArgumentException("Decimal places has to be non negative.");
        } else if (Math.abs(value) < 1) {
            final String[] PREFIXES = new String[] { "", "m", "µ", "n", "p", "f", "a", "z", "y" };
            return getPrefix(value, 1 / (double) ONE_ITERATION, PREFIXES, decimalPlaces);
        } else {
            // Yes the k is lowercase
            final String[] PREFIXES = new String[] { "", "k", "M", "G", "T", "P", "E", "Z", "Y" };
            return getPrefix(value, ONE_ITERATION, PREFIXES, decimalPlaces);
        }
    }

    private static String getPrefix(double value, double factor, String[] prefixes, int decimalPlaces) {
        int iterations = 0;
        while ((factor < 1 ? Math.abs(value) <= factor : Math.abs(value) >= factor) && iterations < prefixes.length - 1) {
            value /= factor;
            iterations++;
        }
        return round(value, decimalPlaces) + " " + prefixes[iterations];
    }

    /**
     * Rounds the value with the specified number of decimal places. Basically a <code>Round(value&nbsp;*&nbsp;10^(decimal&nbsp;places))&nbsp;/&nbsp;10^(decimal&nbsp;places)</code>.
     * @param value Value to round.
     * @param decimalPlaces Number of specified decimal places and have to be non negative.
     * @return the value with the specified number of decimal places.
     * @since 1.3
     */
    public static double round(double value, int decimalPlaces) {
        if (decimalPlaces < 0) {
            throw new IllegalArgumentException("Decimal places has to be non negative.");
        } else if (decimalPlaces == 0) {
            return Math.round(value);
        } else {
            final double factor = Math.pow(10, decimalPlaces);
            return Math.round(value * factor) / factor;
        }
    }

    /**
     * Rounds the value with the specified number of decimal places. Basically a <code>Round(value&nbsp;*&nbsp;10^(decimal&nbsp;places))&nbsp;/&nbsp;10^(decimal&nbsp;places)</code>.
     * @param value Value to round.
     * @param decimalPlaces Number of specified decimal places and have to be non negative.
     * @return the value with the specified number of decimal places.
     * @since 1.3
     */
    public static float round(float value, int decimalPlaces) {
        if (decimalPlaces < 0) {
            throw new IllegalArgumentException("Decimal places has to be non negative.");
        }  if (decimalPlaces == 0) {
            return Math.round(value);
        } else {
            final float factor = (float) Math.pow(10, decimalPlaces);
            return Math.round(value * factor) / factor;
        }
    }

    /**
     * Try to cast an object to a specific class and returns a default value if
     * it was impossible.
     * 
     * @param clazz
     *            the target class of the object.
     * @param o
     *            the casted object.
     * @param def
     *            the default value if the object couldn't be casted to the
     *            class.
     * @return the casted object or the default value if the cast wasn't
     *         possible.
     * @since 1.0
     */
    public static <T> T cast(Class<T> clazz, Object o, T def) {
        try {
            return clazz.cast(o);
        } catch (ClassCastException e) {
            return def;
        }
    }

    /**
     * Try to cast an object to a specific class and returns <code>null</code>
     * if it was impossible.
     * 
     * @param clazz
     *            the target class of the object.
     * @param o
     *            the casted object.
     * @return the casted object or <code>null</code> if the cast wasn't
     *         possible.
     * @since 1.0
     */
    public static <T> T cast(Class<T> clazz, Object o) {
        return cast(clazz, o, null);
    }

    /**
     * Checks if two doubles a nearly equal with the given tolerance.
     * @param a first double.
     * @param b second double.
     * @param tolerance the tolerance.
     * @return if both doubles are nearly equals within the tolerance.
     * @since 1.3
     */
    public static boolean equals(double a, double b, double tolerance) {
        return a - tolerance > b && a + tolerance < b;
    }

    /**
     * Checks if two doubles a nearly equal. The tolerance is {@link #EPSILON}.
     * @param a first double.
     * @param b second double.
     * @return if both doubles are nearly equals within the tolerance.
     */
    public static boolean equals(double a, double b) {
        return equals(a, b, EPSILON);
    }

    /**
     * Checks if both objects are equals. This includes a null check.
     * @param a first object.
     * @param b second object.
     * @return if both objects are equals or null.
     * @since 1.2
     * @see MinecraftUtil#equalObjects(Object, Object)
     */
    public static boolean equals(Object a, Object b) {
        return a == null ? b == null : a.equals(b);
    }

    /**
     * Checks if both objects are equals. This includes a null check. An alias
     * method if java selects {@link MinecraftUtil#equals(double, double)} it
     * is possible to use this method.
     * @param a first object.
     * @param b second object.
     * @return if both objects are equals or null.
     * @since 1.3
     * @see MinecraftUtil#equals(Object, Object)
     */
    public static boolean equalObjects(Object a, Object b) {
        return equals(a, b);
    }

    /**
     * Creates a new array of the given length and type.
     * @param clazz class of the elements.
     * @param newLength new length of the array.
     * @return new array with the given length and type.
     * @since 1.3
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] createArray(Class<? extends Object[]> clazz, int newLength) {
        return ((Object) clazz == (Object) Object[].class) ? (T[]) new Object[newLength] : (T[]) Array.newInstance(clazz.getComponentType(), newLength);
    }

    /**
     * Concatenate one element an a list of elements.
     * @param t one element.
     * @param ts an array of elements.
     * @return an array with the element on the first place and the rest of the array following it.
     * @since 1.0
     */
    public static <T> T[] concat(T t, T... ts) {
        int newLength = ts.length + 1;
        T[] completed = createArray(ts.getClass(), newLength);
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
     * @since 1.0
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
        char[] c = word.toCharArray();
        Collections.shuffle(Arrays.asList(c));
        return new String(c);
    }

    /**
     * Returns the width where the base could be dynamically defined. If the base
     * is a power of 2 the {@link #getWidthWithBinaryBase(int, int)} would be faster.
     * @param number the given number.
     * @param base the given base.
     * @return the “width” of the given number with the given base.
     * @since 1.0
     */
    public static int getWidth(int number, final int base) {
        int width = 1;
        while (number >= base) {
            number /= base;
            width++;
        }
        return width;
    }

    /**
     * Returns the width where the base is a power of 2 (<a href="http://oeis.org/A000079">oeis.org/A000079</a>).
     * @param number the given number.
     * @param baseBit base bit. The base will be 2^(base bit).
     * @return the “width” of the given number with the given base.
     * @since 1.3
     */
    public static int getWidthWithBinaryBase(int number, final int baseBit) {
        int width = 1;
        final int base = 1 << baseBit;
        while (width >= base) {
            width <<= baseBit;
            width++;
        }
        return width;
    }

    /**
     * Toggles an entry in a collection. So if the entry is in the collection it
     * will be removed and if it isn't in the collection it will be added.
     * @param entry tested entry.
     * @param collection tested collection.
     * @return If the entry was added.
     * @since 1.3
     */
    public static <T> boolean toggleEntry(T entry, Collection<T> collection) {
        if (collection.remove(entry)) {
            return false;
        } else {
            collection.add(entry);
            return true;
        }
    }

    /**
     * Toggles an entry in a list. So if the entry is in the list it
     * will be removed and if it isn't in the list it will be added.
     * @param entry tested entry.
     * @param list tested list.
     * @return If the entry was added.
     * @deprecated Use {@link #toggleEntry(Object, Collection)} instead.
     * @since 1.0
     */
    @Deprecated
    //TODO: Remove in BPU 2! Spelled wrong.
    public static <T> boolean toogleEntry(T entry, List<T> list) {
        return toggleEntry(entry, list);
    }

    /**
     * Tries to convert a string into an integer. If the string is invalid it
     * returns <code>null</code>.
     * 
     * @param string
     *            The string to be parsed.
     * @return The value if the string is valid, otherwise <code>null</code>.
     * @since 1.0
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
     * @since 1.0
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
     * @since 1.0
     */
    public static Byte tryAndGetByte(String string) {
        try {
            return Byte.parseByte(string);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Tries to convert a string into a float. If the string is invalid it
     * returns <code>null</code>.
     * 
     * @param string
     *            The string to be parsed.
     * @return The value if the string is valid, otherwise <code>null</code>.
     * @since 1.3
     */
    public static Float tryAndGetFloat(String string) {
        try {
            return Float.parseFloat(string);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Tries to convert a string into a double. If the string is invalid it
     * returns <code>null</code>.
     * 
     * @param string
     *            The string to be parsed.
     * @return The value if the string is valid, otherwise <code>null</code>.
     * @since 1.0
     */
    public static Double tryAndGetDouble(String string) {
        try {
            return Double.parseDouble(string);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Truncates the specified value.
     * @param value
     * @return truncated value.
     * @since 1.0
     */
    public static long trunc(double value) {
        return (long) value;
    }

    /**
     * Get all decimal places of the specified value. For example <code>4.2</code> would return
     * <code>0.2</code>.
     * @param value Specified value.
     * @return decimal places of the specified value.
     * @since 1.0
     */
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
     * @since 1.0
     * @deprecated Use {@link ArrayReferenceList#indexOf(Object, Object...)} instead.
     */
    @Deprecated
    public static <T> int indexOf(T o, T[] a) {
        return ArrayReferenceList.indexOf(o, a);
    }

    public static <T> T replaceNull(T value, T nullReplacement) {
        return value == null ? nullReplacement : value;
    }

    /**
     * Returns if the tested object <code>o</code> is in the array <code>a</code>.
     * @param o Searched object.
     * @param a Searched array.
     * @return if the object is in the array.
     * @since 1.0
     * @deprecated Use {@link ArrayReferenceList#contains(Object, Object[])} instead.
     */
    @Deprecated
    public static <T> boolean contains(T o, T[] a) {
        return ArrayReferenceList.contains(o, a);
    }

    public static interface ChanceElement<T> {
        public Double getChance();

        public T getElement();
    }

    public static class DefaultChanceElement<T> implements ChanceElement<T> {
        private final Double chance;
        private final T element;

        public DefaultChanceElement(Double chance, T element) {
            this.chance = chance;
            this.element = element;
        }

        public DefaultChanceElement(T element) {
            this(null, element);
        }

        @Override
        public Double getChance() {
            return this.chance;
        }

        @Override
        public T getElement() {
            return this.element;
        }
    }

    public static <T> T getRandomFromChances(List<ChanceElement<T>> chances) {
        if (MinecraftUtil.isSet(chances)) {
            // Cumulate chances
            final double defChance = 1.0 / chances.size();
            double totalchance = 0;
            for (ChanceElement<T> chanceElement : chances) {
                totalchance += MinecraftUtil.replaceNull(chanceElement.getChance(), defChance);
            }

            // Determine element
            double value = Math.random() * totalchance;
            for (ChanceElement<T> chanceElement : chances) {
                value -= MinecraftUtil.replaceNull(chanceElement.getChance(), defChance);
                if (value < 0) {
                    return chanceElement.getElement();
                }
            }
            // Normally never reached
            return null;
        } else {
            return null;
        }
    }

    /**
     * Returns a random value of the list. If the list is not set (
     * {@link #isSet(Object)}) it will return null.
     * 
     * @param list
     *            The given list.
     * @return a random element of the list.
     * @since 1.0
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
     * @since 1.0
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
            T[] newArray = createArray(t.getClass(), len);

            System.arraycopy(t, start, newArray, 0, Math.min(t.length, newArray.length));

            return newArray;
        }
    }

    /**
     * Creates a new array, but only with the elements begining at the start.
     * 
     * @param t
     *            array.
     * @param start
     *            Start and first element in new array.
     * @return new array with a new first element.
     * @deprecated {@link Arrays#copyOf(Object[], int)}
     */
    @Deprecated
    //TODO: Remove with BPU 2
    public static <T> T[] subArray(T[] t, int start) {
        return Arrays.copyOf(t, start);
    }

    /**
     * Creates an map, mapping an identifier to all values of an enum.
     * 
     * @param enumClass
     *            The class of the enum.
     * @param keys
     *            The callback class defining a name for each enum.
     * @return A map mapping an identifier to an enum.
     * @deprecated Use {@link #createReverseEnumMap(Class, Callback)} instead.
     */
    @Deprecated
    //TODO: Remove with BPU 2
    public static <K, V extends Enum<?>> Map<K, V> createEnumMap(Class<V> enumClass, Callback<K, ? super V> keys) {
        return createReverseEnumMap(enumClass, keys);
    }

    /**
     * Creates an map, mapping an identifier to all values of an enum.
     * 
     * @param enumClass
     *            The class of the enum.
     * @param keys
     *            The callback class defining a name for each enum.
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
     * See also:
     * http://code.google.com/p/guava-libraries/source/browse/trunk/guava
     * /src/com/google/common/collect/Lists.java
     */
    /**
     * Creates a <i>mutable</i>, empty {@code HashMap} instance.
     * 
     * @return a new, empty {@code HashMap}
     * @deprecated Use {@link Maps#newHashMap()} instead.
     */
    @Deprecated
    //TODO: Remove with BPU 2
    public static <K, V> HashMap<K, V> createHashMap() {
        return new HashMap<K, V>();
    }

    /**
     * Creates an {@code EnumMap} instance.
     * 
     * @param type
     *            the key type for this map
     * @return a new, empty {@code EnumMap}
     * @deprecated Use {@link Maps#newEnumMap(Class)} instead.
     */
    @Deprecated
    //TODO: Remove with BPU 2
    public static <K extends Enum<K>, V> EnumMap<K, V> createEnumMap(Class<K> keyType) {
        return new EnumMap<K, V>(keyType);
    }

    /**
     * Creates an immutable map of the given map. If the given map is null it
     * returns an empty immutable map.
     * @param map the map on which the immutable bases on.
     * @return the immutable map bases on the map.
     * @since 1.3
     */
    public static <K, V> ImmutableMap<K, V> createImmutableMap(Map<K, V> map) {
        return map == null ? ImmutableMap.<K, V>of() : ImmutableMap.copyOf(map);
    }

    public static <K extends Enum<K>, V> EnumMap<K, V> createEnumMap(Map<K, V> map, Class<K> keyType) {
        if (map == null) {
            return new EnumMap<K, V>(keyType);
        } else {
            return new EnumMap<K, V>(map);
        }
    }

    /**
     * Reads all lines of a file into a string list.
     * @param f file object.
     * @return all lines of the file.
     * @throws IOException if there were problems on reading the file.
     */
    public static List<String> readLines(File f) throws IOException {
        List<String> lines = new ArrayList<String>();
        FileReader fileReader = new FileReader(f);
        try {
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
        } finally {
            fileReader.close();
        }
        return lines;
    }
}
