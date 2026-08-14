# Mismatched Parenthesis

문제 ID: `FIXPAREN`

## 문제

#### 문제

You are working on a sequence of parentheses. There are four types of parentheses and the symbols used are ( ), { }, [ ], < >. We call '(', '{', '[', and '<' the left parentheses and ')', '}', ']', and '>' the right parentheses.

The original sequence was a perfectly matched sequence. That is, every left parenthesis was matched to a right parenthesis of the same type (e.g. '(' and ')'). But by a mistake, some of the left parentheses are incorrectly transcribed to left parentheses of different type(s), and so are some of the right parentheses.

You want to modify the sequence such that the resulting sequence is a perfectly matched sequence, by correcting a mismatched pair of a left parenthesis and a right parenthesis into a matched pair of parentheses of the same type. However, the problem complicates as there is a rule you must follow: to correct a mismatched pair, you should change exactly one of the two parentheses. Also, you can only replace a lower priority parenthesis with a higher priority parenthesis -- not the other way. Suppose that the type '{ }' has higher priority than the type '( )'. Then you must correct the mismatched pair '( }' to '{ }', but not to '( )'.

Given the mistranscribed seqeunce of parentheses and an ordering of priority of different types, write a program that fixes the given sequence. You are to find all of the mismatched pairs, and for each pair, change the parenthesis of lower-priority type to a higher-priority type.

## 입력

#### 입력

The input consists of T test cases. T will be given in the first line of the input file, and T test cases will follow.

Each test case will be given by two strings in a single line. The first string is the sequence of transcribed parentheses, and the second string denotes the priority order of parentheses types. Each character of the first string(sequence of parentheses) will be one of the eight symbols: '(', ')', '{', '}', '[', ']', '<', '>'. And for the priority order, left parenthesis symbols will be given in the order of highest to lowest.

Strings will not contain any whitespaces, and two strings will be separated by a single space. The length of the first string will not exceed 100, and the length of the second string will be always 4.

Note that you started with a perfectly matched sequence and that no left parentheses are wrongly transcribed to right parentheses, and vice versa. For example, '(}', '()([)]', and '([}{])' are valid mis-transcribed sequences, but '(}}', '()(([)]', '([}]{])' are not. You can assume that input will consist only of valid sequences.

## 출력

#### 출력

For each of the test case, print the resulting sequence in a single line.

## 노트

#### 노트

For the second test case, there are two mismatched pairs: ( > and [ ). The priority order of the parentheses, from highest to lowest, is < >, ( ), { }, and [ ]. The first mismatched pair ( > should be modified to < >, as < > has higher priority than ( ). And the second mismatched pair [ ) should be modified to ( ), as ( ) has higher priority than [ ]. Thus the result should be ()<()>.
