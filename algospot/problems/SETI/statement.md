# SETI@ICPC

문제 ID: `SETI`

## 문제

#### 문제

SETI (Search for Extra-Terrestrial Intelligence) is an effort driven by NASA, to analyze radio signal data from the space and see if they are messages from aliens. We plan to help NASA by analyzing some radio signals at ICPC, so NASA sent us a set of signal records that are suspected to be alien messages. These signal records are represented by strings consisting of alphabet letters `a` to `d`.

According to our newest theory, all the messages from aliens are composed by concatenating even-length palindromes. For example, `bbaabaab` is a message from aliens since it can be decomposed into three even-length palindromes: `bb`, `aa`, and `baab`. The whole message can be composed of one palindrome too, like in `abccba`. However, strings like `ababba` which cannot be decomposed into even-length palindromes are not from aliens; they are treated as noises.

The data we got from NASA are encrypted by a simple encoding scheme. The encoding scheme uses a password, which is one-to-four digit number, each digit being `0` to `3`.

The encryption process is described below:

1. The original text is lined up with the password, repeating the password if the text is longer.
2. Each letter in the original text is incremented by the digit written below: for example, if '`a`' is incremented by 1, it will become '`b`'.
3. If the incremented letter is after '`d`', they will wrap around to '`a`'. For example, if '`c`' is incremented by 2, it will become '`a`' instead of '`e`'.

```
    a b b a d a a d
    0 2 3 0 2 3 0 2
    ---------------
    a d a a b d a b
```

The above depicts the encryption process, with original text '`abbadaad`' and password `023`.

Unfortunately, the password was lost during the transmissions so we are left only with encrypted data.

Write a program that given an encrypted text, determine if there can be a password which makes the decrypted text an alien message.

## 입력

#### 입력

The first line of the input will contain the number of test cases $C$. $C$ lines follow, which will contain the encrypted text. The length of each encrypted string is equal to, or less than $1000$.

## 출력

#### 출력

Print one line for each test case: print '`Alien!`' if there is a password which makes the original text an alien message, and print '`noise`' otherwise. (Quotes are for clarity)

## 노트

#### 노트
