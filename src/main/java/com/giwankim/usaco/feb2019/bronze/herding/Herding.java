package com.giwankim.usaco.feb2019.bronze.herding;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Herding {
  public static void main(String[] args) throws IOException {
    try (BufferedReader r = new BufferedReader(new FileReader("herding.in"));
        PrintWriter pw = new PrintWriter("herding.out")) {
      herding(r, pw);
    }
  }

  public static void herding(BufferedReader r, PrintWriter pw) throws IOException {
    StringTokenizer st = new StringTokenizer(r.readLine());
    int a = Integer.parseInt(st.nextToken());
    int b = Integer.parseInt(st.nextToken());
    int c = Integer.parseInt(st.nextToken());

    int[] nums = {a, b, c};
    Arrays.sort(nums);
    a = nums[0];
    b = nums[1];
    c = nums[2];

    int minMoves = 2;
    if (c - a == 2) {
      minMoves = 0;
    } else if (b - a == 2 || c - b == 2) {
      minMoves = 1;
    }
    int maxMoves = Math.max(b - a - 1, c - b - 1);

    pw.println(minMoves);
    pw.println(maxMoves);
  }
}
