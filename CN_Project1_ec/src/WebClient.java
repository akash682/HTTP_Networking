//UTA ID:1001661458
//Name: Akash Lohani

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;


public class WebClient {
    //PORT, IPADD
    public final static int PORT = 6788;
    public final static String IPADD = "localhost";

    public static void main(String[] args) {
        try {
            //Create Socket
            Socket clientSocket = new Socket(IPADD, PORT);
            System.out.println("Connected to Server.");

            //Create object for in/out put stream
            DataInputStream c_in = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream c_out = new DataOutputStream(clientSocket.getOutputStream());

            long start = System.currentTimeMillis();

            //Ooutput HTTP Request
            c_out.writeUTF("GET /Sample.html HTTP/1.1");

            //-------------------SEND---------------------
            //Send host name
            c_out.writeUTF(InetAddress.getLocalHost().getHostName());

            //Send timeout
            clientSocket.setSoTimeout(20000);
            c_out.writeInt(clientSocket.getSoTimeout());

            //Send peer name

            //------------------READ-----------------------
            long end = System.currentTimeMillis();
            //Read Hostname
            String host = c_in.readUTF();
            System.out.println("Server hostname: " + host);

            //Read Protocol
            System.out.println("Server Protocol: " + c_in.readUTF());

            //Read timeout
            System.out.println("Server Timeout: " + c_in.readInt());

            //Print Peername
            System.out.println("Server peername: " + host);

            //Read statusline
            String statusline = c_in.readUTF();
            System.out.println("StatusLine:" + statusline);

            //Read contentTypeLine
            String contentTypeLine = c_in.readUTF();
            System.out.println("ContentTypeLine" + contentTypeLine);

            //RTT
            System.out.println("RTT: "+ (end-start)/1000.0 + "sec");

            //Receive the number of bytes of the entire body
            int buffer_length = c_in.readInt();

            ///Read Entire file body
            FileOutputStream fos = new FileOutputStream("received.html");
            byte[] buffer_file = new byte[4096];
            int rec = 0;
            while((rec = c_in.read(buffer_file, 0, buffer_length)) > 0) {
                fos.write(buffer_file, 0, rec);
            }


        } catch (IOException e) {
            System.out.println("Client shutdown unexpectedly.");
            e.printStackTrace();
        }
    }
}
