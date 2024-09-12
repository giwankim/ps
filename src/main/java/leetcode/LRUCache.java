package leetcode;

import java.util.LinkedHashMap;

public class LRUCache {
  private final int capacity;
  private final LinkedHashMap<Integer, Integer> cache;

  public LRUCache(int capacity) {
    this.capacity = capacity;
    cache = new LinkedHashMap<>(capacity, 0.75f, true);
  }

  public int get(int key) {
    return cache.getOrDefault(key, -1);
  }

  public void put(int key, int value) {
    cache.put(key, value);
    if (cache.size() > capacity) {
      Integer remove = cache.firstEntry().getKey();
      cache.remove(remove);
    }
  }
}
