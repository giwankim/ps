package com.giwankim.leetcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class InsertDeleteGetRandomO1 {
  public static class RandomizedSet {
    // Time complexity: O(1), Space complexity: O(n)
    private final Map<Integer, Integer> valToIdx;
    private final List<Integer> vals;

    public RandomizedSet() {
      valToIdx = new HashMap<>();
      vals = new ArrayList<>();
    }

    public boolean insert(int val) {
      if (valToIdx.containsKey(val)) {
        return false;
      }
      valToIdx.put(val, vals.size());
      vals.add(val);
      return true;
    }

    public boolean remove(int val) {
      if (!valToIdx.containsKey(val)) {
        return false;
      }
      // swap last element into its place
      int idx = valToIdx.get(val);
      int lastVal = vals.getLast();
      vals.set(idx, lastVal);
      valToIdx.put(lastVal, idx);
      // remove
      vals.removeLast();
      valToIdx.remove(val);
      return true;
    }

    public int getRandom() {
      int randomIndex = ThreadLocalRandom.current().nextInt(vals.size());
      return vals.get(randomIndex);
    }
  }
}
