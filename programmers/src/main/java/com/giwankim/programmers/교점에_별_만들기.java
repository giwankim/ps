package com.giwankim.programmers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class 교점에_별_만들기 {
  public String[] solution(int[][] line) {
    List<Point> points = new ArrayList<>();
    for (int i = 0; i < line.length; i++) {
      for (int j = i + 1; j < line.length; j++) {
        Point point = intersect(line[i], line[j]);
        if (point != null) {
          points.add(point);
        }
      }
    }

    Point lowerLeft = getLowerLeftCorner(points);
    Point upperRight = getUpperRightCorner(points);

    return draw(upperRight, lowerLeft, points);
  }

  private static String[] draw(Point upperRight, Point lowerLeft, List<Point> points) {
    int width = (int) (upperRight.getX() - lowerLeft.getX() + 1);
    int height = (int) (upperRight.getY() - lowerLeft.getY() + 1);

    char[][] chars = new char[height][width];
    for (char[] row : chars) {
      Arrays.fill(row, '.');
    }

    for (Point point : points) {
      int x = (int) (point.getX() - lowerLeft.getX());
      int y = (int) (upperRight.getY() - point.getY());
      chars[y][x] = '*';
    }

    String[] answer = new String[height];
    for (int i = 0; i < answer.length; i++) {
      answer[i] = String.valueOf(chars[i]);
    }
    return answer;
  }

  private Point getLowerLeftCorner(List<Point> points) {
    long x = Long.MAX_VALUE;
    long y = Long.MAX_VALUE;
    for (Point point : points) {
      if (point.getX() < x) {
        x = point.getX();
      }
      if (point.getY() < y) {
        y = point.getY();
      }
    }
    return new Point(x, y);
  }

  private Point getUpperRightCorner(List<Point> points) {
    long x = Long.MIN_VALUE;
    long y = Long.MIN_VALUE;
    for (Point point : points) {
      if (point.getX() > x) {
        x = point.getX();
      }
      if (point.getY() > y) {
        y = point.getY();
      }
    }
    return new Point(x, y);
  }

  private Point intersect(int[] l1, int[] l2) {
    long a = l1[0];
    long b = l1[1];
    long e = l1[2];
    long c = l2[0];
    long d = l2[1];
    long f = l2[2];
    double x = (double) (b * f - e * d) / (a * d - b * c);
    double y = (double) (e * c - a * f) / (a * d - b * c);
    if (x % 1 != 0 || y % 1 != 0) {
      return null;
    }
    return new Point((long) x, (long) y);
  }

  public static class Point {
    private final long x;
    private final long y;

    public Point(long x, long y) {
      this.x = x;
      this.y = y;
    }

    public long getX() {
      return x;
    }

    public long getY() {
      return y;
    }
  }
}
