package leetcode

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class ProductExceptSelfKtTest {
    @ParameterizedTest
    @MethodSource
    fun productExceptSelf(
        nums: IntArray,
        expected: IntArray,
    ) {
        val actual = ProductExceptSelfKt().productExceptSelf(nums)
        assertThat(actual).isEqualTo(expected)
    }

    companion object {
        @JvmStatic
        fun productExceptSelf(): List<Arguments> =
            listOf(
                Arguments.of(intArrayOf(1, 2, 3, 4), intArrayOf(24, 12, 8, 6)),
                Arguments.of(intArrayOf(-1, 1, 0, -3, 3), intArrayOf(0, 0, 9, 0, 0)),
            )
    }
}
