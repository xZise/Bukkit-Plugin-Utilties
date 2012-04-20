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

package de.xzise.bukkit.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.MemorySection;

public class MemorySectionFromMap extends MemoryConfiguration {

    public MemorySectionFromMap(final Map<? extends String, ? extends Object> map) {
        super();
        this.map.putAll(map);
    }

    @SuppressWarnings("unchecked")
    public static MemorySectionFromMap create(final Object object) {
        try {
            return new MemorySectionFromMap((Map<String, Object>) object);
        } catch (ClassCastException e) {
            return null;
        }
    }

    /* remove in 1.3 stable! */
    @Deprecated
    public static Map<String, MemorySectionFromMap> getNodes(final MemorySection section, final String path) {
        return getSectionMap(section, path);
    }

    public static Map<String, MemorySectionFromMap> getSectionMap(final ConfigurationSection section, final String path) {
        ConfigurationSection subSection = section.getConfigurationSection(path);
        if (subSection == null) {
            return null;
        } else {
            final Map<String, MemorySectionFromMap> map = new HashMap<String, MemorySectionFromMap>();
            for (Map.Entry<String, Object> entry : subSection.getValues(false).entrySet()) {
                if (entry.getValue() instanceof Map) {
                    final MemorySectionFromMap memorySectionFromMap = MemorySectionFromMap.create(entry.getValue());
                    if (memorySectionFromMap != null) {
                        map.put(entry.getKey(), memorySectionFromMap);
                    }
                }
            }
            return map;
        }
    }

    public static List<MemorySectionFromMap> getSectionList(final MemorySection configuration, final String path) {
        List<Map<?, ?>> mapList = configuration.getMapList(path);
        if (mapList != null) {
            MemorySectionFromMap[] sectionArray = new MemorySectionFromMap[mapList.size()];
            for (int i = 0; i < mapList.size(); i++) {
                sectionArray[i] = MemorySectionFromMap.create(mapList.get(i));
            }
            return Arrays.asList(sectionArray);
        } else {
            return new ArrayList<MemorySectionFromMap>(0);
        }
    }
}
