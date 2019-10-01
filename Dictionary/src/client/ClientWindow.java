//Qiaoqiao Li; 1106856
package client;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.util.ArrayList;
import java.io.*;
import java.net.*;

import org.json.JSONException;
import org.json.JSONObject;
public class ClientWindow {

	private JFrame frame;
	private JTextField SearchText;
	private JTextField addText;
	private JTextField meaningText;
	private JTextField deleteText;
	private JButton btnDelete;
	private JButton btnAdd;
	private JButton btnSearch;
	private JTextArea textArea;
	private JLabel lblMeaning ;
	private JLabel lblStatus;
	private static Socket socket;
    private static boolean onlineStatus;
    private static BufferedReader in;
    private static BufferedWriter out;    
    public static final int search = 1;
    public static final int add = 2;
    public static final int delete = 3;
    private static int numberAction;
    
	/**
	 * Launch the application.
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) throws UnknownHostException, IOException {
		if(initConnection(args))
		{
			onlineStatus = true;
			}
		else {
	
			onlineStatus = false;
		}
		
		/*try {
            initConnection(args);
            onlineStatus = true;
        } catch (IOException e) {
            onlineStatus = false;
        }*/
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientWindow window = new ClientWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	 private static boolean initConnection(String[] args) throws UnknownHostException, IOException {
	      
	    	try{
	    		String hostname = args[0];	
	    		//String hostname = "127.0.0.1";
	        int port=Integer.parseInt(args[1]);
	    		//int port =8000;
	        socket = new Socket(hostname, port);
	        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
	        return true;}
	    	catch(Exception e) {
	    		System.out.println("Please confirm your address and port!");
	    		//e.printStackTrace();
	    		return false;
	    	}
	        
	        
	        
	    }
	/**
	 * Create the application.
	 */
	public ClientWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 520, 370);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Client Window");
		frame.getContentPane().setLayout(null);
		
		SearchText = new JTextField();
		SearchText.setBounds(27, 55, 130, 30);
		GridBagConstraints textField = new GridBagConstraints();
		textField.insets = new Insets(0, 10, 5, 5);
	    textField.weightx = 0.5;
	    textField.fill = GridBagConstraints.HORIZONTAL;
	    textField.gridx = 0;
	    textField.gridy = 2;
		frame.getContentPane().add(SearchText,textField);
		SearchText.setColumns(10);
		
		addText = new JTextField();
		addText.setBounds(27, 121, 130, 30);
		frame.getContentPane().add(addText);
		addText.setColumns(10);
		
		meaningText = new JTextField();
		meaningText.setBounds(27, 159, 130, 30);
		frame.getContentPane().add(meaningText);
		meaningText.setColumns(10);
		
		deleteText = new JTextField();
		deleteText.setBounds(27, 225, 130, 30);
		frame.getContentPane().add(deleteText);
		deleteText.setColumns(10);
		
		btnSearch = new JButton("Search");
		GridBagConstraints btnNewButton = new GridBagConstraints();
		btnNewButton.insets = new Insets(0, 0, 5, 0);
	    btnNewButton.gridx = 1;
	    btnNewButton.gridy = 2;
		btnSearch.setBounds(157, 55, 117, 29);
		frame.getContentPane().add(btnSearch,btnNewButton);
		
		btnAdd = new JButton("Add");
		btnAdd.setBounds(157, 121, 117, 29);
		frame.getContentPane().add(btnAdd);
		
		btnDelete = new JButton("Delete");
		btnDelete.setBounds(157, 225, 117, 29);
		frame.getContentPane().add(btnDelete);
		
		lblMeaning = new JLabel("meaning");
		lblMeaning.setBounds(189, 164, 61, 16);
		frame.getContentPane().add(lblMeaning);
		
		textArea = new JTextArea();
		textArea.setBounds(280, 55, 200, 200);
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
		
		lblStatus = new JLabel("Status");
		lblStatus.setText("Status: "+onlineStatus);
		GridBagConstraints textLable = new GridBagConstraints();
		textLable.insets = new Insets(0, 10, 5, 5);
		textLable.fill = GridBagConstraints.HORIZONTAL;
		textLable.gridx = 2;
	    textLable.gridy = 0;
		lblStatus.setBounds(280, 20, 150, 16);
		frame.getContentPane().add(lblStatus,textLable);
		 search();
		 add();
		 delete();
		 if (onlineStatus == false) {
	            disconnected(false);
	}
		
}
	public void  disconnected(boolean nextStatus) {
		
        //onlineStatus = nextStatus;
        
        if (nextStatus == false) {
            textArea.setText("Please check your server connection! \n");
            btnAdd.setEnabled(false);
            btnDelete.setEnabled(false);
            btnSearch.setEnabled(false);
        } else {
        	btnAdd.setEnabled(true);
        	btnDelete.setEnabled(true);
        	btnSearch.setEnabled(true);
        }
        lblStatus.setText("Status: "+nextStatus);
		
		}
	public void search() {
		btnSearch.addActionListener(new ActionListener(){
            @Override
			public void actionPerformed(ActionEvent e) {
            	 textArea.setText("");
            	 {
            	        clearIfNumberActionHigh();
            	        String word = SearchText.getText();
            	        System.out.println("Search clicked! Searching for " + word + "....");

            	        try {
            	        	
            	          //String response = client.send(Client.search, word);
            	          //////////////////////////////////////////////////////////////////////////////////////////
            	          out.write(new JSONObject().put("command", search).put("word", word).toString());
            	          out.write("\n");
            	          out.flush();
            	          String received = in.readLine();
            	          //////////////////////////////////////////////////////////////////////////////////////////
            	          textArea.append(">> " + received + "\n \n");
            	          disconnected(true);
            	        } catch (Exception ex) {
            	          textArea.append(ex.getMessage() + "\n \n");
            	          disconnected(true);
            	        }
            	      }
				// TODO Auto-generated method stub
				
			}

			
        });
	}
	public void delete() {
		btnDelete.addActionListener(new ActionListener() {
            @Override
			public void actionPerformed(ActionEvent grg0) {
            	 textArea.setText("");
            	 clearIfNumberActionHigh();
                 String word = deleteText.getText();
                 System.out.println("Delete clicked! Deleting for " + word);

                 try {
                   //String response = client.send(Client.delete, word);
                   ///////////////////////////////////////////////////////////////////////////////////////////
                   out.write(new JSONObject().put("command", delete).put("word", word).toString());
                   out.write("\n");
                   out.flush();
                   String received = in.readLine();
                   ///////////////////////////////////////////////////////////////////////////////////////////
                   textArea.append(">> " + received + "\n \n");
                   
                   disconnected(true);
                 } catch (Exception e) {
                   textArea.append(e.getMessage() + "\n \n");
                   disconnected(true);
                 }
				// TODO Auto-generated method stub
				
			}
        });
		
	}
	public void add() {
		
	    btnAdd.addActionListener(new ActionListener() {
	        @Override
			public void actionPerformed(ActionEvent arg0) {
	        	textArea.setText("");
	        	clearIfNumberActionHigh();
	            String word = addText.getText();
	            ///////////////////////////////////////////////////////////
	            
	            ArrayList<String> meaning = new ArrayList<String>();
	            meaning.add(meaningText.getText());
	            //String meaning = meaningText.getText();
	            //ArrayList<String> meanings = new ArrayList<String>();
	            JSONObject jsonObj=new JSONObject();
	           /* for (JTextField jTextField : listMeanings) {
	              if (!jTextField.getText().isEmpty() && jTextField.getText() != null) {
	                meanings.add(jTextField.getText());
	              }
	            }*/
	            try {
					jsonObj.put(word, meaning);
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

	            System.out.println("Add clicked! Adding " + word + " : " + meaning.toString());

	            try {
	              //String response = client.send(Client.add, word, meanings);
	              //////////////////////////////////////////////////////////////////////////////////////////
	                out.write(new JSONObject().put("command", add).put("word", word).put("meaning", meaning).toString());
	                out.write("\n");
	                out.flush();
	                String received = in.readLine();
	              //////////////////////////////////////////////////////////////////////////////////////////
	              textArea.append(">> " + received + "\n \n");
	              disconnected(true);
	            } catch (Exception e) {
	              textArea.append(e.getMessage() + "\n \n");
	              disconnected(true);
	            }
				// TODO Auto-generated method stub
				
			}
	      });
	    //listMeanings.add(meaningText);
		
	}
	@SuppressWarnings("unused")
	private void clearIfNumberActionHigh() {
        if (numberAction > 1) {
            textArea.setText("");
            numberAction = 0;
        }
    }
	}

