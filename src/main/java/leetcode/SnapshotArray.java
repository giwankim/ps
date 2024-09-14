package leetcode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SnapshotArray {
  private List<List<Integer>> snapshots;
  private List<Integer> array;

  public SnapshotArray(int length) {
    snapshots = new ArrayList<>();
    array = new ArrayList<>(Collections.nCopies(length, 0));
  }

  public void set(int index, int val) {
    array.set(index, val);
  }

  public int snap() {
    snapshots.add(array);
    array = new ArrayList<>(array);
    return snapshots.size() - 1;
  }

  public int get(int index, int snap_id) {
    if (snapshots.size() > snap_id) {
      return snapshots.get(snap_id).get(index);
    }
    return -1;
  }
}
