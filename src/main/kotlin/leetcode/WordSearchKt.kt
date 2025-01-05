package leetcode

class WordSearchKt {
    fun exist(
        board: Array<CharArray>,
        word: String,
    ): Boolean {
        for (x in board.indices) {
            for (y in board[x].indices) {
                val visited = Array(board.size) { BooleanArray(board[x].size) }
                if (exist(x, y, 0, visited, board, word)) {
                    return true
                }
            }
        }
        return false
    }

    private fun exist(
        x: Int,
        y: Int,
        i: Int,
        visited: Array<BooleanArray>,
        board: Array<CharArray>,
        word: String,
    ): Boolean {
        if (i == word.length) {
            return true
        }
        if (x !in board.indices || y !in board[x].indices || visited[x][y] || board[x][y] != word[i]) {
            return false
        }
        visited[x][y] = true
        if (exist(x - 1, y, i + 1, visited, board, word)) {
            return true
        }
        if (exist(x, y - 1, i + 1, visited, board, word)) {
            return true
        }
        if (exist(x + 1, y, i + 1, visited, board, word)) {
            return true
        }
        if (exist(x, y + 1, i + 1, visited, board, word)) {
            return true
        }
        visited[x][y] = false
        return false
    }
}
