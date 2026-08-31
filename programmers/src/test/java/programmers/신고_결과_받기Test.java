package programmers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

class 신고_결과_받기Test {
  신고_결과_받기 sut = new 신고_결과_받기();

  // 규모 테스트용 id 생성기. 제한사항상 id는 알파벳 소문자로만 이루어지므로 숫자를 쓸 수 없다.
  // a~j 열 글자를 십진 자릿수 삼아 "aaa"(0)부터 "jjj"(999)까지 1000개를 만든다
  private static String id(int i) {
    return new String(
        new char[] {(char) ('a' + i / 100), (char) ('a' + i / 10 % 10), (char) ('a' + i % 10)});
  }

  // Step 1: 가장 단순한 입력 — 한 명이 다른 한 명을 신고하고 한 번만 신고당해도 정지되는 k = 1.
  //         빈 배열을 돌려주는 구현이 여기서 걸린다. 메일을 받는 쪽은 신고당한 유저가 아니라
  //         신고한 유저이므로, 방향을 뒤집은 구현은 [0, 1]을 낸다
  @Test
  void 신고한_유저가_정지_메일을_받는다() {
    assertThat(sut.solution(new String[] {"muzi", "frodo"}, new String[] {"muzi frodo"}, 1))
        .containsExactly(1, 0);
  }

  // Step 2: 결과 배열의 길이는 id_list의 길이이고, 순서도 id_list를 따른다. 신고에 한 번도
  //         등장하지 않는 유저를 맨 앞에 두었으므로, 신고 기록에 등장한 순서로 자리를 잡는
  //         구현은 muzi의 1을 0번 자리에 놓아 [1, 0, 0]을 낸다
  @Test
  void 신고에_등장하지_않는_유저도_제자리에_0으로_남는다() {
    assertThat(
            sut.solution(new String[] {"ghost", "muzi", "frodo"}, new String[] {"muzi frodo"}, 1))
        .containsExactly(0, 1, 0);
  }

  // Step 3: 신고당한 횟수가 k에 못 미치면 정지되지 않는다. k를 무시하고 신고당한 유저를
  //         모두 정지시키는 구현이 여기서 [1, 0, 0]을 낸다
  @Test
  void k회_미만으로_신고당하면_정지되지_않는다() {
    assertThat(
            sut.solution(new String[] {"muzi", "apeach", "frodo"}, new String[] {"muzi frodo"}, 2))
        .containsExactly(0, 0, 0);
  }

  // Step 4: 경계 — 정지 기준은 "k번 이상"이다. 정확히 k번 신고당한 유저는 정지된다.
  //         부등호를 초과(> k)로 쓴 구현은 여기서 [0, 0, 0]을 낸다. Step 3과 짝을 이뤄
  //         경계를 양쪽에서 못박는다
  @Test
  void 정확히_k회_신고당하면_정지된다() {
    assertThat(sut.solution(
            new String[] {"muzi", "apeach", "frodo"},
            new String[] {"muzi frodo", "apeach frodo"},
            2))
        .containsExactly(1, 1, 0);
  }

  // Step 5: 같은 유저가 같은 유저를 두 번 신고해도 신고 횟수는 1회다. 신고 기록의 줄 수를
  //         그대로 세는 구현은 frodo를 2회 신고당한 것으로 보아 정지시키고 [2, 0, 0] 또는
  //         [1, 0, 0]을 낸다
  @Test
  void 같은_유저를_여러_번_신고해도_신고_횟수는_1회다() {
    assertThat(sut.solution(
            new String[] {"muzi", "apeach", "frodo"}, new String[] {"muzi frodo", "muzi frodo"}, 2))
        .containsExactly(0, 0, 0);
  }

