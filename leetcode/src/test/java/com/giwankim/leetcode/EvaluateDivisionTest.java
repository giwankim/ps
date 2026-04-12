package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.withPrecision;

import java.util.List;
import org.junit.jupiter.api.Test;

class EvaluateDivisionTest {
  EvaluateDivision sut = new EvaluateDivision();

  // Step 1: direct edge — simplest case; forces graph lookup for a known pair.
  @Test
  void shouldReturnDirectDivisionResult() {
    List<List<String>> equations = List.of(List.of("a", "b"));
    double[] values = {2.0};
    List<List<String>> queries = List.of(List.of("a", "b"));

    double[] actual = sut.calcEquation(equations, values, queries);

    assertThat(actual).containsExactly(new double[] {2.0}, withPrecision(1e-5));
  }

  // Step 2: reversed query — forces either reciprocal edge storage or inverse
  // traversal. With a/b=2, querying b/a must return 0.5.
  @Test
  void shouldReturnInverseForReversedQuery() {
    List<List<String>> equations = List.of(List.of("a", "b"));
    double[] values = {2.0};
    List<List<String>> queries = List.of(List.of("b", "a"));

    double[] actual = sut.calcEquation(equations, values, queries);

    assertThat(actual).containsExactly(new double[] {0.5}, withPrecision(1e-5));
  }

  // Step 3: one query variable absent from the graph — forces the missing-variable
  // bail on the "to" side.
  @Test
  void shouldReturnNegativeOneWhenOneQueryVariableIsUnknown() {
    List<List<String>> equations = List.of(List.of("a", "b"));
    double[] values = {2.0};
    List<List<String>> queries = List.of(List.of("a", "c"));

    double[] actual = sut.calcEquation(equations, values, queries);

    assertThat(actual).containsExactly(new double[] {-1.0}, withPrecision(1e-5));
  }

  // Step 4: both query variables absent from the graph — the missing-variable
  // bail must fire regardless of which side is checked first.
  @Test
  void shouldReturnNegativeOneWhenBothQueryVariablesAreUnknown() {
    List<List<String>> equations = List.of(List.of("a", "b"));
    double[] values = {2.0};
    List<List<String>> queries = List.of(List.of("x", "y"));

    double[] actual = sut.calcEquation(equations, values, queries);

    assertThat(actual).containsExactly(new double[] {-1.0}, withPrecision(1e-5));
  }

  // Step 5: self-query on a KNOWN variable — identity shortcut returns 1.0
  // when the variable is present in the graph.
  @Test
  void shouldReturnOneForSelfQueryOnKnownVariable() {
    List<List<String>> equations = List.of(List.of("a", "b"));
    double[] values = {2.0};
    List<List<String>> queries = List.of(List.of("a", "a"));

    double[] actual = sut.calcEquation(equations, values, queries);

    assertThat(actual).containsExactly(new double[] {1.0}, withPrecision(1e-5));
  }

  // Step 6: self-query on an UNKNOWN variable — returns -1.0, not 1.0. The
  // identity shortcut from step 5 must only fire for variables present in the
  // graph; an implementation that unconditionally returns 1.0 for from.equals(to)
  // will silently break this case.
  @Test
  void shouldReturnNegativeOneForSelfQueryOnUnknownVariable() {
    List<List<String>> equations = List.of(List.of("a", "b"));
    double[] values = {2.0};
    List<List<String>> queries = List.of(List.of("x", "x"));

    double[] actual = sut.calcEquation(equations, values, queries);

    assertThat(actual).containsExactly(new double[] {-1.0}, withPrecision(1e-5));
  }

  // Step 7: 2-hop transitive query — first test that actually forces graph
  // traversal past a direct edge. a/b=2, b/c=3, query a/c must compute 2*3=6.
  //   a → b → c
  @Test
  void shouldComputeTransitiveDivisionThroughOneIntermediate() {
    List<List<String>> equations = List.of(List.of("a", "b"), List.of("b", "c"));
    double[] values = {2.0, 3.0};
    List<List<String>> queries = List.of(List.of("a", "c"));

    double[] actual = sut.calcEquation(equations, values, queries);

    assertThat(actual).containsExactly(new double[] {6.0}, withPrecision(1e-5));
  }

  // Step 8: disconnected components — traversal must terminate without finding
  // a path and return -1.0 even though both query variables exist in the graph.
  //   a → b     c → d
  @Test
  void shouldHandleDisconnectedComponents() {
    List<List<String>> equations = List.of(List.of("a", "b"), List.of("c", "d"));
    double[] values = {2.0, 4.0};
    List<List<String>> queries = List.of(List.of("a", "d"), List.of("c", "d"));

    double[] actual = sut.calcEquation(equations, values, queries);

    assertThat(actual).containsExactly(new double[] {-1.0, 4.0}, withPrecision(1e-5));
  }

  // Step 9: 3-hop transitive, forward and backward — deeper traversal than
  // step 7, and the d/a query exercises walking inverse edges during traversal.
  //   a → b → c → d
  @Test
  void shouldComputeTransitiveDivisionAcrossLongerPath() {
    List<List<String>> equations = List.of(List.of("a", "b"), List.of("b", "c"), List.of("c", "d"));
    double[] values = {2.0, 3.0, 4.0};
    List<List<String>> queries = List.of(List.of("a", "d"), List.of("d", "a"));

    double[] actual = sut.calcEquation(equations, values, queries);

    assertThat(actual).containsExactly(new double[] {24.0, 1.0 / 24.0}, withPrecision(1e-5));
  }

  // Step 10: triangle with redundant equations — every node has branching
  // out-edges and the same pair is reachable via multiple paths. Exercises
  // visited-tracking during traversal and confirms that redundant input
  // (which the LeetCode problem guarantees is self-consistent) is handled
  // without infinite loops or inconsistent results.
  //         a ── b
  //          \  /
  //           c
  // equations: a/b=2, b/c=3, a/c=6   →   a/c=6, c/a=1/6, b/a=0.5, a/a=1
  @Test
  void shouldResolveQueriesOnCyclicGraph() {
    List<List<String>> equations = List.of(List.of("a", "b"), List.of("b", "c"), List.of("a", "c"));
    double[] values = {2.0, 3.0, 6.0};
    List<List<String>> queries =
        List.of(List.of("a", "c"), List.of("c", "a"), List.of("b", "a"), List.of("a", "a"));

    double[] actual = sut.calcEquation(equations, values, queries);

    assertThat(actual)
        .containsExactly(new double[] {6.0, 1.0 / 6.0, 0.5, 1.0}, withPrecision(1e-5));
  }

  // Step 11: multi-character variable names — data format robustness. Ensures
  // the implementation uses String equality on whole keys rather than assuming
  // single-character variables. "bc" and "cd" are distinct keys, not b/c/c/d.
  @Test
  void shouldHandleMultiCharacterVariableNames() {
    List<List<String>> equations =
        List.of(List.of("a", "b"), List.of("b", "c"), List.of("bc", "cd"));
    double[] values = {1.5, 2.5, 5.0};
    List<List<String>> queries =
        List.of(List.of("a", "c"), List.of("c", "b"), List.of("bc", "cd"), List.of("cd", "bc"));

    double[] actual = sut.calcEquation(equations, values, queries);

    assertThat(actual).containsExactly(new double[] {3.75, 0.4, 5.0, 0.2}, withPrecision(1e-5));
  }
}
