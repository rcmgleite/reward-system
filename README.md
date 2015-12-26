# reward-system

## 1 - Problem

A company, called Acme Inc, is planning a way to reward customers for inviting their friends. They're planning a reward system that will
give customer points for each invitation that he or anyone that was invited by someone that he invited or anyone that was invited by someone that was invited by someone he
invited. But of course, this value decreases exponentially: he gets (1/2)^k where k is the level of the invitation: level 0 (people he invited) he gets 1 point, level 1 (people invited by a person he invited) he gets
1/2 points and so on. Only the first invitation counts: so if someone gets invited by someone that was already invited, it doesn't award any points.
Also, to count as a valid invitation, the invited customer must have invited someone (so customers that didn't invite anyone don't count as points for the customer that invited them)

So, given the input:
1 2
1 3 
3 4
2 4
4 5
4 6

The score is:
1 - 2.5 (2 because he invited 2 and 3 plus 0.5 as 3 invited 4)
3 - 1 (1 as 3 invited 4 and 4 invited someone)
2 - 0 (even as 2 invited 4, it doesn't count as 4 was invited before by 3)
4 - 0 (invited 5 and 6, but 5 and 6 didn't invite anyone)

Note that 2 invited 4, but, since 3 invited 4 first, customer 3 gets the points.

Write a program that receives a text file with the input and exposes the ranking on a JSON format on a HTTP endpoint. Also, add another endpoint to add a new invitation.

You should deliver a git repository, or a link to a shared private repository on github, bitbucket or similar, with your code and a short README file outlining the solution and explaining how to build and run the code. You should deliver your code in a functional programming language — Clojure, Common Lisp, Scheme, Haskell, ML, F# and Scala are acceptable — and we'll analyse the structure and readability of the code-base.  We expect production-grade code. There is no problem in using libraries, for instance for testing or network interaction, but please avoid using a library that already implements tree algorithms.

## 2 - Solution
The solution was based on the fact that each subtree of each child of a node contribute to it's score proportionally
to the height of that subtree.

If a child subtree has height 1, it's contribution to it's parent score is: <br>
-  (1/2)⁰ <br>
If a child subtree has height 2, it's contribuition to it's parent score is: <br>
-  (1/2)⁰ + (1/2)¹ <br>
and so on... <br>

Given the tree: <br>
![alt tag](http://lcm.csa.iisc.ernet.in/dsa/img151.gif)

### 2.1 - Example 1: Score of node 1.
1. The subtree with root on 2 has height 2.
2. The subtree with root on 3 has height  1.

Using 1 and 2, node's 1 score is:
-  ((1/2)⁰ + (1/2)¹) + ((1/2)⁰) = 2.5

### 2.2 - Example 2: Score of node 2.
3. The subtree with root on node 4 has height 1
4. The subtree with root on node 5 has height 0

Using 3 and 4, node's 2 score is:
-  (1/2)⁰ + 0 = 1.0 <br>

The datastructure used to develop the solution was a tree. <br>
The implementation uses a map to represent the tree: <br>
-  key: index(name) of the node (given on input.txt)
-  value: another map that has: the subtree-height, the node parent and a vector with it's children.

### 2.3 - Node insertion
Nodes can be added to the tree on invite insertion. <br>
When each node is inserted, the application update the height of all nodes on its subtree recursively. <br>

### 2.4 - Invite insertion
The invite insertion function has the following constraints: <br>
1. The inviter must have been invited before inviting someone <br>
2. Inviter and invited must be numbers <br>
Also, the endpoint for inserting an invite execute the insertion asynchronously.
The endpoint verifies the input and creates a new thread to execute the insertion.

### 2.5 - score 
To calculate the score of a node, the application just find all the children of the given node and calculates each subtree
contribution to the score based on it's height. As the height was already computed when the node was inserted, the score becomes much cheaper than if it had to traverse all subtree-trees. <br>
To be even cheaper, once the score is calculated, it's cached on an atom. <br>
Every time a new invitation is made, the score atom is cleared. The function that calculates the score verifies if the score atom is empty.
If it is, the calculation is executed. If it's not, the cached value is returned.


## 3 - How to run
-  To start the application, execute: <br>
   `$ lein run-dev` <br>
or <br>
   `$ lein run` <br>

Any of those commands will start the server and already seed the application with the invites located on 'input.txt' on
project root.

## 4 - How to test
-  To execute all tests, execute: <br>
   `$ lein test` <br>

## 5 - Endpoints

### 5.1 Discovery:
-	Method: GET
-	Path: /

### 5.2 Retreive score:
-  Method: GET
-  Path: /api/score

### 5.3 Insert invite
- Method: POST
- Path: /api/invite
- Content-Type: application/json
- Body: {inviter: x, invited: y}