  // Step 6: 중복 제거는 메일 횟수에도 똑같이 적용된다. Step 5를 통과하려고 정지 여부만
  //         중복을 걸러 판정하고, 메일은 원본 신고 기록을 훑으며 세는 구현은 muzi에게
  //         같은 정지 건으로 메일을 두 번 보내 [2, 0]을 낸다
  @Test
  void 중복_신고는_메일_횟수도_1회로_친다() {
    assertThat(sut.solution(
            new String[] {"muzi", "frodo"}, new String[] {"muzi frodo", "muzi frodo"}, 1))
        .containsExactly(1, 0);
  }

  // Step 7: 중복된 신고가 나란히 붙어 있지 않을 수도 있다. 앞뒤 줄만 비교해 연속된 중복을
  //         걷어내는 구현은 떨어져 있는 muzi의 두 번째 신고를 놓쳐 [2, 1, 0]을 낸다
  @Test
  void 중복_신고가_떨어져_있어도_한_건으로_묶는다() {
    assertThat(sut.solution(
            new String[] {"muzi", "apeach", "frodo"},
            new String[] {"muzi frodo", "apeach frodo", "muzi frodo"},
            2))
        .containsExactly(1, 1, 0);
  }

  // Step 8: 메일 횟수는 신고한 횟수가 아니라 그중 정지된 유저를 신고한 횟수다. muzi는 두 명을
  //         신고했지만 neo는 한 번만 신고당해 정지되지 않으므로 메일은 한 번뿐이다.
  //         신고 건수를 그대로 메일 횟수로 세는 구현은 [2, 1, 0, 0]을 낸다
  @Test
  void 정지되지_않은_유저를_신고한_건은_메일이_되지_않는다() {
    assertThat(sut.solution(
            new String[] {"muzi", "apeach", "frodo", "neo"},
            new String[] {"muzi frodo", "apeach frodo", "muzi neo"},
            2))
        .containsExactly(1, 1, 0, 0);
  }

  // Step 9: 반대로 정지된 유저를 여럿 신고했다면 메일도 그만큼 받는다. 유저당 메일 여부를
  //         0/1 플래그로만 다루는 구현은 여기서 [1, 0, 0]을 낸다
  @Test
  void 정지된_유저를_여럿_신고하면_메일도_여러_번_받는다() {
    assertThat(sut.solution(
            new String[] {"muzi", "frodo", "neo"}, new String[] {"muzi frodo", "muzi neo"}, 1))
        .containsExactly(2, 0, 0);
  }

  // Step 10: 정지된 유저도 자신이 신고한 건에 대한 결과 메일은 받는다. frodo는 muzi에게
  //          신고당해 정지되지만, 자신이 신고한 neo 역시 정지되므로 메일 1회를 받는다.
  //          정지된 유저의 메일 수를 0으로 지우는 구현은 [1, 0, 0]을 낸다
  @Test
  void 정지된_유저도_자신이_신고한_건의_메일은_받는다() {
    assertThat(sut.solution(
            new String[] {"muzi", "frodo", "neo"}, new String[] {"muzi frodo", "frodo neo"}, 1))
        .containsExactly(1, 1, 0);
  }

  // Step 11: 결과 순서는 id_list 순서다. id_list를 알파벳 역순으로 두고 유저마다 메일 수를
  //          다르게 만들었으므로, 내부에서 정렬된 자료구조(TreeMap 등)의 순회 순서를 그대로
  //          내보내는 구현은 amy, bob, zed 순인 [1, 0, 2]를 낸다
  @Test
  void 결과는_정렬_순서가_아니라_id_list_순서를_따른다() {
    assertThat(sut.solution(
            new String[] {"zed", "amy", "bob"}, new String[] {"zed amy", "zed bob", "amy bob"}, 1))
        .containsExactly(2, 1, 0);
  }

  // Step 12: 신고 기록은 공백 하나를 기준으로 정확히 두 토큰으로 나눠야 한다. neo는 neon의
  //          접두사이므로, 줄에 id가 들어 있는지(contains)나 줄이 id로 시작하는지(startsWith)로
  //          신고자를 찾는 구현은 "neon frodo"를 neo의 신고로도 세어 [1, 1, 1]을 낸다
  @Test
  void id가_다른_id의_접두사여도_공백으로_정확히_나눈다() {
    assertThat(sut.solution(
            new String[] {"neo", "neon", "frodo"}, new String[] {"neon frodo", "frodo neo"}, 1))
        .containsExactly(0, 1, 1);
  }

