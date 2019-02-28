CSE 5344 Computer Networks Project1
Student ID: 1001661458
---------------------------------------------------------------------
*Instruction to Run the Code.

1.Open the Webserver.java & WebClient.java in Eclipse.

2.Compile&Run the Webserver.java
	Rightclick on the project -> 'Run As'-> 'Java Application'
	Server starts, creates the socket and waits for the Client.
3.Compile&Run the WebClient.java multiple times
	Rightclick on the project -> 'Run As'-> 'Java Application'
	Client starts, requests & sets connection to server.
	Soon after that, send REQUESTLINE(GET /Sample.html HTTP/1.1)to the server
4.Check received.txt in the same directory
	Server process the request and send byte information of Sample.html.
	Client catch the information and clone the information to received.html.
	If you wait for a while, you will see Threads close after succesive threads in server window, 
	which means WebServer.java is working as multithreaded server.

**IPAddress is set to 'localhost', Port number is set to '6788' in the entire Project.

--------------------------------------------------------------------
*Reference & Citations:

* Programming Assignment 1_reference_Java.pdf; Reference handsout from BlackBoard
* Sample.html; http://help.websiteos.com/websiteos/example_of_a_simple_html_page.htm

