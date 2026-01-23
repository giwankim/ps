package leetcode

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class SlidingWindowMaximumKtTest {
    @ParameterizedTest
    @MethodSource
    fun maxSlidingWindow(
        nums: IntArray,
        k: Int,
        expected: IntArray,
    ) {
        val actual: IntArray = SlidingWindowMaximumKt().maxSlidingWindow(nums, k)
        assertThat(actual).isEqualTo(expected)
    }

    companion object {
        @JvmStatic
        fun maxSlidingWindow(): List<Arguments> = listOf(
            Arguments.of(intArrayOf(1, 3, -1, -3, 5, 3, 6, 7), 3, intArrayOf(3, 3, 5, 5, 6, 7)),
            Arguments.of(intArrayOf(1), 1, intArrayOf(1)),
        )
    }
}