  // Step 13: 제한사항의 최대 k인 200에서의 경계 — 서로 다른 200명이 신고하면 정지된다.
  //          신고자 200명 전원이 메일 1회를 받고, 신고당한 마지막 유저만 0이다
  @Test
  void k가_최댓값_200일_때_정확히_200명이_신고하면_정지된다() {
    String[] ids = IntStream.rangeClosed(0, 200).mapToObj(i -> id(i)).toArray(String[]::new);
    String[] report =
        IntStream.range(0, 200).mapToObj(i -> id(i) + " " + id(200)).toArray(String[]::new);

    int[] expected = new int[201];
    Arrays.fill(expected, 0, 200, 1);

    assertThat(sut.solution(ids, report, 200)).containsExactly(expected);
  }

  // Step 14: Step 13의 경계 반대편 — 한 명 모자란 199명이면 정지되지 않아 아무도 메일을
  //          받지 않는다. 큰 k에서 부등호가 하루도 아니고 한 명도 어긋나지 않아야 한다
  @Test
  void k가_최댓값_200일_때_199명이_신고하면_정지되지_않는다() {
    String[] ids = IntStream.rangeClosed(0, 200).mapToObj(i -> id(i)).toArray(String[]::new);
    String[] report =
        IntStream.range(0, 199).mapToObj(i -> id(i) + " " + id(200)).toArray(String[]::new);

    assertThat(sut.solution(ids, report, 200)).containsExactly(new int[201]);
  }

  // Step 15: 제한사항의 최대 규모 — 유저 1000명, 신고 기록 200,000줄. 서로 다른 신고는
  //          200건뿐이고 각 건이 1000번씩 번갈아 되풀이되므로, 중복을 O(n^2)로 걸러내거나
  //          신고 기록마다 id_list를 훑어 자리를 찾는 구현은 제한시간 10초 안에 끝나지
  //          못한다. 중복을 못 거른 구현은 신고자마다 메일을 1000회씩 세어 걸린다
  @Test
  void 유저_1000명과_신고_200000건도_모두_처리한다() {
    String[] ids = IntStream.range(0, 1000).mapToObj(i -> id(i)).toArray(String[]::new);
    String[] report = IntStream.range(0, 200_000)
        .mapToObj(i -> id(i % 200) + " " + id(999))
        .toArray(String[]::new);

    int[] expected = new int[1000];
    Arrays.fill(expected, 0, 200, 1);

    assertThat(sut.solution(ids, report, 200)).containsExactly(expected);
  }

  // Step 16: official example 1 — 정지된 유저와 정지되지 않은 유저를 함께 신고한 apeach,
  //          자신도 정지되면서 메일을 받는 frodo, 아무도 신고하지 않은 neo가 한 입력에
  //          모여 있는 대표 사례다
  @Test
  void 공식_예제1은_2와_1과_1과_0이다() {
    assertThat(sut.solution(
            new String[] {"muzi", "frodo", "apeach", "neo"},
            new String[] {"muzi frodo", "apeach frodo", "frodo neo", "muzi neo", "apeach muzi"},
            2))
        .containsExactly(2, 1, 1, 0);
  }

  // Step 17: official example 2 — ryan이 con을 네 번 신고했지만 신고 횟수는 1회이므로
  //          k = 3에 못 미쳐 아무도 정지되지 않는다. 중복 처리와 k 판정이 함께 걸리는 사례다
  @Test
  void 공식_예제2는_0과_0이다() {
    assertThat(sut.solution(
            new String[] {"con", "ryan"},
            new String[] {"ryan con", "ryan con", "ryan con", "ryan con"},
            3))
        .containsExactly(0, 0);
  }
}
