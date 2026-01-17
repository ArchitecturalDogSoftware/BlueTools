package net.architecturaldog.bluetools.utility;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;

public class MapCombinations<K extends Comparable<K>, V> {

    protected final Map<K, List<V>> map;
    protected final List<K> keys;

    public MapCombinations(final Map<K, List<V>> map, final Comparator<K> comparator) {
        this.map = map;
        this.keys = this.map.keySet().stream().sorted(comparator).toList();
    }

    public MapCombinations(final Map<K, List<V>> map) {
        this(map, Comparator.naturalOrder());
    }

    public final Stream<Map<K, V>> stream() {
        return this.stream(0, this.newMap());
    }

    protected Map<K, V> newMap() {
        return new Object2ObjectLinkedOpenHashMap<>();
    }

    protected Map<K, V> copyMap(final Map<K, V> map) {
        return new Object2ObjectLinkedOpenHashMap<>(map);
    }

    protected final Map<K, V> copyWithEntry(final Map<K, V> map, final K key, final V value) {
        final Map<K, V> copiedMap = this.copyMap(map);

        copiedMap.put(key, value);

        return copiedMap;
    }

    protected final Stream<Map<K, V>> stream(final int index, final Map<K, V> next) {
        if (index >= this.keys.size()) return Stream.of(next);

        final K key = this.keys.get(index);

        return this.map.get(key).stream().distinct().flatMap(value -> {
            return this.stream(index + 1, this.copyWithEntry(next, key, value));
        });
    }

}
