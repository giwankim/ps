# Dart

문제 ID: `DART`

## 문제

#### 문제

Nana was given a beautiful dart table as a surprise present from friends. Front side of it was a standard dart table, and back side was a target, consisting of circles each having score associated with the circle. In addition to the circles originally drawn in the target, Nana’s friends drew more circles and wrote sentences they wanted him to read in the circles and score behind each circle.

After enjoying the table for some time, Nana started to think about expected score he could get when he throws a dart on the table. He had an idea, and he wanted to verify whether his idea was valid.

Given circles in a big circle representing the whole table, calculate the expected score for each dart Nana throws. Every point of the table will have equal probability to be hit by a dart, and no dart will miss the big circle. Also, if a dart hits a place where two or more circles overlap, only the highest score will be given to the throw.

## 입력

#### 입력

Your program is to read from standard input. The input consists of T(<= 100) test cases. The number of test cases T is given in the first line of the input. In the first line of each test case, information of a whole table will be given as 3 integers, center of the circle, x (−10000 <= x <= 10000), y (−10000 <= y <=  10000), and radius r (1 <= r <= 10000). In the next line, number of  
inner circles, an integer m (1 <= m <= 100), will be given. In each of following m lines, information of each inner circle will be give as 3 integers, center of the circle, x (−10000 <= x <= 10000), y(−10000 <= y <= 10000), radius r (1 <= r <= 10000), and s (1 <= s <= 10000), score associated with the circle. Every inner circle is guaranteed to be inside or to touch internally the big circle.

## 출력

#### 출력

Your program is to write to standard output. Print exactly one number for each test case, representing the expected score for a dart thrown to the target. Your result should be accurate to within a relative or absolute value of 10^-7.

## 노트

#### 노트
