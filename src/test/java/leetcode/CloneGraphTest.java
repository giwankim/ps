package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import leetcode.support.Node;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class CloneGraphTest {

  @ParameterizedTest
  @MethodSource
  void cloneGraph(Node node) {
    Node clone = new CloneGraph().cloneGraph(node);
    if (clone != null) {
      assertThat(node).isNotSameAs(clone);
    }
    assertThat(node).isEqualTo(clone);
  }

  private static Stream<Arguments> cloneGraph() {
    return Stream.of(
        Arguments.of(
            Node.fromLists(List.of(List.of(2, 4), List.of(1, 3), List.of(2, 4), List.of(1, 3)))),
        Arguments.of(Node.fromLists(List.of(Collections.emptyList()))),
        Arguments.of(Node.fromLists(Collections.emptyList())));
  }
}
