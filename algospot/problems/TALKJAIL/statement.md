# Talk jail

문제 ID: `TALKJAIL`

## 문제

#### 문제

> How have you been?
>
> I want to play a game.
>
> This game will be played on a smartphone, on which you spend most of your time.
>
> On the 'Talk.'
>
> Are you happy because you got unlimited 3G?
>
> Tonight, we shall see how far you go in order not to turn your phone off.
>
> Will you get a push or turn it off?
>
> Your choice.
>
> -Jigsaw

Jigsaw invited N people to the Talk jail room. Here is a brief description of how Talk room works. Everyone can send messages, and all messages are delivered to everyone in the room, including the sender. However, a new message arrived on someone's smartphone is not displayed immediately if the chat room window is not open. Instead, it is kept in the **unread** status. As soon as one **opens** the Talk jail room window, the status of all messages that have been arrived so far changes to the **read** status. Also, while the chat room window is open, newly arrived messages will be displayed and marked as read immediately. One can close the chat room window, but the messages will still arrive. One can send a message only when he/she has the chat room window open.

Moreover, when one opens the chat room window, one can see 3 pieces of information on each message: the sent time, the sender, and the number of people who have not read the message yet.

This simple chatting service can actually serve as a jail as follows. When a lot of people send messages in the Talk jail room, everyone's phone will keep beeping continuously, which makes it impossible for him/her to manage ordinary life. Even if someone tries to leave the room, Jigsaw invites him/her back immediately: so it is impossible to leave the room. As a result, people will complain in the room which yields more messages and more beeps..

Jigsaw will never send any message to the room but will keep inviting leavers back to the room. Being so frustrated, people eventually choose one or the other: (A) just suck it up and live with it or (B) ignore the smartphone entirely and live free.

Jigsaw just opened the chat room window and checked out the M messages that have been sent ever since the room was created. Based on this, he can infer that certain people have not read certain messages with certainty. That is, for each message, he may be able to infer whether someone has read the message or not (sometimes, such inference may not be possible). He calls those who have not read the message for sure, "the wretched addict."

For each message, your job is to figure out how many people Jigsaw can identify as "the wretched addicts" because you can infer that they have not read the message.

## 입력

#### 입력

The input consists of T test cases. The first line of the input contains T.

Each test case starts with two integers N (1 <= N <= 60,000) and M (1 <= M <= 60,000), separated by a whitespace. The following M lines contain three integers each: ti, pi, and ci where ti (0 <= ti < 231) denotes the sent time, pi (1 <= pi <= N) denotes the sender, and ci (0 <= ci < N) denotes the number of people who have not read the message i. No two messages have the same value of ti. The input is consistent and valid.

## 출력

#### 출력

For each test case, you must output #(Test case number) in the first line.  
You must output the number of people who are certain that have not read the i-th message yet in a separate line.

## 노트

#### 노트

Here comes the explanation of the first test case.

Everybody read the first message (from the input data). Hence, the answer is 0.

There is one person who has not read the second message yet. Person 2 must have read the message (as he sent it). Either Person 1 or 3 could have read the message, and Jigsaw cannot identify exactly who has not read it. The answer is 0.

The third message is again read by Person 2. However, both Person 1 and Person 3 have not read it yet, and thus Jigsaw can be certain that those two have not read. The answer is 2.
