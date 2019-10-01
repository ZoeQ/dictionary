//Qiaoqiao Li; 1106856

package server;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import org.json.JSONException;
import org.json.JSONObject;


import javax.swing.JButton;

public class ServerWindow {


	
	private JFrame frame;
	private JButton btnRefresh;
	private JTextArea textArea;
	private static BufferedReader in;
    private static BufferedWriter out;
    private String dictionary;
    private JSONObject jsonObject;
    private String filename;
    private String content;
    
    ///////////////////////////////////////////
    private static Server server;
 /**
  * Launch the application.
 * @throws IOException 
 * @throws UnknownHostException 
  */
 public static void main(String[] args) throws UnknownHostException, IOException {
	 
	 //start server
	 try {
		 initConnection(args);
	 }catch (IOException e) {
	     System.out.println("We can not launch the server, please check your information !");
	   }
	
	
	 EventQueue.invokeLater(new Runnable() {
		 public void run() {
			 try {
				 ServerWindow window = new ServerWindow();
				 window.frame.setVisible(true);
			 } catch (Exception e) {
			  e.printStackTrace();
	         }
	     }
	  });
 }

 ////////////////////////////////////////
 private static void initConnection(String[] args) throws UnknownHostException, IOException {
	 server = new Server(args);
	 server.start();
 }
 /**
  * Create the application.
  * @throws Exception 
  */
 public ServerWindow() throws Exception {
  initialize();
  
 }

 /**
  * Initialize the contents of the frame.
  * @throws Exception s
  */
	 private void initialize() throws Exception {
	  frame = new JFrame();
	  frame.setBounds(100, 100, 450, 300);
	  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	  frame.setTitle("Server Window");
	  frame.getContentPane().setLayout(null);
	  
	  textArea = new JTextArea();
	  textArea.setEditable(false);
	  textArea.setBounds(67, 38, 307, 145);
	  GridBagConstraints gtextArea = new GridBagConstraints();
	  gtextArea.insets = new Insets(10, 10, 10, 10);
	  gtextArea.fill = GridBagConstraints.BOTH;
	  gtextArea.gridx = 2;
	  gtextArea.gridy = 1;
	  gtextArea.weightx = 5;
	  gtextArea.gridheight = 20;
	  frame.getContentPane().add(textArea,gtextArea);
	  textArea.setRows(10);
	  textArea.setLineWrap(true);
	  textArea.setWrapStyleWord(true);
	  
	
	  refersh();
	 }
	 
	public void refersh(){
		btnRefresh = new JButton("Refresh");
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
	    gbc_btnNewButton.insets = new Insets(0, 0, 5, 0);
	    gbc_btnNewButton.gridx = 1;
	    gbc_btnNewButton.gridy = 2;
		btnRefresh.setBounds(163, 212, 117, 29);
		frame.getContentPane().add(btnRefresh,gbc_btnNewButton);
		
		btnRefresh.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				//clean text area
				textArea.setText("");
				
				filename="Dictionary/dictionary.json";
				//filename="src/dictionary.json";
				try {
					
					dictionary = new String(Files.readAllBytes(Paths.get(filename)));
					jsonObject=new JSONObject(dictionary);
					
					@SuppressWarnings("unchecked")
					Iterator<String> it = jsonObject.keys(); 
					while(it.hasNext()){
						// get key
						String key = it.next(); 
						String value = jsonObject.get(key).toString();  
						String stringLine=key+"  "+value+"\n";
						textArea.append(stringLine);
						System.out.println(stringLine);
					}

				} catch (IOException | JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				
			}
	
	   
		});
	}
}