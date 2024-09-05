package leetcode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class AddBoldTag {
  public String addBoldTag(String s, String[] words) {
    // find all intervals
    List<Interval> intervals = getIntervals(s, words);

    // merge intervals
    List<Interval> mergedIntervals = merge(intervals);

    // construct string
    int previous = 0;
    StringBuilder sb = new StringBuilder();
    for (Interval interval : mergedIntervals) {
      sb.append(s, previous, interval.start);
      sb.append("<b>");
      sb.append(s, interval.start, interval.end + 1);
      sb.append("</b>");
      previous = interval.end + 1;
    }
    sb.append(s, previous, s.length());
    return sb.toString();
  }

  private List<Interval> merge(List<Interval> intervals) {
    if (intervals.isEmpty()) {
      return Collections.emptyList();
    }

    List<Interval> result = new ArrayList<>();
    Collections.sort(intervals);

    int start = intervals.get(0).start;
    int end = intervals.get(0).end;

    for (int i = 1; i < intervals.size(); i++) {
      Interval interval = intervals.get(i);
      if (interval.start - end <= 1) {
        end = Math.max(end, interval.end);
      } else {
        result.add(new Interval(start, end));
        start = interval.start;
        end = interval.end;
      }
    }
    result.add(new Interval(start, end));
    return result;
  }

  private static List<Interval> getIntervals(String s, String[] words) {
    List<Interval> intervals = new ArrayList<>();
    for (String word : words) {
      int index = s.indexOf(word);
      while (index != -1) {
        intervals.add(new Interval(index, index + word.length() - 1));
        index = s.indexOf(word, index + 1);
      }
    }
    return intervals;
  }

  public static class Interval implements Comparable<Interval> {
    int start;
    int end;

    public Interval(int start, int end) {
      this.start = start;
      this.end = end;
    }

    @Override
    public int compareTo(Interval that) {
      return Integer.compare(this.start, that.start);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof Interval interval)) {
        return false;
      }
      return start == interval.start && end == interval.end;
    }

    @Override
    public int hashCode() {
      return Objects.hash(start, end);
    }

    @Override
    public String toString() {
      return "Interval{" + "start=" + start + ", end=" + end + '}';
    }
  }
}
