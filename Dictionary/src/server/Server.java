
//Qiaoqiao Li; 1106856
package server;

import java.io.*;
import java.net.*;
public class Server extends Thread{
  //private String filename;
  //private static ServerSocket socket;
  private static int clientId = 0;
  private static String dictionary;
  private static ServerSocket socket;
  private static int port;
  


public  Server (String[] args){
	  
	 // port = Integer.parseInt(args[0]);
	port =8000;
  }
  
  public  void run() {
   String filename="Dictionary/dictionary.json";///////////////////////////
	   //String filename="src/dictionary.json";
    
    File dicPath = new File(filename);
    if (!dicPath.exists()) {
    	System.out.println("Please confirm your dictionary path!");
    }else {
     try {  
    	ServerSocket socket = new ServerSocket(port);
       System.out.println("Waiting for client connection..."); 
       while (true) {         
         Socket clientSocket = socket.accept();
         clientId++;
         System.out.println("Client " +clientId+" Applying for connection to deal with  "+ dicPath+" from port " +port );                   
         ServerThread subthread = new ServerThread(clientSocket, clientId, filename, dictionary);
         subthread.start();
        }
      } catch (IOException e) {
        e.printStackTrace();
        }
      }

    }
  }
