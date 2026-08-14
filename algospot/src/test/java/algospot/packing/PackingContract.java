package algospot.packing;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * Algospot PACKING -- fill one carrier of capacity {@code W} with a subset of the items, maximizing
 * the total desperation score, and report which items went in. A 0/1 knapsack with reconstruction.
 *
 * <p><b>I/O contract.</b> Line 1 is {@code C} ({@code 1 <= C <= 50}), the number of test cases.
 * Each test case is a line with {@code N} and {@code W} ({@code 1 <= N <= 100}, {@code 1 <= W <=
 * 1000}) followed by {@code N} lines of {@code name volume priority}; names are 1 to 20 letters,
 * volumes and priorities are natural numbers at most 1000. Each test case prints the maximum total
 * priority and the number of items chosen, then one line per chosen item name.
 *
 * <p><b>Any optimal combination is accepted.</b> The statement says so outright, so the assertions
 * below pin the header line exactly -- the total and the count are determined -- while comparing
 * the item names as a <em>set</em>. Pinning their order would fail a correct solution that
 * reconstructs the bag from the other end. Every rung's instance is additionally designed so the
 * optimal set is unique, which is what lets the expected names be hand-derived literals at all; the
 * one rung with a genuine tie is a GUARD that says so explicitly.
 *
 * <p><b>TDD ladder.</b> Work down the file one rung at a time. Each rung is the smallest input that
 * fails against the minimal code satisfying every rung above it, so the first failure is always the
 * next thing to write. The staircase was verified by building those intermediate implementations
 * and observing each rung break exactly the one below it.
 *
 * <ol>
 *   <li>one item that fits &rarr; the two-part output shape
 *   <li>one item too bulky &rarr; capacity is a constraint, and an empty bag prints {@code "0 0"}
 *   <li>two items, one slot &rarr; choose by priority, not by input order
 *   <li>two items, both fit &rarr; a combination can beat the best single item
 *   <li>an item exactly filling the carrier &rarr; the bound is {@code <=}, not {@code <}
 *   <li>a dense item crowding out two better ones &rarr; no greedy; search combinations
 *   <li>two cases &rarr; the loop over {@code C}
 *   <li>fifty cases of a hundred items &rarr; a table, not an enumeration of subsets
 * </ol>
 *
 * <p>This class holds the contract; {@link PackingTest} and {@link PackingRecursiveTest} bind it to
 * the two solutions in this package so both face exactly the same ladder.
 */
abstract class PackingContract {

  /** Runs the solution under test on the current {@code System.in}. */
  abstract void solve() throws IOException;

  // RUNG 1 -- forces: the header line and the item list beneath it.
  // Fails on: an empty main. One item that comfortably fits leaves nothing to decide, so all this
  // pins is the shape: total and count on one line, then the chosen names.
  @Test
  @StdIo({"1", "1 10", "laptop 4 7"})
  void singleItemThatFitsIsPacked(StdOut out) throws IOException {
    // LADDER-CHECK: packing_optimal
    solve();

    assertPacked(out, "7 1|laptop");
  }

  // RUNG 2 -- forces: capacity to constrain anything at all.
  // Fails on: rung-1 code that packs whatever it is given. The lone item is bulkier than the
  // carrier, so the bag comes back empty and the header degenerates to "0 0" with no names after
  // it -- the empty-list boundary of the output format.
  @Test
  @StdIo({"1", "1 3", "laptop 4 7"})
  void singleItemTooBulkyIsLeftBehind(StdOut out) throws IOException {
    // LADDER-CHECK: packing_optimal
    solve();

    assertPacked(out, "0 0|");
  }

  // RUNG 3 -- forces: choosing by priority rather than by position.
  // Fails on: rung-2 code that keeps the first item that fits. Both items are volume 4 in a
  // capacity-5 carrier, so exactly one can go, and the valuable one is listed second.
  @Test
  @StdIo({"1", "2 5", "cheap 4 3", "dear 4 9"})
  void mostDesperatelyNeededItemWinsTheOnlySlot(StdOut out) throws IOException {
    // LADDER-CHECK: packing_optimal
    solve();

    assertPacked(out, "9 1|dear");
  }

