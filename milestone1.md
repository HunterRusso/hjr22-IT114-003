<table><tr><td> <em>Assignment: </em> It114 Milestone1</td></tr>
<tr><td> <em>Student: </em> Hunter Russo (hjr22)</td></tr>
<tr><td> <em>Generated: </em> 10/12/2023 5:02:23 PM</td></tr>
<tr><td> <em>Grading Link: </em> <a rel="noreferrer noopener" href="https://learn.ethereallab.app/homework/IT114-003-F23/it114-milestone1/grade/hjr22" target="_blank">Grading</a></td></tr></table>
<table><tr><td> <em>Instructions: </em> <ol><li>Create a new branch called Milestone1</li><li>At the root of your repository create a folder called Project if one doesn't exist yet</li><ol><li>You will be updating this folder with new code as you do milestones</li><li>You won't be creating separate folders for milestones; milestones are just branches</li></ol><li>Create a milestone1.md file inside the Project folder</li><li>Git add/commit/push it to Github (yes it'll be blank for now)</li><li>Create a pull request from Milestone1 to main (don't complete/merge it yet, just have it in open status)</li><li>Copy in the latest Socket sample code from the most recent Socket Part example of the lessons</li><ol><li>Recommended Part 5 (clients should be having names at this point and not ids)</li><li><a href="https://github.com/MattToegel/IT114/tree/Module5/Module5">https://github.com/MattToegel/IT114/tree/Module5/Module5</a>&nbsp;<br></li></ol><li>Fix the package references at the top of each file (these are the only edits you should do at this point)</li><li>Git add/commit the baseline</li><li>Ensure the sample is working and fill in the below deliverables</li><ol><li>Note: The client commands likely are different in part 5 with the /name and /connect options instead of just connect</li></ol><li>Get the markdown content or the file and paste it into the milestone1.md file or replace the file with the downloaded version</li><li>Git add/commit/push all changes</li><li>Complete the pull request merge from step 5</li><li>Locally checkout main</li><li>git pull origin main</li></ol></td></tr></table>
<table><tr><td> <em>Deliverable 1: </em> Startup </td></tr><tr><td><em>Status: </em> <img width="100" height="20" src="https://user-images.githubusercontent.com/54863474/211707773-e6aef7cb-d5b2-4053-bbb1-b09fc609041e.png"></td></tr>
<tr><td><table><tr><td> <em>Sub-Task 1: </em> Add screenshot showing your server being started and running</td></tr>
<tr><td><table><tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fhjr22%2F2023-10-12T20.13.17milestone1.1.png.webp?alt=media&token=da3e91cf-8cbb-4dfd-af23-1d9f4268a35b"/></td></tr>
<tr><td> <em>Caption:</em> <p>You can see that the server is waiting for the input and successfully<br>accepted a name. <br></p>
</td></tr>
</table></td></tr>
<tr><td> <em>Sub-Task 2: </em> Add screenshot showing your client being started and running</td></tr>
<tr><td><table><tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fhjr22%2F2023-10-12T20.14.50milestone1.2.png.webp?alt=media&token=f179c2d5-d5e9-49c2-85b5-671601734491"/></td></tr>
<tr><td> <em>Caption:</em> <p>It shows the client waiting for an input and shows the clients connected<br>to a server successfully.<br></p>
</td></tr>
</table></td></tr>
<tr><td> <em>Sub-Task 3: </em> Briefly explain the connection process</td></tr>
<tr><td> <em>Response:</em> <p>So the server side is set up with a local host for the<br>clients to connect to. The clients are set up a name for themselves<br>to be identified and connect to the server via the designated port. When<br>this is all established, the server will wait for the clients to request<br>its service.<br></p><br></td></tr>
</table></td></tr>
<table><tr><td> <em>Deliverable 2: </em> Sending/Receiving </td></tr><tr><td><em>Status: </em> <img width="100" height="20" src="https://user-images.githubusercontent.com/54863474/211707773-e6aef7cb-d5b2-4053-bbb1-b09fc609041e.png"></td></tr>
<tr><td><table><tr><td> <em>Sub-Task 1: </em> Add screenshot(s) showing evidence related to the checklist</td></tr>
<tr><td><table><tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fhjr22%2F2023-10-12T20.43.37milestone1.3.png.webp?alt=media&token=2ba87bd0-cd43-4ce5-8c08-8a2e853e8cb8"/></td></tr>
<tr><td> <em>Caption:</em> <p>The screenshot shows two servers and two clients. Each server shows the messages<br>and connection for 3005 and 3004 respectively. The two clients (Hunter and Joe)<br>join 3005 and send messages to each other which they are both able<br>to see. Joe connects to 3004 and sends a message which Hunter (Still<br>in 3005) can not see.<br></p>
</td></tr>
</table></td></tr>
<tr><td> <em>Sub-Task 2: </em> Briefly explain how the messages are sent, broadcasted (sent to all connected clients), and received</td></tr>
<tr><td> <em>Response:</em> <p>Messages are sent by the server to all of the connected clients in<br>its own room that was set up. The clients connect to a room<br>and send messages to everyone else who also connected to the same room.<br>The Serverthread basically prints out everything happening in the server for all that<br>are connected to see. The room is set up and clients in the<br>server are able to join. Clients are able to receive messages from the<br>sever about things happening or messages from clients in the room/server.<br></p><br></td></tr>
</table></td></tr>
<table><tr><td> <em>Deliverable 3: </em> Disconnecting / Terminating </td></tr><tr><td><em>Status: </em> <img width="100" height="20" src="https://user-images.githubusercontent.com/54863474/211707773-e6aef7cb-d5b2-4053-bbb1-b09fc609041e.png"></td></tr>
<tr><td><table><tr><td> <em>Sub-Task 1: </em> Add screenshot(s) showing evidence related to the checklist</td></tr>
<tr><td><table><tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fhjr22%2F2023-10-12T20.49.37milestone1.4.png.webp?alt=media&token=4c3bae21-2c9e-4db7-a991-1397f57d8d77"/></td></tr>
<tr><td> <em>Caption:</em> <p>One of the clients disconnects from the sever. The Serverthreads displays the messages<br>but the server remains up. A new client joins the same server and<br>shows that its still running.<br></p>
</td></tr>
</table></td></tr>
<tr><td> <em>Sub-Task 2: </em> Briefly explain how the various disconnects/terminations are handled</td></tr>
<tr><td> <em>Response:</em> <p>After a client inputs a specific command, they are booted from the server.<br>The client calls the close method which sends a termination request. The server<br>doesn&#39;t crash because of the termination request that was sent. The server will<br>remain running until another client joins in which the server will be active<br>again.<br></p><br></td></tr>
</table></td></tr>
<table><tr><td> <em>Deliverable 4: </em> Misc </td></tr><tr><td><em>Status: </em> <img width="100" height="20" src="https://user-images.githubusercontent.com/54863474/211707795-a9c94a71-7871-4572-bfae-ad636f8f8474.png"></td></tr>
<tr><td><table><tr><td> <em>Sub-Task 1: </em> Add the pull request for this branch</td></tr>
<tr><td>Not provided</td></tr>
<tr><td> <em>Sub-Task 2: </em> Talk about any issues or learnings during this assignment</td></tr>
<tr><td> <em>Response:</em> <p>(missing)</p><br></td></tr>
</table></td></tr>
<table><tr><td><em>Grading Link: </em><a rel="noreferrer noopener" href="https://learn.ethereallab.app/homework/IT114-003-F23/it114-milestone1/grade/hjr22" target="_blank">Grading</a></td></tr></table>