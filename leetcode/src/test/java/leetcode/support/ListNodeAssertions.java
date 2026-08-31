package leetcode.support;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

/** Structural assertions for {@link ListNode} chains, which compare by identity. */
public final class ListNodeAssertions {
  private ListNodeAssertions() {}

  public static void assertListEquals(ListNode actual, ListNode expected) {
    assertThat(values(actual)).isEqualTo(values(expected));
  }

  private static List<Integer> values(ListNode head) {
    List<Integer> out = new ArrayList<>();
    for (ListNode it = head; it != null; it = it.next) {
      out.add(it.val);
    }
    return out;
  }
}
