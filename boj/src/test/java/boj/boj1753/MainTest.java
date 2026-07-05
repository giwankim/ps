package boj.boj1753;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * BOJ 1753 최단경로 ("Shortest Path") -- print the shortest-path cost from a start vertex to every
 * vertex of a weighted directed graph.
 *
 * <p>The first input line holds the vertex count {@code V} and the edge count {@code E}; the second
 * holds the start vertex {@code K}; each of the next {@code E} lines is one directed edge {@code u
 * v w} of weight {@code w} from {@code u} to {@code v}. Print {@code V} lines: line {@code i} is
 * the cost of the shortest path from {@code K} to vertex {@code i} -- {@code 0} for the start
 * itself, and {@code INF} when no path exists.
 *
 * <p>Constraints: {@code 1 <= V <= 20,000}; {@code 1 <= E <= 300,000}; {@code 1 <= K <= V};
 * {@code u != v}; {@code w} is a natural number at most 10. Multiple edges may join the same
 * ordered pair of vertices.
 *
 * <ul>
 *   <li><b>The output contract: {@code V} lines in vertex order, 0 for the start, INF when
 *       unreachable.</b> The official sample pins all three rules at once
 *       ({@link #officialSamplePrintsZeroForTheStartAndInfForTheUnreachableVertex(StdOut)}), the
 *       smallest graph pins the zero-then-weight shape
 *       ({@link #singleEdgeFromTheStartPrintsZeroThenTheEdgeWeight(StdOut)}), and a start at
 *       {@code K = V} keeps rows keyed by vertex number, not by distance
 *       ({@link #startAtTheTopNumberedVertexKeepsRowsInVertexOrder(StdOut)}).
 *   <li><b>Edges are directed.</b> The lone edge cannot be walked against its direction
 *       ({@link #singleEdgeIntoTheStartLeavesItsSourceUnreachable(StdOut)}), and opposite edges
 *       between one pair keep separate weights
 *       ({@link #oppositeEdgesBetweenTheSamePairKeepTheirOwnWeights(StdOut)}).
 *   <li><b>Parallel edges are allowed and the cheapest wins.</b> Weights 7, 3, 9 on the same
 *       ordered pair must yield 3
 *       ({@link #parallelEdgesUseTheMinimumWeightRegardlessOfInputOrder(StdOut)}).
 *   <li><b>Relaxation: cheaper detours win.</b> A two-hop detour beats the direct edge
 *       ({@link #cheaperTwoHopDetourBeatsTheDirectEdge(StdOut)}), three light hops beat the heavy
 *       shortcut ({@link #longChainOfLightEdgesBeatsTheHeavyShortcut(StdOut)}), and an improvement
 *       found later must flow on to vertices past it
 *       ({@link #improvedDistanceMustPropagateToDownstreamVertices(StdOut)}).
 *   <li><b>Cycles terminate.</b> The directed triangle keeps the start at 0 and the traversal
 *       finite ({@link #directedCycleTerminatesAndKeepsTheStartAtZero(StdOut)}).
 *   <li><b>Unreachable vertices print INF.</b> A start with no outgoing edge leaves everything else
 *       INF even where edges exist among the unreachable vertices
 *       ({@link #edgesAmongUnreachableVerticesDoNotLeakDistances(StdOut)}), and a disjoint second
 *       chain stays INF while the start's own chain gets distances
 *       ({@link #onlyTheStartsComponentGetsFiniteDistances(StdOut)}).
 *   <li><b>Constraint bounds.</b> An all-tens chain over 20,000 vertices reaches 199,990, the
 *       largest finite distance the constraints allow
 *       ({@link #maximumWeightChainAcrossAllVerticesReachesDistance199990()}), and a graph at the
 *       full {@code V = 20,000, E = 300,000} scale must finish within the time limit
 *       ({@link #maximumSizeGraphFinishesWithinTheTimeLimit()}).
 *   <li><b>Randomized cross-check.</b> Tiny dense digraphs
 *       ({@link #randomSmallGraphsMatchTheBellmanFordOracle()}) and medium digraphs across sparse
 *       to well-connected densities ({@link #randomMediumGraphsMatchTheBellmanFordOracle()}) are
 *       compared line-for-line with an independent Bellman-Ford oracle.
 * </ul>
 *
 * <p>The oracle ({@link #bellmanFordOracle(int, int, List)}) relaxes the raw edge list over and
 * over with no priority queue and no visitation order -- a genuinely different evaluation order
 * from the Dijkstra an intended solution runs, so agreement is independent evidence rather than the
 * same algorithm checked against itself. At the judge-scale bound Bellman-Ford's {@code O(V * E)}
 * work is out of reach, so the maximum-size test instead constructs a graph whose answer is known:
 * a unit-weight chain plus random backward edges that provably never improve a distance.
 */
class MainTest {

  // --- Official sample. ---

  // The official sample: V = 5, E = 6, K = 1. Vertex 4 is cheapest via 1 -> 2 -> 4 (7), not the
  // two-edge 1 -> 3 -> 4 route (9), and vertex 5 only has an outgoing edge, so it is unreachable.
  @Test
  @StdIo({"5 6", "1", "5 1 1", "1 2 2", "1 3 3", "2 3 4", "2 4 5", "3 4 6"})
  void officialSamplePrintsZeroForTheStartAndInfForTheUnreachableVertex(StdOut out)
      throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("0", "2", "3", "7", "INF");
  }

  // --- The output contract on the smallest graphs. ---

  // The smallest meaningful instance: two vertices, one edge out of the start. Line 1 is the
  // start's own 0, line 2 the edge weight.
  @Test
  @StdIo({"2 1", "1", "1 2 5"})
  void singleEdgeFromTheStartPrintsZeroThenTheEdgeWeight(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("0", "5");
  }

  // The same edge with the start on its head: 1 -> 2 cannot be walked backward, so vertex 1 is
  // INF. An undirected reading prints 5 instead.
  @Test
  @StdIo({"2 1", "2", "1 2 5"})
  void singleEdgeIntoTheStartLeavesItsSourceUnreachable(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("INF", "0");
  }

  // Start at the top-numbered vertex K = V = 3 on the path 3 -> 1 -> 2. Rows stay in vertex order
  // (2, 5, 0), not discovery or distance order, and the 0 lands on the last line.
  @Test
  @StdIo({"3 2", "3", "3 1 2", "1 2 3"})
  void startAtTheTopNumberedVertexKeepsRowsInVertexOrder(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("2", "5", "0");
  }

  // --- Edges are directed. ---

  // Both directions between the same pair carry different weights (1 -> 2 costs 4, 2 -> 1 costs
  // 9). From start 2 only the 9-edge applies; a solver that merges the pair into one undirected
  // edge answers 4.
  @Test
  @StdIo({"2 2", "2", "1 2 4", "2 1 9"})
  void oppositeEdgesBetweenTheSamePairKeepTheirOwnWeights(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("9", "0");
  }

  // --- Parallel edges between the same pair. ---

  // The problem explicitly allows multi-edges: 1 -> 2 appears with weights 7, 3, and 9. The
  // answer is the minimum 3 -- an adjacency matrix that keeps the first sees 7, one that
  // overwrites with the last sees 9.
  @Test
  @StdIo({"2 3", "1", "1 2 7", "1 2 3", "1 2 9"})
  void parallelEdgesUseTheMinimumWeightRegardlessOfInputOrder(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("0", "3");
  }

  // --- Relaxation: cheaper detours must win. ---

  // The direct edge 1 -> 2 (10) loses to the detour 1 -> 3 -> 2 (1 + 2 = 3). A solver that
  // finalizes a vertex the moment any edge reaches it keeps the 10.
  @Test
  @StdIo({"3 3", "1", "1 2 10", "1 3 1", "3 2 2"})
  void cheaperTwoHopDetourBeatsTheDirectEdge(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("0", "3", "1");
  }

  // Three light hops (1+1+1) beat the one heavy shortcut (10) to vertex 4: fewest-edges order
  // (plain BFS) is not shortest-distance order.
  @Test
  @StdIo({"4 4", "1", "1 4 10", "1 2 1", "2 3 1", "3 4 1"})
  void longChainOfLightEdgesBeatsTheHeavyShortcut(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("0", "1", "2", "3");
  }

  // Vertex 2 is first seen at distance 5 (1 -> 2) and later improved to 2 (1 -> 3 -> 2). The
  // improvement must flow on to vertex 4 (2 + 1 = 3); a solver that processed 2 at its stale
  // distance 5 hands 4 a 6.
  @Test
  @StdIo({"4 4", "1", "1 2 5", "1 3 1", "3 2 1", "2 4 1"})
  void improvedDistanceMustPropagateToDownstreamVertices(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("0", "2", "1", "3");
  }

  // --- Cycles terminate. ---

  // The directed triangle 1 -> 2 -> 3 -> 1: walking around the cycle back to the start (cost 9)
  // must not overwrite its 0, and the traversal must stop instead of circling forever.
  @Test
  @StdIo({"3 3", "1", "1 2 2", "2 3 3", "3 1 4"})
  void directedCycleTerminatesAndKeepsTheStartAtZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("0", "2", "5");
  }

  // --- Unreachable vertices print INF. ---

  // No edge leaves the start (vertex 2), so every other vertex is INF -- including 3 and 4,
  // which the edges 1 -> 3 -> 4 connect only to the unreachable side. Relaxation must never run
  // from a vertex still at infinity.
  @Test
  @StdIo({"4 2", "2", "1 3 2", "3 4 1"})
  void edgesAmongUnreachableVerticesDoNotLeakDistances(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("INF", "0", "INF", "INF");
  }

  // Two disjoint chains: 1 -> 2 -> 3 holds the start and gets real distances; 4 -> 5 -> 6 has
  // internal edges but no connection from the start's side, so all three print INF.
  @Test
  @StdIo({"6 4", "1", "1 2 3", "2 3 4", "4 5 1", "5 6 1"})
  void onlyTheStartsComponentGetsFiniteDistances(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("0", "3", "7", "INF", "INF", "INF");
  }

  // --- Constraint bounds. ---

  // A chain 1 -> 2 -> ... -> 20,000 with every edge at the maximum weight 10 pushes the farthest
  // vertex to 199,990 -- the largest finite distance the constraints allow. An INF sentinel or a
  // distance type too small for six digits corrupts the tail of the output.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void maximumWeightChainAcrossAllVerticesReachesDistance199990() throws IOException {
    int v = 20000;
    List<int[]> edges = new ArrayList<>();
    for (int u = 1; u < v; u++) {
      edges.add(new int[] {u, u + 1, 10});
    }
    assertThat(runMain(buildInput(v, 1, edges))).isEqualTo(chainDistances(v, 10));
  }

  // The maximum-size graph: V = 20,000 and E = 300,000. A unit-weight forward chain fixes the
  // answer (vertex i sits at distance i - 1), and the other 280,001 edges are random backward
  // jumps u -> v with u > v, which can never improve a distance: dist[u] + w >= u > v - 1 =
  // dist[v]. Bellman-Ford as an oracle would need V * E = 6 * 10^9 relaxations here, so the
  // expected output comes from the construction itself; the judge-scale edge count and output
  // volume are what is under test.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void maximumSizeGraphFinishesWithinTheTimeLimit() throws IOException {
    Random rng = new Random(175300L); // fixed seed -> deterministic across JVMs.
    int v = 20000;
    int e = 300000;
    List<int[]> edges = new ArrayList<>();
    for (int u = 1; u < v; u++) {
      edges.add(new int[] {u, u + 1, 1});
    }
    while (edges.size() < e) {
      int from = 2 + rng.nextInt(v - 1); // 2..v
      int to = 1 + rng.nextInt(from - 1); // 1..from-1, strictly backward
      edges.add(new int[] {from, to, 1 + rng.nextInt(10)});
    }
    Collections.shuffle(edges, rng);
    assertThat(runMain(buildInput(v, 1, edges))).isEqualTo(chainDistances(v, 1));
  }

  // --- Randomized cross-check against the Bellman-Ford oracle. ---

  // Hundreds of tiny random digraphs (V <= 8, up to 14 edges) compared line-for-line with the
  // oracle. Dense little boards pack parallel edges, two-way pairs, cycles, and unreachable
  // vertices into shapes no hand case anticipates.
  @Test
  void randomSmallGraphsMatchTheBellmanFordOracle() throws IOException {
    Random rng = new Random(1753L); // fixed seed -> deterministic across JVMs.
    for (int trial = 0; trial < 300; trial++) {
      int v = 2 + rng.nextInt(7); // 2..8, so an edge with distinct endpoints always exists
      int e = 1 + rng.nextInt(14); // 1..14
      int k = 1 + rng.nextInt(v);
      List<int[]> edges = randomEdges(v, e, rng);
      String input = buildInput(v, k, edges);
      assertThat(runMain(input))
          .as("v=%d e=%d k=%d%ninput:%n%s", v, e, k, input)
          .isEqualTo(bellmanFordOracle(v, k, edges));
    }
  }

  // Medium random digraphs (V = 50..250) across densities from E = V/2 (mostly INF) to E = 4V
  // (well connected). Big enough for real relaxation cascades and stale-entry churn no tiny board
  // can produce, small enough that the O(V * E) oracle stays affordable.
  @Test
  void randomMediumGraphsMatchTheBellmanFordOracle() throws IOException {
    Random rng = new Random(17530L); // fixed seed -> deterministic across JVMs.
    double[] densities = {0.5, 1, 2, 4};
    for (int trial = 0; trial < 60; trial++) {
      int v = 50 + rng.nextInt(201); // 50..250
      int e = (int) (v * densities[trial % densities.length]);
      int k = 1 + rng.nextInt(v);
      List<int[]> edges = randomEdges(v, e, rng);
      String input = buildInput(v, k, edges);
      assertThat(runMain(input))
          .as("v=%d e=%d k=%d", v, e, k)
          .isEqualTo(bellmanFordOracle(v, k, edges));
    }
  }

  /**
   * Independent oracle: Bellman-Ford relaxes the raw edge list pass after pass (stopping early once
   * a pass changes nothing) with no priority queue and no visitation order, so its evaluation order
   * is genuinely different from the Dijkstra an intended solution runs.
   *
   * @implNote {@code O(V * E)} time and {@code O(V)} space, where {@code V} is the vertex count and
   *     {@code E} is the edge count.
   */
  private static String bellmanFordOracle(int v, int k, List<int[]> edges) {
    int[] dist = new int[v + 1];
    Arrays.fill(dist, Integer.MAX_VALUE);
    dist[k] = 0;
    for (int pass = 1; pass < v; pass++) {
      boolean changed = false;
      for (int[] edge : edges) {
        if (dist[edge[0]] != Integer.MAX_VALUE && dist[edge[0]] + edge[2] < dist[edge[1]]) {
          dist[edge[1]] = dist[edge[0]] + edge[2];
          changed = true;
        }
      }
      if (!changed) {
        break;
      }
    }
    StringBuilder sb = new StringBuilder();
    for (int i = 1; i <= v; i++) {
      if (i > 1) {
        sb.append('\n');
      }
      if (dist[i] == Integer.MAX_VALUE) {
        sb.append("INF");
      } else {
        sb.append(dist[i]);
      }
    }
    return sb.toString();
  }

  /**
   * Random directed edges over distinct endpoints with weights 1..10; parallel edges, two-way
   * pairs, and unreachable vertices all arise freely from the uniform draw.
   */
  private static List<int[]> randomEdges(int v, int e, Random rng) {
    List<int[]> edges = new ArrayList<>();
    for (int i = 0; i < e; i++) {
      int from = 1 + rng.nextInt(v);
      int to = 1 + rng.nextInt(v - 1);
      if (to >= from) {
        to++;
      }
      edges.add(new int[] {from, to, 1 + rng.nextInt(10)});
    }
    return edges;
  }

  /**
   * Builds BOJ 1753 input: {@code "V E"} on the first line, the start vertex {@code K} on the
   * second, then one {@code "u v w"} line per directed edge.
   */
  private static String buildInput(int v, int k, List<int[]> edges) {
    StringBuilder sb = new StringBuilder();
    sb.append(v).append(' ').append(edges.size()).append('\n').append(k).append('\n');
    for (int[] edge : edges) {
      sb.append(edge[0]).append(' ').append(edge[1]).append(' ').append(edge[2]).append('\n');
    }
    return sb.toString();
  }

  /**
   * The expected output when vertex 1 starts a chain whose edges all weigh {@code step}: vertex
   * {@code i} sits at distance {@code (i - 1) * step}.
   */
  private static String chainDistances(int v, int step) {
    StringBuilder sb = new StringBuilder();
    for (int i = 1; i <= v; i++) {
      if (i > 1) {
        sb.append('\n');
      }
      sb.append((i - 1) * step);
    }
    return sb.toString();
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
