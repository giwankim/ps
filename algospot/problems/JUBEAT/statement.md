# Jubeat

문제 ID: `JUBEAT`

## 문제

#### 문제

Recently, Nana is spending a lot of time playing “Jubeat”, the latest arcade music game from Konami. The game consists of a 4x4 grid. When a note pops up in some of the cells, the player must push that cell following the rhythm of the music.

For each note pushed, the player is awarded points. If a song contains N notes, a player will receive 900,000/N points if the timing of the push is perfect. For notes where the timing was less perfect, she can receive either 70%, 30%, or 0% of this score. After the song is over, the sum of all the points will be rounded down to the nearest integer.

Nana just played a song and received a score of S: however he doesn’t know the number of notes N in the song. However, he suspects N lies in an interval [a, b]. Write a program that calculates the sum of all possible Ns in that range.

## 입력

#### 입력

The input consists of T test cases. The number of test cases T is given in the first line of the input.

Each test case consists of a single line with three integers: S (0 <= S <= 900,000), a and b (1 <= a <= b <= 10^9).

## 출력

#### 출력

Print exactly one line for each test case. The line should contain an integer indicating the sum of all possible N in the given range.

## 노트

#### 노트
