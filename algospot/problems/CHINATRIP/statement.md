# China Trip

문제 ID: `CHINATRIP`

## 문제

#### 문제

Preo, who is busy with preparing for GRE, plans to travel in China for a week with Taeyoon. He plans to go from Beijing to Chengdu, but not sure about which cities they should visit whlie they travel from Beijing To Chengdu. Preo has made a list of candidate cities that he wants to visit, and told Taeyoon:

“I have compiled this list of my preferred cities, but I’m too busy to make the actual schedule. Would you choose the list of cities we should visit? However, please try to minimize the total distance we travel; Chengdu is quite far from Beijing.”

As a former ACM-ICPC World Finalist, it’s actually an easy job for Taeyoon to find such a shortest route. However, in addition to finding the shortest route, Taeyoon wants to visit his favorite cities as early as possible. Taeyoon marked each city with a rank denoting his preference; the smaller a city’s rank is, the more Taeyoon likes to visit the city.

Write a program to find such a route. The route should be one of the shortest routes beginning at Beijing and ending at Chengdu, and it should visit the cities in lexicographically smallest order.

## 입력

#### 입력

Your program is to read from standard input. The input consists of T test cases. The number of test cases T is given in the first line of the input. The first line of each test case contains a natural number N denoting the number of cities and a natural number M denoting the number of roads. The following M lines contain information on the roads in the form of ’abd’. a and b represent the cities and d is the distance between two cities. They can travel both from a to b and from b to a (bidirectional), and there will be at most 1 road given in the input data for any pair of cities. City 1 is Beijing and City N is Chengdu. Other than these two cities, Taeyoon wants to visit smaller numbered cities first. N is between 2 and 1, 000. M is between 1 and  
N(N − 1)/2, inclusive. a, b are between 1 and N. d is between 1 and 10, inclusive.

## 출력

#### 출력

For each test case, the first line should contain a single number representing the distance Pero  
and Taeyoon will travel. The following line should contain the list of cities in order of their visit.  
There must be 1 whitespace between two numbers.

Hint
----

In the first sample test case, the shortest path from Beijing to Chengdu is 6. There are two ways to go from Beijing to Chengdu:

```
1 − 3 − 4 − 5
1 − 3 − 5
```

Since Taeyoon prefers visiting smaller numbered city first, the first path is the desired answer. In the second test case, the shortest path is 9, and the only way to do so is 1 − 4 − 3 − 5.

## 노트

#### 노트
