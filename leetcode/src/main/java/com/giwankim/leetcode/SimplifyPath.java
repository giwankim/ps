package com.giwankim.leetcode;

import java.util.ArrayList;
import java.util.List;

public class SimplifyPath {
  public String simplifyPath(String path) {
    List<String> list = new ArrayList<>();
    String[] components = path.split("/");

    for (String component : components) {
      if (component.isBlank() || ".".equals(component)) {
        continue;
      }

      if ("..".equals(component)) {
        if (!list.isEmpty()) {
          list.removeLast();
        }
      } else {
        list.add(component);
      }
    }

    StringBuilder sb = new StringBuilder();
    for (String component : list) {
      sb.append("/").append(component);
    }

    if (sb.isEmpty()) {
      return "/";
    } else {
      return sb.toString();
    }
  }
}
