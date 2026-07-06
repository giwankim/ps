package boj.boj1238;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * BOJ 1238 파티 ("Party") -- the longest shortest round trip to a party town on one-way roads.
 *
 * <p>Each of {@code N} towns ({@code 1 <= N <= 1,000}) houses one student, and all of them attend a
 * party in town {@code X} ({@code 1 <= X <= N}). The {@code M} roads ({@code 1 <= M <= 10,000}) are
 * one-way, and road {@code i} takes {@code T_i} ({@code 1 <= T_i <= 100}) to walk. Every student
 * walks a shortest route to the party and a shortest route back home, and because the roads are
 * one-way the two routes may differ. The input is {@code N M X} followed by {@code M} lines of
 * {@code start end T}; no road starts and ends in the same town, each ordered town pair carries at
 * most one road, and every student is guaranteed to be able to reach {@code X} and return home.
 * Print the largest round-trip time over the {@code N} students -- the maximum over towns {@code i}
 * of {@code dist(i -> X) + dist(X -> i)}.
 *
 * <ul>
 *   <li><b>The statement sample.</b> The published example: the student in town 4 walks 4 -> 2
 *       directly but returns through 2 -> 1 -> 3 -> 4, for the worst round trip of 3 + 7 = 10. The
 *       header values 4, 8, 2 are pairwise distinct, so a solver that permutes {@code N M X} also
 *       trips here, and the expected output is the time, not the student's town number
 *       ({@link #statementSampleLongestRoundTripIsTen(StdOut)}).
 *   <li><b>One-way roads.</b> A one-way triangle whose return legs must go the long way around --
 *       an undirected solver halves the trip -- and a two-town pair whose opposite roads carry
 *       different weights, which symmetric or overwriting adjacency storage corrupts
 *       ({@link #oneWayTriangleForcesTheLongWayHome(StdOut)},
 *       {@link #oppositeRoadsKeepTheirOwnWeights(StdOut)}).
 *   <li><b>Shortest legs need relaxation.</b> The direct road costs 10 but a two-road detour costs
 *       2; a fewest-hops search or a solver that never improves an already-queued town keeps the
 *       expensive road ({@link #aCheapDetourBeatsTheDirectRoad(StdOut)}).
 *   <li><b>Per-student round trips.</b> The smallest party -- one road out, one road back, and the
 *       host walking nothing -- and a star where the winning student holds neither the longest
 *       outbound leg nor the longest return leg, so pairing leg maxima from different students
 *       overshoots ({@link #smallestPartyIsOneRoadOutAndOneRoadBack(StdOut)},
 *       {@link #roundTripsAreSummedPerStudentNotPerLeg(StdOut)}).
 *   <li><b>Full-size road networks.</b> A 1,000-town chain of weight-100 roads both ways (the far
 *       student rides 999 roads each way, 199,800 total) and a forward-only loop that spends the
 *       full 10,000-road budget, where every non-host student's round trip is exactly one lap
 *       ({@link #maximumChainAccumulatesTheFullRoundTrip()},
 *       {@link #fullRoadBudgetLoopCostsEveryStudentOneLap()}).
 *   <li><b>Sweeps against the Floyd-Warshall oracle.</b> Every two- and three-town road layout over
 *       weights one and two, for every choice of party town the guarantee admits, plus randomized
 *       networks of mixed sizes and densities
 *       ({@link #everyTinyRoadNetworkMatchesTheFloydWarshallOracle()},
 *       {@link #randomRoadNetworksMatchTheFloydWarshallOracle()}).
 * </ul>
 *
 * <p>The small hand-picked answers are verified by the leg-by-leg walks quoted in each test
 * comment, and the full-size anchors are road networks whose shortest distances collapse to closed
 * forms (a heavy chain, and a forward-only loop where every road advances {@code k} towns at cost
 * {@code k}, so any route from {@code a} to {@code b} costs exactly the forward gap). Everything
 * else is cross-checked against a transparently coded Floyd-Warshall oracle ({@link #allPairs(int,
 * int[][])}) -- a three-loop transcription of all-pairs shortest paths with no heap and no reversed
 * graph, so agreement with a Dijkstra solver is real evidence.
 */
class MainTest {

  private static final int INF = 1 << 29;

  // --- The statement sample. ---

  // The published example: round trips are 4 + 1 = 5 (town 1), 0 (the host), 6 + 3 = 9 (town 3),
  // and 3 + 7 = 10 (town 4, returning 2 -> 1 -> 3 -> 4 because 2 -> 1 -> 4 costs 8). The answer
  // is the worst time, 10 -- not the town number 4.
  @Test
  @StdIo({"4 8 2", "1 2 4", "1 3 2", "1 4 7", "2 1 1", "2 3 5", "3 1 2", "3 4 4", "4 2 3"})
  void statementSampleLongestRoundTripIsTen(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("10");
  }

  // --- One-way roads. ---

  // A one-way triangle 1 -> 2 -> 3 -> 1 with the party at 2: town 1 walks one road out but two
  // roads home (2 -> 3 -> 1), and town 3 walks two roads out (3 -> 1 -> 2) and one home, so both
  // round trips are 3. An undirected solver answers 2.
  @Test
  @StdIo({"3 3 2", "1 2 1", "2 3 1", "3 1 1"})
  void oneWayTriangleForcesTheLongWayHome(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // The statement allows both 1 -> 2 and 2 -> 1 as separate roads with their own weights: the
  // guest's round trip is 3 + 9 = 12. A symmetric adjacency matrix (or one direction overwriting
  // the other) answers 6 or 18.
  @Test
  @StdIo({"2 2 2", "1 2 3", "2 1 9"})
  void oppositeRoadsKeepTheirOwnWeights(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("12");
  }

  // --- Shortest legs need relaxation. ---

  // The direct road 1 -> 2 costs 10 but the detour 1 -> 3 -> 2 costs 2, so both round trips are 3
  // (town 1: 2 out, 1 home; town 3: 1 out, 2 home via 2 -> 1 -> 3). A fewest-hops search charges
  // town 1 the direct road and answers 11.
  @Test
  @StdIo({"3 4 2", "1 2 10", "1 3 1", "3 2 1", "2 1 1"})
  void aCheapDetourBeatsTheDirectRoad(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- Per-student round trips. ---

  // The smallest party: two towns joined both ways. The guest's round trip is 1 + 1 = 2 and the
  // host walks nothing; a solver that charges the host a lap answers 4, and one that drops the
  // guest answers 0.
  @Test
  @StdIo({"2 2 1", "1 2 1", "2 1 1"})
  void smallestPartyIsOneRoadOutAndOneRoadBack(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // A star around the host: town 2 walks 10 out / 1 home (11), town 3 walks 2 out / 8 home (10),
  // and town 4 walks 6 out / 6 home (12). The answer is 12, yet town 4 holds neither the longest
  // outbound leg (town 2's 10) nor the longest return leg (town 3's 8) -- summing the two leg
  // maxima answers 18, and following either extreme student answers 11 or 10.
  @Test
  @StdIo({"4 6 1", "2 1 10", "1 2 1", "3 1 2", "1 3 8", "4 1 6", "1 4 6"})
  void roundTripsAreSummedPerStudentNotPerLeg(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("12");
  }

  // --- Full-size road networks. ---

  // A 1,000-town chain with weight-100 roads in both directions and the party at town 1: the far
  // student rides 999 roads out and 999 back, 199,800 in total. Pins weight accumulation well
  // past 16 bits and requires the full-size search to finish quickly.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void maximumChainAccumulatesTheFullRoundTrip() throws IOException {
    List<int[]> roads = new ArrayList<>();
    for (int i = 1; i < 1000; i++) {
      roads.add(new int[] {i, i + 1, 100});
      roads.add(new int[] {i + 1, i, 100});
    }
    assertThat(runMain(roadsToInput(1000, 1, roads))).isEqualTo("199800");
  }

  // Exactly M = 10,000 roads: for k = 1..10 every town gets a road advancing k towns around a
  // 1,000-town one-way loop at cost k. Every route from a to b then costs exactly the forward
  // gap, so each non-host round trip is the gap out plus the complementary gap home -- one full
  // lap of 1,000. Nothing points backward, so an undirected shortcut is wrong at full scale too.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void fullRoadBudgetLoopCostsEveryStudentOneLap() throws IOException {
    List<int[]> roads = new ArrayList<>();
    for (int k = 1; k <= 10; k++) {
      for (int i = 1; i <= 1000; i++) {
        roads.add(new int[] {i, (i - 1 + k) % 1000 + 1, k});
      }
    }
    assertThat(runMain(roadsToInput(1000, 500, roads))).isEqualTo("1000");
  }

  // --- Sweeps against the Floyd-Warshall oracle. ---

  // Every directed road layout on two and three towns with weights one and two -- each ordered
  // pair independently absent, weight 1, or weight 2 -- and every party town the guarantee
  // admits (all students can attend and return). Dense in exactly the shapes the named tests
  // sample: one-way cycles, opposite-weight pairs, and detours.
  @Test
  @Timeout(value = 30, unit = TimeUnit.SECONDS)
  void everyTinyRoadNetworkMatchesTheFloydWarshallOracle() throws IOException {
    for (int n = 2; n <= 3; n++) {
      int pairs = n * (n - 1);
      int total = 1;
      for (int i = 0; i < pairs; i++) {
        total *= 3;
      }
      for (int code = 0; code < total; code++) {
        int[][] time = emptyRoads(n);
        List<int[]> roads = new ArrayList<>();
        int rest = code;
        for (int a = 1; a <= n; a++) {
          for (int b = 1; b <= n; b++) {
            if (a == b) {
              continue;
            }
            int w = rest % 3;
            rest /= 3;
            if (w > 0) {
              time[a][b] = w;
              roads.add(new int[] {a, b, w});
            }
          }
        }
        if (roads.isEmpty()) {
          continue; // the statement requires at least one road.
        }
        int[][] d = allPairs(n, time);
        for (int x = 1; x <= n; x++) {
          if (!everyoneCanAttend(n, x, d)) {
            continue; // the statement guarantees every student can attend and return.
          }
          assertThat(runMain(roadsToInput(n, x, roads)))
              .as("n=%d code=%d x=%d", n, code, x)
              .isEqualTo(Integer.toString(longestRoundTrip(n, x, d)));
        }
      }
    }
  }

  // Random networks across mixed sizes (up to 8 towns) and densities: a one-way loop backbone
  // keeps the reach-and-return guarantee, and each trial draws its own extra-road density and
  // full-range weights, so the sample spans sparse rings and near-complete road maps.
  @Test
  @Timeout(value = 30, unit = TimeUnit.SECONDS)
  void randomRoadNetworksMatchTheFloydWarshallOracle() throws IOException {
    Random rng = new Random(1238L); // fixed seed -> deterministic across JVMs.
    for (int trial = 0; trial < 200; trial++) {
      int n = 2 + rng.nextInt(7);
      int[][] time = emptyRoads(n);
      List<int[]> roads = new ArrayList<>();
      for (int i = 1; i <= n; i++) {
        int next = i % n + 1;
        time[i][next] = 1 + rng.nextInt(100);
        roads.add(new int[] {i, next, time[i][next]});
      }
      double density = rng.nextDouble() * 0.7;
      for (int a = 1; a <= n; a++) {
        for (int b = 1; b <= n; b++) {
          if (a == b || time[a][b] != INF || rng.nextDouble() >= density) {
            continue;
          }
          time[a][b] = 1 + rng.nextInt(100);
          roads.add(new int[] {a, b, time[a][b]});
        }
      }
      int x = 1 + rng.nextInt(n);
      int[][] d = allPairs(n, time);
      assertThat(runMain(roadsToInput(n, x, roads)))
          .as("trial=%d n=%d x=%d", trial, n, x)
          .isEqualTo(Integer.toString(longestRoundTrip(n, x, d)));
    }
  }

  /**
   * Transparent oracle: Floyd-Warshall straight from the definition -- three nested loops over a
   * road matrix, no heap and no reversed graph -- so agreement with a Dijkstra solver is real
   * evidence.
   *
   * @implNote {@code O(n^3)} time; unreachable entries stay at {@code INF}, which is small enough
   *     that {@code INF + INF} does not overflow {@code int}.
   */
  private static int[][] allPairs(int n, int[][] time) {
    int[][] d = new int[n + 1][n + 1];
    for (int a = 1; a <= n; a++) {
      for (int b = 1; b <= n; b++) {
        d[a][b] = a == b ? 0 : time[a][b];
      }
    }
    for (int k = 1; k <= n; k++) {
      for (int a = 1; a <= n; a++) {
        for (int b = 1; b <= n; b++) {
          d[a][b] = Math.min(d[a][b], d[a][k] + d[k][b]);
        }
      }
    }
    return d;
  }

  private static boolean everyoneCanAttend(int n, int x, int[][] d) {
    for (int i = 1; i <= n; i++) {
      if (d[i][x] >= INF || d[x][i] >= INF) {
        return false;
      }
    }
    return true;
  }

  private static int longestRoundTrip(int n, int x, int[][] d) {
    int worst = 0;
    for (int i = 1; i <= n; i++) {
      worst = Math.max(worst, d[i][x] + d[x][i]);
    }
    return worst;
  }

  private static int[][] emptyRoads(int n) {
    int[][] time = new int[n + 1][n + 1];
    for (int[] row : time) {
      Arrays.fill(row, INF);
    }
    return time;
  }

  private static String roadsToInput(int n, int x, List<int[]> roads) {
    StringBuilder sb = new StringBuilder();
    sb.append(n).append(' ').append(roads.size()).append(' ').append(x).append('\n');
    for (int[] road : roads) {
      sb.append(road[0]).append(' ').append(road[1]).append(' ').append(road[2]).append('\n');
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
