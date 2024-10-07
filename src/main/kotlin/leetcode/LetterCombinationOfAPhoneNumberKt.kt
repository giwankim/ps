package leetcode

class LetterCombinationOfAPhoneNumberKt {
    fun letterCombinations(digits: String): List<String> {
        if (digits.isEmpty()) {
            return emptyList()
        }

        val map: MutableMap<Char, List<Char>> =
            mutableMapOf(
                '2' to listOf('a', 'b', 'c'),
                '3' to listOf('d', 'e', 'f'),
                '4' to listOf('g', 'h', 'i'),
                '5' to listOf('j', 'k', 'l'),
                '6' to listOf('m', 'n', 'o'),
                '7' to listOf('p', 'q', 'r', 's'),
                '8' to listOf('t', 'u', 'v'),
                '9' to listOf('w', 'x', 'y', 'z'),
            )

        val result: MutableList<String> = mutableListOf()

        fun letterCombinations(
            i: Int,
            sb: StringBuilder,
        ) {
            if (i == digits.length) {
                result.add(sb.toString())
                return
            }
            for (c in map[digits[i]]!!) {
                sb.append(c)
                letterCombinations(i + 1, sb)
                sb.deleteAt(i)
            }
        }

        letterCombinations(0, StringBuilder())
        return result
    }
}
