package com.giwankim.leetcode;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class SnapshotArray {
  private int snapId;
  private final List<TreeMap<Integer, Integer>> snapshots;

  public SnapshotArray(int length) {
    snapId = 0;
    snapshots = new ArrayList<>(length);
    for (int i = 0; i < length; i++) {
      snapshots.add(new TreeMap<>());
      snapshots.get(i).put(0, 0);
    }
  }

  public void set(int index, int val) {
    snapshots.get(index).put(snapId, val);
  }

  public int snap() {
    snapId += 1;
    return snapId - 1;
  }

  public int get(int index, int snap_id) {
    return snapshots.get(index).floorEntry(snap_id).getValue();
  }
}
