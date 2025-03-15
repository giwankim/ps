package leetcode

class NumberOfProvincesKt {
    fun findCircleNum(isConnected: Array<IntArray>): Int {
        var result = 0
        val n = isConnected.size
        val visited = BooleanArray(n)
        for (i in 0 until n) {
            if (!visited[i]) {
                dfs(i, visited, isConnected)
                result += 1
            }
        }
        return result
    }

    private fun dfs(
        i: Int,
        visited: BooleanArray,
        isConnected: Array<IntArray>,
    ) {
        visited[i] = true
        for (j in 0 until isConnected.size) {
            if (isConnected[i][j] == 0 || visited[j]) {
                continue
            }
            dfs(j, visited, isConnected)
        }
    }
}
