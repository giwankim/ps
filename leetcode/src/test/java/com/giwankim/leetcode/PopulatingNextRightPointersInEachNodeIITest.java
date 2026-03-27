package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.leetcode.PopulatingNextRightPointersInEachNodeII.Node;
import org.junit.jupiter.api.Test;

class PopulatingNextRightPointersInEachNodeIITest {
  PopulatingNextRightPointersInEachNodeII sut = new PopulatingNextRightPointersInEachNodeII();

  @Test
  void emptyTree() {
    assertThat(sut.connect(null)).isNull();
  }

  @Test
  void singleton() {
    assertThat(sut.connect(Node.of(1))).isEqualTo(Node.of(1));
  }

  @Test
  void twoLevelComplete() {
    Node left = new Node(2);
    Node right = new Node(3);
    Node root = new Node(1, left, right, null);

    sut.connect(root);

    assertThat(root.next).isNull();
    assertThat(left.next).isSameAs(right);
    assertThat(right.next).isNull();
  }

  @Test
  void leftChildOnly() {
    Node left = new Node(2);
    Node root = new Node(1, left, null, null);

    sut.connect(root);

    assertThat(root.next).isNull();
    assertThat(left.next).isNull();
  }

  @Test
  void exampleTree() {
    Node n4 = new Node(4);
    Node n5 = new Node(5);
    Node n7 = new Node(7);
    Node n2 = new Node(2, n4, n5, null);
    Node n3 = new Node(3, null, n7, null);
    Node root = new Node(1, n2, n3, null);

    sut.connect(root);

    assertThat(root.next).isNull();
    assertThat(n2.next).isSameAs(n3);
    assertThat(n3.next).isNull();
    assertThat(n4.next).isSameAs(n5);
    assertThat(n5.next).isSameAs(n7);
    assertThat(n7.next).isNull();
  }

  @Test
  void connectsAcrossSubtrees() {
    Node n4 = new Node(4);
    Node n5 = new Node(5);
    Node n2 = new Node(2, n4, null, null);
    Node n3 = new Node(3, null, n5, null);
    Node root = new Node(1, n2, n3, null);

    sut.connect(root);

    assertThat(root.next).isNull();
    assertThat(n2.next).isSameAs(n3);
    assertThat(n3.next).isNull();
    assertThat(n4.next).isSameAs(n5);
    assertThat(n5.next).isNull();
  }
}
