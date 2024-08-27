package leetcode

class MostCommonWordKt {
    fun mostCommonWord(
        paragraph: String,
        banned: Array<String>,
    ): String {
        val counts: MutableMap<String, Int> = mutableMapOf()
        val words =
            paragraph
                .replace("\\W+".toRegex(), " ")
                .trim()
                .lowercase()
                .split(" ")
        for (word in words) {
            if (!banned.contains(word)) {
                counts[word] = counts.getOrDefault(word, defaultValue = 0) + 1
            }
        }
        return counts.maxByOrNull { it.value }?.key ?: ""
    }
}
