# 알고스팟 디자인 스쿨

문제 ID: `DESIGNSCHOOL`

## 문제

#### 문제

"Woojoo! Where have you been?"  
"What's the matter, teacher?"  
"I told you that the admissions information on Algospot Design School was out, and that you needed to prepare a portfolio for that. Did you finish drawing it yet?"  
"Yeah, sure. All done, perfectly."  
"Really? Let me take a look at it."  
"I forgot to bring it today. I can show you tomorrow."  
"Are you sure? Don't forget!"

After talking to the teacher, Woojoo rushed to meet her best friend, Bada.

"Hey, Bada! I'm screwed!"  
"What's up? Why?"  
"I forgot to prepare a portfolio for Algospot Design School. Do you have any extra one that I can borrow?"  
"Extra ones... Oh, I have one that I drew for the exhibition the other day. Wait a second..."

Bada found her sketchbook and gave it to Woojoo. The book had a lot of drawings, on both front and back of the pages.

"That's a gift for you. You can use however you would like."  
"Really? Is that OK?"  
"It's my pleasure."

![그림](assets/6d3d3376078600f8-DesignSchool.png)

On the next day, Woojoo brought the sketchbook to the teacher.

"Hmm... the drawings look good, but why did you on earth draw so many things on a single page? Worse yet, you have drawings on the back, too. I think you would need to cut the drawings out. Try to cut them so that you will be left with the most drawings."

Here comes your task:

We place a single, square page from the sketchbook on a 2D plane, with two opposite corners at (0,0) and (100,100). When you want to cut out a piece of drawing from the page, you have to cut it along the smallest enclosing axis-aligned (i.e. borders are parallel to either X or Y axis) rectangle. You are given coordinates of these rectangles, and which side the drawing is on. Find the maximum number of pieces that can be cut out from the page. Please note that a single piece can only count for up to 1 drawing even if it contains other drawings on the other side.

## 입력

#### 입력

The input consists of T test cases, each representing a sheet of the sketchbook. The first line of the input contains T.

Each test case describes the drawings on the sheet.   
The first line of the test case contains the number of drawings on both sides of the sheet, N (1 <= N <= 100).

The following N lines describe the N drawings: the coordinates of the bottom-left corner, the coordinates of the upper-right corner, and its side (F for front, B for back). The coordinates will be given as four integers, and each token will be separated by a single space. You can assume that no two drawings on the same page will overlap with each other.

## 출력

#### 출력

For each sheet, output the maximum number of drawings you can cut-out of the sheet.

## 노트

#### 노트

* 출제: Taeyoon\_Lee
* 번역: ltdtl
* 번역 감수: JongMan
* 검수: Being
* 일러스트: rainpunch
