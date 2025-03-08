package leetcode

class SetMatrixZeroesKt {
    fun setZeroes(matrix: Array<IntArray>) {
        var firstRow = matrix[0].any { it == 0 }
        var firstCol = matrix.any { it[0] == 0 }

        for (i in 1 until matrix.size) {
            for (j in 1 until matrix[i].size) {
                if (matrix[i][j] == 0) {
                    matrix[0][j] = 0
                    matrix[i][0] = 0
                }
            }
        }

        for (i in 1 until matrix.size) {
            for (j in 1 until matrix[i].size) {
                if (matrix[i][0] == 0 || matrix[0][j] == 0) {
                    matrix[i][j] = 0
                }
            }
        }

        if (firstRow) {
            for (j in matrix[0].indices) {
                matrix[0][j] = 0
            }
        }

        if (firstCol) {
            for (i in matrix.indices) {
                matrix[i][0] = 0
            }
        }
    }
}