  // RUNG 4 -- forces: considering combinations, not just the single best item.
  // Fails on: rung-3 code that packs the one highest-priority item and stops. Two items of volume
  // 3 both fit in a capacity-7 carrier, and 5 + 5 beats either 5 alone.
  @Test
  @StdIo({"1", "2 7", "alpha 3 5", "beta 3 5"})
  void twoItemsTogetherBeatTheBestSingleItem(StdOut out) throws IOException {
    // LADDER-CHECK: packing_optimal
    solve();

    assertPacked(out, "10 2|alpha,beta");
  }

  // RUNG 5 -- forces: the capacity bound to be inclusive.
  // Fails on: rung-4 code written with a strict `volume < capacity` test, a classic knapsack
  // off-by-one that every earlier rung tolerates because none of them fills the carrier exactly.
  // Here the valuable item is volume 4 in a capacity-4 carrier; reject it and the answer collapses
  // to the trinket.
  @Test
  @StdIo({"1", "2 4", "exact 4 9", "tiny 1 2"})
  void itemFillingTheCarrierExactlyStillFits(StdOut out) throws IOException {
    // LADDER-CHECK: packing_optimal
    solve();

    assertPacked(out, "9 1|exact");
  }

  // RUNG 6 -- forces: an actual search over combinations; no greedy rule survives here.
  // Fails on: rung-5 code that sorts by priority-per-volume (or by raw priority -- the same item
  // wins both) and fills. Taking `dense` (volume 6, priority 12, the best on both rules) leaves 4
  // units of a 10-unit carrier and neither volume-5 item fits, for 12. Leaving it behind packs both
  // for 18. Greedy is off by a third.
  @Test
  @StdIo({"1", "3 10", "dense 6 12", "bulky1 5 9", "bulky2 5 9"})
  void greedilyTakingTheDensestItemIsNotOptimal(StdOut out) throws IOException {
    // LADDER-CHECK: packing_optimal
    solve();

    assertPacked(out, "18 2|bulky1,bulky2");
  }

  // RUNG 7 -- forces: the loop over C.
  // Fails on: rung-6 code that packs one carrier and stops. The second case is the too-bulky
  // instance, so the two blocks differ in shape as well as in numbers -- a solver that emitted one
  // block twice would fail on the count line too.
  @Test
  @StdIo({"2", "1 10", "laptop 4 7", "1 3", "laptop 4 7"})
  void everyTestCaseGetsItsOwnBlock(StdOut out) throws IOException {
    // LADDER-CHECK: packing_optimal
    solve();

    assertPacked(out, "7 1|laptop", "0 0|");
  }

  // RUNG 8 -- forces: a capacity-indexed table rather than an enumeration of subsets.
  // Fails on: rung-7 code that tries every subset, which is the natural way to satisfy rung 6 and
  // stays perfectly correct -- it simply never finishes. At the stated maximum of 100 items there
  // are 2^100 subsets; tabulated it is 100 x 500 states per case, and the fifty cases here run in
  // milliseconds.
  //
  // The instance is built so its answer is derivable by hand: 100 items of equal volume 10 with
  // distinct priorities 1..100, and a carrier of 500 that holds exactly 50 of them. The optimum is
  // therefore the 50 most desperately needed items, priorities 51..100, totalling 3775.
  @Test
  @Timeout(value = 30, unit = TimeUnit.SECONDS)
  @StdIo
  void hundredItemCasesNeedATableNotEverySubset(StdOut out) throws IOException {
    // LADDER-GEN: packing_max_cases
    // LADDER-CHECK: packing_optimal
    runOn(equalVolumeCases(50, 100, 500));

    List<String> expected = new ArrayList<>();
    for (int i = 50; i < 100; i++) {
      expected.add("item" + i);
    }
    String[] lines = out.capturedLines();
    assertThat(lines).hasSize(50 * 51);
    for (int c = 0; c < 50; c++) {
      int at = c * 51;
      assertThat(lines[at]).as("header of case %d", c).isEqualTo("3775 50");
      assertThat(Arrays.copyOfRange(lines, at + 1, at + 51))
          .as("items of case %d", c)
          .containsExactlyInAnyOrderElementsOf(expected);
    }
  }

