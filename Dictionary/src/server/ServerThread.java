//Qiaoqiao Li; 1106856
package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ServerThread extends Thread {

  private Socket clientSocket;
  private int clientId;
  private String clientMessage;
  private String filename;
  private JSONObject jsonObject;
  private BufferedReader input;
  private BufferedWriter output;
  private String dictionary;
  private String word;
  private JSONObject jsonMess;
 
  public ServerThread(final Socket clientSocket, final int clientId,  final String filename,String dictionary ) {
    this.clientSocket = clientSocket;
    this.clientId = clientId; 
    this.filename=filename;
    this.dictionary=dictionary;
  }

  
  public void run(){
     System.out.println("Client " + clientId + " connected "+"("+ clientSocket.toString() + ")\n");
     try  {     
     input =new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
     output = new BufferedWriter( new OutputStreamWriter(clientSocket.getOutputStream()));

     while ((clientMessage = input.readLine()) != null) {
      jsonMess = new JSONObject(clientMessage);
      word = jsonMess.get("word").toString();  
      
         System.out.println("client " + clientId + " apply for " + clientMessage);        
         String information = deal(jsonMess);
         output.write(information);
         output.write("\n");
         output.flush();
       }
       clientSocket.close();
     } catch (Exception e) {
       e.printStackTrace();
     } 
   }
 private  String deal(JSONObject jsonObj) throws JSONException,  Exception {    
     String response =null;
     dictionary = new String(Files.readAllBytes(Paths.get(filename)));
     jsonObject=new JSONObject(dictionary);
     try {
      int command = Integer.parseInt(jsonObj.get("command").toString());
      switch(command) {
      case 1:
       String meaning="";
       if(jsonObject.has(word)) {
        meaning = jsonObject.get(word).toString();
       }else {
        output.flush();
           meaning=" is not the in dictionary!";
       }
       
             response = word + " : " + meaning;
             break;
      case 2:
       JSONArray arrJson = jsonObj.getJSONArray("meaning");
       if(!jsonObject.has(word)&&arrJson.length()!=0) {
       JSONArray jsonArray = new JSONArray();
             String[] meaning1 = new String[arrJson.length()];
            
             for (int i = 0; i < arrJson.length(); i++) {
               meaning1[i] = arrJson.getString(i);
               jsonArray.put(meaning1[i]);
               } 
             jsonObject.put(word, jsonArray);
          try (BufferedWriter writetoJson = new BufferedWriter(new FileWriter(filename))) {
           writetoJson.write(jsonObject.toString());
          }
          response = "Add word successful";
         
          }else {
        output.write( word + " has already existed in dictionary! ");
        output.flush();
        response= " Add word unsuccessful!";
       }
             
             break;
      case 3:
    	  if(word==null) {
    		  response = "Please enter a word!";
    	  }
    	  else if (jsonObject.has(word)) {
         jsonObject.remove(word);
         try (BufferedWriter writeJson = new BufferedWriter(new FileWriter(filename))) {
             writeJson.write(jsonObject.toString());
              response= " Delete word successful!";
            }
        }
        else {
          output.write("There is no " + word + " in Dictionary!");
          output.flush();
          response= " Delete word unsuccessful!";
         }
     
       break;
       
      }     
     } 
     catch (IOException e) {
       response = e.getMessage();
     }
     return response;
   }
 
}