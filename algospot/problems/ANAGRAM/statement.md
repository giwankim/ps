# ANAGRAM

문제 ID: `ANAGRAM`

## 문제

#### 문제

Algospot Inc. is planning to launch a new product iWook on September 2011. iWook, is a diskless desktop computer and will be used by geek CS major students around the world. To classify each device, a unique serial number is assigned.

Wookayin, the tech lead of iWook, decided that the password for a iWook device must use all letters from the device’s serial number exactly once in a case-sensitive way, and **not** in the same order as the original number (\*Obviously, in the real world, it’s a faily bad security practice to enforce such restrictions to passwords.\*)

Algospot Inc. just hired you to write a program to verify passwords entered by users. For a successful launch, develop the perfect verifier ASAP!

## 입력

#### 입력

The input consists of *T* test cases. The number of test cases *T* is given in the first line of the input.

The following *T* lines will each contain two strings, the first of which being the serial number of the device, and the second being a password entered by a user for the device.

Both strings will only contain alphanumeric characters, and will not exceed 100 characters in length.

## 출력

#### 출력

For each test case, print a single line. If the given password is valid for the device, print **“Yes”** (quotes are for clarity). Otherwise, print **“No.”**.

## 노트

#### 노트
