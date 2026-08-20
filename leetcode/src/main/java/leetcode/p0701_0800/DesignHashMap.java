package leetcode.p0701_0800;

import java.util.Arrays;

/** <a href="https://leetcode.com/problems/design-hashmap/">706. Design HashMap</a> */
public class DesignHashMap {
  private final int[] values;

  public DesignHashMap() {
    values = new int[1_000_000 + 1];
    Arrays.fill(values, -1);
  }

  public void put(int key, int value) {
    values[key] = value;
  }

  public int get(int key) {
    return values[key];
  }

  public void remove(int key) {
    values[key] = -1;
  }
}
