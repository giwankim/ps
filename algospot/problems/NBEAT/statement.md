# N-beat

문제 ID: `NBEAT`

## 문제

#### 문제

N-beat is a popular rhythm arcade game, in which you push buttons arranged in a x\*y grid. The buttons light up when you have to push them; you have to push them with precise timing to get scores.

One game of N-beat consists of one or more screens. A **screen** is a configuration of lit buttons which you have to push.

![](assets/117043ac808aacdc-text8657-small.png)

For example, the picture above shows 4 successive **screen**s for a N-beat machine with 4\*4 grid. Here, boxes with the target mark are the lit buttons you have to press.

A sequence of screens in a game is called a **transcription**.

Now, Jaeha Koo, a genius composer, just made a new song for N-beat. You're going to make transcription for this song.

There are B beats in this song, so this transcription will consist of B **screen**s. Therefore, without any restriction, there are a total of (2^(x\*y))^B possible transcriptions.

But **transcription**s differ in difficulty. Generally, you can push one button with one finger, so it's harder when a single screen has more lit buttons. Especially, if a screen has more than 10 lit buttons, it's very difficult to push them successfully. Also, adjacent screens having a lot of lit buttons can also can make the game difficult. For example, two successive screens with 7 and 6 lit buttons each might be more difficult than two screens with 2 and 8 lit buttons each.

As a generous transcriptor, you decided to limit the number of lit buttons. The limit is given as three nonnegative integers: p1, p2, and p3. p1 is the maximum number of lit buttons you can have in any one screen. p2 is the maximum number of lit buttons you can have in any two consecutive screens. Also, as you guessed, p3 is the maximum number of lit buttons you can have in any three consecutive screens. In the picture above, 4 screens each have 4, 8, 6, 8 of turned-on buttons, so this transcription will only be valid if p1 >= 8, p2 >= 14, and p3 >= 22.

Your task is to calculate the number of possible transcriptions given x, y, B, p1, p2 and p3. As the result can be quite huge, just calculate the answer MOD 109+7.

## 입력

#### 입력

The input consists of T test cases. The first line of the input contains T.

Each test case starts with 6 integers x, y (1 <= x, y <= 1000), B (1 <= B <= 10^9), p1 (0 <= p1 <= 10), p2 (0 <= p2 <= 20), p3 (0 <= p3 <= 30) separated by a whitespace.

## 출력

#### 출력

For each test case, print the number of possible transcriptions MOD 109+7, in a single line.

## 노트

#### 노트

* 출제: 일루
* 영문 감수: JongMan
* 그림: Being
