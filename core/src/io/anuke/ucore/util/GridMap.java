package io.anuke.ucore.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

/* renamed from: io.anuke.ucore.util.GridMap */
public class GridMap<T> {
    protected HashMap<Long, T> map = new HashMap<>();

    public T get(int x, int y) {
        return this.map.get(Long.valueOf(getHash(x, y)));
    }

    public boolean containsKey(int x, int y) {
        return this.map.containsKey(Long.valueOf(getHash(x, y)));
    }

    public void put(int x, int y, T t) {
        this.map.put(Long.valueOf(getHash(x, y)), t);
    }

    public Collection<T> values() {
        return this.map.values();
    }

    public Set<Long> keys() {
        return this.map.keySet();
    }

    public void clear() {
        this.map.clear();
    }

    public int size() {
        return this.map.size();
    }

    private long getHash(int x, int y) {
        return (((long) x) << 32) | (((long) y) & 4294967295L);
    }
}
