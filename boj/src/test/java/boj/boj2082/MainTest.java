package boj.boj2082;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/** BOJ 2082 시계 (Clock). */
class MainTest {

  // --- The three samples published with the problem. ---

  @Test
  @StdIo({
    "#.# ... ... #..",
    "#.# ... ... #..",
    "#.# ### ### ###",
    "#.# #.. ..# ..#",
    "### ### ### ..#"
  })
  void publishedSampleOneReadsAsTwoThirtyFour(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Hour-tens glyph is 0 with the top-middle diode dark (matches {0, 8} → smallest 0).
    // Hour-ones glyph forces digit 2; minute-tens forces 3; minute-ones forces 4. Output 02:34.
    assertThat(out.capturedString().trim()).isEqualTo("02:34");
  }

  @Test
  @StdIo({
    "### ### ..# ...",
    "... #.# #.# #.#",
    "### #.# .#. .#.",
    "#.. #.# ..# ..#",
    "### #.# ..# ###"
  })
  void publishedSampleTwoReadsAsTwentyFortyEight(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Hour-tens forces 2; hour-ones forces 0; minute-tens forces 4; minute-ones forces 8.
    assertThat(out.capturedString().trim()).isEqualTo("20:48");
  }

  @Test
  @StdIo({
    "#.. #.# #.# .#.",
    "..# #.. ... #..",
    "#.# .## ### .##",
    "..# #.# ... ...",
    "#.# ### ##. ###"
  })
  void publishedSampleThreeReadsAsSixTwentyFive(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Hour-tens reads as 0 (broken display compatible with 0); hour-ones forces 6;
    // minute-tens forces 2; minute-ones forces 5. Output 06:25.
    assertThat(out.capturedString().trim()).isEqualTo("06:25");
  }

  // --- Every diode broken: nothing is lit, so every digit is a valid reading. ---

  @Test
  @StdIo({
    "... ... ... ...",
    "... ... ... ...",
    "... ... ... ...",
    "... ... ... ...",
    "... ... ... ..."
  })
  void allDiodesBrokenReadsAsMidnight(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Empty lit-set is a subset of every digit's lit-set, so every position picks the smallest:
    // 0 for hh-tens (≤ 2), 0 for hh-ones, 0 for mm-tens (≤ 5), 0 for mm-ones. Output 00:00.
    assertThat(out.capturedString().trim()).isEqualTo("00:00");
  }

  // --- A fully-lit, unbroken display of four zeros reads as 00:00. The only other digit whose
  //     lit-set is a superset of 0's is 8 (it adds the center diode), but 0 < 8. ---

  @Test
  @StdIo({
    "### ### ### ###",
    "#.# #.# #.# #.#",
    "#.# #.# #.# #.#",
    "#.# #.# #.# #.#",
    "### ### ### ###"
  })
  void unbrokenZerosAtEveryPositionReadAsMidnight(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Each "0" glyph matches {0, 8}; smallest is 0 in every position. Output 00:00.
    assertThat(out.capturedString().trim()).isEqualTo("00:00");
  }

  // --- The lit-set of 0 contains the lit-set of 1 (1's right column is also lit in 0).
  //     Therefore a literal "1" glyph is always also a valid reading of 0. ---

  @Test
  @StdIo({
    "..# ..# ..# ..#",
    "..# ..# ..# ..#",
    "..# ..# ..# ..#",
    "..# ..# ..# ..#",
    "..# ..# ..# ..#"
  })
  void unbrokenOneGlyphAtEveryPositionReadsAsMidnight(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The "1" lit-set {(0,2),(1,2),(2,2),(3,2),(4,2)} is a subset of 0's lit-set, so each
    // position reads as 0. Output 00:00, NOT 11:11.
    assertThat(out.capturedString().trim()).isEqualTo("00:00");
  }

  // --- The lit-set of 0 also contains the lit-set of 7. A literal "7" glyph reads as 0. ---

