package atcoder.abc471.b;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.DefaultLocale;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * AtCoder ABC 471 B -- Survey Tabulation.
 *
 * <p>N (1 ≤ N ≤ 100) survey answers follow the count, one per line, each a string of 1 to 10
 * English letters. Print how many people gave the most popular answer, comparing answers without
 * regard to case: {@code AtCoder}, {@code ATCODER} and {@code atcoder} are one answer.
 *
 * <p>The tally is over people, not spellings, and it is a maximum rather than a total: tied answers
 * do not add up, and the count of the first or last answer read is not the answer. Folding case is
 * the only normalization allowed -- answers that merely share letters or a prefix stay distinct.
 * Every answer is given by someone, so the result is never below 1.
 */
class MainTest {

  // --- Official samples. ---

  @Test
  @StdIo({"7", "ARC", "abc", "ahc", "ABC", "beginner", "AbC", "ahc"})
  void officialSampleOneCountsThreeSpellingsOfAbcAsOneAnswer(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  @Test
  @StdIo({"10", "x", "x", "x", "x", "x", "x", "x", "x", "x", "x"})
  void officialSampleTwoHasEveryoneAgreeing(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("10");
  }

  // --- Case is not distinguished. ---

  @Test
  @StdIo({"3", "AtCoder", "ATCODER", "atcoder"})
  void theStatementsOwnExampleIsASingleAnswer(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  @Test
  @StdIo({"3", "abcdefghij", "ABCDEFGHIJ", "AbCdEfGhIj"})
  void caseIsFoldedAtEveryPositionOfTheLongestAllowedAnswer(StdOut out) throws IOException {
    // Ten letters, the maximum length: folding only the first character would report 1.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  @Test
  @DefaultLocale(language = "tr", country = "TR")
  @StdIo({"2", "I", "i"})
  void iAndDottedIAreTheSameAnswerEvenUnderATurkishDefaultLocale(StdOut out) throws IOException {
    // Turkish maps I to the dotless ı and i to the dotted İ, so a locale-sensitive
    // toLowerCase()/toUpperCase() splits these two answers and reports 1. Folding through
    // Locale.ROOT keeps them together.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- Case is the only normalization: answers sharing letters or a prefix stay apart. ---

  @Test
  @StdIo({"4", "abc", "cba", "bca", "cab"})
  void anagramsAreFourDifferentAnswers(StdOut out) throws IOException {
    // Keying on sorted characters would merge all four and report 4.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo({"4", "ab", "ba", "AB", "BA"})
  void caseFoldsButLetterOrderStillSeparatesAnswers(StdOut out) throws IOException {
    // ab/AB and ba/BA each pair up; a sorted-character key would instead report 4.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  @Test
  @StdIo({"3", "ab", "abc", "abcd"})
  void answersSharingAPrefixStayApart(StdOut out) throws IOException {
    // Matching with startsWith rather than equals would report 3.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- The answer is a maximum over groups, not the first group, the last group, the
  // number of groups, or their total. ---

  @Test
  @StdIo({"3", "zzz", "yy", "yy"})
  void theWinningAnswerNeedNotBeTheFirstOneRead(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  @Test
  @StdIo({"3", "yy", "yy", "zzz"})
  void theWinningAnswerNeedNotBeTheLastOneRead(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  @Test
  @StdIo({"6", "a", "a", "b", "b", "c", "c"})
  void tiedAnswersReportTheSharedMaximumRatherThanTheirTotal(StdOut out) throws IOException {
    // Three answers of two people each: not 6, and not the three distinct answers.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  @Test
  @StdIo({"5", "p", "q", "r", "s", "t"})
  void answersThatAreAllDifferentLeaveEveryoneTiedAtOne(StdOut out) throws IOException {
    // Reporting N, or the number of distinct answers, would give 5.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Constraint boundaries. ---

  @Test
  @StdIo({"1", "a"})
  void aLoneRespondentIsAMajorityOfOne(StdOut out) throws IOException {
    // The smallest N, and the smallest answer the problem can have.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  void hundredRespondentsAgreeingInMixedCase() throws IOException {
    // The largest N, spelled three ways, so the ceiling of the answer needs the fold too.
    StringBuilder input = new StringBuilder("100\n");
    String[] spellings = {"atcoder", "ATCODER", "AtCoder"};
    for (int i = 0; i < 100; i++) {
      input.append(spellings[i % 3]).append('\n');
    }
    assertThat(runMain(input.toString())).isEqualTo("100");
  }

  @Test
  void hundredRespondentsAllDisagreeing() throws IOException {
    // The largest N spread over 100 distinct answers: aa, ab, ... dv.
    StringBuilder input = new StringBuilder("100\n");
    for (int i = 0; i < 100; i++) {
      input.append((char) ('a' + i / 26)).append((char) ('a' + i % 26)).append('\n');
    }
    assertThat(runMain(input.toString())).isEqualTo("1");
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
