package boj.boj1759;

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
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * BOJ 1759 암호 만들기 (Making Password) -- from C distinct lowercase letters, list every password that
 * can be built from them.
 *
 * <p>Input: line 1 holds two integers L and C ({@code 3 <= L <= C <= 15}); line 2 holds the C
 * distinct lowercase letters separated by spaces, in arbitrary order. A password is a choice of L
 * of those letters that (1) is written in strictly increasing alphabetical order, (2) contains at
 * least one vowel (a, e, i, o, u), and (3) contains at least two consonants. Output: every such
 * password in dictionary order, one per line; print nothing when no choice qualifies.
 *
 * <p>Because the letters are distinct and each password is sorted, a password is exactly a size-L
 * subset of the letters. There are at most {@code C(15, 7) = 6435} subsets, so enumerating them all
 * and filtering by the vowel/consonant rule is ample -- no cleverness is required, only the rule
 * applied correctly.
 *
 * <p>The acceptance test is a conjunction -- at least one vowel AND at least two consonants -- so
 * the fixtures pin each floor from its own side: an all-consonant alphabet fails the vowel floor
 * ({@link #smallestInputAllConsonantsFailsTheVowelFloor}) while a vowel-heavy choice carrying a
 * single consonant fails the consonant floor ({@link #singleConsonantFailsEvenAmidManyVowels}).
 * There is no upper bound on vowels ({@link #extraVowelsAreFineWhileTwoConsonantsRemain}), and the
 * lone vowel of an otherwise-consonant alphabet must appear in every password
 * ({@link #everyPasswordMustIncludeTheOnlyVowel}).
 *
 * <p>Two ordering hazards are pinned separately. The letters arrive unsorted, so the program must
 * sort them before forming passwords ({@link #unsortedInputStillProducesDictionaryOrder}); and the
 * whole output list must be in dictionary order, which is not the order a numeric subset
 * enumeration visits subsets in ({@link #mediumInstanceMixesBothVowelsInOrder}).
 *
 * <p>The randomized cases drive shuffled alphabets through stdin/stdout and cross-check the program
 * against an independent oracle that enumerates every subset and sorts the surviving passwords as
 * strings -- a deliberately different route to the answer than any single-pass backtracking solver.
 */
class MainTest {

  // --- Official sample from the statement. ---

  @Test
  @StdIo({"4 6", "a t c i s w"})
  void officialSampleListsTheFourteenValidPasswords(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Letters sort to a c i s t w (vowels a,i; consonants c,s,t,w). Every length-4 combination is
    // valid except cstw (no vowel), so 14 of the C(6,4) = 15 combinations survive, printed in
    // dictionary order one per line.
    assertThat(out.capturedString().trim())
        .isEqualTo(
            "acis\nacit\naciw\nacst\nacsw\nactw\naist\naisw\naitw\nastw\ncist\ncisw\ncitw\nistw");
  }

  // --- Smallest worlds: L = C = 3, so the whole alphabet is the one and only candidate. ---

  @Test
  @StdIo({"3 3", "a b c"})
  void smallestInputOneVowelTwoConsonantsIsValid(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The single candidate abc has one vowel (a) and two consonants (b, c) -- the exact minimum a
    // length-3 password needs, so it is printed.
    assertThat(out.capturedString().trim()).isEqualTo("abc");
  }

  @Test
  @StdIo({"3 3", "b c d"})
  void smallestInputAllConsonantsFailsTheVowelFloor(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The single candidate bcd carries no vowel, so it fails the "at least one vowel" floor and
    // nothing is printed.
    assertThat(out.capturedString().trim()).isEqualTo("");
  }

  @Test
  @StdIo({"3 3", "a e i"})
  void smallestInputAllVowelsFailsTheTwoConsonantFloor(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The single candidate aei is all vowels: it clears the vowel floor but has zero consonants, so
    // it fails the "at least two consonants" floor and nothing is printed.
    assertThat(out.capturedString().trim()).isEqualTo("");
  }

  // --- The vowel floor at L = 3: with one vowel available it must be in every password. ---

  @Test
  @StdIo({"3 4", "a b c d"})
  void everyPasswordMustIncludeTheOnlyVowel(StdOut out) throws IOException {
    Main.main(new String[0]);
    // a is the only vowel, so a valid length-3 password is a plus two of {b, c, d}: abc, abd, acd.
    // The all-consonant combination bcd is rejected by the vowel floor.
    assertThat(out.capturedString().trim()).isEqualTo("abc\nabd\nacd");
  }

  // --- Sorting: the letters arrive in arbitrary order but the output must be dictionary order. ---

  @Test
  @StdIo({"3 4", "d c b a"})
  void unsortedInputStillProducesDictionaryOrder(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The same four letters as above arrive fully reversed; the program must sort them before
    // forming passwords, so the output is identical: abc, abd, acd.
    assertThat(out.capturedString().trim()).isEqualTo("abc\nabd\nacd");
  }

  // --- Counting: at least two consonants is the floor; vowels have no ceiling. ---

  @Test
  @StdIo({"4 4", "a b c e"})
  void twoVowelsAndTwoConsonantsIsValid(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The single candidate abce has two vowels (a, e) and two consonants (b, c), meeting both
    // floors exactly, so it is printed.
    assertThat(out.capturedString().trim()).isEqualTo("abce");
  }

  @Test
  @StdIo({"5 5", "a b c e i"})
  void extraVowelsAreFineWhileTwoConsonantsRemain(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The single candidate abcei holds three vowels (a, e, i) and two consonants (b, c). Three
    // vowels is allowed -- there is no upper bound on vowels, only the two-consonant floor.
    assertThat(out.capturedString().trim()).isEqualTo("abcei");
  }

  @Test
  @StdIo({"5 5", "a e i o b"})
  void singleConsonantFailsEvenAmidManyVowels(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Sorted, the single candidate is abeio: four vowels but only one consonant (b). It clears the
    // vowel floor yet fails the two-consonant floor, so nothing is printed.
    assertThat(out.capturedString().trim()).isEqualTo("");
  }

  // --- A medium instance: both vowels in play and several passwords, exercising the ordering. ---

  @Test
  @StdIo({"3 5", "a b c d e"})
  void mediumInstanceMixesBothVowelsInOrder(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Vowels a, e and consonants b, c, d. Valid length-3 passwords are those with exactly one vowel
    // and two consonants: a-led abc, abd, acd then e-led bce, bde, cde. The e-led passwords sort
    // after the a-led ones because their first letter is a consonant > a -- dictionary order across
    // the whole list, not subset-enumeration order.
    assertThat(out.capturedString().trim()).isEqualTo("abc\nabd\nacd\nbce\nbde\ncde");
  }

  // --- Upper bound C = 15, and the forced single combination when L = C. ---

  @Test
  @StdIo({"15 15", "o n m l k j i h g f e d c b a"})
  void fullAlphabetForcedAsOnePasswordAtTheUpperBound(StdOut out) throws IOException {
    Main.main(new String[0]);
    // L = C = 15 forces the one subset that is the whole alphabet. The fifteen letters arrive fully
    // reversed and must be sorted; the result has four vowels (a, e, i, o) and eleven consonants,
    // so
    // the lone password is the sorted alphabet a..o.
    assertThat(out.capturedString().trim()).isEqualTo("abcdefghijklmno");
  }

  @Test
  @StdIo({"15 4", "b c d f g h j k l m n p q r s"})
  void alphabetWithoutVowelsYieldsNothing(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Fifteen consonants and no vowel anywhere: every length-4 subset fails the vowel floor, so the
    // answer is empty regardless of how the 1365 subsets are explored.
    assertThat(out.capturedString().trim()).isEqualTo("");
  }

  // --- Randomized scale, cross-checked against an independent subset-enumeration oracle. ---

  @Test
  void randomWideBreadthMatchesOracle() throws IOException {
    char[] letters = shuffledAlphabet(15, new Random(1759L));
    // L = 3 over the full alphabet is the widest case: C(15, 3) = 455 candidates, many of them
    // all-consonant triples the vowel floor must drop.
    assertThat(runMain(buildInput(letters, 3))).isEqualTo(solve(letters, 3));
  }

  @Test
  void randomMidWidthMatchesOracle() throws IOException {
    char[] letters = shuffledAlphabet(15, new Random(17591L));
    // L = 7 is the heaviest band: C(15, 7) = 6435 candidates spanning every legal vowel/consonant
    // mix, so the filter and the dictionary ordering are both stressed at once.
    assertThat(runMain(buildInput(letters, 7))).isEqualTo(solve(letters, 7));
  }

  @Test
  void randomNearFullMatchesOracle() throws IOException {
    char[] letters = shuffledAlphabet(15, new Random(17592L));
    // L = 13 drops only two letters, so all C(15, 13) = 105 subsets keep enough vowels and
    // consonants to pass -- a long, fully-accepted output that pins the ordering at length.
    assertThat(runMain(buildInput(letters, 13))).isEqualTo(solve(letters, 13));
  }

  @Test
  void smallerAlphabetMatchesOracle() throws IOException {
    char[] letters = shuffledAlphabet(10, new Random(17593L));
    // A second shape (C = 10, L = 4) varies the dimensions away from the C = 15 ceiling.
    assertThat(runMain(buildInput(letters, 4))).isEqualTo(solve(letters, 4));
  }

  private static boolean isVowel(char ch) {
    return ch == 'a' || ch == 'e' || ch == 'i' || ch == 'o' || ch == 'u';
  }

  /**
   * Independent oracle: enumerate every C-bit subset, keep those of size {@code l} that hold at
   * least one vowel and at least two consonants, and return the surviving passwords joined by
   * newlines in dictionary order. The candidate letters are sorted first so each password reads in
   * increasing order, and the password list is sorted as strings because numeric subset-enumeration
   * order is not dictionary order. Returns {@code ""} when nothing qualifies.
   */
  private static String solve(char[] letters, int l) {
    char[] sorted = letters.clone();
    Arrays.sort(sorted);
    int c = sorted.length;
    List<String> passwords = new ArrayList<>();
    for (int mask = 0; mask < (1 << c); mask++) {
      if (Integer.bitCount(mask) != l) {
        continue;
      }
      int vowels = 0;
      int consonants = 0;
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < c; i++) {
        if ((mask & (1 << i)) == 0) {
          continue;
        }
        char ch = sorted[i];
        sb.append(ch);
        if (isVowel(ch)) {
          vowels++;
        } else {
          consonants++;
        }
      }
      if (vowels >= 1 && consonants >= 2) {
        passwords.add(sb.toString());
      }
    }
    Collections.sort(passwords);
    return String.join("\n", passwords);
  }

  /** Builds BOJ 1759 input: an {@code "L C"} header line then the C letters separated by spaces. */
  private static String buildInput(char[] letters, int l) {
    StringBuilder sb = new StringBuilder();
    sb.append(l).append(' ').append(letters.length).append('\n');
    for (int i = 0; i < letters.length; i++) {
      if (i > 0) {
        sb.append(' ');
      }
      sb.append(letters[i]);
    }
    sb.append('\n');
    return sb.toString();
  }

  /**
   * The first {@code c} lowercase letters a..(a + c - 1), Fisher-Yates shuffled with {@code rng} so
   * the generated input is unsorted and the solver is forced to sort it.
   */
  private static char[] shuffledAlphabet(int c, Random rng) {
    char[] letters = new char[c];
    for (int i = 0; i < c; i++) {
      letters[i] = (char) ('a' + i);
    }
    for (int i = c - 1; i > 0; i--) {
      int j = rng.nextInt(i + 1);
      char tmp = letters[i];
      letters[i] = letters[j];
      letters[j] = tmp;
    }
    return letters;
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