  @Test
  @StdIo({
    "### ### ### ###",
    "..# ..# ..# ..#",
    "..# ..# ..# ..#",
    "..# ..# ..# ..#",
    "..# ..# ..# ..#"
  })
  void unbrokenSevenGlyphAtEveryPositionReadsAsMidnight(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The "7" lit-set {(0,0),(0,1),(0,2),(1,2),(2,2),(3,2),(4,2)} is a subset of 0's lit-set;
    // 0 < 7, so each position reads as 0. Output 00:00, NOT 17:17 or 07:07.
    assertThat(out.capturedString().trim()).isEqualTo("00:00");
  }

  // --- The lit-set of 8 contains the lit-set of 9 (9 lacks only the lower-left diode that 8 has).
  //     Therefore a literal "9" glyph always also reads as 8, never as 9. ---

  @Test
  @StdIo({
    "### ### ### ###",
    "#.# #.# #.# #.#",
    "#.# #.# #.# ###",
    "#.# #.# #.# ..#",
    "### ### ### ###"
  })
  void unbrokenNineGlyphAtMinuteOnesReadsAsEight(StdOut out) throws IOException {
    Main.main(new String[0]);
    // First three positions are unbroken 0 → 0. Minute-ones is exact "9" but is also a subset
    // of 8's lit-set, and 8 < 9, so it reads as 8. Output 00:08, NOT 00:09.
    assertThat(out.capturedString().trim()).isEqualTo("00:08");
  }

  // --- The "2" lit-set is NOT a subset of 0's (0 lacks the row-2 middle diode and the lower-left
  //     diode that 2 has). Among 0..9 the "2" glyph is matched only by {2, 8}, so it reads as 2
  //     in every position. ---

  @Test
  @StdIo({
    "### ### ### ###",
    "..# ..# ..# ..#",
    "### ### ### ###",
    "#.. #.. #.. #..",
    "### ### ### ###"
  })
  void unbrokenTwoGlyphAtEveryPositionReadsAsTwentyTwoTwentyTwo(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The "2" lit-set is matched only by {2, 8}; at hh-tens the per-position cap of 2 picks 2,
    // and 2 is the smallest match at every other position too. Every position reads as 2.
    assertThat(out.capturedString().trim()).isEqualTo("22:22");
  }

  // --- The latest time the clock can ever print: 23:58. Each unbroken 2/3/5/8 glyph is matched
  //     only by digits at or above the target, with the target as the smallest match. Minute-ones
  //     in particular is the exact "8" glyph: among 0..9 only 8 has a lit-set covering it (9
  //     lacks the lower-left diode), so the position is forced to 8. ---

  @Test
  @StdIo({
    "### ### ### ###",
    "..# ..# #.. #.#",
    "### ### ### ###",
    "#.. ..# ..# #.#",
    "### ### ### ###"
  })
  void unbrokenLatestPrintableTimeReadsAsTwentyThreeFiftyEight(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Hour-tens (exact 2) matches {2, 8}, smallest in 0..2 is 2.
    // Hour-ones (exact 3) matches {3, 8, 9}, smallest in 0..9 is 3.
    // Minute-tens (exact 5) matches {5, 6, 8, 9}, smallest in 0..5 is 5.
    // Minute-ones (exact 8) is matched only by 8.
    assertThat(out.capturedString().trim()).isEqualTo("23:58");
  }

  // --- Output format: a single ':' separator between the two-digit hour and minute fields, with
  //     leading zeros preserved. Minute-tens forced to 5 alone, the rest 0. ---

  @Test
  @StdIo({
    "### ### ### ###",
    "#.# #.# #.. #.#",
    "#.# #.# ### #.#",
    "#.# #.# ..# #.#",
    "### ### ### ###"
  })
  void formatsAsTwoColonTwoWithLeadingZerosPreserved(StdOut out) throws IOException {
    Main.main(new String[0]);
    // First two positions are unbroken 0 → 0. Minute-tens is exact "5": matched by {5, 6, 8, 9};
    // smallest in 0..5 is 5. Minute-ones is unbroken 0 → 0. Output 00:50, with the colon
    // between the second and third digits and both leading zeros preserved.
    assertThat(out.capturedString().trim()).isEqualTo("00:50");
  }
}
