package programmers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class 비밀지도Test {
  비밀지도 sut = new 비밀지도();

  // Step 1: smallest valid map (1 ≦ n) — 두 지도가 모두 공백이면 전체 지도도 공백이다.
  //         빈 문자열이 아니라 공백 한 칸이어야 하며, 이후 모든 줄의 길이는 항상 n이다
  @Test
  void 한_칸_지도에서_두_지도가_모두_공백이면_공백_한_칸을_반환한다() {
    assertThat(sut.solution(1, new int[] {0}, new int[] {0})).containsExactly(" ");
  }

  // Step 2: 어느 하나라도 벽이면 벽 — 지도 1에만 벽이 있는 최소 사례.
  //         두 지도를 AND로 겹치는 구현은 여기서 공백을 낸다
  @Test
  void 한_칸_지도에서_지도1만_벽이어도_벽이_된다() {
    assertThat(sut.solution(1, new int[] {1}, new int[] {0})).containsExactly("#");
  }

  // Step 3: Step 2의 대칭 사례 — 지도 2만 벽인 경우.
  //         arr2를 읽지 않고 arr1만 해독하는 구현은 여기서 걸린다
  @Test
  void 한_칸_지도에서_지도2만_벽이어도_벽이_된다() {
    assertThat(sut.solution(1, new int[] {0}, new int[] {1})).containsExactly("#");
  }

  // Step 4: 겹침의 정의를 고정한다 — 두 지도가 같은 칸에 벽을 가져도 벽 하나다.
  //         XOR(^)로 겹치면 공백이 되고, 덧셈(1+1=2)으로 겹치면 자릿수가 늘어난다
  @Test
  void 두_지도가_같은_칸에_벽을_가지면_벽_하나로_합쳐진다() {
    assertThat(sut.solution(1, new int[] {1}, new int[] {1})).containsExactly("#");
  }

  // Step 5: 비트 순서 — 이진수의 최상위 비트가 지도의 가장 왼쪽 칸이다.
  //         줄을 뒤집어 출력하는 구현은 [" #", "# "]를 내놓는다
  @Test
  void 이진수의_최상위_비트가_가장_왼쪽_칸이_된다() {
    assertThat(sut.solution(2, new int[] {2, 1}, new int[] {0, 0})).containsExactly("# ", " #");
  }

  // Step 6: 두 배열은 같은 인덱스끼리만 겹친다 — 줄마다 벽의 출처가 다른데도 Step 5와
  //         같은 지도가 나와야 한다. 배열 전체를 한꺼번에 OR하면 두 줄 다 "##"가 된다
  @Test
  void arr1과_arr2는_같은_인덱스끼리만_겹친다() {
    assertThat(sut.solution(2, new int[] {2, 0}, new int[] {0, 1})).containsExactly("# ", " #");
  }

  // Step 7: 앞자리 0은 생략되지 않는다 — Integer.toBinaryString(1)은 "1"이라 그대로 쓰면
  //         길이 1인 줄이 나와 지도가 어긋난다. 왼쪽을 공백으로 채워 길이를 n으로 맞춰야 한다
  @Test
  void 앞자리_0은_생략되지_않고_공백으로_채워진다() {
    assertThat(sut.solution(3, new int[] {1, 0, 0}, new int[] {0, 0, 0}))
        .containsExactly("  #", "   ", "   ");
  }

  // Step 8: 뒤쪽 공백도 결과의 일부다 — trim()이나 stripTrailing()으로 다듬는 구현,
  //         또는 공백 줄을 ""로 줄이는 구현은 여기서 무너진다
  @Test
  void 뒤쪽_공백도_잘리지_않고_그대로_남는다() {
    assertThat(sut.solution(3, new int[] {4, 2, 0}, new int[] {0, 0, 0}))
        .containsExactly("#  ", " # ", "   ");
  }

  // Step 9: 서로 어긋난 벽이 합쳐져 한 줄을 채운다 — 1010 | 0101 = 1111.
  //         한쪽 지도만으로는 어느 줄도 완성되지 않으므로 두 지도를 겹쳐야만 나오는 결과다
  @Test
  void 두_지도의_벽이_서로_어긋나_있으면_한_줄이_모두_벽이_된다() {
    assertThat(sut.solution(4, new int[] {10, 5, 0, 15}, new int[] {5, 10, 0, 0}))
        .containsExactly("####", "####", "    ", "####");
  }

  // Step 10: official example 1 — 처음으로 여러 줄이 서로 다른 모양을 갖는 실제 예제
  @Test
  void 공식_예제1_5칸_지도를_해독한다() {
    assertThat(sut.solution(5, new int[] {9, 20, 28, 18, 11}, new int[] {30, 1, 21, 17, 28}))
        .containsExactly("#####", "# # #", "### #", "#  ##", "#####");
  }

  // Step 11: official example 2 — 앞뒤가 모두 공백인 줄(" #### ")이 포함되어
  //          Step 7의 왼쪽 채우기와 Step 8의 오른쪽 보존이 한 줄에서 동시에 요구된다
  @Test
  void 공식_예제2_6칸_지도를_해독한다() {
    assertThat(
            sut.solution(6, new int[] {46, 33, 33, 22, 31, 50}, new int[] {27, 56, 19, 14, 14, 10}))
        .containsExactly("######", "###  #", "##  ##", " #### ", " #####", "### # ");
  }

  // Step 12: 최대 크기(n = 16)에서의 양 끝 비트 — 2^15와 2^0만 켜져 있어
  //          첫 칸과 마지막 칸에만 벽이 선다. 폭이 16으로 고정되는지도 함께 확인한다
  @Test
  void 최대_크기_지도에서_양_끝_비트가_각각_첫_칸과_마지막_칸이_된다() {
    int[] arr1 = new int[16];
    int[] arr2 = new int[16];
    Arrays.fill(arr1, 1 << 15);
    Arrays.fill(arr2, 1);

    String[] expected = new String[16];
    Arrays.fill(expected, "#" + " ".repeat(14) + "#");

    assertThat(sut.solution(16, arr1, arr2)).containsExactly(expected);
  }

  // Step 13: 최대 크기에서의 빈 지도 — 0 한 자리를 16자리로 늘려야 하므로
  //          Step 7의 채우기를 가장 넓은 폭에서 다시 확인한다
  @Test
  void 최대_크기_지도가_모두_공백이면_공백_16칸짜리_줄이_16개_나온다() {
    String[] expected = new String[16];
    Arrays.fill(expected, " ".repeat(16));

    assertThat(sut.solution(16, new int[16], new int[16])).containsExactly(expected);
  }

  // Step 14: 제약의 상한 x = 2^n - 1 = 65,535 — 모든 칸이 벽이며,
  //          폭이 16을 넘지도(과한 채우기) 모자라지도 않아야 한다
  @Test
  void 최대_크기_지도에서_x가_상한인_65535면_모두_벽이_된다() {
    int[] arr1 = new int[16];
    int[] arr2 = new int[16];
    Arrays.fill(arr1, 65_535);
    Arrays.fill(arr2, 65_535);

    String[] expected = new String[16];
    Arrays.fill(expected, "#".repeat(16));

    assertThat(sut.solution(16, arr1, arr2)).containsExactly(expected);
  }
}
