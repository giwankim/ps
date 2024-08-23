package com.giwankim.algospot.helloworld;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.*;
import org.junit.jupiter.api.Test;

class HelloWorldTest {
  @Test
  void helloWorld() {
    String input =
        """
        5
        World
        Algospot
        Illu
        Jullu
        Kodori
        """;
    String expected =
        """
        Hello, World!
        Hello, Algospot!
        Hello, Illu!
        Hello, Jullu!
        Hello, Kodori!
        """;

    InputStream is = System.in;
    PrintStream ps = System.out;

    System.setIn(new ByteArrayInputStream(input.getBytes()));

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    System.setOut(new PrintStream(out));

    try {
      Main.main(new String[0]);
      assertThat(out).hasToString(expected);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      System.setIn(is);
      System.setOut(ps);
    }
  }
}
