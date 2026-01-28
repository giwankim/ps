package com.giwankim.grind75.support;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class TreeNode {

  public int val;
  public TreeNode left;
  public TreeNode right;

  public TreeNode(int val) {
    this(val, null, null);
  }
}
