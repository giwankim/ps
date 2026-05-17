package boj.boj2309;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

class MainTest {

  // Step 1: published sample. The two false dwarfs are 15 and 25, and the
  // remaining seven heights must be printed in ascending order.
  @Test
  @StdIo({"20", "7", "23", "19", "10", "15", "25", "8", "13"})
  void sample(StdOut out) throws IOException {
    Main.main(new String[0]);

    assertThat(out.capturedLines()).containsExactly("7", "8", "10", "13", "19", "20", "23");
  }

  // Step 2: output order is independent from input order. The input arrives in
  // descending order, and the two tallest heights are the false dwarfs.
  @Test
  @StdIo({"99", "98", "79", "6", "5", "4", "3", "2", "1"})
  void descendingInputStillPrintsAnswerAscending(StdOut out) throws IOException {
    Main.main(new String[0]);

    assertThat(out.capturedLines()).containsExactly("1", "2", "3", "4", "5", "6", "79");
  }

  // Step 3: the two shortest heights can be the false dwarfs, so keeping the
  // smallest seven sorted values is not sufficient.
  @Test
  @StdIo({"1", "2", "25", "15", "14", "13", "12", "11", "10"})
  void twoShortestHeightsCanBeFalseDwarfs(StdOut out) throws IOException {
    Main.main(new String[0]);

    assertThat(out.capturedLines()).containsExactly("10", "11", "12", "13", "14", "15", "25");
  }

  // Step 4: the maximum allowed input height can appear, but it cannot be part
  // of a valid seven-dwarf answer because the other six heights are positive.
  @Test
  @StdIo({"100", "20", "79", "6", "5", "4", "3", "2", "1"})
  void heightOneHundredCanBeAFalseDwarf(StdOut out) throws IOException {
    Main.main(new String[0]);

    assertThat(out.capturedLines()).containsExactly("1", "2", "3", "4", "5", "6", "79");
  }

  // Step 5: the false pair can be separated by real heights in the input, so
  // the search must consider arbitrary pairs, not only adjacent values.
  @Test
  @StdIo({"45", "1", "2", "3", "4", "5", "6", "34", "79"})
  void separatedFalseDwarfsAreRemovedTogether(StdOut out) throws IOException {
    Main.main(new String[0]);

    assertThat(out.capturedLines()).containsExactly("1", "2", "3", "4", "5", "6", "79");
  }

  // Step 6: the seven real dwarfs can already be the first seven input lines.
  // The program still needs to ignore trailing false dwarfs and sort the answer.
  @Test
  @StdIo({"79", "1", "2", "3", "4", "5", "6", "20", "100"})
  void answerCanComeFromTheFirstSevenInputLines(StdOut out) throws IOException {
    Main.main(new String[0]);

    assertThat(out.capturedLines()).containsExactly("1", "2", "3", "4", "5", "6", "79");
  }

  // Step 7: the seven real dwarfs can also be the last seven input lines. This
  // guards against stopping before all nine heights are considered.
  @Test
  @StdIo({"20", "100", "79", "1", "2", "3", "4", "5", "6"})
  void answerCanComeFromTheLastSevenInputLines(StdOut out) throws IOException {
    Main.main(new String[0]);

    assertThat(out.capturedLines()).containsExactly("1", "2", "3", "4", "5", "6", "79");
  }

  // Step 8: BOJ marks this as special judge. When more than one pair can be
  // excluded, any sorted seven heights from the input that sum to 100 is valid.
  @Test
  @StdIo({"1", "2", "3", "4", "5", "6", "40", "41", "42"})
  void multipleValidAnswersAreAccepted(StdOut out) throws IOException {
    Main.main(new String[0]);

    List<Integer> input = List.of(1, 2, 3, 4, 5, 6, 40, 41, 42);
    List<Integer> answer =
        Arrays.stream(out.capturedLines()).map(Integer::valueOf).toList();
    assertThat(answer).hasSize(7);
    assertThat(answer).isSorted();
    assertThat(answer).doesNotHaveDuplicates();
    assertThat(answer).allSatisfy(height -> assertThat(input).contains(height));
    assertThat(answer.stream().mapToInt(Integer::intValue).sum()).isEqualTo(100);
  }
}
