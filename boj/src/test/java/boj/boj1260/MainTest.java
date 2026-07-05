package boj.boj1260;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * BOJ 1260 DFS와 BFS ("DFS and BFS") -- print a graph's DFS visit order, then its BFS visit order.
 *
 * <p>An undirected graph has vertices numbered {@code 1..N}. The first input line holds the vertex
 * count {@code N}, the edge count {@code M}, and the start vertex {@code V}; each of the next
 * {@code M} lines names the two endpoints of one edge. Print the vertices in DFS visit order from
 * {@code V} on the first line and in BFS visit order from {@code V} on the second, space-separated.
 * Whenever several unvisited neighbors are available, the lowest-numbered one is visited first, and
 * a traversal stops when nothing visitable remains -- vertices unreachable from {@code V} are never
 * printed.
 *
 * <p>Constraints: {@code 1 <= N <= 1,000}; {@code 1 <= M <= 10,000}. Edges are bidirectional, and
 * the same pair of vertices may be joined by more than one edge.
 *
 * <ul>
 *   <li><b>Output shape: DFS line first, BFS line second, both starting at {@code V}.</b> The three
 *       official samples pin both lines, including the top-bound start vertex {@code V = N = 1,000}
 *       ({@link #officialSampleOneDfsLeavesVertexThreeForLast(StdOut)},
 *       {@link #officialSampleTwoStartsAwayFromVertexOne(StdOut)},
 *       {@link #officialSampleThreeVisitsOnlyTheStartVertexNeighborAtTheTopBound(StdOut)}), and the
 *       smallest meaningful graph separates the two lines at minimal scale
 *       ({@link #singleEdgePairIsVisitedInTheSameOrderByBothTraversals(StdOut)}).
 *   <li><b>The lowest-numbered neighbor goes first.</b> A star whose edges arrive in descending
 *       neighbor order must still be visited ascending, isolating a solver that walks adjacency in
 *       input order
 *       ({@link #neighborsListedInDescendingInputOrderAreStillVisitedAscending(StdOut)}).
 *   <li><b>Edges are bidirectional.</b> A path given only "toward" the start vertex must still be
 *       walkable away from it; a directed reading of the edges would strand {@code V} alone
 *       ({@link #edgesAreTraversableAgainstTheirListedDirection(StdOut)}).
 *   <li><b>Duplicate edges are allowed but must not repeat visits.</b> The same pair joined three
 *       times (in both orientations) still yields each vertex exactly once
 *       ({@link #duplicateEdgesDoNotMakeAVertexAppearTwice(StdOut)}).
 *   <li><b>DFS dives, BFS sweeps levels.</b> A chain plus a spur makes the two lines differ: DFS
 *       runs the chain to its end before the spur, BFS drains the start vertex's level first
 *       ({@link #dfsDivesDownTheChainWhileBfsDrainsTheFirstLevelFirst(StdOut)}); a two-branch tree
 *       forces DFS to backtrack to the root between branches
 *       ({@link #dfsBacktracksToTheRootBeforeTakingTheSecondBranch(StdOut)}); a four-cycle checks
 *       that a visited start vertex is never re-entered from the far side
 *       ({@link #cycleIsWalkedAroundOnceWithoutRevisitingTheStart(StdOut)}).
 *   <li><b>Only vertices reachable from {@code V} appear.</b> A start vertex with no incident edges
 *       prints just itself ({@link #startVertexWithNoIncidentEdgesPrintsOnlyItself(StdOut)}), and a
 *       second component is left out of both lines
 *       ({@link #unreachableComponentIsLeftOutOfBothTraversals(StdOut)}).
 *   <li><b>Constraint bounds.</b> A path over all 1,000 vertices drives a recursive DFS to full
 *       depth ({@link #maximumLengthPathIsWalkedToFullDepth()}), and a random graph at the maximum
 *       size {@code N = 1,000, M = 10,000} must finish within the time limit
 *       ({@link #denseMaximumSizeGraphMatchesTheMatrixOracle()}).
 * </ul>
 *
 * <p>The hand-worked answers are cross-checked against an independent adjacency-matrix oracle whose
 * ascending column scan realizes the lowest-neighbor-first rule by construction instead of via
 * sorted adjacency lists ({@link #oracleAnswer(int, int, boolean[][])}). The randomized sweep
 * ({@link #randomSmallGraphsMatchTheMatrixOracle()}) drives it across hundreds of shapes packing in
 * duplicate edges, unreachable vertices, and unsorted input orders.
 */
class MainTest {

  // --- Official samples. ---

  // Official sample 1: n = 4, m = 5, v = 1 on a near-complete graph. DFS dives 1 -> 2 -> 4 and
  // only then reaches 3, while BFS lists 1's neighbors 2 3 4 in ascending order, so the two lines
  // differ on the same input.
  @Test
  @StdIo({"4 5 1", "1 2", "1 3", "1 4", "2 4", "3 4"})
  void officialSampleOneDfsLeavesVertexThreeForLast(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("1 2 4 3", "1 2 3 4");
  }

  // Official sample 2: the start vertex is 3, not 1, and the five edges arrive unsorted
  // (5 4, 5 2, 1 2, 3 4, 3 1). A solver that walks adjacency in input order visits the right
  // vertices in the wrong order, so this pins the sort-before-traverse step on a real board.
  @Test
  @StdIo({"5 5 3", "5 4", "5 2", "1 2", "3 4", "3 1"})
  void officialSampleTwoStartsAwayFromVertexOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("3 1 2 5 4", "3 1 4 2 5");
  }

  // Official sample 3: n = 1,000 vertices but a single edge 999-1000, starting at v = n = 1000.
  // Both traversals print just the two-vertex component, guarding the V = N boundary and showing
  // the other 998 vertices never appear.
  @Test
  @StdIo({"1000 1 1000", "999 1000"})
  void officialSampleThreeVisitsOnlyTheStartVertexNeighborAtTheTopBound(StdOut out)
      throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("1000 999", "1000 999");
  }

  // --- Smallest graphs. ---

  // The smallest meaningful graph: two vertices, one edge. Both traversals cross the edge once,
  // pinning the two-line output shape at minimal scale.
  @Test
  @StdIo({"2 1 1", "1 2"})
  void singleEdgePairIsVisitedInTheSameOrderByBothTraversals(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("1 2", "1 2");
  }

  // M >= 1 forces an edge to exist somewhere, but it need not touch the start vertex: with the
  // only edge joining 2 and 3, a search from 1 finds nothing and each line is just "1".
  @Test
  @StdIo({"3 1 1", "2 3"})
  void startVertexWithNoIncidentEdgesPrintsOnlyItself(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("1", "1");
  }

  // --- The lowest-numbered neighbor is visited first. ---

  // A star centered on 1 whose edges are listed in descending neighbor order (4, 3, 2). Both
  // traversals must still emit 1 2 3 4; a solver that keeps adjacency in input order prints
  // 1 4 3 2 instead.
  @Test
  @StdIo({"4 3 1", "1 4", "1 3", "1 2"})
  void neighborsListedInDescendingInputOrderAreStillVisitedAscending(StdOut out)
      throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("1 2 3 4", "1 2 3 4");
  }

  // --- Edges are bidirectional. ---

  // The path 1-2-3 is given with every edge pointing "toward" the start vertex (2 1, then 3 2).
  // A directed reading gives vertex 1 no outgoing edge and prints just "1"; the required answer
  // walks the path to its far end.
  @Test
  @StdIo({"3 2 1", "2 1", "3 2"})
  void edgesAreTraversableAgainstTheirListedDirection(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("1 2 3", "1 2 3");
  }

  // --- Duplicate edges never repeat a visit. ---

  // The pair 1-2 appears three times (twice as 1 2, once flipped as 2 1) alongside 2-3. The
  // problem explicitly allows multi-edges; each vertex must still appear exactly once per line.
  @Test
  @StdIo({"3 4 1", "1 2", "1 2", "2 1", "2 3"})
  void duplicateEdgesDoNotMakeAVertexAppearTwice(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("1 2 3", "1 2 3");
  }

  // --- DFS dives while BFS sweeps levels. ---

  // A chain 1-2-3-4 plus the spur 1-5. DFS runs the chain to its end before backtracking to the
  // spur (1 2 3 4 5); BFS drains vertex 1's level first, so 5 comes right after 2 (1 2 5 3 4).
  // The maximal disagreement between the lines pins depth-first against level order.
  @Test
  @StdIo({"5 4 1", "1 2", "2 3", "3 4", "1 5"})
  void dfsDivesDownTheChainWhileBfsDrainsTheFirstLevelFirst(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("1 2 3 4 5", "1 2 5 3 4");
  }

  // A two-branch tree rooted at 1 (branch 2 -> 4, branch 3 -> 5). After exhausting the branch
  // under 2, DFS must backtrack to the root and enter the branch under 3 (1 2 4 3 5); BFS visits
  // by level (1 2 3 4 5). Guards the backtracking step a purely iterative "dive" would miss.
  @Test
  @StdIo({"5 4 1", "1 2", "2 4", "1 3", "3 5"})
  void dfsBacktracksToTheRootBeforeTakingTheSecondBranch(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("1 2 4 3 5", "1 2 3 4 5");
  }

  // The four-cycle 1-2-3-4-1 started at 2. DFS steps to the smaller neighbor 1, wraps around the
  // far side through 4, and must stop at already-visited 2 (2 1 4 3); BFS expands both sides of
  // the cycle a level at a time (2 1 3 4). Checks a visited vertex is never re-entered.
  @Test
  @StdIo({"4 4 2", "1 2", "2 3", "3 4", "4 1"})
  void cycleIsWalkedAroundOnceWithoutRevisitingTheStart(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("2 1 4 3", "2 1 3 4");
  }

  // --- Only the start vertex's component is printed. ---

  // Two components: 1-2-3 and 4-5-6. Starting at 4 must print exactly its own component in both
  // lines; vertices 1..3 exist but are unreachable and never appear.
  @Test
  @StdIo({"6 4 4", "1 2", "2 3", "4 5", "5 6"})
  void unreachableComponentIsLeftOutOfBothTraversals(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("4 5 6", "4 5 6");
  }

  // --- Constraint bounds. ---

  // All 1,000 vertices strung into the single path 1-2-...-1000, started at 1. Both lines are the
  // full ascending sequence, and a recursive DFS descends 1,000 frames deep -- the deepest
  // recursion the constraints allow.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void maximumLengthPathIsWalkedToFullDepth() throws IOException {
    int n = 1000;
    StringBuilder input = new StringBuilder().append(n).append(" 999 1\n");
    for (int v = 1; v < n; v++) {
      input.append(v).append(' ').append(v + 1).append('\n');
    }
    StringBuilder line = new StringBuilder();
    for (int v = 1; v <= n; v++) {
      if (v > 1) {
        line.append(' ');
      }
      line.append(v);
    }
    assertThat(runMain(input.toString())).isEqualTo(line + "\n" + line);
  }

  // A random multigraph at the maximum size, n = 1,000 and m = 10,000, cross-checked against the
  // matrix oracle and clocked. Dense random edges land duplicate pairs and leave stragglers
  // unreachable, exercising every rule at once at judge scale.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void denseMaximumSizeGraphMatchesTheMatrixOracle() throws IOException {
    Random rng = new Random(126000L); // fixed seed -> deterministic across JVMs.
    int n = 1000;
    int m = 10000;
    int v = 1 + rng.nextInt(n);
    boolean[][] adj = new boolean[n + 1][n + 1];
    String input = randomInput(n, m, v, adj, rng);
    assertThat(runMain(input)).isEqualTo(oracleAnswer(n, v, adj));
  }

  // --- Randomized cross-check against the matrix oracle. ---

  // Hundreds of tiny random graphs (n <= 8) compared line-for-line with the oracle. Small dense
  // boards pack in duplicate edges, unsorted input orders, cycles, and unreachable vertices, so
  // this sweep re-exercises every hand-picked rule above across shapes no hand case anticipates.
  @Test
  void randomSmallGraphsMatchTheMatrixOracle() throws IOException {
    Random rng = new Random(1260L); // fixed seed -> deterministic across JVMs.
    for (int trial = 0; trial < 300; trial++) {
      int n = 2 + rng.nextInt(7); // 2..8, so an edge with distinct endpoints always exists
      int m = 1 + rng.nextInt(12); // 1..12
      int v = 1 + rng.nextInt(n);
      boolean[][] adj = new boolean[n + 1][n + 1];
      String input = randomInput(n, m, v, adj, rng);
      assertThat(runMain(input))
          .as("n=%d m=%d v=%d%ninput:%n%s", n, m, v, input)
          .isEqualTo(oracleAnswer(n, v, adj));
    }
  }

  /**
   * Independent oracle: DFS and BFS over an adjacency matrix, scanning candidate neighbors in
   * ascending vertex order so the lowest-neighbor-first rule falls out of the scan itself rather
   * than a sorted adjacency list. Returns the two output lines joined by a single newline.
   *
   * @implNote {@code O(n^2)} time per traversal, where {@code n} is the vertex count: each visited
   *     vertex scans its full matrix row.
   */
  private static String oracleAnswer(int n, int v, boolean[][] adj) {
    StringBuilder dfs = new StringBuilder();
    matrixDfs(v, n, adj, new boolean[n + 1], dfs);

    StringBuilder bfs = new StringBuilder();
    boolean[] visited = new boolean[n + 1];
    Deque<Integer> queue = new ArrayDeque<>();
    queue.add(v);
    visited[v] = true;
    while (!queue.isEmpty()) {
      int cur = queue.poll();
      if (bfs.length() > 0) {
        bfs.append(' ');
      }
      bfs.append(cur);
      for (int next = 1; next <= n; next++) {
        if (adj[cur][next] && !visited[next]) {
          visited[next] = true;
          queue.add(next);
        }
      }
    }
    return dfs + "\n" + bfs;
  }

  private static void matrixDfs(
      int cur, int n, boolean[][] adj, boolean[] visited, StringBuilder sb) {
    visited[cur] = true;
    if (sb.length() > 0) {
      sb.append(' ');
    }
    sb.append(cur);
    for (int next = 1; next <= n; next++) {
      if (adj[cur][next] && !visited[next]) {
        matrixDfs(next, n, adj, visited, sb);
      }
    }
  }

  /**
   * Builds BOJ 1260 input with {@code m} random edges over distinct endpoints (self-loops are not
   * part of the problem; duplicate pairs are, and the random draw produces them freely), marking
   * each edge in the caller's adjacency matrix.
   */
  private static String randomInput(int n, int m, int v, boolean[][] adj, Random rng) {
    StringBuilder input = new StringBuilder();
    input.append(n).append(' ').append(m).append(' ').append(v).append('\n');
    for (int e = 0; e < m; e++) {
      int a = 1 + rng.nextInt(n);
      int b = 1 + rng.nextInt(n - 1);
      if (b >= a) {
        b++;
      }
      adj[a][b] = true;
      adj[b][a] = true;
      input.append(a).append(' ').append(b).append('\n');
    }
    return input.toString();
  }

  private static String runMain(String input) throws IOException {
    InputStream originalIn = System.in;
    PrintStream originalOut = System.out;
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    try {
      System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
      System.setOut(new PrintStream(out, true, StandardCharsets.UTF_8));

      Main.main(new String[0]);

      return out.toString(StandardCharsets.UTF_8).trim();
    } finally {
      System.setIn(originalIn);
      System.setOut(originalOut);
    }
  }
}
