# Planet Hunting

문제 ID: `PLANET`

## 문제

#### 문제

In a fictitious solar system consisting of star S, a planet P, and its moon M, planet P orbits in a perfect circle around star S with a revolution period of exactly T Earth days, and moon M orbits in a perfect circle around planet P with an unknown revolution period. Given the position of moon M relative to star S at three different time points, your goal is to compute the distance of planet P from star S.

To do this, consider a two-dimensional Cartesian coordinate system with its origin centered at star S. You may assume that P’s counterclockwise orbit around S and M’s counterclockwise orbit around P both lie completely within the x−y coordinate plane. Let (x1, y1) denote the position of the moon M on the first observation, let (x2, y2) denote its position k1 Earth days later, and let (x3, y3) denote its position k2 Earth  
days after the second observation.

## 입력

#### 입력

The input test file will contain multiple test cases. Each test case consists of two lines. The first line contains the integers T , k1, and k2, where 1  T, k1, k2  1000. The second line contains six floating-point values x1, y1, x2, y2, x3, and y3. Input points have been selected so as to guarantee a unique solution; the final distance from planet P to star S will always be within 0.1 of the nearest integer. The end-of-file is denoted with a single line containing “0 0 0”.

## 출력

#### 출력

For each input case, the program should print the distance from planet P to star S, rounded to the nearest integer.

## 노트

#### 노트
