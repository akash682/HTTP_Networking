//UTA ID:1001661458
//Name: Akash Lohani

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

public class WebServer {
    //Set the port number
    public final static int port = 6788;

    public static void main(String[] args) {
        try {
            //Establish the listen socket.
            ServerSocket serverSocket = new ServerSocket(port);
            Socket clientsocket = new Socket();

            //Process HTTP service requests in an infinite loop
            while (true) {
                //Listen for a TCP connection request.
                System.out.println("Server Ready for Connection. Waiting for Client...");

                //Accept the connection
                clientsocket = serverSocket.accept();
                System.out.println("Connection Succesful.Client accepted.");

                //Construct and object to process the HTTP request
                HttpRequest request = new HttpRequest(clientsocket);

                //Create a new thread to process the request
                Thread thread = new Thread(request);

                //Start Thread.
                thread.start();
            }

        } catch (Exception e) {
            System.out.println("Server Shutdown Unexpectedly.");
            e.printStackTrace();
        }

    }
}

final class HttpRequest implements Runnable {
    final static String CRLF = "\r\n";
    Socket socket;

    //Constructor
    public HttpRequest(Socket socket) throws Exception {
        this.socket = socket;
    }

    //Implement the run() method of the Runnable interface.
    public void run() {
        try {
            processRequest();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processRequest() throws Exception {
        //Create object for in/out put stream
        DataInputStream s_in = new DataInputStream(socket.getInputStream());
        DataOutputStream s_out = new DataOutputStream(socket.getOutputStream());

        BufferedReader s_br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        //Set timeout
        socket.setSoTimeout(10000);

        //Get the request line of the HTTP request message.
        String requestline = s_in.readUTF();

        //Display the request line.
        System.out.println();
        System.out.println("Request line:" + requestline);

        //Extract the filename from the request line
        StringTokenizer tokens = new StringTokenizer(requestline);
        tokens.nextToken();

        //Skip over the method, which should be "GET"
        String filename = tokens.nextToken();

        //Prepend a "." so that file request is within the current directory.
        filename = "." + filename;

        //Open the requestedfile.
        FileInputStream fis = null;
        boolean fileExists = true;
        try {
            fis = new FileInputStream(filename);
        } catch (FileNotFoundException e) {
            fileExists = false;
        }

        //Construct the response message
        String statusLine = null;
        String contentTypeLine = null;
        String entityBody = null;
        if (fileExists) {
            statusLine = "200 File is found";
            contentTypeLine = "Content - type:" + contentType(filename) + CRLF;
        } else {
            statusLine = "404 File not found";
            contentTypeLine = "File doesn't exists";
            entityBody = "<HTML>" + "<HEAD><TITLE>NOt Found</TITLE></HEAD>"
                    + "<BODY>Not Found</BODY></HTML>";
        }

        //--------------SEND-------------------
        //Send host name
        s_out.writeUTF(InetAddress.getLocalHost().getHostName());

        //Send Protocol
        String pro = tokens.nextToken();
        s_out.writeUTF(pro);

        //Send timeout
        s_out.writeInt(socket.getSoTimeout());

        //Send the status line
        s_out.writeUTF(statusLine);

        //Send the contenttypeline
        s_out.writeUTF(contentTypeLine);

        //Send the entity body.
        if (fileExists) {
            sendBytes(fis, s_out);
        } else {
            s_out.writeBytes(entityBody);
        }

        //--------------READ------------------
        //Read host name
        String host = s_in.readUTF();
        System.out.println("Client Hostname:" + host);

        //Print protocol
        System.out.println("Client Protocol:" + pro);

        //Read timeout
        System.out.println("Client Timeout:" + s_in.readInt());

        //Print Peername
        System.out.println("Client Peername: " + host);

        Thread.sleep(10000);
        System.out.println("Thread Closed.");

        fis.close();
        s_out.close();

    }

    private static void sendBytes(FileInputStream fis, DataOutputStream dos) throws Exception {
        // Construct a 1K buffer to hold bytes on their way to the縲�socket.
        byte[] buffer = new byte[1024];
        int bytes = 0;
        // Copy requested file into the socket's output stream.
        while ((bytes = fis.read(buffer)) != -1) {
            //Send buffer length
            dos.writeInt(buffer.length);
            //Send buffer
            dos.write(buffer, 0, bytes);
        }
    }

    private static String contentType(String fileName) {
        if (fileName.endsWith(".htm") || fileName.endsWith(".html")) {
            return "text/html";
        }
        if (fileName.endsWith(".gif") || fileName.endsWith(".GIF")) {
            return "image/gif";
        }
        if (fileName.endsWith(".jpeg")) {
            return "image/jpeg";
        }
        if (fileName.endsWith(".java")) {
            return "java file";
        }
        if (fileName.endsWith(".sh")) {
            return "bourne/awk";
        }
        if (fileName.endsWith(".txt")) {
            return "Text/txt";
        }
        return "application/octet-stream";
    }
}


