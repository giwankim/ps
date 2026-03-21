package com.giwankim.leetcode;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class LRUCache {
  // Time complexity: O(1), Space complexity: O(capacity)
  private final Map<Integer, Integer> cache;

  public LRUCache(int capacity) {
    cache =
        new LinkedHashMap<>(capacity, 0.75f, true) {
          @Override
          protected boolean removeEldestEntry(Entry<Integer, Integer> eldest) {
            return size() > capacity;
          }
        };
  }

  public int get(int key) {
    return cache.getOrDefault(key, -1);
  }

  public void put(int key, int value) {
    cache.put(key, value);
  }
}
