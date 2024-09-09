package leetcode;

import java.util.Arrays;

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
