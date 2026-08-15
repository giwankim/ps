package programmers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class 실패율Test {
  실패율 sut = new 실패율();

  // Step 1: smallest valid input (N = 1, stages 길이 = 1) — 유일한 스테이지에 멈춰 있으니
  //         실패율은 1/1이다. 결과가 0부터 시작하는 인덱스가 아니라 1부터 시작하는
  //         스테이지 번호라는 점을 먼저 고정한다
  @Test
  void 스테이지가_하나뿐이면_그_스테이지_번호만_반환한다() {
    assertThat(sut.solution(1, new int[] {1})).containsExactly(1);
  }

  // Step 2: N + 1은 스테이지가 아니라 "마지막까지 클리어한 사용자"라는 표식이다.
  //         stages 값을 그대로 인덱스로 쓰면서 크기 N + 1짜리 배열을 잡는 구현은
  //         여기서 ArrayIndexOutOfBoundsException으로 터진다
  @Test
  void N_더하기_1에_있는_사용자는_모두_클리어한_것으로_센다() {
    assertThat(sut.solution(1, new int[] {2})).containsExactly(1);
  }

  // Step 3: 결과의 길이는 언제나 N이다 — 아무도 멈춰 있지 않아 실패율이 전부 0이어도
  //         모든 스테이지가 한 번씩 들어간다. 실패율 0인 스테이지를 걸러내는 구현이나
  //         stages에 등장한 번호만 모으는 구현은 여기서 빈 배열을 내놓는다
  @Test
  void 모두_클리어했어도_모든_스테이지가_번호순으로_들어간다() {
    assertThat(sut.solution(3, new int[] {4, 4, 4})).containsExactly(1, 2, 3);
  }

  // Step 4: 정렬 방향 — 실패율이 높은 스테이지가 앞에 온다.
  //         1번은 0/2, 2번은 2/2다. 오름차순으로 정렬하는 구현은 [1, 2]를 내놓는다
  @Test
  void 실패율이_높은_스테이지가_먼저_온다() {
    assertThat(sut.solution(2, new int[] {2, 2})).containsExactly(2, 1);
  }

  // Step 5: 도달한 유저가 없는 스테이지의 실패율은 계산 결과가 아니라 0으로 "정의"된다.
  //         0 / 0을 그대로 나누면 NaN이 되고 Double.compare는 NaN을 가장 큰 값으로 보므로,
  //         아무도 도달하지 못한 3번이 1등으로 올라가 [3, 2, 1]이 나온다
  @Test
  void 도달한_유저가_없는_스테이지의_실패율은_0이다() {
    assertThat(sut.solution(3, new int[] {2})).containsExactly(2, 1, 3);
  }

  // Step 6: 동점 처리 — 1번(2/4)과 2번(1/2)의 실패율이 똑같이 0.5다. 번호가 작은 쪽이
  //         먼저 와야 하므로, 동점에서 번호를 내림차순으로 깨는 구현이나 오름차순 정렬
  //         결과를 통째로 뒤집는 구현은 [2, 1]을 내놓는다
  @Test
  void 실패율이_같으면_번호가_작은_스테이지가_먼저_온다() {
    assertThat(sut.solution(2, new int[] {1, 1, 2, 3})).containsExactly(1, 2);
  }

  // Step 7: 실패율은 정수가 아니라 분수다 — 1번은 1/4 = 0.25, 2번은 2/3 ≒ 0.667이라
  //         2번이 앞선다. 정수 나눗셈(1 / 4 == 0, 2 / 3 == 0)으로 실패율을 구하는 구현은
  //         둘을 동점으로 보고, (int) (b.rate - a.rate) 꼴의 비교자도 차이 0.417을 0으로
  //         잘라 동점으로 본다. 둘 다 번호순으로 밀려 [1, 2]가 된다
  @Test
  void 실패율은_정수_나눗셈이_아니라_실수로_비교한다() {
    assertThat(sut.solution(2, new int[] {1, 2, 2, 3})).containsExactly(2, 1);
  }

  // Step 8: 분모는 전체 인원이 아니라 그 스테이지에 "도달한" 인원이다 —
  //         1번은 4/10 = 0.4, 2번은 3/6 = 0.5다. 분모를 stages.length로 고정하면
  //         0.4 대 0.3이 되고, 멈춰 있는 인원 수(4명 대 3명)로만 비교해도 1번이 앞서서
  //         두 경우 모두 [1, 2]가 된다
  @Test
  void 분모는_전체_인원이_아니라_그_스테이지에_도달한_인원이다() {
    assertThat(sut.solution(2, new int[] {1, 1, 1, 1, 2, 2, 2, 3, 3, 3})).containsExactly(2, 1);
  }

  // Step 9: 실패율 0인 스테이지의 두 가지 출처가 한 입력에 섞인다 — 2번은 도달자가 있지만
  //         아무도 멈추지 않아 0이고, 4·5번은 도달자가 없어 Step 5의 정의로 0이다.
  //         둘을 다른 부류로 취급해 한쪽을 뒤로 몰아내면 안 되고, 0끼리는 오직 번호순이다
  //         (3번 1/1, 1번 2/3, 그다음 0인 2·4·5번)
  @Test
  void 실패율_0인_스테이지끼리는_도달자_유무와_무관하게_번호순이다() {
    assertThat(sut.solution(5, new int[] {1, 1, 3})).containsExactly(3, 1, 2, 4, 5);
  }

  // Step 10: official example 1 — 1/8, 3/7, 2/4, 1/2, 0/1이 한 입력에 섞여 있고
  //          3번과 4번이 0.5로 동점이라 Step 6의 번호 동점 처리까지 함께 요구한다
  @Test
  void 공식_예제1_실패율이_제각각인_다섯_스테이지를_정렬한다() {
    assertThat(sut.solution(5, new int[] {2, 1, 2, 6, 2, 4, 3, 3})).containsExactly(3, 4, 2, 1, 5);
  }

  // Step 11: official example 2 — 모든 사용자가 마지막 스테이지에 멈춰 있어
  //          4번만 실패율 1이고 앞선 세 스테이지는 전부 0이다
  @Test
  void 공식_예제2_모두_마지막_스테이지에_멈춰있다() {
    assertThat(sut.solution(4, new int[] {4, 4, 4, 4, 4})).containsExactly(4, 1, 2, 3);
  }

  // Step 12: 제약의 상한(N = 500, stages 길이 = 200,000)에서 전원이 클리어한 경우 —
  //          실패율이 모두 0이라 번호순이 그대로 답이다. Step 2의 N + 1 = 501을
  //          가장 큰 N에서 다시 확인하고, 200,000개를 훑는 카운팅이 버티는지 함께 본다
  @Test
  void 최대_제약에서_전원이_클리어하면_번호순_그대로다() {
    int[] stages = new int[200_000];
    Arrays.fill(stages, 501);

    assertThat(sut.solution(500, stages))
        .containsExactly(IntStream.rangeClosed(1, 500).toArray());
  }

  // Step 13: 최대 제약에서의 순서와 성능 — 각 스테이지에 400명씩 고르게 멈춰 있으면
  //          k번의 실패율은 400 / (400 × (501 - k)) = 1 / (501 - k)이라 번호가 클수록 높다.
  //          동점이 하나도 없어 순서는 오직 실패율 비교로 갈리며, 스테이지마다 stages를
  //          다시 세는 O(N × 길이) 구현은 여기서 1억 번을 훑는다
  @Test
  void 최대_제약에서_실패율은_스테이지_번호가_클수록_높아진다() {
    int[] stages = new int[200_000];
    for (int i = 0; i < stages.length; i++) {
      stages[i] = i % 500 + 1;
    }
    int[] expected = IntStream.iterate(500, k -> k - 1).limit(500).toArray();

    assertThat(sut.solution(500, stages)).containsExactly(expected);
  }
}
