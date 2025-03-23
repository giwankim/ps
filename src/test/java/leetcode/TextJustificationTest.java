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

  private static Stream<Arguments> getCurrentLine() {
    return Stream.of(
        Arguments.of(0, List.of("What", "must", "be")),
        Arguments.of(3, List.of("acknowledgment")),
        Arguments.of(4, List.of("shall", "be")));
  }

  @ParameterizedTest
  @MethodSource
  void justifyLine(List<String> line, int maxWidth, String expected) {
    String justifiedLine = sut.justifyLine(false, line, maxWidth);
    assertThat(justifiedLine).isEqualTo(expected);
  }

  private static Stream<Arguments> justifyLine() {
    return Stream.of(
        Arguments.of(List.of("This", "is", "an"), 16, "This    is    an"),
        Arguments.of(List.of("example", "of", "text"), 16, "example  of text"),
        Arguments.of(List.of("What", "must", "be"), 16, "What   must   be"),
        Arguments.of(List.of("acknowledgment"), 16, "acknowledgment  "),
        Arguments.of(List.of("Science", "is", "what", "we"), 20, "Science  is  what we"),
        Arguments.of(List.of("understand", "well"), 20, "understand      well"),
        Arguments.of(List.of("enough", "to", "explain", "to"), 20, "enough to explain to"),
        Arguments.of(List.of("a", "computer.", "Art", "is"), 20, "a  computer.  Art is"),
        Arguments.of(List.of("everything", "else", "we"), 20, "everything  else  we"));
  }

  @ParameterizedTest
  @MethodSource
  void justifyLastLine(List<String> line, int maxWidth, String expected) {
    String justifiedLine = sut.justifyLine(true, line, maxWidth);
    assertThat(justifiedLine).isEqualTo(expected);
  }

  private static Stream<Arguments> justifyLastLine() {
    return Stream.of(
        Arguments.of(List.of("shall", "be"), 16, "shall be        "),
        Arguments.of(List.of("do"), 20, "do                  "));
  }

  @ParameterizedTest
  @MethodSource
  void fullJustify(String[] words, int maxWidth, List<String> expected) {
    List<String> actual = sut.fullJustify(words, maxWidth);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> fullJustify() {
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
