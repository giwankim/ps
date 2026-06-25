package leetcode.support;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ListNodeTest {

  @Test
  void ofEmpty() {
    assertThat(ListNode.of()).isNull();
  }

  @ParameterizedTest
  @MethodSource
  void of(int[] values, String expected) {
    assertThat(ListNode.of(values)).hasToString(expected);
  }

  private static Stream<Arguments> of() {
    return Stream.of(
        Arguments.of(new int[] {1}, "1->null"),
        Arguments.of(new int[] {1, 2}, "1->2->null"),
        Arguments.of(new int[] {1, 2, 4}, "1->2->4->null"));
  }
}
