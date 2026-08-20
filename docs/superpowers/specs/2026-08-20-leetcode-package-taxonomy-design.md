# LeetCode module package taxonomy

**Date:** 2026-08-20
**Status:** approved 2026-08-20
**Scope:** `leetcode/` module only

## Problem

`leetcode/src/main/java/leetcode/` holds 281 solution classes in a single flat
package, alongside `leetcode.support` (3 shared node types). The mirrored test
tree holds 283 more. Browsing the module in the IDE means scrolling one
undifferentiated list of 281 entries.

Two properties of the module make this cheap to fix:

- **No coupling between solutions.** No solution class imports another
  (`grep -rE '^import leetcode\.[A-Z]'` returns nothing). Every solution is a
  leaf; only `package` declarations change. The sole shared dependency is
  `leetcode.support`, imported by 46 solution classes.
- **Unique descriptive class names.** Unlike `boj/`, nothing forces a rename.

## Decision

Group solutions into **fixed-width-100 numeric range packages**, keyed on the
LeetCode problem number:

```
leetcode/
├─ support/          ListNode, TreeNode, Node   (unchanged)
├─ p0001_0100/       68
├─ p0101_0200/       46
├─ p0201_0300/       35
├─ p0301_0400/       13
│  … 36 non-empty packages …
└─ p3801_3900/        2
```

A class's package is a pure function of its problem number:
`p{floor((n-1)/100)*100+1}_{ceil(n/100)*100}`. Problem 322 is always in
`p0301_0400`.

### Why fixed-width and not equal-count

Equal-count buckets (~32 each) balance better but produce boundaries like
`p0238_0502`, and are **unstable**: each new solve shifts the edges and forces
unrelated files to migrate packages. Fixed-width boundaries never move, which is
the entire point of a number-keyed scheme.

The cost is accepted imbalance — 36 packages with a long tail of 1–3-class ones,
and 68 in `p0001_0100`. That is 68 instead of 281, in the package you navigate to
most confidently. `p0001_0100` is also the fastest-growing package, and the
javadoc stamp below is what makes splitting it later a scripted operation rather
than a second research pass.

## Rejected alternatives

| Alternative | Reason rejected |
|---|---|
| Topic/algorithm packages (`dp`, `graph`, `tree`) | Best browse story, but each class lives in exactly one package while many problems are genuinely multi-topic. Requires classifying 281 implementations by hand, and the taxonomy is subjective. |
| One package per problem, `boj`-style (`leetcode.lc0322`) | `boj/` uses this because 138 of its 139 classes are named `Main.java` (a Baekjoon requirement) and cannot share a package. LeetCode has no such constraint, and 281 single-file folders would make the tree *more* crowded, not less. |
| Difficulty packages (`easy`/`medium`/`hard`) | Leaves ~150 classes in `medium/`. Does not solve the crowding. |
| No move; IntelliJ Scopes only | Supports multi-membership and zero churn, but is IDE-local config, invisible to anything outside IntelliJ and to the module's own structure. |

## Problem-number source

`https://leetcode.com/api/problems/all/` — public, unauthenticated, HTTP 200,
~2 MB, 4029 problems. (Note: `leetcode.com` 403s ordinary page fetches; this
JSON endpoint does not.)

**Use `stat.frontend_question_id`, not `stat.question_id`.** The latter is
LeetCode's internal row ID and diverges from the displayed number for premium and
reworked problems.

### Resolution method

Normalizing both sides to `[^a-z0-9]` -stripped lowercase matched **257 of 281
(91.5%)** automatically. The remaining 24 were resolved by fuzzy match plus
reading each class's method signature, and are marked **manual** in the appendix.
Notable cases:

- `ThreeSum` → 15 (*3Sum*), `Sqrt` → 69 (*Sqrt(x)*), `Pow` → 50 (*Pow(x, n)*) —
  digit/punctuation forms that normalization cannot bridge.
- `FindGreatesCommonDivisorOfArray` → 1979 — typo in the class name (`Greates`).
- `ImplementTrie` → 208, not 1804 (*Trie II*): exposes only
  `insert`/`search`/`startsWith`.
- `MinStepsToMakeTwoStringsAnagram` → 1347, not 2186 (*II*): counts surplus in
  one direction only.
- `CountCompletePairPairs2` → 3185 (*Complete Day II*), not 3184: returns `long`.

The mapping is a verified bijection: every one of the 281 classes on disk has
exactly one number, and every mapped class exists on disk. **No two classes map
to the same problem number.**

## Javadoc problem stamp

Each solution class gains a class-level javadoc link:

```java
/** <a href="https://leetcode.com/problems/coin-change/">322. Coin Change</a> */
public class CoinChange {
```

