package boj.boj2875;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

// BOJ 2875 "대회 or 인턴": N females, M males, K internship students. A team is
// 2 females + 1 male; the K interns cannot be on any team. Maximum teams is
//   min( floor(N/2), M, floor((N + M - K) / 3) ).
// Input is a single line "N M K"; output is the team count.
class MainTest {

  // Step 1: simplest happy path — exactly enough for one team and nothing to spare.
  // N=2,M=1,K=0 -> min(1, 1, 3/3=1) = 1.
  @Test
  @StdIo({"2 1 0"})
  void smallestInputThatFormsExactlyOneTeam(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // Step 2: the answer can be zero. Fewer than 2 females means floor(N/2)=0
  // regardless of how many males or how small K is. N=1,M=100,K=0 -> 0.
  @Test
  @StdIo({"1 100 0"})
  void tooFewFemalesForcesZeroTeams(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // Step 3: zero teams from the other shortage — no males at all caps the answer at
  // M=0 even with 100 females available. N=100,M=0,K=0 -> 0.
  @Test
  @StdIo({"100 0 0"})
  void noMalesForcesZeroTeams(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // Step 4: the female term floor(N/2) is the unique minimum. N=4,M=100,K=0 ->
  // min(2, 100, 104/3=34) = 2.
  @Test
  @StdIo({"4 100 0"})
  void femaleSupplyIsTheBindingConstraint(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // Step 5: confirm floor(N/2) truncates rather than rounds. Odd N=5 must give 2,
  // not 3. N=5,M=100,K=0 -> min(5/2=2, 100, 105/3=35) = 2.
  @Test
  @StdIo({"5 100 0"})
  void oddFemaleCountFloorsTheHalving(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // Step 6: the male term M is the unique minimum. N=100,M=3,K=0 ->
  // min(50, 3, 197/3=65) = 3.
  @Test
  @StdIo({"100 3 0"})
  void maleSupplyIsTheBindingConstraint(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // Step 7: the internship term floor((N+M-K)/3) is the unique minimum — K starves
  // the team pool below both gender limits. N=10,M=10,K=15 ->
  // min(5, 10, (20-15)/3=1) = 1.
  @Test
  @StdIo({"10 10 15"})
  void internshipCountIsTheBindingConstraint(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // Step 8: confirm floor((N+M-K)/3) truncates. 7/3 must give 2, not 3.
  // N=10,M=10,K=13 -> min(5, 10, (20-13)/3=2) = 2.
  @Test
  @StdIo({"10 10 13"})
  void internshipTermFloorsTheDivisionByThree(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // Step 9: published sample #1 — internship term binds.
  // N=8,M=5,K=3 -> min(4, 5, 10/3=3) = 3.
  @Test
  @StdIo({"8 5 3"})
  void publishedSampleInternshipBinds(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // Step 10: published sample #2 — female and internship terms tie at 4.
  // N=8,M=5,K=1 -> min(4, 5, 12/3=4) = 4.
  @Test
  @StdIo({"8 5 1"})
  void publishedSampleFemaleAndInternshipTie(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // Step 11: published sample #3 — male term binds despite ample females/budget.
  // N=18,M=4,K=1 -> min(9, 4, 21/3=7) = 4.
  @Test
  @StdIo({"18 4 1"})
  void publishedSampleMaleBinds(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // Step 12: published sample #4 — male term binds with K nonzero.
  // N=18,M=3,K=5 -> min(9, 3, 16/3=5) = 3.
  @Test
  @StdIo({"18 3 5"})
  void publishedSampleMaleBindsWithInterns(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // Step 13: all three terms are exactly equal — the optimum sits on every ceiling
  // at once. N=4,M=2,K=0 -> min(2, 2, 6/3=2) = 2.
  @Test
  @StdIo({"4 2 0"})
  void allThreeConstraintsTieAtTheOptimum(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // Step 14: K at its maximum (K = N + M) — everyone interns, so no team can form
  // even though both genders are plentiful. N=50,M=50,K=100 ->
  // min(25, 50, 0/3=0) = 0.
  @Test
  @StdIo({"50 50 100"})
  void maximumInternshipCountLeavesNoTeams(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // Step 15: lower boundary of the input domain — every value zero. Degenerate but
  // well-defined: min(0, 0, 0) = 0.
  @Test
  @StdIo({"0 0 0"})
  void allZeroInputYieldsZeroTeams(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // Step 16: upper boundary of the input domain — N=M=100, K=0. Females cap the
  // answer: min(100/2=50, 100, 200/3=66) = 50.
  @Test
  @StdIo({"100 100 0"})
  void maximumInputIsBoundedByFemaleSupply(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("50");
  }
}
