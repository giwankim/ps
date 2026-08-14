# Wedding Planner

문제 ID: `WEDDING`

## 문제

#### 문제

JongMan (a.k.a JM) asked his girlfriend to marry him. His girlfriend said yes to his proposal, but it is a conditional consent. JongMan earned lots of money, but she wanted to save down expenses for the wedding march. However, JongMan already sorted out all the expenses for the wedding, except for the day of the wedding. So, JongMan plans to reduce expenses by collecting congratulatory money (축의금). JongMan knows who will come to wedding, and how much each of them will pay. Therefore, it's an easy problem to find the day which will maximize the total income from congratulatory money. However, JongMan is too busy, so he passed this problem to his wedding consultant. You are going to help the consultant to find the maximum income.

Each wedding guest's congratulatory money is determined with the following rules:

* Each guest will donate 0.1% of his yearly salary for the congratulatory money.
* All people work from birth until retirement, and everybody retires at the same age, which is fixed.
* Before retirement, one's salary changes with a linear amount every year.
* From the year of retirement, one will receive pension; the annual amount of pension will equal the average of last 5 year's annual salary.

## 입력

#### 입력

The first line of the input contains one integer C, the number of test cases. (1 <= C <= 50)  
T cases follow. For each test cases, the first line consists of two integers N (1 <= N <= 1,000), R (2 <= R <= 1,000,000), the number of wedding guests and the retirement age. And the following N lines each contain 3 integers Ai, Si, Di. Ai (1 <= Ai <= 1,000,000) is the current age of the guest i, and Si (0 <= Si <= 1,000,000) is the current income of the guest. Di (-100,000 <= Di <= 100,000) will be the annual salary change rate of the guest.

For the purpose of this problem, let's assume that everybody lives forever. You can also assume that the given data will have nobody receiving a negative salary during his career.

## 출력

#### 출력

For each case, print the maximum congratulatory money JongMan can get, as a single floating point number rounded to the nearest 0.001. All output numbers must have three digits after the decimal point.

## 노트

#### 노트
