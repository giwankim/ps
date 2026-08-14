# Turn off the Lights

문제 ID: `TURNOFFTHELIGHTS`

## 문제

#### 문제

Eunjin has a rectangular-shaped board of light bulbs. It is consisted of R rows and C columns, therefore there are RC bulbs in total. A light bulb can have either of two states: turned on (often denoted by “1”) or turned off (often denoted by “0”).

Wonha, a mischievous boyfriend, messed up her light board by turning on some bulbs. The other bulbs are currently turned off. After Eunjin noticed his mischief, she decided to turn all the lights off using minimum number of operations. The only operation she can do is to reverse the state of a consecutive sequence of  
light bulbs in a row or column.

You must write a program that computes the smallest possible number of operations needed for her, to make all the bulbs turned off. It is allowed that some light bulbs to be turned on during the whole process, but after a sequence of appropriate operations, all the bulbs must be turned off.

## 입력

#### 입력

The input consists of T test cases. The number of test cases T is given in the first line of the input.

The first line of each test cases contains two integers R and C, which denotes the number of rows and the number of columns, respectively, in the light board. Each of the following R rows contains a string of C digits “1” or “0”. Each digit denotes the initial state of each light bulb after Wonha changed the state of some light bulbs: “1” means the bulb is turned on, and “0” means the bulb is turned off. You may assume that both R and C are not greater than 15.

## 출력

#### 출력

Print exactly one line for each test case. The line should contain an integer indicating the smallest number of operations needed.

## 노트

#### 노트
