package leetcode

class CombinationSumKt {
    fun combinationSum(
        candidates: IntArray,
        target: Int,
    ): List<List<Int>> {
        val result = mutableListOf<List<Int>>()
        backtrack(0, result, mutableListOf(), candidates, target)
        return result
    }

    private fun backtrack(
        start: Int,
        result: MutableList<List<Int>>,
        combination: MutableList<Int>,
        candidates: IntArray,
        target: Int,
    ) {
        if (target < 0) {
            return
        }
        if (target == 0) {
            result.add(combination.toList())
            return
        }
        for (j in start until candidates.size) {
            combination.add(candidates[j])
            backtrack(j, result, combination, candidates, target - candidates[j])
            combination.removeLast()
        }
    }
}
