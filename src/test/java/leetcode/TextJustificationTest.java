package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class TextJustificationTest {
  private TextJustification sut;

  @BeforeEach
  void setUp() {
    sut = new TextJustification();
  }

  @ParameterizedTest
  @MethodSource
  void getCurrentLine(int lineStart, List<String> expected) {
    String[] words = {"What", "must", "be", "acknowledgment", "shall", "be"};
    List<String> currentLine = sut.getCurrentLine(lineStart, words, 16);
    assertThat(currentLine).isEqualTo(expected);
  }

  static Stream<Arguments> getCurrentLine() {
    return Stream.of(
        Arguments.of(0, List.of("What", "must", "be")),
        Arguments.of(3, List.of("acknowledgment")),
        Arguments.of(4, List.of("shall", "be")));
  }

  @ParameterizedTest
  @MethodSource
  void fullJustify(String[] words, int maxWidth, List<String> expected) {
    List<String> actual = sut.fullJustify(words, maxWidth);
    assertThat(actual).isEqualTo(expected);
  }

  static Stream<Arguments> fullJustify() {
    return Stream.of(
        Arguments.of(
            new String[] {"This", "is", "an", "example", "of", "text", "justification."},
            16,
            List.of("This    is    an", "example  of text", "justification.  ")),
        Arguments.of(
            new String[] {"What", "must", "be", "acknowledgment", "shall", "be"},
            16,
            List.of("What   must   be", "acknowledgment  ", "shall be        ")),
        Arguments.of(
            new String[] {
              "Science",
              "is",
              "what",
              "we",
              "understand",
              "well",
              "enough",
              "to",
              "explain",
              "to",
              "a",
              "computer.",
              "Art",
              "is",
              "everything",
              "else",
              "we",
              "do"
            },
            20,
            List.of(
                "Science  is  what we",
                "understand      well",
                "enough to explain to",
                "a  computer.  Art is",
                "everything  else  we",
                "do                  ")));
  }
}
