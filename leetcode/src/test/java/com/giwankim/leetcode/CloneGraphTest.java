package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.leetcode.support.Node;
import java.util.ArrayDeque;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import org.junit.jupiter.api.Test;

class CloneGraphTest {
  CloneGraph sut = new CloneGraph();

  // Step 1: null input → null output
  @Test
  void nullReturnsNull() {
    assertThat(sut.cloneGraph(null)).isNull();
  }

  // Step 2: single node with no neighbors
  @Test
  void singleNodeNoNeighbors() {
    Node original = new Node(1);
    Node clone = sut.cloneGraph(original);
    assertThat(clone).isNotNull().isNotSameAs(original);
    assertThat(clone.val).isEqualTo(1);
    assertThat(clone.neighbors).isEmpty();
  }

  // Step 3: two mutually-connected nodes (smallest undirected graph)
  //   1 ↔ 2
  @Test
  void twoConnectedNodes() {
    Node original = Node.from(List.of(List.of(2), List.of(1)));
    Node clone = sut.cloneGraph(original);
    assertDeepClone(original, clone);
  }

  // Step 4: three-node linear chain
  //   1 - 2 - 3
  @Test
  void threeNodeLinearChain() {
    Node original = Node.from(List.of(List.of(2), List.of(1, 3), List.of(2)));
    Node clone = sut.cloneGraph(original);
    assertDeepClone(original, clone);
  }

  // Step 5: three-node cycle — exercises cycle-handling via visited map
  //     1
  //    / \
  //   2 - 3
  @Test
  void threeNodeTriangle() {
    Node original = Node.from(List.of(List.of(2, 3), List.of(1, 3), List.of(1, 2)));
    Node clone = sut.cloneGraph(original);
    assertDeepClone(original, clone);
  }

  // Step 6: LeetCode example 1 — four-node square with two cycles
  //   1 - 2
  //   |   |
  //   4 - 3
  @Test
  void leetCodeExampleFourNodeSquare() {
    Node original = Node.from(List.of(List.of(2, 4), List.of(1, 3), List.of(2, 4), List.of(1, 3)));
    Node clone = sut.cloneGraph(original);
    assertDeepClone(original, clone);
  }

  // Walks both graphs in lockstep, maintaining an identity-keyed mapping from
  // each original node to the clone node committed to it on first visit. On
  // every revisit we assert the incoming clone reference is the same committed
  // clone — this catches buggy implementations that mix original/clone
  // references or bind a single original to multiple distinct clones.
  private static void assertDeepClone(Node original, Node clone) {
    assertThat(clone).isNotNull().isNotSameAs(original);
    Map<Node, Node> originalToClone = new IdentityHashMap<>();
    Queue<NodePair> queue = new ArrayDeque<>();
    originalToClone.put(original, clone);
    queue.offer(new NodePair(original, clone));
    while (!queue.isEmpty()) {
      NodePair pair = queue.poll();
      Node o = pair.original();
      Node c = pair.cloned();
      assertThat(c).isNotSameAs(o);
      assertThat(c.val).isEqualTo(o.val);
      assertThat(c.neighbors).hasSameSizeAs(o.neighbors);
      for (int i = 0; i < o.neighbors.size(); i++) {
        Node nextO = o.neighbors.get(i);
        Node nextC = c.neighbors.get(i);
        Node committedClone = originalToClone.get(nextO);
        if (committedClone == null) {
          originalToClone.put(nextO, nextC);
          queue.offer(new NodePair(nextO, nextC));
        } else {
          assertThat(nextC).isSameAs(committedClone);
        }
      }
    }
  }

  private record NodePair(Node original, Node cloned) {}
}
