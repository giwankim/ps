# Bunker Rush

문제 ID: `BUNKER`

## 문제

#### 문제

'BoxeR' Lim, Yo-Hwan(임요환) and 'Yellow' Hong, Jin-Ho(홍진호) will face each other at the StarCraft 6 Championship in 2022!

StarCraft is a real-time strategy video game series. The series consists of many gameplay modes, but usually two players compete to destroy each other. Players choose one of many strategies to play each match, for example, "Bunker Rush" or "Hidden Barracks". Sometimes, the result of a match is trivially determined based on players' strategies like a rock-scissors-paper game. So in professional tournaments, it is usual to play more than a single match to declare who is victorious.

BoxeR has a peculiar style of choosing his game plans: he chooses a sequence of strategies, and repeats them over multiple games.

| Match No. | Strategy |
| --- | --- |
| Game 1 | Bunker Rush |
| Game 2 | Bunker Rush |
| Game 3 | Bunker Rush |

For example, the above table shows how BoxeR chose to use one strategy (the Bunker Rush) three times in a row (in a best-of-five series). In this case, the sequence was of length 1 and was repeated three times.

| Match No. | Strategy |
| --- | --- |
| Game 1 | Marine Rush |
| Game 2 | Bunker Rush |
| Game 3 | Double Command Center |
| Game 4 | Bunker Rush |
| Game 5 | Double Command Center |
| Game 6 | Dropship Rush |
| Game 7 | Dropship Rush |

The above table shows another example. In this case, BoxeR played "Marine Rush" once, and he repeated a sequence of length 2 twice - "Bunker Rush" and "Double Command Center". Finally, he repeated a sequence of length 1 twice, which was "Dropship Rush".

An average player would notice if his opponent repeats the same sequence of strategies more than two times, and will not get fooled the third time. However, Yellow has had bad games against BoxeR before, and now Yellow is determined to be not fooled again.

As such, Yellow is interested in finding all sequences of strategies that BoxeR used three times consecutively in the past. You are to write a program that tells Yellow how many such sequences exist as well as the length of the longest such sequence, given a complete log of strategies used by BoxeR in his career.

Please help Yellow prepare for the championship series!

## 입력

#### 입력

The first line contains the number of test cases, T.

For each test case, a single line contains a string that describes the complete log of the strategies used by BoxeR. Each character is an English letter (either lower-case or upper-case) that represents a single-game strategy. The total number of games of BoxeR is no more than 10,000 for each test case.

## 출력

#### 출력

For each test case, output two integers in a single line, separated by a single white space: the number of all sequences of strategies that BoxeR repeatedly used three times, and the length of the longest such sequence.

Note that if the same sequence of strategies was repeatedly used at different times, then it must be counted multiple times, once per its starting index (see the sample test case for clarification).

If no sequence of strategies was repeated three times or more, output two '-2's, separated by a single space (quotes for clarification only).

## 노트

#### 노트

Clarification for the sample input and output:

1. There are four different sequences of strategies that BoxeR repeated three times. One is 'bc' that starts and ends at the indices [3, 8], [5, 10], [7, 12], [9, 14], [11, 16], and [13, 18]. Another is 'cb' that starts and ends at the indices [4, 9], [6, 11], [8, 13], [10, 15], and [12, 17], and 'bcbc' that starts and ends at the indices [3, 14], [5, 16], and [7, 18]. The other is 'cbcb' that starts and ends at the indices [4, 15] and [6, 17]. Hence, the total number of sequences of the interest is 16 and the length of the longest one is 4 due to 'bcbc'.
2. The only sequence that was repeated three times is 'A' at index 7. Hence the correct answer is '1 1'.
3. In this case no sequence was repeated three times.
