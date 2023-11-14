<table><tr><td> <em>Assignment: </em> IT114 Trivia Milestone 2</td></tr>
<tr><td> <em>Student: </em> Hunter Russo (hjr22)</td></tr>
<tr><td> <em>Generated: </em> 11/13/2023 11:10:36 PM</td></tr>
<tr><td> <em>Grading Link: </em> <a rel="noreferrer noopener" href="https://learn.ethereallab.app/homework/IT114-003-F23/it114-trivia-milestone-2/grade/hjr22" target="_blank">Grading</a></td></tr></table>
<table><tr><td> <em>Instructions: </em> <p>Implement the features from Milestone2 from the proposal document:&nbsp;<a href="https://docs.google.com/document/d/1h2aEWUoZ-etpz1CRl-StaWbZTjkd9BDMq0b6TXK4utI/view">https://docs.google.com/document/d/1h2aEWUoZ-etpz1CRl-StaWbZTjkd9BDMq0b6TXK4utI/view</a></p>
</td></tr></table>
<table><tr><td> <em>Deliverable 1: </em> Payload </td></tr><tr><td><em>Status: </em> <img width="100" height="20" src="https://user-images.githubusercontent.com/54863474/211707773-e6aef7cb-d5b2-4053-bbb1-b09fc609041e.png"></td></tr>
<tr><td><table><tr><td> <em>Sub-Task 1: </em> Payload Screenshots</td></tr>
<tr><td><table><tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fhjr22%2F2023-11-13T20.01.48m2payload1.png.webp?alt=media&token=b85fb8a1-a00e-4dcf-b3cd-db83f106a567"/></td></tr>
<tr><td> <em>Caption:</em> <p>This shows the payload code with the ucid and date with a comments<br>explaining that they are getters and setters<br></p>
</td></tr>
<tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fhjr22%2F2023-11-13T20.02.36m2payload2.png.webp?alt=media&token=e5d742d3-fd3b-416c-a07e-b174c2f36aba"/></td></tr>
<tr><td> <em>Caption:</em> <p>This shows the payload code with the ucid and date with a comments<br>explaining that they are getters and setters. This is just the second part<br>of the code<br></p>
</td></tr>
<tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fhjr22%2F2023-11-13T20.02.55m2payload3.png.webp?alt=media&token=4633f4cd-125f-4b28-b35c-91dc14701e89"/></td></tr>
<tr><td> <em>Caption:</em> <p>This shows the terminal side from the server. <br></p>
</td></tr>
</table></td></tr>
</table></td></tr>
<table><tr><td> <em>Deliverable 2: </em> Game Play Code </td></tr><tr><td><em>Status: </em> <img width="100" height="20" src="https://user-images.githubusercontent.com/54863474/211707773-e6aef7cb-d5b2-4053-bbb1-b09fc609041e.png"></td></tr>
<tr><td><table><tr><td> <em>Sub-Task 1: </em> Show the code related to the question/category choice</td></tr>
<tr><td><table><tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fhjr22%2F2023-11-13T20.26.29m2gameplay1.png.webp?alt=media&token=86715977-b56c-480a-9111-c4770a634ecf"/></td></tr>
<tr><td> <em>Caption:</em> <p>This code creates the category list and a list of all of the<br>questions in each category. It also sets the round duration.<br></p>
</td></tr>
<tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fhjr22%2F2023-11-13T20.27.11m2gameplay2.png.webp?alt=media&token=08b344fa-e72d-44b0-aa52-f3b2964c6413"/></td></tr>
<tr><td> <em>Caption:</em> <p>This code starts a round, gets a random category, and uses that selected<br>category to get a random question from that category. It also has the<br>getRandomCategory method which uses the random method to select a random category<br></p>
</td></tr>
<tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fhjr22%2F2023-11-13T20.27.59m2gameplay3.png.webp?alt=media&token=0fd61c8f-bd10-4fef-b6b4-a3b55a28405c"/></td></tr>
<tr><td> <em>Caption:</em> <p>This code shows that a random question will be pulled from the selected<br>category, depending on what category is set to<br></p>
</td></tr>
<tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fhjr22%2F2023-11-13T20.28.35m2gameplay4.png.webp?alt=media&token=1a2c8125-d1c6-4ce7-8939-8a51f4e0978e"/></td></tr>
<tr><td> <em>Caption:</em> <p>This code shows all of the questions and what the potential answers are<br>and sets correctOption to the right answer.<br></p>
</td></tr>
</table></td></tr>
<tr><td> <em>Sub-Task 2: </em> Show the code related to players picking answers</td></tr>
<tr><td><table><tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fhjr22%2F2023-11-14T01.14.07m2endround1.png.webp?alt=media&token=39cdfa61-1bd0-42a2-b809-0a79704abeac"/></td></tr>
<tr><td> <em>Caption:</em> <p>This is the code that shows teh startRoundTimer method. This also calls the<br>endRound method when the timer of 60 secs runs out. The endRound calls<br>calculateRoundScores to calculate the end of round scores for players. At the end<br>is the calculateRoundScores method which checks the players answers and awards points based<br>on a correct answer.<br></p>
</td></tr>
<tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fhjr22%2F2023-11-14T01.14.39m2timer1.png.webp?alt=media&token=1f0a2ed4-fdab-4fcb-8cf1-7c825d71ee3b"/></td></tr>
<tr><td> <em>Caption:</em> <p>This shows the startRound Timer method as well, but just the timer code.<br></p>
</td></tr>
<tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fhjr22%2F2023-11-14T01.14.58m2picking1.png.webp?alt=media&token=d468ac52-8dd6-40fa-9397-a17abf80cc15"/></td></tr>
<tr><td> <em>Caption:</em> <p>This shows the case PICK: in the proccessMessage method in ServerThread.java. This checks<br>the current room, and checks the players answers. It uses an if statement<br>and displays a message to the player and server based on the players<br>option. <br></p>
</td></tr>
<tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fhjr22%2F2023-11-14T01.15.32m2picking2.png.webp?alt=media&token=e01e72c7-0a4e-40e4-8e4e-52fed9835405"/></td></tr>
<tr><td> <em>Caption:</em> <p>The top left is the server. What the server is showing is that<br>it is granting points when the players guess right and that the server<br>is receiving the that player chose wrong and not granting points. The top<br>right is player 1. Player 1 shows the /pick command to pick an<br>option, which happens to be correct, and the /score command to get his<br>current score. Player 2 and 3 are on a different question because I<br>wanted to show player 1 using the /score command. The bottom left is<br>player 2. Player 2 shows the round end message along with /pick command<br>on the wrong option and the message that comes up when that happens.<br>The bottom right is player 3. Player 3 also used /pick and picked<br>the wrong option, getting the same incorrect answer message and stopping him from<br>picking again.<br></p>
</td></tr>
</table></td></tr>
<tr><td> <em>Sub-Task 3: </em> Show the code related to the timer functionality</td></tr>
<tr><td><table><tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fhjr22%2F2023-11-14T01.37.48m2timer1.png.webp?alt=media&token=981b5333-3dd0-468c-86ed-4b64aec68dff"/></td></tr>
<tr><td> <em>Caption:</em> <p>This again shows the same timer code and the logic for it. I<br>wont go into to much depth again for it.<br></p>
</td></tr>
<tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fhjr22%2F2023-11-14T01.42.37m2timer2.png.webp?alt=media&token=f66f652e-dc60-4b43-95cd-c889ad5e5638"/></td></tr>
<tr><td> <em>Caption:</em> <p>This shows the player not inputting any commands and a new round beginning<br>when the timer expires (after 60 seconds)<br></p>
</td></tr>
<tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fhjr22%2F2023-11-14T01.42.42m2timer3.png.webp?alt=media&token=4ee1a141-a1ae-4479-bb7c-428c424cdd92"/></td></tr>
<tr><td> <em>Caption:</em> <p>This shows the player using /pass and /ready to start a new round,<br>canceling the timer before it expires<br></p>
</td></tr>
</table></td></tr>
<tr><td> <em>Sub-Task 4: </em> Explain the Game flow code at a high level</td></tr>
<tr><td> <em>Response:</em> <p>So first the server and client are set up using the Java and<br>Javac commands, a player selects a name and connects to the localhost via<br>the port using the connect commands. This name they selected is assigned to<br>them as a player and is used when they select answers or when<br>they are given points. The player can use the createroom and joinroom commands<br>to create a room or join an existing room. There are methods that<br>allow players to use commands like /ready to start a trivia round when<br>they enter a room. This sends a READY payload to the server. Once<br>they are ready, a round starts and the room selects a random category<br>and a random question from that array and sends payloads to the server.<br>This allows the server to display the category, question, and potential answers to<br>the question to the players. Players are able to select an answer via<br>the /pick command which sends a PICK payload to the server.&nbsp;When a player<br>picks an answer, the server checks if it&#39;s correct and notifies the client<br>on whether its right or not and updates their score accordingly. The players<br>are able to view their score via the /score command. A timer is<br>also involved, with it being set to 60. This means that each round<br>is 1 minute, and after a minute the end round method is called<br>and a new category and question are selected. The player/client can then use<br>/joinroom to join a different room or /disconnect or /logoff to leave the<br>server.<br></p><br></td></tr>
</table></td></tr>
<table><tr><td> <em>Deliverable 3: </em> Game Evidence </td></tr><tr><td><em>Status: </em> <img width="100" height="20" src="https://user-images.githubusercontent.com/54863474/211707773-e6aef7cb-d5b2-4053-bbb1-b09fc609041e.png"></td></tr>
<tr><td><table><tr><td> <em>Sub-Task 1: </em> Show screenshots of the terminal output of a working game flow</td></tr>
<tr><td><table><tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fhjr22%2F2023-11-14T04.01.34m2categories.png.webp?alt=media&token=55c2b323-1e6d-4fa3-9e6c-47ea37bea67d"/></td></tr>
<tr><td> <em>Caption:</em> <p>This shows the server and client of the running trivia game. The player<br>is shown the random category and random question selected from that category with<br>the appropriate answers. The player is also seen picking an answer. The server<br>is seen giving the player a point for their correct answer<br></p>
</td></tr>
<tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fhjr22%2F2023-11-14T04.04.25m2gameflowtimerexpires.png.webp?alt=media&token=225ce4e5-97f7-4acc-a083-40b3574d42ab"/></td></tr>
<tr><td> <em>Caption:</em> <p>This shows the client side timer expiring without a command from the player.<br></p>
</td></tr>
</table></td></tr>
</table></td></tr>
<table><tr><td> <em>Deliverable 4: </em> Misc </td></tr><tr><td><em>Status: </em> <img width="100" height="20" src="https://user-images.githubusercontent.com/54863474/211707795-a9c94a71-7871-4572-bfae-ad636f8f8474.png"></td></tr>
<tr><td><table><tr><td> <em>Sub-Task 1: </em> Include the pull request for Milestone2 to main</td></tr>
<tr><td>Not provided</td></tr>
</table></td></tr>
<table><tr><td><em>Grading Link: </em><a rel="noreferrer noopener" href="https://learn.ethereallab.app/homework/IT114-003-F23/it114-trivia-milestone-2/grade/hjr22" target="_blank">Grading</a></td></tr></table>