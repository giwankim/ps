package com.giwankim.leetcode;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

public class ConstructQuadTree {
  /**
   * @implNote Time {@code O(n²)}, space {@code O(log n)} excluding the output, where
   *     {@code n = grid.length}.
   *     <p><b>Time:</b> the recurrence {@code T(n) = 4·T(n/2) + O(1)} captures four
   *     recursive calls on sub-grids of side {@code n/2} plus constant work per call
   *     (midpoint arithmetic, the four-way leaf-and-value check, and one {@link Node}
   *     allocation). Applying the master theorem with {@code a = 4}, {@code b = 2},
   *     {@code f(n) = O(1)}: {@code n^log_b(a) = n²} strictly dominates {@code f(n)},
   *     so case 1 gives {@code T(n) = Θ(n²)}.
   *     <p><b>Space:</b> only one of the four child calls is on the stack at a time,
   *     so the auxiliary-stack recurrence is {@code S(n) = S(n/2) + O(1) = O(log n)}.
   *     The output quadtree is not counted.
   */
  public Node construct(int[][] grid) {
    return construct(grid, 0, grid.length - 1, 0, grid[0].length - 1);
  }

  private Node construct(int[][] grid, int rowStart, int rowEnd, int colStart, int colEnd) {
    if (rowStart == rowEnd && colStart == colEnd) {
      return new Node(grid[rowStart][colStart] == 1, true);
    }
    int rowMid = rowStart + (rowEnd - rowStart) / 2;
    int colMid = colStart + (colEnd - colStart) / 2;
    Node topLeft = construct(grid, rowStart, rowMid, colStart, colMid);
    Node topRight = construct(grid, rowStart, rowMid, colMid + 1, colEnd);
    Node bottomLeft = construct(grid, rowMid + 1, rowEnd, colStart, colMid);
    Node bottomRight = construct(grid, rowMid + 1, rowEnd, colMid + 1, colEnd);
    if (topLeft.isLeaf
        && topRight.isLeaf
        && bottomLeft.isLeaf
        && bottomRight.isLeaf
        && topLeft.val == topRight.val
        && topRight.val == bottomRight.val
        && bottomLeft.val == bottomRight.val) {
      return new Node(topLeft.val, true);
    }
    return new Node(true, false, topLeft, topRight, bottomLeft, bottomRight);
  }

  @AllArgsConstructor
  @EqualsAndHashCode
  @NoArgsConstructor
  public static class Node {
    public boolean val;
    public boolean isLeaf;
    public Node topLeft;
    public Node topRight;
    public Node bottomLeft;
    public Node bottomRight;

    public Node(boolean val, boolean isLeaf) {
      this(val, isLeaf, null, null, null, null);
    }
  }
}
