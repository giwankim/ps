package programmers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class 자연수_뒤집어_배열로_만들기Test {
  자연수_뒤집어_배열로_만들기 sut = new 자연수_뒤집어_배열로_만들기();

  // Step 1: 제약 하한 — n은 자연수이므로 0은 들어오지 않고, 가장 작은 입력은 한 자리 수다.
  //         뒤집을 것이 없어도 원소가 하나인 배열이어야 하며, 빈 배열이면 안 된다
  @Test
  void 한_자리_수는_그_숫자만_담은_배열이_된다() {
    assertThat(sut.solution(1)).containsExactly(1);
  }

  // Step 2: 처음으로 "뒤집기"를 요구하는 입력 — 자리 숫자를 읽은 순서 그대로 담는 구현은
  //         여기서 [1, 2]를 낸다. 이 단계가 배열의 방향을 고정한다
  @Test
  void 두_자리_수는_자리_순서가_뒤집힌다() {
    assertThat(sut.solution(12)).containsExactly(2, 1);
  }

  // Step 3: official example — 12345의 답은 [5,4,3,2,1]이다.
  //         Step 2의 방향을 자릿수가 늘어난 상태에서 다시 확인한다
  @Test
  void 공식_예제_12345는_5부터_1까지_내려간다() {
    assertThat(sut.solution(12345)).containsExactly(5, 4, 3, 2, 1);
  }

  // Step 4: 회문수는 뒤집어도 자기 자신이라 방향 오류를 잡지 못한다.
  //         이 사실을 명시해 두어 이후 케이스를 회문수로만 채우는 실수를 막는다
  @Test
  void 회문수는_뒤집어도_같은_배열이_나온다() {
    assertThat(sut.solution(12321)).containsExactly(1, 2, 3, 2, 1);
  }

  // Step 5: 같은 숫자가 여러 번 나오면 나온 횟수만큼 남아야 한다.
  //         스트림에 distinct()를 끼우거나 Set에 모으는 구현은 여기서 2를 하나로 줄인다
  @Test
  void 같은_숫자가_반복되면_반복된_횟수만큼_원소가_된다() {
    assertThat(sut.solution(1223)).containsExactly(3, 2, 2, 1);
  }

  // Step 6: n의 끝자리 0은 뒤집으면 배열의 첫 원소 0이 된다. 뒤집은 문자열을 다시
  //         숫자로 되돌리는 구현("01" -> 1)은 이 0을 잃어버려 [1]을 반환한다
  @Test
  void 끝자리_0은_뒤집으면_첫_원소_0으로_남는다() {
    assertThat(sut.solution(10)).containsExactly(0, 1);
  }

  // Step 7: 0이 연달아 끝나는 수 — Step 6의 0이 하나가 아니라 여러 개인 경우.
  //         선행 0을 한 번만 걷어내는 구현은 여기서 [0, 0, 1]로 하나를 흘린다
  @Test
  void 끝자리_0이_여러_개면_그_개수만큼_0으로_시작한다() {
    assertThat(sut.solution(1000)).containsExactly(0, 0, 0, 1);
  }

  // Step 8: 가운데 0도 자리 하나를 차지한다 — 0을 "값이 없음"으로 보고 건너뛰는 구현은
  //         [2, 1]로 길이가 줄어든다. 결과 길이는 언제나 n의 자릿수와 같다
  @Test
  void 가운데_0도_자리를_그대로_유지한다() {
    assertThat(sut.solution(1020)).containsExactly(0, 2, 0, 1);
  }

  // Step 9: int에 담기는 가장 큰 값(Integer.MAX_VALUE) — 아직 int로도 버틸 수 있는
  //         마지막 지점이라, 다음 단계와 짝을 이뤄 경계를 정확히 집는다
  @Test
  void int_최댓값_2147483647을_뒤집는다() {
    assertThat(sut.solution(2_147_483_647L)).containsExactly(7, 4, 6, 3, 8, 4, 7, 4, 1, 2);
  }

  // Step 10: int 범위를 처음으로 넘는 값 — 제약 상한이 10,000,000,000이라 int 파라미터나
  //          중간의 (int) 캐스팅은 반드시 필요하다. 래핑되면 음수가 되어 루프가 돌지 않고
  //          빈 배열이 나온다
  @Test
  void int_범위를_넘는_2147483648도_열_자리로_뒤집힌다() {
    assertThat(sut.solution(2_147_483_648L)).containsExactly(8, 4, 6, 3, 8, 4, 7, 4, 1, 2);
  }

  // Step 11: 열 자리 최대 수 — 0이 하나도 없는 최대 규모 입력이라
  //          Step 12가 0으로만 채워진 답에 묻어가는 것을 막는다
  @Test
  void 아홉이_열_개인_수는_아홉_열_개를_그대로_반환한다() {
    assertThat(sut.solution(9_999_999_999L)).containsExactly(9, 9, 9, 9, 9, 9, 9, 9, 9, 9);
  }

  // Step 12: 제약 상한 10,000,000,000 — Step 7(연속된 0)과 Step 10(int 초과)이 겹치는
  //          유일한 입력이며, 열한 자리로 결과가 가장 길어진다
  @Test
  void 제약_상한_100억은_0_열_개_뒤에_1이_온다() {
    assertThat(sut.solution(10_000_000_000L)).containsExactly(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1);
  }
}
