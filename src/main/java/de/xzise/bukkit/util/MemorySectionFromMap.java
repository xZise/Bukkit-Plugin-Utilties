package de.xzise.bukkit.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.MemorySection;

public class MemorySectionFromMap extends MemoryConfiguration {

    public MemorySectionFromMap(final Map<String, Object> map) {
        super();
        this.map.putAll(map);
    }

    public static List<MemorySectionFromMap> getSectionList(final MemorySection configuration, final String path) {
        List<Map<String, Object>> mapList = configuration.getMapList(path);
        if (mapList != null) {
            MemorySectionFromMap[] sectionArray = new MemorySectionFromMap[mapList.size()];
            for (int i = 0; i < mapList.size(); i++) {
                sectionArray[i] = new MemorySectionFromMap(mapList.get(i));
            }
            return Arrays.asList(sectionArray);
        } else {
            return new ArrayList<MemorySectionFromMap>(0);
        }
    }
}
