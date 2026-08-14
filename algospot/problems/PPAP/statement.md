# PPAP

문제 ID: `PPAP`

## 문제

#### 문제

![](assets/f106992ebac4386f-ppap.png)  
*I have a pen*  
*I have an apple*  
*Apple pen!*

*I have a pen*  
*I have a pineapple*  
*Pineapple pen!*

*Apple pen,*  
*Pineapple pen,*  
*pen-pineapple-apple-pen*

---

Piko just can't stop singing along to PPAP! PPAP is an extremely addictive song which became an internet meme. Piko is especially obsessed with the song's chorus part, which just repeats the words "pen-pineapple-apple-pen". One afternoon, Piko started repeatedly singing PPAP's chorus part, while writing down the lyrics in a notebook as he sang. Piko delimited words with a dash(-) and each chorus part with a forward slash(/). The first 30 characters Piko has written down can be seen below:

| Index | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10 | 11 | 12 | 13 | 14 | 15 |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
|  | p | e | n | - | p | i | n | e | a | p | p | l | e | - | a |

| Index | 16 | 17 | 18 | 19 | 20 | 21 | 22 | 23 | 24 | 25 | 26 | 27 | 28 | 29 | 30 | ... |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
|  | p | p | l | e | - | p | e | n | / | p | e | n | - | p | i | ... |

The song was so addictive, Piko spent the entire afternoon writing down the lyrics! While Piko has gone to the bathroom, his friend Taro came over and discovered Piko’s notebook. He started wondering the following: how many times does a given pattern $P$ appear as a substring of Piko’s string, within a given range?

## 입력

#### 입력

Your program needs to solve the problem for a single test case. A test case is given in a single line as two integers $L$, $R$ and a string $P$, separated by a single space. ($1 \le L \le R \le 1\,000\,000$). $L$ and $R$ respectively represents the index of the first and the last character of the range Taro is interested in. (The first character of the string has index $1$.) $P$ is a string that only consists of lowercase alphabet letters, dashes, and forward slash, and its length will be between $1$ and $10\,000$, inclusive.

## 출력

#### 출력

Print the number of times $P$ appears as a substring of Pico's string, starting on or after index $L$ and ending on or before index $R$.

## 노트

#### 노트

예제 입출력에서 Example로 시작하는 줄은 실제 입출력에 포함되지 않습니다. 각각을 하나의 입출력 세트로 읽어주세요.
