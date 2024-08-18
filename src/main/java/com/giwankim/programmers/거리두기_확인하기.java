package com.giwankim.programmers;

@SuppressWarnings("NonAsciiCharacters")
public class 거리두기_확인하기 {
  private static final int[] dx = {0, -1, 1, 0};
  private static final int[] dy = {-1, 0, 0, 1};

  public int[] solution(String[][] places) {
    int[] answer = new int[places.length];
    for (int i = 0; i < places.length; i++) {
      String[] place = places[i];
      char[][] room = new char[place.length][];
      for (int j = 0; j < place.length; j++) {
        room[j] = place[j].toCharArray();
      }
      answer[i] = isDistanced(room) ? 1 : 0;
    }
    return answer;
  }

  private boolean isDistanced(char[][] room) {
    for (int y = 0; y < room.length; y++) {
      for (int x = 0; x < room[y].length; x++) {
        if (room[y][x] != 'P') {
          continue;
        }
        if (!isDistanced(room, x, y)) {
          return false;
        }
      }
    }
    return true;
  }

  private boolean isDistanced(char[][] room, int x, int y) {
    for (int d = 0; d < 4; d++) {
      int nx = x + dx[d];
      int ny = y + dy[d];
      if (ny < 0 || nx < 0 || ny == room.length || nx == room[ny].length) {
        continue;
      }
      if (room[ny][nx] == 'P') {
        return false;
      } else if (room[ny][nx] == 'O') {
        if (!isDistanced(room, nx, ny, 3 - d)) {
          return false;
        }
      }
    }
    return true;
  }

  private boolean isDistanced(char[][] room, int x, int y, int exclude) {
    for (int d = 0; d < 4; d++) {
      if (d == exclude) {
        continue;
      }
      int nx = x + dx[d];
      int ny = y + dy[d];
      if (ny < 0 || ny == room.length || nx < 0 || nx == room[ny].length) {
        continue;
      }
      if (room[ny][nx] == 'P') {
        return false;
      }
    }
    return true;
  }
}
