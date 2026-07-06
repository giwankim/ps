package boj.boj15971;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
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
 * BOJ 15971 두 로봇 ("Two Robots", KOI 2018) -- the minimum total distance two robots in a tree-shaped
 * cave must move before they can communicate.
 *
 * <p>The cave has {@code N} rooms numbered {@code 1..N} joined by exactly {@code N - 1} corridors,
 * each with a positive integer length, and any two rooms are connected by a unique simple path --
 * the cave is a tree. Two robots sit in given rooms {@code A} and {@code B}. They can communicate
 * only when both are on the same corridor, where a corridor's two endpoint rooms count as being on
 * it. Print the minimum total distance the two robots move until they can communicate.
 *
 * <p>Input: the first line holds {@code N A B}; each of the next {@code N - 1} lines holds one
 * corridor as two room numbers and a length. Constraints: {@code 1 <= N <= 100,000} and every
 * corridor length is a positive integer at most {@code 1,000}, so the answer is at most
 * {@code 99,999 * 1,000} and fits an {@code int}.
 *
 * <p>Because the A-B path is unique, the robots end up at the two ends of exactly one corridor on
 * that path, and leaving the heaviest one unwalked is optimal: the answer is {@code dist(A, B)}
 * minus the largest corridor length on the A-B path, and {@code 0} when {@code A == B}.
 *
 * <ul>
 *   <li><b>The statement's own samples.</b> The path-shaped cave and the branching nine-room cave
 *       from the problem statement, with the official answers 6 and 14
 *       ({@link #pathCaveFromStatementNeedsSixTotalMovement(StdOut)},
 *       {@link #branchingCaveFromStatementNeedsFourteenTotalMovement(StdOut)}).
 *   <li><b>Already-communicating starts.</b> Robots in the same room, robots in the two rooms of a
 *       two-room cave, and the degenerate one-room cave all need zero movement; solvers that force
 *       the robots to cross at least one corridor, or that mishandle {@code A == B}, break here
 *       ({@link #robotsInTheSameRoomNeedNoMovement(StdOut)},
 *       {@link #robotsInAdjacentRoomsAlreadyShareTheirCorridor(StdOut)},
 *       {@link #singleRoomCavePutsBothRobotsTogether(StdOut)}).
 *   <li><b>Skipping the heaviest corridor on the path.</b> Wherever the heaviest path corridor sits
 *       -- between the robots, right next to one of them, or tied with another -- exactly one copy
 *       of its length is subtracted, and the all-unit-length cave (subtask 2's shape) walks all but
 *       one corridor ({@link #robotsMeetAcrossTheLighterOfTwoCorridors(StdOut)},
 *       {@link #heaviestCorridorInTheMiddleIsLeftUnwalked(StdOut)},
 *       {@link #heaviestCorridorNextToARobotLetsItStayPut(StdOut)},
 *       {@link #tiedHeaviestCorridorsSkipOnlyOne(StdOut)},
 *       {@link #unitLengthCorridorsWalkAllButOneEdge(StdOut)}).
 *   <li><b>Only corridors on the A-B path matter.</b> Far heavier corridors hanging off the path
 *       must not be subtracted; a solver tracking a global maximum instead of the path maximum goes
 *       negative or undercounts ({@link #heavierCorridorOffThePathIsIgnored(StdOut)},
 *       {@link #starCaveRobotsMeetOnTheLighterSpoke(StdOut)}).
 *   <li><b>Input-order robustness.</b> Corridor lines arrive in arbitrary order with endpoints in
 *       either orientation, and the robot rooms may be given with {@code A > B}; a solver that
 *       assumes subtask 1's consecutive-room lines fails
 *       ({@link #shuffledCorridorOrderAndSwappedEndpointsParseCorrectly(StdOut)}).
 *   <li><b>Randomized cross-check and maximum sizes.</b> Random caves are compared against an
 *       oracle built straight from the statement, and the two extreme 100,000-room shapes -- the
 *       deepest path and the widest star -- must finish within the time limit
 *       ({@link #randomCavesMatchTheCorridorEnumerationOracle()},
 *       {@link #maximumPathCaveIsHandledWithinTheTimeLimit()},
 *       {@link #maximumStarCaveIsHandledWithinTheTimeLimit()}).
 * </ul>
 *
 * <p>The hand-picked answers are cross-checked by an oracle that mirrors the statement instead of
 * the subtract-the-heaviest identity: it enumerates every communicating configuration -- both
 * robots in one room, or the robots at the two ends of one corridor -- from per-robot distance
 * arrays and keeps the cheapest ({@link #oracleMinTotalMovement(int, int, int, int[][])}). The
 * randomized sweep ({@link #randomCavesMatchTheCorridorEnumerationOracle()}) drives it over caves
 * with shuffled labels, shuffled corridor lines, and both tie-heavy and varied lengths.
 */
class MainTest {

  // --- The statement's own samples. ---

  // Statement sample 1: the five rooms form a path with corridor lengths 1, 2, 3, 4. The robots at
  // the two ends walk toward each other and leave the length-4 corridor unwalked: 10 - 4 = 6.
  @Test
  @StdIo({"5 1 5", "1 2 1", "2 3 2", "3 4 3", "4 5 4"})
  void pathCaveFromStatementNeedsSixTotalMovement(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("6");
  }

  // Statement sample 2: the nine-room branching cave. The unique 1-9 path is 1-2-5-9 with lengths
  // 8, 10, 6, so the robots move to rooms 2 and 5 and skip the length-10 corridor: 24 - 10 = 14.
  // The length-14 corridor between rooms 5 and 6 is heavier than anything on the path and must be
  // ignored.
  @Test
  @StdIo({"9 1 9", "1 2 8", "2 3 6", "2 4 5", "2 5 10", "9 5 6", "6 5 14", "6 7 7", "8 6 7"})
  void branchingCaveFromStatementNeedsFourteenTotalMovement(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("14");
  }

  // --- Already-communicating starts. ---

  // Both robots start in the same interior room, so they already share every corridor touching it.
  // Guards the A == B case, where a path-walking solver can recurse forever or subtract a maximum
  // it never found.
  @Test
  @StdIo({"3 2 2", "1 2 3", "2 3 4"})
  void robotsInTheSameRoomNeedNoMovement(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // The robots occupy the two rooms of a two-room cave: both endpoints of the single corridor
  // already lie on it, so nobody moves. A solver that makes the robots cross at least one corridor
  // reports 7 instead of 0.
  @Test
  @StdIo({"2 1 2", "1 2 7"})
  void robotsInAdjacentRoomsAlreadyShareTheirCorridor(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // The constraint allows N = 1: a one-room cave with zero corridor lines and both robots in room
  // 1. The parser must not block waiting for a second line, and the answer is 0.
  @Test
  @StdIo({"1 1 1"})
  void singleRoomCavePutsBothRobotsTogether(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- Skipping the heaviest corridor on the path. ---

  // Two corridors between the robots: one robot walks the lighter corridor to the middle room and
  // the pair then shares the heavier corridor, so the answer is min(4, 9) = 4 -- equivalently
  // (4 + 9) - 9. Pins that the heavier of the two is the one skipped.
  @Test
  @StdIo({"3 1 3", "1 2 4", "2 3 9"})
  void robotsMeetAcrossTheLighterOfTwoCorridors(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // The heaviest corridor sits mid-path: both robots walk inward to its two ends and the length-100
  // corridor is never crossed, so the answer is (2 + 100 + 3) - 100 = 5.
  @Test
  @StdIo({"4 1 4", "1 2 2", "2 3 100", "3 4 3"})
  void heaviestCorridorInTheMiddleIsLeftUnwalked(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("5");
  }

  // The heaviest corridor touches robot B's own room, so B stays put while A walks the rest of the
  // path: (5 + 6 + 50) - 50 = 11. Guards against solvers that insist both robots move.
  @Test
  @StdIo({"4 1 4", "1 2 5", "2 3 6", "3 4 50"})
  void heaviestCorridorNextToARobotLetsItStayPut(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("11");
  }

  // Both path corridors share the maximum length 7, but only one of them may be skipped:
  // (7 + 7) - 7 = 7, not 0. Guards against subtracting every corridor that attains the maximum.
  @Test
  @StdIo({"3 1 3", "1 2 7", "2 3 7"})
  void tiedHeaviestCorridorsSkipOnlyOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("7");
  }

  // Subtask 2's shape: every corridor has length 1, so the answer is simply the number of path
  // corridors minus one -- here the 2-6 path has four corridors, giving 3.
  @Test
  @StdIo({"6 2 6", "1 2 1", "2 3 1", "3 4 1", "4 5 1", "5 6 1"})
  void unitLengthCorridorsWalkAllButOneEdge(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- Only corridors on the A-B path matter. ---

  // Two maximum-length corridors (1,000) hang off room 2, away from the 1-3 path whose corridors
  // are 4 and 5. The answer is (4 + 5) - 5 = 4; a solver that subtracts the global maximum instead
  // of the path maximum computes 9 - 1,000 and goes negative.
  @Test
  @StdIo({"5 1 3", "1 2 4", "2 3 5", "2 4 1000", "4 5 1000"})
  void heavierCorridorOffThePathIsIgnored(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // A star cave: hub room 1, robots on the leaves 2 and 3. The path is the two spokes of lengths 3
  // and 8, so the robot on the lighter spoke walks it to the hub: (3 + 8) - 8 = 3. The third spoke
  // never participates.
  @Test
  @StdIo({"4 2 3", "1 2 3", "1 3 8", "1 4 1"})
  void starCaveRobotsMeetOnTheLighterSpoke(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- Input-order robustness. ---

  // The same tree as statement sample 1, but the corridor lines arrive deepest-first with endpoints
  // swapped, and the robots are given as A=4, B=1. The 4-1 path has lengths 3, 2, 1, so the answer
  // is 6 - 3 = 3. A solver that assumes subtask 1's "line i joins rooms i-1 and i" layout, or
  // builds a directed adjacency, breaks here.
  @Test
  @StdIo({"5 4 1", "5 4 4", "3 4 3", "2 3 2", "2 1 1"})
  void shuffledCorridorOrderAndSwappedEndpointsParseCorrectly(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- Randomized cross-check against the statement-shaped oracle. ---

  // Random caves with shuffled room labels, shuffled corridor lines, randomly oriented endpoints,
  // and robot rooms that may coincide. Half the trials draw lengths from 1..3 so ties on the
  // heaviest corridor are common; the rest use the full 1..1,000 range. Each answer is compared
  // against the enumeration oracle, which never uses the subtract-the-heaviest identity.
  @Test
  @Timeout(value = 30, unit = TimeUnit.SECONDS)
  void randomCavesMatchTheCorridorEnumerationOracle() throws IOException {
    Random rng = new Random(15971L); // fixed seed -> deterministic across JVMs.
    for (int trial = 0; trial < 300; trial++) {
      int n = 1 + rng.nextInt(50);
      int lengthCap = rng.nextBoolean() ? 3 : 1_000;
      int[] label = shuffledLabels(n, rng);
      int[][] corridors = new int[n - 1][];
      for (int i = 2; i <= n; i++) {
        int parent = 1 + rng.nextInt(i - 1);
        int length = 1 + rng.nextInt(lengthCap);
        int u = label[i];
        int v = label[parent];
        corridors[i - 2] = rng.nextBoolean() ? new int[] {u, v, length} : new int[] {v, u, length};
      }
      Collections.shuffle(Arrays.asList(corridors), rng);
      int a = 1 + rng.nextInt(n);
      int b = 1 + rng.nextInt(n);
      assertThat(runMain(buildInput(n, a, b, corridors)))
          .as("n=%d a=%d b=%d corridors=%s", n, a, b, Arrays.deepToString(corridors))
          .isEqualTo(Integer.toString(oracleMinTotalMovement(n, a, b, corridors)));
    }
  }

  // --- Maximum-size caves: linear-time traversal must fit the time limit. ---

  // Subtask 1's shape at full size: a 100,000-room path whose corridors all have the maximum
  // length 1,000, robots at the two ends. The answer 99,999 * 1,000 - 1,000 = 99,998,000 pins that
  // distances near 10^8 do not overflow, and the 100,000-deep tree requires either iteration or
  // the judge-like 64m thread stack the build grants test JVMs.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void maximumPathCaveIsHandledWithinTheTimeLimit() throws IOException {
    int n = 100_000;
    int[][] corridors = new int[n - 1][];
    for (int i = 1; i < n; i++) {
      corridors[i - 1] = new int[] {i, i + 1, 1_000};
    }
    assertThat(runMain(buildInput(n, 1, n, corridors))).isEqualTo("99998000");
  }

  // The widest cave: a star with 99,999 spokes on hub room 1. The robots' two spokes have lengths
  // 999 and 1,000, so the lighter one is walked: (999 + 1,000) - 1,000 = 999. Stresses fan-out of
  // one huge adjacency list rather than depth.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void maximumStarCaveIsHandledWithinTheTimeLimit() throws IOException {
    int n = 100_000;
    int[][] corridors = new int[n - 1][];
    for (int i = 2; i <= n; i++) {
      corridors[i - 2] = new int[] {1, i, i == 2 ? 999 : 1_000};
    }
    assertThat(runMain(buildInput(n, 2, n, corridors))).isEqualTo("999");
  }

  /**
   * Independent oracle that mirrors the statement instead of the intended identity: the robots can
   * communicate exactly when both stand in one room or when they occupy the two ends of one
   * corridor, so it enumerates every such configuration from per-robot distance arrays and keeps
   * the cheapest total movement. Agreement with the subtract-the-heaviest formula across many
   * random caves is therefore real evidence.
   *
   * @implNote {@code O(n)} time, dominated by the two tree traversals and one sweep over rooms and
   *     corridors.
   */
  private static int oracleMinTotalMovement(int n, int a, int b, int[][] corridors) {
    int[] fromA = distancesFrom(n, a, corridors);
    int[] fromB = distancesFrom(n, b, corridors);
    int best = Integer.MAX_VALUE;
    for (int room = 1; room <= n; room++) {
      best = Math.min(best, fromA[room] + fromB[room]);
    }
    for (int[] corridor : corridors) {
      best = Math.min(best, fromA[corridor[0]] + fromB[corridor[1]]);
      best = Math.min(best, fromA[corridor[1]] + fromB[corridor[0]]);
    }
    return best;
  }

  /**
   * Distances from {@code start} to every room, by breadth-first traversal; paths in a tree are
   * unique, so the first visit is the only (and shortest) one.
   *
   * @implNote {@code O(n)} time and space.
   */
  private static int[] distancesFrom(int n, int start, int[][] corridors) {
    List<List<int[]>> adjacent = new ArrayList<>();
    for (int room = 0; room <= n; room++) {
      adjacent.add(new ArrayList<>());
    }
    for (int[] corridor : corridors) {
      adjacent.get(corridor[0]).add(new int[] {corridor[1], corridor[2]});
      adjacent.get(corridor[1]).add(new int[] {corridor[0], corridor[2]});
    }
    int[] dist = new int[n + 1];
    Arrays.fill(dist, -1);
    dist[start] = 0;
    ArrayDeque<Integer> queue = new ArrayDeque<>();
    queue.add(start);
    while (!queue.isEmpty()) {
      int room = queue.poll();
      for (int[] next : adjacent.get(room)) {
        if (dist[next[0]] < 0) {
          dist[next[0]] = dist[room] + next[1];
          queue.add(next[0]);
        }
      }
    }
    return dist;
  }

  /** A random permutation of {@code 1..n} stored at indices {@code 1..n} (index 0 unused). */
  private static int[] shuffledLabels(int n, Random rng) {
    int[] label = new int[n + 1];
    for (int i = 1; i <= n; i++) {
      label[i] = i;
    }
    for (int i = n; i > 1; i--) {
      int j = 1 + rng.nextInt(i);
      int tmp = label[i];
      label[i] = label[j];
      label[j] = tmp;
    }
    return label;
  }

  /**
   * Builds BOJ 15971 input: {@code N A B} on the first line, then one corridor per line as the two
   * room numbers followed by the corridor length.
   */
  private static String buildInput(int n, int a, int b, int[][] corridors) {
    StringBuilder sb = new StringBuilder(corridors.length * 16 + 32);
    sb.append(n).append(' ').append(a).append(' ').append(b).append('\n');
    for (int[] corridor : corridors) {
      sb.append(corridor[0])
          .append(' ')
          .append(corridor[1])
          .append(' ')
          .append(corridor[2])
          .append('\n');
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
