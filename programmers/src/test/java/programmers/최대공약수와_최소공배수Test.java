package programmers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class 최대공약수와_최소공배수Test {
  최대공약수와_최소공배수 sut = new 최대공약수와_최소공배수();

  // Step 1: 제약의 최솟값(1 ≦ n, m) — 1과 1은 최대공약수도 최소공배수도 1이다.
  //         두 값이 같아 순서로는 아무것도 가릴 수 없는 대신, 반환값이 빈 배열이 아니라
  //         원소가 정확히 둘인 배열이라는 것을 먼저 고정한다
  @Test
  void 두_수가_모두_1이면_최대공약수도_최소공배수도_1이다() {
    assertThat(sut.solution(1, 1)).containsExactly(1, 1);
  }

  // Step 2: 배열의 순서 — 1은 모든 수의 약수이므로 gcd는 1, lcm은 상대 수 그 자체다.
  //         두 값이 처음으로 달라지므로, [최소공배수, 최대공약수] 순으로 담는 구현은
  //         여기서 [7, 1]을 내놓는다
  @Test
  void 한쪽이_1이면_최대공약수는_1이고_최소공배수는_다른_수다() {
    assertThat(sut.solution(1, 7)).containsExactly(1, 7);
  }

  // Step 3: official example 2 — 서로소인 두 수는 gcd가 1이고 lcm은 두 수의 곱이다
  @Test
  void 공식_예제2_서로소인_2와_5는_1과_10을_반환한다() {
    assertThat(sut.solution(2, 5)).containsExactly(1, 10);
  }

  // Step 4: official example 1 — 한쪽이 다른 쪽의 배수면 gcd는 작은 수, lcm은 큰 수다.
  //         Step 3만 보고 "lcm = n × m"으로 굳힌 구현은 여기서 36을 낸다
  @Test
  void 공식_예제1_배수_관계인_3과_12는_3과_12를_반환한다() {
    assertThat(sut.solution(3, 12)).containsExactly(3, 12);
  }

  // Step 5: 인자 순서는 결과를 바꾸지 않는다 — Step 4의 두 인자를 뒤집은 사례.
  //         n ≧ m을 전제하고 나머지 연산을 한 방향으로만 도는 구현이 여기서 걸린다
  @Test
  void 큰_수를_먼저_넘겨도_같은_결과를_반환한다() {
    assertThat(sut.solution(12, 3)).containsExactly(3, 12);
  }

  // Step 6: 두 수가 같은 경우 — gcd도 lcm도 그 수 자신이다. 유클리드 호제법의 첫 나머지가
  //         곧바로 0이 되는 경계이고, 서로 빼 나가는 방식(while (a != b))은 한 번도
  //         돌지 않은 채 끝나야 한다
  @Test
  void 두_수가_같으면_최대공약수와_최소공배수가_모두_그_수다() {
    assertThat(sut.solution(6, 6)).containsExactly(6, 6);
  }

  // Step 7: gcd가 1도 min도 아닌 첫 사례 — gcd(12, 18) = 6이고 lcm은 12 × 18 / 6 = 36이다.
  //         "gcd = 작은 수"로 두면 12, "lcm = 큰 수"로 두면 18, "lcm = 곱"으로 두면 216이
  //         나와 Step 3·4의 지름길이 여기서 한꺼번에 무너진다
  @Test
  void 배수_관계가_아니어도_공약수가_있으면_그_최댓값을_찾는다() {
    assertThat(sut.solution(12, 18)).containsExactly(6, 36);
  }

  // Step 8: Step 7에서는 두 수의 차(18 − 12 = 6)가 우연히 gcd와 같았다.
  //         10과 4는 차가 6이지만 gcd는 2이므로, 차를 gcd로 오해한 구현이 여기서 갈린다
  @Test
  void 두_수의_차가_공약수인_것은_아니다() {
    assertThat(sut.solution(10, 4)).containsExactly(2, 20);
  }

  // Step 9: 제약의 상한과 하한을 한 입력에 담는다 — lcm이 처음으로 100만에 이른다.
  //         나눗셈 대신 서로 빼 나가는 유클리드는 여기서 100만 번을 돈다
  @Test
  void 제약_상한과_1의_최소공배수는_상한_그_자체다() {
    assertThat(sut.solution(1_000_000, 1)).containsExactly(1, 1_000_000);
  }

  // Step 10: n × m이 int를 넘는 첫 사례 — 정답은 100만으로 작지만 중간 곱 10^12는
  //          int로 표현되지 않는다. n * m / g를 int로 계산하면 곱이 −727,379,968로
  //          래핑되어 lcm이 −727이 된다. long으로 곱하거나 n / g * m 순으로 나눠야 한다
  @Test
  void 두_수가_모두_제약_상한이면_중간_곱이_넘쳐도_100만을_반환한다() {
    assertThat(sut.solution(1_000_000, 1_000_000)).containsExactly(1_000_000, 1_000_000);
  }

  // Step 11: 서로 다른 두 큰 수의 배수 관계 — Step 4를 최대 규모에서 다시 확인한다.
  //          n × m = 5 × 10^11도 int로 1,783,793,664로 래핑되어 lcm이 3,567이 되므로,
  //          Step 10의 실패가 "두 인자가 같아서" 생긴 우연이 아님을 보인다
  @Test
  void 제약_상한과_그_절반은_상한을_최소공배수로_갖는다() {
    assertThat(sut.solution(1_000_000, 500_000)).containsExactly(500_000, 1_000_000);
  }

  // Step 12: 정답 자체가 20억에 가까운 사례 — 1,000,000 = 2^6 × 5^6이고
  //          999,500 = 2^2 × 5^3 × 1999이므로 gcd는 500, lcm은 1,999,000,000이다.
  //          답은 int에 들어가지만 n × m은 넘쳐서 −2,454,759가 나온다. 반면
  //          n / g * m = 2,000 × 999,500은 int 안에서 정확하므로 나누기를 곱하기보다
  //          먼저 하면 통과한다
  @Test
  void 최소공배수가_20억에_가까워도_넘치지_않고_그대로_반환한다() {
    assertThat(sut.solution(1_000_000, 999_500)).containsExactly(500, 1_999_000_000);
  }

  // Step 13: Step 5와 Step 12의 결합 — 큰 쪽이 앞에 오든 뒤에 오든 같은 답이어야 한다.
  //          Step 5는 두 수가 작아 순서를 뒤집어도 넘칠 일이 없었으므로, 순서와 오버플로가
  //          겹쳤을 때도 안전한지는 여기서 처음 확인된다
  @Test
  void 인자가_역순이어도_20억에_가까운_최소공배수를_반환한다() {
    assertThat(sut.solution(999_500, 1_000_000)).containsExactly(500, 1_999_000_000);
  }

  // Step 14: int에 담을 수 있는 가장 큰 최소공배수 — 연속한 두 수는 항상 서로소이므로
  //          lcm은 곱 그대로인 2,147,441,940이고, 이는 Integer.MAX_VALUE보다 41,707 작다.
  //          다음 연속쌍 (46,341, 46,342)의 곱은 이미 int를 넘으니 여기가 경계다.
  //          작은 쪽부터 하나씩 내려오며 공약수를 찾는 구현은 46,340번을 헛돌아야 1에 닿는다
  @Test
  void 연속한_두_수는_서로소이며_최소공배수가_int_상한_직전까지_커진다() {
    assertThat(sut.solution(46_340, 46_341)).containsExactly(1, 2_147_441_940);
  }
}
