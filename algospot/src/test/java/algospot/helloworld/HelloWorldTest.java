package algospot.helloworld;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * Algospot HELLOWORLD -- the judge's introductory problem: read names and greet each one.
 *
 * <p><b>I/O contract.</b> Line 1 is {@code C} ({@code 1 <= C <= 50}), the number of people, then
 * {@code C} lines each holding one name (letters and digits only, at most 50 characters). The
 * program prints {@code C} lines, the i-th being {@code "Hello, <name>!"} for the i-th name, in
 * input order.
 *
 * <p><b>TDD ladder.</b> Work down the file one rung at a time; each rung fails against the minimal
 * code that satisfies every rung above it, so the first failure is always the next thing to write.
 *
 * <ol>
 *   <li>greet a single hard-coded-looking name &rarr; the output format exists at all
 *   <li>greet a <em>different</em> single name &rarr; the name must come from input
 *   <li>greet two names &rarr; the loop over {@code C}
 * </ol>
 *
 * <p>The ladder is only three rungs long because the problem holds only three decisions. The tests
 * below it are marked GUARD: they pin the judge's sample and the stated input alphabet, and no
 * partial implementation of this problem fails them. That distinction is measured, not asserted --
 * every rung here was checked against the intermediate implementations it is supposed to break.
 */
class HelloWorldTest {

  // RUNG 1 -- forces: producing "Hello, <name>!" at all.
  // Fails on: an empty main. The greeting's comma, space and bang are all pinned here, so this is
  // the only rung that has to change if the judge's format ever does.
  @Test
  @StdIo({"1", "Algospot"})
  void greetsASingleName(StdOut out) throws IOException {
    Main.main(new String[0]);

    assertThat(out.capturedString().trim()).isEqualTo("Hello, Algospot!");
  }

  // RUNG 2 -- forces: reading the name from stdin rather than hard-coding it.
  // Fails on: rung-1 code that prints the literal "Hello, Algospot!". This is the triangulation
  // step -- one example can always be faked, two of the same shape cannot.
  @Test
  @StdIo({"1", "World"})
  void greetsWhicheverNameIsGiven(StdOut out) throws IOException {
    Main.main(new String[0]);

    assertThat(out.capturedString().trim()).isEqualTo("Hello, World!");
  }

  // RUNG 3 -- forces: consuming the count line, then looping over the names.
  // Fails on: rung-2 code that reads exactly one name and stops, which prints a single line here.
  @Test
  @StdIo({"2", "World", "Algospot"})
  void greetsEveryNameInInputOrder(StdOut out) throws IOException {
    Main.main(new String[0]);

    assertThat(out.capturedLines()).containsExactly("Hello, World!", "Hello, Algospot!");
  }

  // GUARD -- the judge's official sample, verbatim from problems/HELLOWORLD/samples/sample-01.
  // Not a rung: any implementation that clears rung 3 clears this too. It earns its place as the
  // characterization test that says "this is the exact artifact the judge accepted".
  @Test
  @StdIo({"5", "World", "Algospot", "Illu", "Jullu", "Kodori"})
  void officialSampleIsReproducedExactly(StdOut out) throws IOException {
    Main.main(new String[0]);

    assertThat(out.capturedLines())
        .containsExactly(
            "Hello, World!", "Hello, Algospot!", "Hello, Illu!", "Hello, Jullu!", "Hello, Kodori!");
  }

  // GUARD -- the stated input alphabet is letters AND digits, and names may repeat.
  // Not a rung: nothing in the solution branches on the characters in a name. It documents that
  // "Kodori2" is a name and not a malformed count, and that repeats are not deduplicated.
  @Test
  @StdIo({"3", "Illu", "Illu", "Kodori2"})
  void namesMayRepeatAndMayContainDigits(StdOut out) throws IOException {
    Main.main(new String[0]);

    assertThat(out.capturedLines())
        .containsExactly("Hello, Illu!", "Hello, Illu!", "Hello, Kodori2!");
  }

  // GUARD -- the upper bound on C is 50, a two-digit count.
  // Not a rung: BufferedReader.readLine plus Integer.parseInt handles one and two digits
  // identically. It exists so a future rewrite to a hand-rolled parser cannot silently read only
  // the first digit and greet five people instead of twelve.
  @Test
  @StdIo({
    "12", "n1", "n2", "n3", "n4", "n5", "n6", "n7", "n8", "n9", "n10", "n11", "n12",
  })
  void twoDigitCountIsReadInFull(StdOut out) throws IOException {
    Main.main(new String[0]);

    assertThat(out.capturedLines())
        .containsExactly(
            "Hello, n1!",
            "Hello, n2!",
            "Hello, n3!",
            "Hello, n4!",
            "Hello, n5!",
            "Hello, n6!",
            "Hello, n7!",
            "Hello, n8!",
            "Hello, n9!",
            "Hello, n10!",
            "Hello, n11!",
            "Hello, n12!");
  }
}