  // GUARD -- the judge's official sample, verbatim from problems/PACKING/samples/sample-01: the
  // same six items against a capacity-10 and then a capacity-17 carrier. Not a rung; any
  // implementation clearing rung 7 clears this. Both optima happen to be unique, so the names can
  // be pinned as a set.
  @Test
  @StdIo({
    "2",
    "6 10",
    "laptop 4 7",
    "camera 2 10",
    "xbox 6 6",
    "grinder 4 7",
    "dumbell 2 5",
    "encyclopedia 10 4",
    "6 17",
    "laptop 4 7",
    "camera 2 10",
    "xbox 6 6",
    "grinder 4 7",
    "dumbell 2 5",
    "encyclopedia 10 4"
  })
  void officialSampleIsReproducedExactly(StdOut out) throws IOException {
    // LADDER-CHECK: packing_optimal
    solve();

    assertPacked(out, "24 3|laptop,camera,grinder", "30 4|laptop,camera,xbox,grinder");
  }

  // GUARD -- two interchangeable items, so there are two optimal bags and the judge takes either.
  // Not a rung. It is the test that documents why every assertion in this file compares names as a
  // set and never pins their order: here even the set is not determined, so only the header can be
  // asserted, and the chosen name is merely required to be one of the two.
  @Test
  @StdIo({"1", "2 5", "twin1 5 9", "twin2 5 9"})
  void eitherOfTwoEquallyGoodBagsIsAccepted(StdOut out) throws IOException {
    // LADDER-CHECK: packing_optimal
    solve();

    String[] lines = out.capturedLines();
    assertThat(lines[0]).isEqualTo("9 1");
    assertThat(lines).hasSize(2);
    assertThat(lines[1]).isIn("twin1", "twin2");
  }

  // GUARD -- a carrier roomy enough for everything, where the answer is simply every item.
  // Not a rung: no partial implementation above fails it. It pins the opposite boundary from rung
  // 2, where the constraint never binds and the reconstruction must walk out the entire list.
  @Test
  @StdIo({"1", "3 100", "a 4 7", "b 2 10", "c 6 6"})
  void everythingIsPackedWhenTheCarrierIsRoomy(StdOut out) throws IOException {
    // LADDER-CHECK: packing_optimal
    solve();

    assertPacked(out, "23 3|a,b,c");
  }

  /**
   * Asserts the captured output is exactly these blocks. A block is written {@code "total
   * count|name,name"} -- the header is compared literally because both numbers are determined,
   * while the names are compared as a set because the statement accepts any optimal combination.
   */
  private static void assertPacked(StdOut out, String... blocks) {
    String[] lines = out.capturedLines();
    int at = 0;
    for (String block : blocks) {
      String[] parts = block.split("\\|", -1);
      List<String> names = parts[1].isEmpty() ? List.of() : Arrays.asList(parts[1].split(",", -1));

      assertThat(lines[at]).as("header of block \"%s\"", block).isEqualTo(parts[0]);
      int count = Integer.parseInt(parts[0].split(" ")[1]);
      assertThat(count).as("count in \"%s\" matches its name list", block).isEqualTo(names.size());
      assertThat(Arrays.copyOfRange(lines, at + 1, at + 1 + count))
          .as("items of block \"%s\"", block)
          .containsExactlyInAnyOrderElementsOf(names);
      at += 1 + count;
    }
    assertThat(lines).as("no output beyond the expected blocks").hasSize(at);
  }

  /**
   * Builds {@code cases} identical test cases of {@code n} items that all have volume 10 and
   * priorities {@code 1..n}, against a carrier of {@code capacity}. With every volume equal the
   * carrier holds exactly {@code capacity / 10} items and the optimum is the that many highest
   * priorities -- an answer derived from the construction rather than from running any solution.
   */
  private static String equalVolumeCases(int cases, int n, int capacity) {
    StringBuilder sb = new StringBuilder();
    sb.append(cases).append('\n');
    for (int c = 0; c < cases; c++) {
      sb.append(n).append(' ').append(capacity).append('\n');
      for (int i = 0; i < n; i++) {
        sb.append("item").append(i).append(" 10 ").append(i + 1).append('\n');
      }
    }
    return sb.toString();
  }

  /**
   * Feeds generated input to the solution. {@code @StdIo} only accepts compile-time constants, so
   * an instance too large to spell out as literals is piped in here instead; {@code System.in} is
   * restored afterwards because a bare {@code @StdIo} captures stdout only and leaves stdin alone.
   */
  private void runOn(String input) throws IOException {
    InputStream original = System.in;
    try {
      System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
      solve();
    } finally {
      System.setIn(original);
    }
  }
}
