# City Driving

문제 ID: `CITYDRIVING`

## 문제

#### 문제

You recently started frequenting San Francisco in your free time and realized that driving in the city is a huge pain. There are only N locations in the city that interest you, though, and you have decided to try to improve your driving experience. Since you lack a GPS and cannot remember too many diﬀerent routes, you wrote down the directions and how long it takes to get between N diﬀerent pairs of locations (the same in both directions), ensuring that using only these paths you can get from any location to any other one.

Now you are planning your trip for the weekend and you need to ﬁgure out the fastest way to get between Q pairs of locations in the city using only the routes you have written down.

## 입력

#### 입력

The input consists of multiple test cases. The ﬁrst line contains a single integer N, 3 ≤ N ≤ 100,000, the number of locations of interest and the number of routes you wrote down. The next N lines each contain three integers u, v, and w (1 ≤ w ≤ 1,000), indicating that you have directions from location u to location v and vice-versa (0-indexed) which take w time. The following line contains a single integer Q, 1 ≤ Q ≤ 10,000, the number of pairs of locations you need to compute the travel time for. The next Q lines each contain two integers u and v, indicating that you should ﬁnd the minimum time to get from location u to location v. The input terminates with a line with N = 0.

## 출력

#### 출력

For each test case, print out Q lines, one for each pair of locations u and v you are ﬁnding the fastest routes for. Each line should simply contain the minimum time it takes to travel from location u to location v.

## 노트

#### 노트