This is deliberately more durable than the folders. It makes `Ctrl+Shift+F "322."`
find the class, gives a clickable route back to the problem, and puts the number
in the source — so any later re-bucketing (splitting `p0001_0100`, or moving to a
topic taxonomy) becomes a scripted operation rather than a second research pass.

Existing `@implNote` complexity javadoc on methods is untouched.

## Resolved: duplicate solution for #121

Mapping to numbers revealed that `MaxProfit` and `BestTimeToBuyAndSellStock` were
the same problem (#121) — same signature, same min-price scan, same result.
Detected only because numbering provides a canonical key that class names do not.

**Resolved before migration:** the user deleted `MaxProfit` and `MaxProfitTest`.
Counts in this spec reflect the post-deletion state (281 solutions, 281 tests),
and the mapping now contains no duplicate problem numbers.

## Migration plan

1. Create the 36 target packages under both `src/main/java/leetcode/` and
   `src/test/java/leetcode/`.
2. `git mv` each solution and its `*Test` counterpart into its package —
   `git mv` preserves file history.
3. Rewrite the `package` line in all 562 moved files.
4. **No import changes required.** Verified: `leetcode.support` is already a
   distinct package from `leetcode`, so all 46 dependent classes import it
   explicitly today and those imports stay valid at any package depth. Five
   classes (`ConstructQuadTree`, `CopyListWithRandomPointer`,
   `DesignAddAndSearchWordsDataStructure`, `ImplementTrie`,
   `PopulatingNextRightPointersInEachNodeII`) declare their own nested `Node`;
   nested-class resolution outranks imports, so these are unaffected.
5. Add the javadoc stamp to each of the 281 solution classes.
6. `./gradlew :leetcode:spotlessApply` (module uses palantir-java-format).

Tests mirror main exactly: `TwoSumTest` follows `TwoSum` into `p0001_0100`.
Pairing is currently perfect — every one of the 281 solutions has exactly one
`*Test`, with no orphans in either direction. `leetcode.support` and its 2 tests
stay where they are.

## Verification

**The full suite is not a usable gate.** A baseline run of `:leetcode:test`
exhausted the default heap after 1133 tests (29 failing, 1 skipped), and a rerun
at 4 GB hung past 10 minutes, dying just after `InterleavingStringTest`. The
cause is uncommitted work-in-progress solutions carrying
`UnsupportedOperationException` stubs — pre-existing, unrelated to this change,
and out of scope here.

Compilation is the correct gate instead: this change edits `package` lines and
comments only, never a method body, so unresolved symbols are the only failure
mode it can introduce.

- `./gradlew :leetcode:compileJava :leetcode:compileTestJava` — exit 0.
- `./gradlew :leetcode:spotlessCheck` — formatting clean.
- Assert 281 solution classes still exist and each declares a `p####_####`
  package: no file silently dropped or left behind in `leetcode`.
- Targeted `--tests` run over a known-green sample that exercises `ListNode` and
  `TreeNode`, proving `leetcode.support` imports still resolve from the deeper
  packages.
- `git log --follow` on a sample moved file confirms history survived.

## Appendix: complete class → package mapping

281 rows, grouped by target package. `manual` marks a number resolved by
inspection rather than exact title match.

### `leetcode.p0001_0100` — 68 classes

| # | Class | LeetCode title | Source |
|---|---|---|---|
| 1 | `TwoSum` | Two Sum | auto |
| 2 | `AddTwoNumbers` | Add Two Numbers | auto |
| 3 | `LongestSubstringWithoutRepeatingCharacters` | Longest Substring Without Repeating Characters | auto |
| 4 | `MedianOfTwoSortedArrays` | Median of Two Sorted Arrays | auto |
| 5 | `LongestPalindromicSubstring` | Longest Palindromic Substring | auto |
| 6 | `ZigzagConversion` | Zigzag Conversion | auto |
| 9 | `PalindromeNumber` | Palindrome Number | auto |
| 11 | `ContainerWithMostWater` | Container With Most Water | auto |
| 12 | `IntegerToRoman` | Integer to Roman | auto |
| 13 | `RomanToInteger` | Roman to Integer | auto |
| 14 | `LongestCommonPrefix` | Longest Common Prefix | auto |
| 15 | `ThreeSum` | 3Sum | **manual** |
| 17 | `LetterCombinationsOfAPhoneNumber` | Letter Combinations of a Phone Number | auto |
| 19 | `RemoveNthFromEndOfList` | Remove Nth Node From End of List | **manual** |
| 20 | `ValidParentheses` | Valid Parentheses | auto |
| 21 | `MergeTwoSortedLists` | Merge Two Sorted Lists | auto |
| 22 | `GenerateParentheses` | Generate Parentheses | auto |
| 23 | `MergeKSortedList` | Merge k Sorted Lists | **manual** |
| 25 | `ReverseNodesInKGroup` | Reverse Nodes in k-Group | auto |
| 26 | `RemoveDuplicatesFromSortedArray` | Remove Duplicates from Sorted Array | auto |
| 27 | `RemoveElement` | Remove Element | auto |
| 28 | `FindTheIndexOfTheFirstOccurrenceInAString` | Find the Index of the First Occurrence in a String | auto |
| 30 | `SubstringWithConcatenationOfAllWords` | Substring with Concatenation of All Words | auto |
| 33 | `SearchInRotatedSortedArray` | Search in Rotated Sorted Array | auto |
| 34 | `FindFirstAndLastPositionOfElementInSortedArray` | Find First and Last Position of Element in Sorted Array | auto |
| 35 | `SearchInsertPosition` | Search Insert Position | auto |
| 36 | `ValidSudoku` | Valid Sudoku | auto |
| 39 | `CombinationSum` | Combination Sum | auto |
| 41 | `FirstMissingPositive` | First Missing Positive | auto |
| 42 | `TrappingRainWater` | Trapping Rain Water | auto |
| 45 | `JumpGameII` | Jump Game II | auto |
| 46 | `Permutations` | Permutations | auto |
| 48 | `RotateImage` | Rotate Image | auto |
| 49 | `GroupAnagrams` | Group Anagrams | auto |
| 50 | `Pow` | Pow(x, n) | **manual** |
| 51 | `NQueens` | N-Queens | auto |
| 52 | `NQueensII` | N-Queens II | auto |
| 53 | `MaximumSubarray` | Maximum Subarray | auto |
| 54 | `SpiralMatrix` | Spiral Matrix | auto |
| 55 | `JumpGame` | Jump Game | auto |
| 56 | `MergeIntervals` | Merge Intervals | auto |
| 57 | `InsertInterval` | Insert Interval | auto |
| 58 | `LengthOfLastWord` | Length of Last Word | auto |
| 61 | `RotateList` | Rotate List | auto |
| 62 | `UniquePaths` | Unique Paths | auto |
| 63 | `UniquePathsII` | Unique Paths II | auto |
| 64 | `MinimumPathSum` | Minimum Path Sum | auto |
| 66 | `PlusOne` | Plus One | auto |
| 67 | `AddBinary` | Add Binary | auto |
| 68 | `TextJustification` | Text Justification | auto |
| 69 | `Sqrt` | Sqrt(x) | **manual** |
| 70 | `ClimbingStairs` | Climbing Stairs | auto |
| 71 | `SimplifyPath` | Simplify Path | auto |
| 72 | `EditDistance` | Edit Distance | auto |
| 73 | `SetMatrixZeroes` | Set Matrix Zeroes | auto |
| 74 | `SearchA2DMatrix` | Search a 2D Matrix | auto |
| 76 | `MinimumWindowSubstring` | Minimum Window Substring | auto |
| 77 | `Combinations` | Combinations | auto |
| 79 | `WordSearch` | Word Search | auto |
| 80 | `RemoveDuplicatesFromSortedArrayII` | Remove Duplicates from Sorted Array II | auto |
| 82 | `RemoveDuplicatesFromSortedListII` | Remove Duplicates from Sorted List II | auto |
| 86 | `PartitionList` | Partition List | auto |
| 88 | `MergeSortedArray` | Merge Sorted Array | auto |
| 92 | `ReverseLinkedListII` | Reverse Linked List II | auto |
| 94 | `BinaryTreeInorderTraversal` | Binary Tree Inorder Traversal | auto |
| 97 | `InterleavingString` | Interleaving String | auto |
| 98 | `ValidateBinarySearchTree` | Validate Binary Search Tree | auto |
| 100 | `SameTree` | Same Tree | auto |

### `leetcode.p0101_0200` — 46 classes

| # | Class | LeetCode title | Source |
|---|---|---|---|
| 101 | `SymmetricTree` | Symmetric Tree | auto |
| 102 | `BinaryTreeLevelOrderTraversal` | Binary Tree Level Order Traversal | auto |
| 103 | `BinaryTreeZigzagLevelOrderTraversal` | Binary Tree Zigzag Level Order Traversal | auto |
| 104 | `MaximumDepthOfBinaryTree` | Maximum Depth of Binary Tree | auto |
| 105 | `ConstructBinaryTreeFromPreorderAndInorderTraversal` | Construct Binary Tree from Preorder and Inorder Traversal | auto |
| 106 | `ConstructBinaryTreeFromInorderAndPostorderTraversal` | Construct Binary Tree from Inorder and Postorder Traversal | auto |
| 108 | `ConvertSortedArrayToBinarySearchTree` | Convert Sorted Array to Binary Search Tree | auto |
| 112 | `PathSum` | Path Sum | auto |
| 114 | `FlattenBinaryTreeToLinkedList` | Flatten Binary Tree to Linked List | auto |
| 117 | `PopulatingNextRightPointersInEachNodeII` | Populating Next Right Pointers in Each Node II | auto |
| 120 | `Triangle` | Triangle | auto |
| 121 | `BestTimeToBuyAndSellStock` | Best Time to Buy and Sell Stock | auto |
| 122 | `BestTimeToBuyAndSellStockII` | Best Time to Buy and Sell Stock II | auto |
| 123 | `BestTimeToBuyAndSellStockIII` | Best Time to Buy and Sell Stock III | auto |
| 124 | `BinaryTreeMaximumPathSum` | Binary Tree Maximum Path Sum | auto |
| 125 | `ValidPalindrome` | Valid Palindrome | auto |
| 127 | `WordLadder` | Word Ladder | auto |
| 128 | `LongestConsecutiveSequence` | Longest Consecutive Sequence | auto |
| 129 | `SumRootToLeafNumbers` | Sum Root to Leaf Numbers | auto |
| 130 | `SurroundedRegions` | Surrounded Regions | auto |
| 133 | `CloneGraph` | Clone Graph | auto |
| 134 | `GasStation` | Gas Station | auto |
| 135 | `Candy` | Candy | auto |
| 136 | `SingleNumber` | Single Number | auto |
| 137 | `SingleNumberII` | Single Number II | auto |
| 138 | `CopyListWithRandomPointer` | Copy List with Random Pointer | auto |
| 139 | `WordBreak` | Word Break | auto |
| 141 | `LinkedListCycle` | Linked List Cycle | auto |
| 143 | `ReorderList` | Reorder List | auto |
| 146 | `LRUCache` | LRU Cache | auto |
| 148 | `SortList` | Sort List | auto |
| 149 | `MaxPointsOnALine` | Max Points on a Line | auto |
| 150 | `EvaluateReversePolishNotation` | Evaluate Reverse Polish Notation | auto |
| 151 | `ReverseWordsInAString` | Reverse Words in a String | auto |
| 153 | `FindMinimumInRotatedSortedArray` | Find Minimum in Rotated Sorted Array | auto |
| 155 | `MinStack` | Min Stack | auto |
| 162 | `FindPeakElement` | Find Peak Element | auto |
| 167 | `TwoSumII` | Two Sum II - Input Array Is Sorted | **manual** |
| 172 | `FactorialTrailingZeroes` | Factorial Trailing Zeroes | auto |
| 173 | `BinarySearchTreeIterator` | Binary Search Tree Iterator | auto |
| 189 | `RotateArray` | Rotate Array | auto |
| 190 | `ReverseBits` | Reverse Bits | auto |
| 191 | `NumberOf1Bits` | Number of 1 Bits | auto |
| 198 | `HouseRobber` | House Robber | auto |
| 199 | `BinaryTreeRightSideView` | Binary Tree Right Side View | auto |
| 200 | `NumberOfIslands` | Number of Islands | auto |

### `leetcode.p0201_0300` — 35 classes

| # | Class | LeetCode title | Source |
|---|---|---|---|
| 201 | `BitwiseAndOfNumbersRange` | Bitwise AND of Numbers Range | auto |
| 202 | `HappyNumber` | Happy Number | auto |
| 205 | `IsomorphicStrings` | Isomorphic Strings | auto |
| 206 | `ReverseLinkedList` | Reverse Linked List | auto |
| 207 | `CourseSchedule` | Course Schedule | auto |
| 208 | `ImplementTrie` | Implement Trie (Prefix Tree) | **manual** |
| 209 | `MinimumSizeSubarraySum` | Minimum Size Subarray Sum | auto |
| 210 | `CourseScheduleII` | Course Schedule II | auto |
| 211 | `DesignAddAndSearchWordsDataStructure` | Design Add and Search Words Data Structure | auto |
| 212 | `WordSearchII` | Word Search II | auto |
| 215 | `KthLargestElementInAnArray` | Kth Largest Element in an Array | auto |
| 217 | `ContainsDuplicate` | Contains Duplicate | auto |
| 219 | `ContainsDuplicateII` | Contains Duplicate II | auto |
| 221 | `MaximalSquare` | Maximal Square | auto |
| 222 | `CountCompleteTreeNodes` | Count Complete Tree Nodes | auto |
| 224 | `BasicCalculator` | Basic Calculator | auto |
| 226 | `InvertBinaryTree` | Invert Binary Tree | auto |
| 228 | `SummaryRanges` | Summary Ranges | auto |
| 230 | `KthSmallestElementInABST` | Kth Smallest Element in a BST | auto |
| 234 | `PalindromeLinkedList` | Palindrome Linked List | auto |
| 236 | `LowestCommonAncestorOfABinaryTree` | Lowest Common Ancestor of a Binary Tree | auto |
| 238 | `ProductOfArrayExceptSelf` | Product of Array Except Self | auto |
| 239 | `SlidingWindowMaximum` | Sliding Window Maximum | auto |
| 242 | `ValidAnagram` | Valid Anagram | auto |
| 252 | `MeetingRooms` | Meeting Rooms | auto |
| 257 | `BinaryTreePaths` | Binary Tree Paths | auto |
| 268 | `MissingNumber` | Missing Number | auto |
| 274 | `HIndex` | H-Index | auto |
| 278 | `FirstBadVersion` | First Bad Version | auto |
| 283 | `MoveZeroes` | Move Zeroes | auto |
| 289 | `GameOfLife` | Game of Life | auto |
| 290 | `WordPattern` | Word Pattern | auto |
| 295 | `FindMedianFromDataStream` | Find Median from Data Stream | auto |
| 297 | `SerializeDeserializeBinaryTree` | Serialize and Deserialize Binary Tree | **manual** |
| 300 | `LongestIncreasingSubsequence` | Longest Increasing Subsequence | auto |

### `leetcode.p0301_0400` — 13 classes

| # | Class | LeetCode title | Source |
|---|---|---|---|
| 322 | `CoinChange` | Coin Change | auto |
| 329 | `LongestIncreasingPathInMatrix` | Longest Increasing Path in a Matrix | **manual** |
| 338 | `CountingBits` | Counting Bits | auto |
| 344 | `ReverseString` | Reverse String | auto |
| 347 | `TopKFrequentElements` | Top K Frequent Elements | auto |
| 358 | `RearrangeStringKDistanceApart` | Rearrange String k Distance Apart | auto |
| 362 | `HitCounter` | Design Hit Counter | **manual** |
| 373 | `FindKPairsWithSmallestSums` | Find K Pairs with Smallest Sums | auto |
| 380 | `InsertDeleteGetRandomO1` | Insert Delete GetRandom O(1) | auto |
| 383 | `RansomNote` | Ransom Note | auto |
| 391 | `PerfectRectangle` | Perfect Rectangle | auto |
| 392 | `IsSubsequence` | Is Subsequence | auto |
| 399 | `EvaluateDivision` | Evaluate Division | auto |

### `leetcode.p0401_0500` — 8 classes

| # | Class | LeetCode title | Source |
|---|---|---|---|
| 406 | `QueueReconstructionByHeight` | Queue Reconstruction by Height | auto |
| 409 | `LongestPalindrome` | Longest Palindrome | auto |
| 427 | `ConstructQuadTree` | Construct Quad Tree | auto |
| 433 | `MinimumGeneticMutation` | Minimum Genetic Mutation | auto |
| 435 | `NonOverlapIntervals` | Non-overlapping Intervals | **manual** |
| 443 | `StringCompression` | String Compression | auto |
| 452 | `MinimumNumberOfArrowsToBurstBalloons` | Minimum Number of Arrows to Burst Balloons | auto |
| 486 | `PredictTheWinner` | Predict the Winner | auto |

### `leetcode.p0501_0600` — 5 classes

| # | Class | LeetCode title | Source |
|---|---|---|---|
| 502 | `IPO` | IPO | auto |
| 530 | `MinimumAbsoluteDifferenceInBST` | Minimum Absolute Difference in BST | auto |
| 547 | `NumberOfProvinces` | Number of Provinces | auto |
| 561 | `ArrayPartition` | Array Partition | auto |
| 572 | `SubtreeOfAnotherTree` | Subtree of Another Tree | auto |

### `leetcode.p0601_0700` — 7 classes

| # | Class | LeetCode title | Source |
|---|---|---|---|
| 616 | `AddBoldTag` | Add Bold Tag in String | **manual** |
| 628 | `MaximumProductOfThreeNumbers` | Maximum Product of Three Numbers | auto |
| 637 | `AverageOfLevelsInBinaryTree` | Average of Levels in Binary Tree | auto |
| 647 | `PalindromicSubstrings` | Palindromic Substrings | auto |
| 652 | `FindDuplicateSubtrees` | Find Duplicate Subtrees | auto |
| 680 | `ValidPalindromeII` | Valid Palindrome II | auto |
| 696 | `CountBinarySubstrings` | Count Binary Substrings | auto |

### `leetcode.p0701_0800` — 3 classes

| # | Class | LeetCode title | Source |
|---|---|---|---|
| 704 | `BinarySearch` | Binary Search | auto |
| 706 | `DesignHashMap` | Design HashMap | auto |
| 739 | `DailyTemperatures` | Daily Temperatures | auto |

### `leetcode.p0801_0900` — 4 classes

| # | Class | LeetCode title | Source |
|---|---|---|---|
| 819 | `MostCommonWord` | Most Common Word | auto |
| 876 | `MiddleOfLinkedList` | Middle of the Linked List | **manual** |
| 877 | `StoneGame` | Stone Game | auto |
| 886 | `PossibleBipartition` | Possible Bipartition | auto |

### `leetcode.p0901_1000` — 5 classes

| # | Class | LeetCode title | Source |
|---|---|---|---|
| 909 | `SnakesAndLadders` | Snakes and Ladders | auto |
| 918 | `MaximumSumCircularSubarray` | Maximum Sum Circular Subarray | auto |
| 937 | `ReorderLogFiles` | Reorder Data in Log Files | **manual** |
| 938 | `RangeSumOfBST` | Range Sum of BST | auto |
| 994 | `RottingOranges` | Rotting Oranges | auto |

### `leetcode.p1001_1100` — 2 classes

| # | Class | LeetCode title | Source |
|---|---|---|---|
| 1071 | `GreatestCommonDivisorOfStrings` | Greatest Common Divisor of Strings | auto |
| 1081 | `SmallestSubsequenceOfDistinctCharacters` | Smallest Subsequence of Distinct Characters | auto |

### `leetcode.p1101_1200` — 5 classes

| # | Class | LeetCode title | Source |
|---|---|---|---|
| 1109 | `CorporateFlightBookings` | Corporate Flight Bookings | auto |
| 1140 | `StoneGameII` | Stone Game II | auto |
| 1143 | `LongestCommonSubsequence` | Longest Common Subsequence | auto |
| 1146 | `SnapshotArray` | Snapshot Array | auto |
| 1189 | `MaximumNumberOfBalloons` | Maximum Number of Balloons | auto |

### `leetcode.p1201_1300` — 5 classes

| # | Class | LeetCode title | Source |
|---|---|---|---|
| 1249 | `MinRemoveToMakeValidParentheses` | Minimum Remove to Make Valid Parentheses | **manual** |
| 1260 | `Shift2DGrid` | Shift 2D Grid | auto |
| 1288 | `RemoveCoveredIntervals` | Remove Covered Intervals | auto |
| 1291 | `SequentialDigits` | Sequential Digits | auto |
| 1297 | `MaxOccurrencesOfASubstring` | Maximum Number of Occurrences of a Substring | **manual** |

### `leetcode.p1301_1400` — 7 classes

| # | Class | LeetCode title | Source |
|---|---|---|---|
| 1301 | `NumberOfPathsWithMaxScore` | Number of Paths with Max Score | auto |
| 1331 | `RankTransformationOfAnArray` | Rank Transform of an Array | **manual** |
| 1344 | `AngleBetweenHandsOfAClock` | Angle Between Hands of a Clock | auto |
| 1347 | `MinStepsToMakeTwoStringsAnagram` | Minimum Number of Steps to Make Two Strings Anagram | **manual** |
| 1358 | `NumberOfSubstringsContainingAllThreeCharacters` | Number of Substrings Containing All Three Characters | auto |
| 1386 | `CinemaSeatAllocation` | Cinema Seat Allocation | auto |
| 1391 | `CheckIfThereIsValidPath` | Check if There is a Valid Path in a Grid | **manual** |

### `leetcode.p1401_1500` — 4 classes

| # | Class | LeetCode title | Source |
|---|---|---|---|
| 1406 | `StoneGameIII` | Stone Game III | auto |
| 1424 | `DiagonalTraverseII` | Diagonal Traverse II | auto |
| 1464 | `MaximumProductOfTwoElementsInAnArray` | Maximum Product of Two Elements in an Array | auto |
| 1481 | `LeastNumberOfUniqueIntegers` | Least Number of Unique Integers after K Removals | **manual** |

### `leetcode.p1501_1600` — 3 classes

| # | Class | LeetCode title | Source |
|---|---|---|---|
| 1510 | `StoneGameIV` | Stone Game IV | auto |
| 1563 | `StoneGameV` | Stone Game V | auto |
| 1593 | `SplitStringMaxNumberUniqueSubstrings` | Split a String Into the Max Number of Unique Substrings | **manual** |

### `leetcode.p1701_1800` — 3 classes

| # | Class | LeetCode title | Source |
|---|---|---|---|
| 1710 | `MaximumUnitsOnATruck` | Maximum Units on a Truck | auto |
| 1732 | `FindTheHighestAltitude` | Find the Highest Altitude | auto |
| 1768 | `MergeStringsAlternately` | Merge Strings Alternately | auto |

### `leetcode.p1801_1900` — 3 classes

| # | Class | LeetCode title | Source |
|---|---|---|---|
| 1833 | `MaximumIceCreamBars` | Maximum Ice Cream Bars | auto |
| 1840 | `MaximumBuildingHeight` | Maximum Building Height | auto |
| 1846 | `MaximumElementAfterDecreasingAndRearranging` | Maximum Element After Decreasing and Rearranging | auto |

### `leetcode.p1901_2000` — 2 classes

| # | Class | LeetCode title | Source |
|---|---|---|---|
| 1967 | `NumberOfStringsThatAppearAsSubstringsInWord` | Number of Strings That Appear as Substrings in Word | auto |
| 1979 | `FindGreatesCommonDivisorOfArray` | Find Greatest Common Divisor of Array | **manual** |

### `leetcode.p2001_2100` — 2 classes

| # | Class | LeetCode title | Source |
|---|---|---|---|
| 2029 | `StoneGameIX` | Stone Game IX | auto |
| 2095 | `DeleteTheMiddleNodeOfALinkedList` | Delete the Middle Node of a Linked List | auto |

### `leetcode.p2101_2200` — 3 classes

| # | Class | LeetCode title | Source |
|---|---|---|---|
| 2130 | `MaximumTwinSumOfALinkedList` | Maximum Twin Sum of a Linked List | auto |
| 2161 | `PartitionArrayAccordingToGivenPivot` | Partition Array According to Given Pivot | auto |
| 2196 | `CreateBinaryTreeFromDescriptions` | Create Binary Tree From Descriptions | auto |

### `leetcode.p2201_2300` — 1 classes

| # | Class | LeetCode title | Source |
|---|---|---|---|
| 2213 | `LongestSubstringOfOneRepeatingCharacter` | Longest Substring of One Repeating Character | auto |

### `leetcode.p2401_2500` — 2 classes

| # | Class | LeetCode title | Source |
|---|---|---|---|
| 2446 | `TwoEventsHaveConflict` | Determine if Two Events Have Conflict | **manual** |
| 2492 | `MinimumScoreOfAPathBetweenTwoCities` | Minimum Score of a Path Between Two Cities | auto |

### `leetcode.p2501_2600` — 1 classes

| # | Class | LeetCode title | Source |
|---|---|---|---|
| 2574 | `LeftAndRightSumDifferences` | Left and Right Sum Differences | auto |

### `leetcode.p2601_2700` — 1 classes

| # | Class | LeetCode title | Source |
|---|---|---|---|
| 2685 | `CountTheNumberOfCompleteComponents` | Count the Number of Complete Components | auto |

### `leetcode.p2801_2900` — 1 classes

| # | Class | LeetCode title | Source |
|---|---|---|---|
| 2812 | `FindTheSafestPathInAGrid` | Find the Safest Path in a Grid | auto |

### `leetcode.p2901_3000` — 2 classes

| # | Class | LeetCode title | Source |
|---|---|---|---|
| 2958 | `LengthOfLongestSubarrayWithAtMostKFrequency` | Length of Longest Subarray With at Most K Frequency | auto |
| 2996 | `SmallestMissingIntegerGreaterThanSequentialPrefixSum` | Smallest Missing Integer Greater Than Sequential Prefix Sum | auto |

### `leetcode.p3001_3100` — 5 classes

| # | Class | LeetCode title | Source |
|---|---|---|---|
| 3014 | `MinimumNumberOfPushesToTypeWordI` | Minimum Number of Pushes to Type Word I | auto |
| 3016 | `MinimumNumberOfPushesToTypeWordII` | Minimum Number of Pushes to Type Word II | auto |
| 3020 | `FindTheMaximumNumberOfElementsInSubset` | Find the Maximum Number of Elements in Subset | auto |
| 3069 | `DistributeElementsIntoTwoArraysI` | Distribute Elements Into Two Arrays I | auto |
| 3090 | `MaximumLengthSubstringWithTwoOccurrences` | Maximum Length Substring With Two Occurrences | auto |

### `leetcode.p3101_3200` — 2 classes

| # | Class | LeetCode title | Source |
|---|---|---|---|
| 3169 | `CountDaysWithoutMeetings` | Count Days Without Meetings | auto |
| 3185 | `CountCompletePairPairs2` | Count Pairs That Form a Complete Day II | **manual** |

### `leetcode.p3201_3300` — 1 classes

| # | Class | LeetCode title | Source |
|---|---|---|---|
| 3286 | `FindASafeWalkThroughAGrid` | Find a Safe Walk Through a Grid | auto |

### `leetcode.p3301_3400` — 6 classes

| # | Class | LeetCode title | Source |
|---|---|---|---|
| 3302 | `FindTheLexicographicallySmallestValidSequence` | Find the Lexicographically Smallest Valid Sequence | auto |
| 3310 | `RemoveMethodsFromProject` | Remove Methods From Project | auto |
| 3312 | `SortedGCDPairQueries` | Sorted GCD Pair Queries | auto |
| 3336 | `FindTheNumberOfSubsequencesWithEqualGCD` | Find the Number of Subsequences With Equal GCD | auto |
| 3345 | `SmallestDivisibleDigitProductI` | Smallest Divisible Digit Product I | auto |
| 3348 | `SmallestDivisibleDigitProductII` | Smallest Divisible Digit Product II | auto |

### `leetcode.p3401_3500` — 2 classes

| # | Class | LeetCode title | Source |
|---|---|---|---|
| 3471 | `FindTheLargestAlmostMissingInteger` | Find the Largest Almost Missing Integer | auto |
| 3499 | `MaximizeActiveSectionWithTradeI` | Maximize Active Section with Trade I | auto |

### `leetcode.p3501_3600` — 7 classes

| # | Class | LeetCode title | Source |
|---|---|---|---|
| 3513 | `NumberOfUniqueXORTripletsI` | Number of Unique XOR Triplets I | auto |
| 3514 | `NumberOfUniqueXORTripletsII` | Number of Unique XOR Triplets II | auto |
| 3517 | `SmallestPalindromicRearrangementI` | Smallest Palindromic Rearrangement I | auto |
| 3518 | `SmallestPalindromicRearrangementII` | Smallest Palindromic Rearrangement II | auto |
| 3532 | `PathExistenceQueriesInAGraphI` | Path Existence Queries in a Graph I | auto |
| 3536 | `MaximumProductOfTwoDigits` | Maximum Product of Two Digits | auto |
| 3558 | `NumberOfWaysToAssignEdgeWeightsI` | Number of Ways to Assign Edge Weights I | auto |

### `leetcode.p3601_3700` — 8 classes

| # | Class | LeetCode title | Source |
|---|---|---|---|
| 3612 | `ProcessStringWithSpecialOperationsI` | Process String with Special Operations I | auto |
| 3614 | `ProcessStringWithSpecialOperationsII` | Process String with Special Operations II | auto |
| 3620 | `NetworkRecoveryPathways` | Network Recovery Pathways | auto |
| 3658 | `GCDOfOddAndEvenSums` | GCD of Odd and Even Sums | auto |
| 3689 | `MaximumTotalSubarrayValueI` | Maximum Total Subarray Value I | auto |
| 3691 | `MaximumTotalSubarrayValueII` | Maximum Total Subarray Value II | auto |
| 3699 | `NumberOfZigZagArraysI` | Number of ZigZag Arrays I | auto |
| 3700 | `NumberOfZigZagArraysII` | Number of ZigZag Arrays II | auto |

### `leetcode.p3701_3800` — 7 classes

| # | Class | LeetCode title | Source |
|---|---|---|---|
| 3702 | `LongestSubsequenceWithNonZeroBitwiseXOR` | Longest Subsequence With Non-Zero Bitwise XOR | auto |
| 3731 | `FindMissingElements` | Find Missing Elements | auto |
| 3737 | `CountSubarraysWithMajorityElementI` | Count Subarrays With Majority Element I | auto |
| 3739 | `CountSubarraysWithMajorityElementII` | Count Subarrays With Majority Element II | auto |
| 3753 | `TotalWavinessOfNumbersInRangeII` | Total Waviness of Numbers in Range II | auto |
| 3754 | `ConcatenateNonZeroDigitsAndMultiplyBySumI` | Concatenate Non-Zero Digits and Multiply by Sum I | auto |
| 3756 | `ConcatenateNonZeroDigitsAndMultiplyBySumII` | Concatenate Non-Zero Digits and Multiply by Sum II | auto |

### `leetcode.p3801_3900` — 2 classes

| # | Class | LeetCode title | Source |
|---|---|---|---|
| 3838 | `WeightedWordMapping` | Weighted Word Mapping | auto |
| 3867 | `SumOfGcdOfFormedPairs` | Sum of GCD of Formed Pairs | auto |