package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.leetcode.support.TreeNode;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class SerializeDeserializeBinaryTreeTest {

  SerializeDeserializeBinaryTree ser;
  SerializeDeserializeBinaryTree deser;

  @BeforeEach
  void setUp() {
    ser = new SerializeDeserializeBinaryTree();
    deser = new SerializeDeserializeBinaryTree();
  }

  @Test
  void serializeNull() {
    assertThat(ser.serialize(null)).isEqualTo("X,");
  }

  @Test
  void serialize() {
    TreeNode root = TreeNode.from(1, 2, 3, null, null, 4, 5);
    assertThat(ser.serialize(root)).isEqualTo("1,2,X,X,3,4,X,X,5,X,X,");
  }

  @Test
  void deserialize() {
    String data = "1,2,X,X,3,4,X,X,5,X,X,";
    TreeNode expected = TreeNode.from(1, 2, 3, null, null, 4, 5);

    TreeNode actual = deser.deserialize(data);

    assertThat(actual).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource
  void serializeDeserialize(TreeNode root) {
    TreeNode actual = deser.deserialize(ser.serialize(root));
    assertThat(actual).isEqualTo(root);
  }

  private static Stream<Arguments> serializeDeserialize() {
    return Stream.of(
        Arguments.of(TreeNode.from(1, 2, 3, null, null, 4, 5)), Arguments.of(TreeNode.from()));
  }
}
