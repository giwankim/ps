package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.leetcode.CopyListWithRandomPointer.Node;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class CopyListWithRandomPointerTest {
  private static final int NO_RANDOM = -1;

  CopyListWithRandomPointer sut = new CopyListWithRandomPointer();

  @Test
  void nullList() {
    assertThat(sut.copyRandomList(null)).isNull();
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource
  void copyRandomList(String description, int[][] specs) {
    Node head = createListWithRandomPointer(specs);
    Node expected = createListWithRandomPointer(specs);

    Node actual = sut.copyRandomList(head);

    assertThat(snapshot(actual)).isEqualTo(snapshot(expected));
    assertDistinctNodes(head, actual);
  }

  static Stream<Arguments> copyRandomList() {
    return Stream.of(
        Arguments.of("single node", new int[][] {{1, NO_RANDOM}}),
        Arguments.of("two nodes", new int[][] {{1, NO_RANDOM}, {2, NO_RANDOM}}),
        Arguments.of("three nodes", new int[][] {{1, NO_RANDOM}, {2, NO_RANDOM}, {3, NO_RANDOM}}),
        Arguments.of("forward random reference", new int[][] {{1, 1}, {2, NO_RANDOM}}),
        Arguments.of("backward random reference", new int[][] {{1, NO_RANDOM}, {2, 0}}),
        Arguments.of("self random reference", new int[][] {{1, 0}}),
        Arguments.of("cross random cycle", new int[][] {{1, 1}, {2, 0}}),
        Arguments.of(
            "leetcode example 1", new int[][] {{7, NO_RANDOM}, {13, 0}, {11, 4}, {10, 2}, {1, 0}}),
        Arguments.of("leetcode example 2", new int[][] {{1, 1}, {2, 1}}),
        Arguments.of("leetcode example 3", new int[][] {{3, NO_RANDOM}, {3, 0}, {3, NO_RANDOM}}));
  }

  private static Node createListWithRandomPointer(int[][] specs) {
    if (specs.length == 0) {
      return null;
    }
    Node[] nodes = new Node[specs.length];
    for (int i = 0; i < specs.length; i++) {
      nodes[i] = new Node(specs[i][0]);
      if (i > 0) {
        nodes[i - 1].next = nodes[i];
      }
    }
    for (int i = 0; i < specs.length; i++) {
      int randomIndex = specs[i][1];
      nodes[i].random = randomIndex == NO_RANDOM ? null : nodes[randomIndex];
    }
    return nodes[0];
  }

  private static List<String> snapshot(Node head) {
    List<Node> nodes = new ArrayList<>();
    IdentityHashMap<Node, Integer> idx = new IdentityHashMap<>();
    for (Node it = head; it != null; it = it.next) {
      idx.put(it, nodes.size());
      nodes.add(it);
    }
    List<String> result = new ArrayList<>();
    for (Node n : nodes) {
      int ri = n.random == null ? NO_RANDOM : idx.get(n.random);
      result.add(n.val + ":" + ri);
    }
    return result;
  }

  private static List<Node> getNodeList(Node head) {
    List<Node> nodes = new ArrayList<>();
    for (Node it = head; it != null; it = it.next) {
      nodes.add(it);
    }
    return nodes;
  }

  private static void assertDistinctNodes(Node expected, Node actual) {
    List<Node> expectedNodes = getNodeList(expected);
    List<Node> actualNodes = getNodeList(actual);
    assertThat(actualNodes).hasSize(expectedNodes.size());
    for (int i = 0; i < expectedNodes.size(); i++) {
      assertThat(actualNodes.get(i)).isNotSameAs(expectedNodes.get(i));
    }
  }
}
