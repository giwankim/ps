package leetcode.p1601_1700;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class FindServersThatHandledMostNumberOfRequestsTest {
  FindServersThatHandledMostNumberOfRequests sut = new FindServersThatHandledMostNumberOfRequests();

  // ===========================================================================================
  // The floor (Steps 1-3).
  // ===========================================================================================

  // Step 1: the smallest legal input (1 <= k and 1 <= n). One server, one request, one winner
  @Test
  void k1SingleRequestIsHandledByServerZero() {
    assertThat(sut.busiestServers(1, new int[] {1}, new int[] {1})).containsExactly(0);
  }

  // Step 2: more servers than requests. Request 0 starts at server 0 and the other four servers
  //         never work, so a count of zero must not make them part of the answer
  @Test
  void k5SingleRequestLeavesIdleServersOutOfTheAnswer() {
    assertThat(sut.busiestServers(5, new int[] {1}, new int[] {1})).containsExactly(0);
  }

  // Step 3: with one server, every overlap empties the pool. A solver that falls back to first()
  //         on an empty ordered set throws NoSuchElementException here instead of dropping
  @Test
  void k1OverlappingRequestsAreDroppedWithoutCrashing() {
    assertThat(sut.busiestServers(1, new int[] {1, 2, 3}, new int[] {5, 1, 1})).containsExactly(0);
  }

  // ===========================================================================================
  // Which server a request starts at (Steps 4-6).
  // ===========================================================================================

  // Step 4: server 0 is free again when request 1 arrives, but request 1 still starts at server 1.
  //         A solver that hands out the lowest free server answers [0]. The result order is
  //         unspecified, so multi-server answers are compared in any order
  @Test
  void k2SecondRequestStartsAtIndexOneNotTheLowestFreeServer() {
    assertThat(sut.busiestServers(2, new int[] {1, 2}, new int[] {1, 1}))
        .containsExactlyInAnyOrder(0, 1);
  }

  // Step 5: the start index is i % k, so the ring is walked more than once. Requests 3 and 4 land
  //         on servers 0 and 1, which reach two requests each while server 2 stays at one
  @Test
  void k3SecondLapOfTheRingWrapsTheStartingIndex() {
    assertThat(sut.busiestServers(3, new int[] {1, 100, 200, 300, 400}, new int[] {1, 1, 1, 1, 1}))
        .containsExactlyInAnyOrder(0, 1);
  }

  // Step 6: a tie is reported among the servers that share the top count, not as "everyone". Three
  //         servers tie at one request and two never work. Reporting all of 0..k-1 on a tie
  //         answers five servers
  @Test
  void k5ServersThatNeverWorkedAreNotPartOfATie() {
    assertThat(sut.busiestServers(5, new int[] {1, 2, 3}, new int[] {10, 10, 10}))
        .containsExactlyInAnyOrder(0, 1, 2);
  }

  // ===========================================================================================
  // Dropping and the release boundary (Steps 7-8).
  // ===========================================================================================

  // Step 7: both servers are busy until time 4 when request 2 arrives at time 3, so it is dropped
  //         and the servers tie at one. Queueing it for the first finisher, or crediting the drop
  //         to server i % k, answers [0]
  @Test
  void k2RequestArrivingWhileAllBusyIsDropped() {
    assertThat(sut.busiestServers(2, new int[] {1, 2, 3}, new int[] {3, 2, 1}))
        .containsExactlyInAnyOrder(0, 1);
  }

  // Step 8: a load of L at time t occupies the server over the half-open interval from t to t + L.
  //         Server 0 finishes at exactly time 3, the moment request 2 arrives, so it takes that
  //         request. Treating the finish instant as still busy drops it and answers [0, 1]
  @Test
  void k2ServerFinishingExactlyAtArrivalIsAvailable() {
    assertThat(sut.busiestServers(2, new int[] {1, 2, 3}, new int[] {2, 1, 1})).containsExactly(0);
  }

  // ===========================================================================================
  // Probing past a busy start (Steps 9-12).
  // ===========================================================================================

  // Step 9: request 3 starts at busy server 0 and moves forward to server 1, giving 1, 2, 1. A
  //         backward probe takes server 2 instead and answers [2]
  @Test
  void k3BusyStartMovesForwardToTheNextServer() {
    assertThat(sut.busiestServers(3, new int[] {1, 2, 3, 4}, new int[] {5, 1, 1, 1}))
        .containsExactly(1);
  }

  // Step 10: request 4 starts at busy server 1 while servers 0 and 2 are both free. The next server
  //          clockwise is 2, giving 2, 1, 2. Falling back to the lowest free server answers [0]
  @Test
  void k3BusyStartTakesTheNextServerNotTheLowestFree() {
    assertThat(sut.busiestServers(3, new int[] {1, 2, 3, 4, 5}, new int[] {1, 5, 1, 1, 1}))
        .containsExactlyInAnyOrder(0, 2);
  }

  // Step 11: the first wrap. Request 3 starts at busy server 1, the last one, and must continue at
  //          server 0 for a 2, 2 tie. A probe that stops at k - 1 drops it and answers [1]
  @Test
  void k2ProbeWrapsPastTheLastServerToServerZero() {
    assertThat(sut.busiestServers(2, new int[] {1, 2, 3, 4}, new int[] {3, 1, 2, 1}))
        .containsExactlyInAnyOrder(0, 1);
  }

  // Step 12: the wrap continues through more than one busy server. Request 7 starts at busy server
  //          3, passes busy server 0, and lands on server 1 while server 2 is also free, so
  //          counts = [2, 3, 2, 1]. Stopping at k - 1 answers [0, 1, 2], and probing backward
  //          answers [2]
  @Test
  void k4ProbeWrapsAcrossSeveralBusyServers() {
    assertThat(sut.busiestServers(
            4, new int[] {1, 2, 3, 4, 5, 6, 7, 8}, new int[] {1, 1, 1, 100, 100, 1, 1, 1}))
        .containsExactly(1);
  }

  // ===========================================================================================
  // Interactions (Steps 13-18).
  // ===========================================================================================

  // Step 13: i counts every request, handled or not. Request 2 is dropped, yet request 3 still
  //          starts at 3 % 2 = server 1. Indexing by the number handled so far starts it at
  //          server 0 and answers [0]
  @Test
  void k2DroppedRequestStillConsumesItsIndex() {
    assertThat(sut.busiestServers(2, new int[] {1, 2, 3, 4}, new int[] {3, 2, 1, 1}))
        .containsExactly(1);
  }

  // Step 14: the start is i % k, not the server after the last assignment. Request 2 is redirected
  //          to server 1, and request 3 still starts at server 1, for counts = [1, 3]. Continuing
  //          round-robin from the last server used sends request 3 to server 0 and answers [0, 1]
  @Test
  void k2StartIsTheRequestIndexNotTheLastServerUsed() {
    assertThat(sut.busiestServers(2, new int[] {1, 2, 3, 4}, new int[] {3, 1, 1, 1}))
        .containsExactly(1);
  }

  // Step 15: a redirected request counts for the server that ran it. Request 2 starts at busy
  //          server 0 and runs on server 1, for counts = [1, 2]. Crediting i % k answers [0]
  @Test
  void k2RedirectedRequestIsCreditedToTheServerThatRanIt() {
    assertThat(sut.busiestServers(2, new int[] {1, 2, 3}, new int[] {3, 1, 1})).containsExactly(1);
  }

  // Step 16: several servers can finish between two arrivals. By time 4 server 1 finished at 3 and
  //          server 0 finished at 4, so request 2 takes its own start, server 0. Releasing at most
  //          one finished server per arrival frees only server 1 and answers [1]
  @Test
  void k2EveryServerFinishedSinceTheLastArrivalIsFreed() {
    assertThat(sut.busiestServers(2, new int[] {1, 2, 4}, new int[] {3, 1, 1})).containsExactly(0);
  }

  // Step 17: one long job pins server 0, so every later request ends up on server 1 whichever
  //          server it starts at, for counts = [1, 5]. Crediting i % k answers [0, 1]
  @Test
  void k2LongJobFunnelsEveryLaterRequestToTheOtherServer() {
    assertThat(sut.busiestServers(2, new int[] {1, 2, 3, 4, 5, 6}, new int[] {100, 1, 1, 1, 1, 1}))
        .containsExactly(1);
  }

  // Step 18: all three servers finish at exactly time 6, when request 3 arrives, and it takes
  //          server 0. Request 4 then takes server 1, for counts = [2, 2, 1]. Treating the finish
  //          instant as busy drops request 3 and answers [1]
  @Test
  void k3ServersFinishingTogetherAreAllAvailableAtThatInstant() {
    assertThat(sut.busiestServers(3, new int[] {1, 2, 3, 6, 7}, new int[] {5, 4, 3, 5, 5}))
        .containsExactlyInAnyOrder(0, 1);
  }

  // ===========================================================================================
  // Official examples (Steps 19-21).
  // ===========================================================================================

  // Step 19: LeetCode Example 1 has both special paths. Request 3 is redirected to server 1 and
  //          request 4 is dropped. Queueing the drop answers [1, 2], and crediting i % k
  //          answers [0]
  @Test
  void leetCodeExample1() {
    assertThat(sut.busiestServers(3, new int[] {1, 2, 3, 4, 5}, new int[] {5, 2, 3, 3, 3}))
        .containsExactly(1);
  }

  // Step 20: LeetCode Example 2. A server is reusable once it finishes, so request 3 returns to
  //          server 0 and its count passes one
  @Test
  void leetCodeExample2() {
    assertThat(sut.busiestServers(3, new int[] {1, 2, 3, 4}, new int[] {1, 2, 1, 2}))
        .containsExactly(0);
  }

  // Step 21: LeetCode Example 3. Every server ties at one, so all of them are returned. Keeping
  //          only the first server with the top count answers [0]
  @Test
  void leetCodeExample3() {
    assertThat(sut.busiestServers(3, new int[] {1, 2, 3}, new int[] {10, 12, 11}))
        .containsExactlyInAnyOrder(0, 1, 2);
  }

  // ===========================================================================================
  // Constraint bounds (Steps 22-26). Up to 10^5 servers and requests: scanning every server at
  // each arrival needs 10^10 operations, and a linear probe around the ring needs 2.5 * 10^9.
  // A free-server ordered set plus a min-heap of finish times is O(k + n log k) and takes
  // milliseconds. The modulo linear probe measured about 1.7 s on the adversary, so a 1 second
  // timeout is what separates it. The usual 5 seconds would let it through.
  // ===========================================================================================

  // Step 22: the value extremes. Server 0 finishes at exactly 10^9, when the last request arrives,
  //          and that request ends at 2 * 10^9, which still fits in an int. Treating the finish
  //          instant as busy answers [0, 1]
  @Test
  void k2LargestTimesStillReleaseExactlyOnTime() {
    assertThat(sut.busiestServers(
            2, new int[] {1, 2, MAX_VALUE}, new int[] {MAX_VALUE - 1, MAX_VALUE, MAX_VALUE}))
        .containsExactly(0);
  }

  // Step 23: the most requests on the fewest servers. The first request holds the only server for
  //          the whole run, so the other 99999 are dropped
  @Test
  @Timeout(value = 1, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void k1MaxRequestsDropsAllButTheFirst() {
    assertThat(sut.busiestServers(1, ramp(MAX_N), filled(MAX_N, MAX_VALUE))).containsExactly(0);
  }

  // Step 24: every request lands on its own server and nothing finishes, so all 10^5 servers tie.
  //          Re-scanning every server at each arrival to find the finished ones costs 10^10
  @Test
  @Timeout(value = 1, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void k100000EveryServerTiesAtOneRequest() {
    assertThat(sorted(sut.busiestServers(MAX_N, ramp(MAX_N), filled(MAX_N, MAX_VALUE))))
        .isEqualTo(idsBelow(MAX_N));
  }

  // Step 25: the first half fills every server for the whole run and the second half is dropped. A
  //          probe that walks the full ring before giving up does 2.5 * 10^9 steps on the drops
  @Test
  @Timeout(value = 1, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void k50000DropsWithoutWalkingTheRing() {
    assertThat(sorted(sut.busiestServers(MAX_N / 2, ramp(MAX_N), filled(MAX_N, MAX_VALUE))))
        .isEqualTo(idsBelow(MAX_N / 2));
  }

  // Step 26: the worst case for probing. Every first-round job has load k + 1, so request k + s
  //          finds exactly one free server, s - 1, right behind its start, and walks k - 1 busy
  //          servers to reach it. Request k finds nothing free and is dropped, so server k - 1 is
  //          the only one that stays at one request while all the others reach two
  @Test
  @Timeout(value = 1, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void k50000FindsTheOnlyFreeServerBehindEveryStart() {
    int k = MAX_N / 2;

    assertThat(sorted(sut.busiestServers(k, ramp(MAX_N), probeAdversaryLoad(k))))
        .isEqualTo(idsBelow(k - 1));
  }

  // ===========================================================================================
  // Hygiene (Step 27).
  // ===========================================================================================

  // Step 27: one instance answers inputs of different sizes, largest in the middle. The large call
  //          ends with every server busy, so a free set, heap, or counter kept on the instance
  //          makes the small calls after it wrong or out of bounds
  @Test
  @Timeout(value = 1, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void oneInstanceAnswersManyInputs() {
    int k = MAX_N / 2;

    assertThat(sut.busiestServers(2, new int[] {1, 2}, new int[] {1, 1}))
        .containsExactlyInAnyOrder(0, 1);
    assertThat(sorted(sut.busiestServers(k, ramp(MAX_N), probeAdversaryLoad(k))))
        .isEqualTo(idsBelow(k - 1));
    assertThat(sut.busiestServers(1, new int[] {1}, new int[] {1})).containsExactly(0);
    assertThat(sut.busiestServers(3, new int[] {1, 2, 3}, new int[] {10, 12, 11}))
        .containsExactlyInAnyOrder(0, 1, 2);
  }

  private static final int MAX_N = 100_000;
  private static final int MAX_VALUE = 1_000_000_000;

  private static int[] ramp(int n) {
    int[] arr = new int[n];
    Arrays.setAll(arr, i -> i + 1);
    return arr;
  }

  private static int[] filled(int n, int value) {
    int[] arr = new int[n];
    Arrays.fill(arr, value);
    return arr;
  }

  private static int[] probeAdversaryLoad(int k) {
    int[] load = new int[2 * k];
    Arrays.setAll(load, i -> i < k ? k + 1 : MAX_VALUE);
    return load;
  }

  private static List<Integer> sorted(List<Integer> ids) {
    return ids.stream().sorted().toList();
  }

  private static List<Integer> idsBelow(int n) {
    return IntStream.range(0, n).boxed().toList();
  }
}
