# Walking To The Andromeda

문제 ID: `ANDROMEDA`

## 문제

#### 문제

The Space Elevator to the Andromeda was shut down due to recent economic meltdown. Two of the today’s passengers, Won-seok and his sister Yeon-Gyoung, decided to climb prepared emergency space stairs.

Yeon-Gyoung really likes to play the game of Rock-Paper-Scissors, and thought this trip could be a little boring, so she proposed a special rule to her brother.

1. Play one throw(i.e., two players throw their gesture once) each step to the Andromeda.
2. It needs n throw-wins to win each match. When a match ﬁnishes, next match will start in the next
   step.
3. If a match is still being played when two reaches Andromeda, that match will be ignored.

For each throw, wining/drawing/losing probability is them same, i.e., 1/3. She thought normal play could be meaningless, and so she decided to maximize the number of matches won, by giving up and restarting matches she doesn’t have a good probability to win.

Let’s take steps = 6 and n = 3 as an example, and assume that she already lost the ﬁrst two throws. If she proceed without abandoning the game, her probability to still win one game is 7/81 — win/win/win/anything, draw/win/win/win, win/draw/win/win, win/win/draw/win, lose/win/win/win: 1/27+4/81. But if she resigns and starts a new next game here, the probability increases to 9/81 = 1/9, as she gains two more ways to win(win/lose/win/win, win/win/lose/win).

Given steps and n, calculate the average number of matches she can win, when she plays optimally.

Note that Won-seok is patient, so he won’t give up any match.

## 입력

#### 입력

The ﬁrst line of the input will contain the number of test cases C(1 ≤ C ≤ 20). C lines will follow, each  
line is for each test case, consisting two numbers steps and n, separated by a single space. steps will be a  
positive integer equals to or less than 100, 000, 000. n will be a positive integer equals to or less than 10.

## 출력

#### 출력

Print one ﬂoating point number for each test case that represents the average number of matches that  
Yeon-Gyoung can win. Your answer should be correct up to an error(relative/absolute) of 1e−7.

## 노트

#### 노트
