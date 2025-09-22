package com.giwankim.grind75.support;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class ListNode {
  public int val;
  public ListNode next;

  public ListNode(int val) {
    this(val, null);
  }
}
