package misc;

import java.util.HashSet;
import java.util.Set;

/**
 * CommitOffset
 * <p>
 * offset 값으로 이루어진 배열이 주어집니다. offset은 0 이상의 정수이며 중복은 없습니다.
 * 이 배열의 값들은 index-0부터 차례로 처리되며, 각 offset이 주어질때마다 우리는 해당 offset과 함께 현재까지 받은 offset들중 최대한 높은 offset을 commit하려고합니다.
 * <p>
 * Commit을 하기 위해서는 0부터 해당 offset까지의 값들이 모두 있어야 합니다.
 * 즉, offset 2를 commit하기 위해서는 0, 1, 2를 모두 가지고있어야합니다.
 * 만약 해당 offset을 받은 시점에 추가로 commit 할 수 없으면 -1을 리턴합니다.
 * <p>
 * Offset들로 이루어진 배열이 주어지고 차례로 처리했을 때, 각각의 시점에서 commit할 수 있는 가장 높은 offset(처리할 수 없다면 -1)을 담은 배열을 리턴하세요.
 * <p>
 * 예제 1:
 * 입력: [2, 0, 1]
 * 출력: [-1, 0, 2]
 * 설명:
 * 2: 2를 commit 하기 위해서 0과 1이 필요한데 아직 없으므로 -1을 리턴합니다
 * 0: 현재까지 [0, 2]의 offset을 받았으며 처리할 수 있는 가장 큰 offset은 0입니다
 * 1: 이제 1의 offset이 생겼으므로 현재까지 [0, 1, 2]의 offset을 받았으며, 이 때 처리할 수 있는 가장 높은 offset은 2입니다
 */
public class CommitOffset {
  public int[] commitOffsets(int[] offsets) {
    int[] result = new int[offsets.length];

    int currentOffset = 0;
    Set<Integer> seen = new HashSet<>();

    for (int i = 0; i < offsets.length; i++) {
      seen.add(offsets[i]);
      if (seen.contains(currentOffset)) {
        while (seen.contains(currentOffset)) {
          currentOffset += 1;
        }
        result[i] = currentOffset - 1;
      } else {
        result[i] = -1;
      }
    }
    return result;
  }
}
