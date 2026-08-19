package leetcode;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CinemaSeatAllocation {
  private static final Set<Integer> LEFT = Set.of(2, 3, 4, 5);
  private static final Set<Integer> MIDDLE = Set.of(4, 5, 6, 7);
  private static final Set<Integer> RIGHT = Set.of(6, 7, 8, 9);

  /**
   * @implNote Time {@code O(m)}, auxiliary space {@code O(m)}, where {@code m =
   *     reservedSeats.length}. The row count {@code n} enters neither bound: only rows that appear
   *     in {@code reservedSeats} are ever materialized, and the untouched rows contribute two
   *     families each by arithmetic — so {@code n <= 10^9} costs nothing while {@code m <= 10^4}
   *     carries the work.
   *     <p><b>Cost per reserved row:</b> {@link Collections#disjoint} iterates its second argument
   *     whenever the first is a {@code Set}, so passing the row's seat set first walks only the
   *     four-element group constant and probes the hash set — {@code O(1)} per group however many
   *     seats that row reserves. The loop visits at most {@code min(m, n)} rows, so it stays within
   *     {@code O(m)}.
   */
  public int maxNumberOfFamilies(int n, int[][] reservedSeats) {
    Map<Integer, Set<Integer>> map = new HashMap<>();
    for (int[] reserved : reservedSeats) {
      int row = reserved[0];
      int seat = reserved[1];
      map.computeIfAbsent(row, k -> new HashSet<>()).add(seat);
    }
    int rows = map.size();
    int result = 2 * (n - rows);
    for (Set<Integer> seats : map.values()) {
      boolean left = Collections.disjoint(seats, LEFT);
      boolean middle = Collections.disjoint(seats, MIDDLE);
      boolean right = Collections.disjoint(seats, RIGHT);
      if (left && right) {
        result += 2;
      } else if (left || middle || right) {
        result += 1;
      }
    }
    return result;
  }
}
