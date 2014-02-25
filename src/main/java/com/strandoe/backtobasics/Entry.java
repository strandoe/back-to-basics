package com.strandoe.backtobasics;

import java.util.LinkedHashMap;
import java.util.Map;

public class Entry {
    public final String key;
    public final Object value;

    public Entry(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public static Entry e(String key, Object value) {
        return new Entry(key, value);
    }

    public static Map<String, Object> map(Entry... entries) {
        Map<String, Object> map = new LinkedHashMap<String, Object>(entries.length);
        for (Entry entry : entries) {
            map.put(entry.key, entry.value);
        }
        return map;
    }

}
