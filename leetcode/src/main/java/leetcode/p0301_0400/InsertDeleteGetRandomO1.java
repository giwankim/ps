package leetcode.p0301_0400;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * <a href="https://leetcode.com/problems/insert-delete-getrandom-o1/">380. Insert Delete GetRandom
 * O(1)</a>
 */
public class InsertDeleteGetRandomO1 {
  /** @implNote Time {@code O(1)}, space {@code O(n)}. */
  public static class RandomizedSet {
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
