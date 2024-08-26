package com.giwankim.codeforces.round839.div3;

import org.junit.jupiter.api.Test;

import java.io.*;

import static org.assertj.core.api.Assertions.assertThat;

class ATest {
  @Test
  void aPlusB() throws IOException {
    String input = """
        4
        4+2
        0+0
        3+7
        8+9
        """;
    String expected = """
        6
        0
        10
        17
        """;
    BufferedReader r = new BufferedReader(new StringReader(input));
    StringWriter out = new StringWriter();
    PrintWriter pw = new PrintWriter(out);

    A.aPlusB(r, pw);

    assertThat(out).hasToString(expected);
  }
}
