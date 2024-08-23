package com.giwankim.algospot.helloworld;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class Main {
  public static void main(String[] args) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    PrintWriter pw = new PrintWriter(System.out);
    int C = Integer.parseInt(br.readLine());
    while (C-- > 0) {
      String s = br.readLine();
      pw.println("Hello, " + s + "!");
    }
    br.close();
    pw.close();
  }
}
