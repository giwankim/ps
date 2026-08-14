# URI Decoding

문제 ID: `URI`

## 문제

#### 문제

*URI* (Uniform Resource Identifier) is a compact string used to identify or name resources on the Internet. Some examples of *URI* are below:

* `http://icpc.baylor.edu.cn/`
* `mailto:foo@bar.org`
* `ftp://127.0.0.1/pub/linux`
* `readme.txt`

When transmitting \*URI\*s through the Internet, we escape some special characters in \*URI\*s with percent-encoding. Percent-encoding encodes an ASCII character into a percent sign ("`%`") followed by a two-digit Hexadecimal representation of the ASCII number. The other characters are not touched in the encoding process. The following table shows the special characters and their corresponding encodings:

| Special Character | Encoded String |
| --- | --- |
| " " | `%20` |
| "`!`" | `%21` |
| "`$`" | `%24` |
| "`%`" | `%25` |
| "`(`" | `%28` |
| "`)`" | `%29` |
| "`*`" | `%2a` |

Note that the quotes are for clarity only.

Write a program which reverses this process.

## 입력

#### 입력

The first line of the input file will contain the number of test cases $C$ $(1 \le C \le 100)$. The following $C$ lines will each contain a test case — which is the percent-encoded *URI*. Their length will be at most 80.

## 출력

#### 출력

Print one line for each test cases — the decoded original *URI*.

## 노트

#### 노트
