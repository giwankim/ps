package com.giwankim.leetcode;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

public class MinimumGeneticMutation {
  private static final char[] ALPHABET = "ACGT".toCharArray();

  public int minMutation(String startGene, String endGene, String[] bank) {
    // Time complexity: O(1), Space complexity: O(1)
    Set<String> bankSet = Set.copyOf(Arrays.asList(bank));

    Queue<String> queue = new ArrayDeque<>();
    Set<String> visited = new HashSet<>();
    queue.offer(startGene);
    visited.add(startGene);

    int result = 0;

    while (!queue.isEmpty()) {
      int size = queue.size();
      // explore all mutations at given level
      while (size-- > 0) {
        String gene = queue.poll();
        if (Objects.equals(gene, endGene)) {
          return result;
        }
        // mutate gene
        for (int i = 0; i < gene.length(); i++) {
          StringBuilder sb = new StringBuilder(gene);
          for (char c : ALPHABET) {
            char d = sb.charAt(i);
            sb.setCharAt(i, c);
            String newGene = sb.toString();
            if (bankSet.contains(newGene) && !visited.contains(newGene)) {
              queue.offer(newGene);
              visited.add(newGene);
            }
            sb.setCharAt(i, d);
          }
        }
      }
      result += 1;
    }
    return -1;
  }
}
