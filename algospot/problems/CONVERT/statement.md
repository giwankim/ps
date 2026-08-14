# Conversions

문제 ID: `CONVERT`

## 문제

#### 문제

Conversion between the metric and English measurement systems is relatively simple. Often, it  
involves either multiplying or dividing by a constant. You must write a program that converts between the following units:

| Type | Metric | English equivalent |
| --- | --- | --- |
| Weight | 1.000 kilogram | 2.2046 pounds |
|  | 0.4536 kilograms | 1.0000 pound |
| Volume | 1.000 liter | 0.2642 gallons |
|  | 3.7854 liters | 1.000 gallon |

## 입력

#### 입력

The first line of input contains a single integer N, (1 ≤ N ≤ 1000) which is the number of datasets that follow.  
Each dataset consists of a single line of input containing a floating point (double precision) number, a space and the unit specification for the measurement to be converted. The unit specification is one of kg, lb, l, or g referring to kilograms, pounds, liters and gallons respectively.

## 출력

#### 출력

For each dataset, you should generate one line of output with the following values: The dataset  
number as a decimal integer (start counting at one), a space, and the appropriately converted value rounded to 4 decimal places, a space and the unit specification for the converted value.

## 노트

#### 노트
