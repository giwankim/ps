package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CourseScheduleIITest {
  CourseScheduleII sut = new CourseScheduleII();

  // Step 1: smallest valid input — one course, no prerequisites (LeetCode example 3)
  @Test
  void oneCourseNoPrerequisites() {
    assertThat(sut.findOrder(1, new int[][] {})).containsExactly(0);
  }

  // Step 2: two disconnected courses — either order is valid
  @Test
  void twoCoursesNoPrerequisites() {
    assertThat(sut.findOrder(2, new int[][] {})).containsExactlyInAnyOrder(0, 1);
  }

  // Step 3: simplest DAG — must take 0 before 1, unique order (LeetCode example 1)
  //   0 → 1
  @Test
  void twoCoursesOnePrerequisite() {
    assertThat(sut.findOrder(2, new int[][] {{1, 0}})).containsExactly(0, 1);
  }

  // Step 4: linear chain of three — unique valid order
  //   0 → 1 → 2
  @Test
  void threeCoursesLinearChain() {
    assertThat(sut.findOrder(3, new int[][] {{1, 0}, {2, 1}})).containsExactly(0, 1, 2);
  }

  // Step 5: smallest two-node cycle — not completable
  //   0 ↔ 1
  @Test
  void twoCoursesMutualPrerequisitesReturnsEmpty() {
    assertThat(sut.findOrder(2, new int[][] {{1, 0}, {0, 1}})).isEmpty();
  }

  // Step 6: three-node cycle — not completable
  //   0 → 1 → 2 → 0
  @Test
  void threeCoursesCycleReturnsEmpty() {
    assertThat(sut.findOrder(3, new int[][] {{1, 0}, {2, 1}, {0, 2}})).isEmpty();
  }

  // Step 7: fan-out tree — root 0 enables three independent leaves; 0 must come
  // first but 1, 2, 3 can be taken in any order
  //   0 → 1
  //   0 → 2
  //   0 → 3
  @Test
  void fanOutSingleRoot() {
    int[][] prerequisites = {{1, 0}, {2, 0}, {3, 0}};
    int[] order = sut.findOrder(4, prerequisites);
    assertValidOrder(4, prerequisites, order);
  }

  // Step 8: fan-in pattern — three independent roots all enable course 3; 3 must
  // be last but 0, 1, 2 can be taken in any order. Dual of step 7.
  //   0 → 3
  //   1 → 3
  //   2 → 3
  @Test
  void fanInSingleSink() {
    int[][] prerequisites = {{3, 0}, {3, 1}, {3, 2}};
    int[] order = sut.findOrder(4, prerequisites);
    assertValidOrder(4, prerequisites, order);
  }

  // Step 9: diamond DAG — combines fan-out and fan-in. 0 fans out to 1 and 2;
  // both feed 3. Multiple valid orders ([0,1,2,3] or [0,2,1,3]). LeetCode example 2.
  //   0 → 1 → 3
  //   0 → 2 → 3
  @Test
  void diamondDagValidOrder() {
    int[][] prerequisites = {{1, 0}, {2, 0}, {3, 1}, {3, 2}};
    int[] order = sut.findOrder(4, prerequisites);
    assertValidOrder(4, prerequisites, order);
  }

  // Step 10: two disconnected DAG components — many valid interleavings
  //   0 → 1     2 → 3
  @Test
  void disconnectedDagsValidOrder() {
    int[][] prerequisites = {{1, 0}, {3, 2}};
    int[] order = sut.findOrder(4, prerequisites);
    assertValidOrder(4, prerequisites, order);
  }

  // Step 11: disconnected with cycle on the node-0 side — DFS starting at 0
  // immediately discovers the cycle
  //   0 ↔ 1     2 → 3
  @Test
  void disconnectedWithCycleReturnsEmpty() {
    assertThat(sut.findOrder(4, new int[][] {{1, 0}, {0, 1}, {3, 2}})).isEmpty();
  }

  // Step 12: cycle isolated from node 0 — node 0 is a disconnected island and the
  // cycle lives in {1, 2, 3}. Forces the algorithm to restart traversal from every
  // unvisited node, not just node 0. Catches implementations that only DFS from 0.
  //   0 (isolated)     1 → 2 → 3 → 1
  @Test
  void cycleNotReachableFromNodeZeroReturnsEmpty() {
    assertThat(sut.findOrder(4, new int[][] {{2, 1}, {3, 2}, {1, 3}})).isEmpty();
  }

  // Step 13: larger curriculum — five courses with single shared root, multiple valid orders
  //   1 → 0, 2 → 0, 3 → 1, 3 → 2, 4 → 3
  @Test
  void largerCurriculumDagValidOrder() {
    int[][] prerequisites = {{1, 0}, {2, 0}, {3, 1}, {3, 2}, {4, 3}};
    int[] order = sut.findOrder(5, prerequisites);
    assertValidOrder(5, prerequisites, order);
  }

  // Asserts that `order` is a valid topological sort over numCourses with the given
  // prerequisites: length matches numCourses, every course appears exactly once and
  // is in range [0, numCourses-1], and for each prerequisite [a, b] (meaning "take b
  // before a") b's position precedes a's. Use this whenever multiple valid orders
  // exist; for unique orders prefer assertThat(...).containsExactly(...) for a
  // tighter assertion.
  private static void assertValidOrder(int numCourses, int[][] prerequisites, int[] order) {
    assertThat(order).as("order length must equal numCourses").hasSize(numCourses);
    Set<Integer> seen = new HashSet<>();
    int[] position = new int[numCourses];
    for (int i = 0; i < order.length; i++) {
      int course = order[i];
      assertThat(course).as("course index out of range").isBetween(0, numCourses - 1);
      assertThat(seen.add(course))
          .as("course %d appeared more than once in the order", course)
          .isTrue();
      position[course] = i;
    }
    for (int[] prerequisite : prerequisites) {
      int after = prerequisite[0];
      int before = prerequisite[1];
      assertThat(position[before])
          .as("prerequisite course %d must come before dependent course %d", before, after)
          .isLessThan(position[after]);
    }
  }
}
