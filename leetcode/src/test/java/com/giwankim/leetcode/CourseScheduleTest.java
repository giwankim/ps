package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CourseScheduleTest {
  CourseSchedule sut = new CourseSchedule();

  // Step 1: zero courses, no prerequisites — vacuously completable
  @Test
  void zeroCoursesNoPrerequisites() {
    assertThat(sut.canFinish(0, new int[][] {})).isTrue();
  }

  // Step 2: one course, no prerequisites — trivially completable
  @Test
  void oneCourseNoPrerequisites() {
    assertThat(sut.canFinish(1, new int[][] {})).isTrue();
  }

  // Step 3: two disconnected courses — completable
  @Test
  void twoCoursesNoPrerequisites() {
    assertThat(sut.canFinish(2, new int[][] {})).isTrue();
  }

  // Step 4: simplest DAG — take 0, then 1 (LeetCode example 1)
  //   0 → 1
  @Test
  void twoCoursesOnePrerequisite() {
    assertThat(sut.canFinish(2, new int[][] {{1, 0}})).isTrue();
  }

  // Step 5: degenerate self-loop — course depends on itself, not completable
  //   0 ↻
  @Test
  void selfLoopNotCompletable() {
    assertThat(sut.canFinish(1, new int[][] {{0, 0}})).isFalse();
  }

  // Step 6: smallest two-node cycle — mutually-dependent courses (LeetCode example 2)
  //   0 ↔ 1
  @Test
  void twoCoursesMutualPrerequisites() {
    assertThat(sut.canFinish(2, new int[][] {{1, 0}, {0, 1}})).isFalse();
  }

  // Step 7: linear chain of three — completable, exercises recursion beyond depth 1
  //   0 → 1 → 2
  @Test
  void threeCoursesLinearChain() {
    assertThat(sut.canFinish(3, new int[][] {{1, 0}, {2, 1}})).isTrue();
  }

  // Step 8: three-node cycle — exercises cycle detection beyond a direct back-edge
  //   0 → 1 → 2 → 0
  @Test
  void threeCoursesCycle() {
    assertThat(sut.canFinish(3, new int[][] {{1, 0}, {2, 1}, {0, 2}})).isFalse();
  }

  // Step 9: diamond DAG — 0 fans out to 1 and 2, both feed 3. Exercises VISITED
  // memoization: when DFS reaches 3 via the 1-branch, the 2-branch must short-circuit
  // on the already-VISITED 3 instead of re-traversing.
  //   0 → 1 → 3
  //   0 → 2 → 3
  @Test
  void diamondDagCompletable() {
    assertThat(sut.canFinish(4, new int[][] {{1, 0}, {2, 0}, {3, 1}, {3, 2}})).isTrue();
  }

  // Step 10: two disconnected DAG components — exercises starting DFS from every
  // node in the outer loop, and confirms a finished DFS leaves nodes marked VISITED
  // (not VISITING) so the next outer-loop iteration doesn't see a phantom cycle.
  //   0 → 1     2 → 3
  @Test
  void disconnectedDagsCompletable() {
    assertThat(sut.canFinish(4, new int[][] {{1, 0}, {3, 2}})).isTrue();
  }

  // Step 11: disconnected components, cycle isolated to one — overall not completable
  //   0 ↔ 1     2 → 3
  @Test
  void disconnectedWithCycleNotCompletable() {
    assertThat(sut.canFinish(4, new int[][] {{1, 0}, {0, 1}, {3, 2}})).isFalse();
  }

  // Step 12: larger LeetCode-style DAG — five-course curriculum, single shared root
  //   1 → 0, 2 → 0, 3 → 1, 3 → 2, 4 → 3
  // (course 0 is the entry; course 4 requires the whole curriculum)
  @Test
  void largerCurriculumDagCompletable() {
    assertThat(sut.canFinish(5, new int[][] {{1, 0}, {2, 0}, {3, 1}, {3, 2}, {4, 3}})).isTrue();
  }
}
