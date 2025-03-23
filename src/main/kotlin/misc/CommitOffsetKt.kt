package misc

class CommitOffsetKt {
    fun commitOffsets(offsets: IntArray): IntArray {
        val result = IntArray(offsets.size)
        val seen: MutableSet<Int> = mutableSetOf()
        var currentOffset = 0
        for (i in offsets.indices) {
            seen.add(offsets[i])
            if (currentOffset in seen) {
                while (seen.contains(currentOffset)) {
                    currentOffset += 1
                }
                result[i] = currentOffset - 1
            } else {
                result[i] = -1
            }
        }
        return result
    }
}
