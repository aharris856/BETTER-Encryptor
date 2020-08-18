package encryptor;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.SwingConstants;
import java.awt.Color;
import javax.swing.JTextField;
import java.awt.Cursor;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.Dimension;

public class runEncryption {

	private JFrame frame;
	private JTextField fileNameField;
	private JTextField newFileNameField;
	private JLabel progressLabel;
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					runEncryption window = new runEncryption();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	//get encrypted version of text
	public String encryptText(String input) {
		return (new Encryptor()).getEncrypted(input);
	}
	
	//get decrypted version of input
	public String decryptText(String input) {
		return (new Decryptor()).getDecrypted(input);
	}
	
	//file Fransfer Method encrypt/decrypt
	public void fileXfer(String fileName, String newFileName, boolean todo) throws Exception 
	{
		//VERIFY FILE NAMES
		if(fileName.equalsIgnoreCase(newFileName)) {
			progressLabel.setText("ERROR: your destination file cannot be the same as your initial file.");
			return;
		}
		if(!endsWith(newFileName, ".txt")){
			progressLabel.setText("ERROR... your destination file must end with .txt");
			return;
		}
		File f = new File(fileName);
		if(!f.exists()) {
			progressLabel.setText("ERROR... file: "+fileName+" does not exist.");
			return;
		}
		
		//track progress in int prog to display
		int prog = 0;
		//if destination exists, remove it
		f = new File(newFileName);
		if(f.exists())f.delete();
		
		//File Readers and Writers
		BufferedReader infile = new BufferedReader(new FileReader(fileName));
		FileWriter fw = new FileWriter(newFileName,true);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter pw = new PrintWriter(bw);
		ArrayList<String> fileContent = new ArrayList<String>();
		
		//load the file to encrypt/decrypt onto fileContent
		while(infile.ready())
			fileContent.add(infile.readLine());
		infile.close();
		
		//begin encrypt/decrypt actions and track progress to display to user
		int size = fileContent.size();
		progressLabel.setText("0 / "+size);
		int progTr = size/100;
		int progTrH = size/100;
		
		
		//to do = true => encrypt ... to do = false; => decrypt
		if(todo) {//call encrypt on each line
			
			for(int i = 0; i < size; i++)
			{
				prog = i;
				if(prog==progTr) {//display progress on progressLabel every 1% of the file
					progressLabel.setText(prog+" / "+size);
					progressLabel.paintImmediately(progressLabel.getVisibleRect());
					progTr = progTr+progTrH;
				}
				try {
					pw.println(encryptText(fileContent.get(i)));
				}catch(ArrayIndexOutOfBoundsException e) {
					System.out.println("Index on fileContent Error.");
					pw.close();
					bw.close();
					fw.close();
					return;
				}
			}
			pw.close();
			//state done
			progressLabel.setText(fileName+" > > ENCRYPTED TO > > "+newFileName+" COMPLETE");
			bw.close();
			fw.close();
			
		}else {//call decrypt on each line
			
			for(int i = 0; i < size; i++)
			{
				prog = i;
				if(prog==progTr) {//display progress on progressLabel every 1% of the file
					progressLabel.setText(prog+" / "+size);
					progressLabel.paintImmediately(progressLabel.getVisibleRect());
					progTr = progTr+progTrH;
				}
				try {
					pw.println(decryptText(fileContent.get(i)));	
				}catch(ArrayIndexOutOfBoundsException e) {
					System.out.println("Index on fileContent Error.");
					pw.close();
					bw.close();
					fw.close();
					return;
				}
			}
			pw.close();
			//state done
			progressLabel.setText(fileName+" > > DECRYPTED TO > > "+newFileName+" COMPLETE");
			bw.close();
			fw.close();
		}
	}
	
	
	//check if input ends with end.
	static boolean endsWith(String input, String end)throws Exception
	{
		int startPos = input.length()-end.length();
		if(end.length() >= input.length())return false;
		String ending = "";
		for(int i = startPos; i < input.length(); i++)
		{
			ending +=input.charAt(i);
		}
		return ending.equals(".txt");
	}
	
	public runEncryption() {
		initialize();
	}
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(51, 102, 153));
		frame.setBounds(100, 100, 1233, 778);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel welcomeLabel = new JLabel("Welcome to my improved Encryptor! ");
		welcomeLabel.setBackground(Color.LIGHT_GRAY);
		welcomeLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		welcomeLabel.setBounds(386, 0, 378, 48);
		frame.getContentPane().add(welcomeLabel);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.LIGHT_GRAY);
		panel.setBounds(10, 46, 1199, 685);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		//TEXT ENCRYPT AREA
		JLabel labelText1 = new JLabel("Text to Encrypt or Decrypt:");
		labelText1.setBackground(Color.LIGHT_GRAY);
		labelText1.setBounds(10, 0, 278, 33);
		labelText1.setFont(new Font("Tahoma", Font.BOLD, 14));
		labelText1.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		panel.add(labelText1);
		
		JTextArea textInput = new JTextArea();
		textInput.setMaximumSize(new Dimension(489, 2147483647));
		textInput.setText("Enter your text to encrypt here.\r\nYou can do more than one line\r\nby pressing the 'enter' key!\r\n(For large amounts of text\r\nplease save it to a text file\r\nand use the file encrypt section)");
		textInput.setBounds(10, 26, 570, 189);
		textInput.setLineWrap(true);
        textInput.setWrapStyleWord(true);
		panel.add(textInput);
		
		JLabel outputLabel = new JLabel("vv Output vv");
		outputLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		outputLabel.setBounds(318, 221, 90, 33);
		panel.add(outputLabel);
		
		JTextArea textOutput = new JTextArea();
		textOutput.setMaximumSize(new Dimension(489, 2147483647));
		textOutput.setText("Enter and encrypt or decrypt\r\nin the above text area to \r\ngenerate an output.");
		textOutput.setBounds(10, 268, 619, 407);
		textOutput.setLineWrap(true);
        textOutput.setWrapStyleWord(true);
        panel.add(textOutput);

		
		//Encrypt Text Button
		JButton encryptTextB = new JButton("Encrypt");
		encryptTextB.setFont(new Font("Tahoma", Font.BOLD, 11));
		encryptTextB.setBackground(new Color(204, 0, 51));
		encryptTextB.setBounds(10, 219, 91, 39);
		encryptTextB.addActionListener(new ActionListener(){
			@Override 
		    public void actionPerformed(ActionEvent e){  
				textOutput.setText(encryptText(textInput.getText()));
		   }  
		});  
		panel.add(encryptTextB);
		
		//Decrypt TextButton
		JButton decryptTextB = new JButton("Decrypt");
		decryptTextB.setFont(new Font("Tahoma", Font.BOLD, 11));
		decryptTextB.setBackground(new Color(0, 102, 204));
		decryptTextB.setBounds(131, 219, 91, 39);
		decryptTextB.addActionListener(new ActionListener(){
			@Override 
		    public void actionPerformed(ActionEvent e){  
				textOutput.setText(decryptText(textInput.getText()));
	    }  
	    });  
		panel.add(decryptTextB);
		
		//FILE ENCRYPT AREA
		JLabel fileLabel = new JLabel("Enter your file name to Encrypt or Decrypt (ex. file.txt):");
		fileLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		fileLabel.setBounds(609, 26, 541, 39);
		panel.add(fileLabel);
		
		fileNameField = new JTextField();
		fileNameField.setBounds(609, 75, 556, 33);
		panel.add(fileNameField);
		fileNameField.setColumns(10);
		
		JLabel newFileLabel = new JLabel("Enter your destination file name (ex. encryptedFile.txt):");
		newFileLabel.setVerticalTextPosition(SwingConstants.TOP);
		newFileLabel.setVerticalAlignment(SwingConstants.TOP);
		newFileLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		newFileLabel.setBounds(609, 127, 556, 52);
		panel.add(newFileLabel);
		
		newFileNameField = new JTextField();
		newFileNameField.setBounds(609, 157, 556, 33);
		panel.add(newFileNameField);
		newFileNameField.setColumns(10);
		
		//encrypt file button
		JButton encryptFileB = new JButton("Encrypt");
		encryptFileB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					fileXfer(fileNameField.getText(),newFileNameField.getText(),true);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		encryptFileB.setFont(new Font("Tahoma", Font.BOLD, 11));
		encryptFileB.setBackground(new Color(204, 0, 51));
		encryptFileB.setBounds(859, 284, 126, 39);
		panel.add(encryptFileB);
		
		//Decrypt file button
		JButton decryptFileB = new JButton("Decrypt");
		decryptFileB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					fileXfer(fileNameField.getText(),newFileNameField.getText(),false);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		decryptFileB.setFont(new Font("Tahoma", Font.BOLD, 11));
		decryptFileB.setBackground(new Color(0, 102, 204));
		decryptFileB.setBounds(1003, 284, 126, 39);
		panel.add(decryptFileB);
		
		JLabel warningLabel = new JLabel("WARNING: Choosing to write into an existing file will delete the old file.");
		warningLabel.setFont(new Font("Segoe UI Black", Font.BOLD | Font.ITALIC, 17));
		warningLabel.setBounds(590, 189, 599, 66);
		panel.add(warningLabel);
		
		progressLabel = new JLabel("0 / 0");
		progressLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		progressLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		progressLabel.setBounds(658, 247, 531, 39);
		panel.add(progressLabel);
		
		JLabel problemsLabel1 = new JLabel("Encrypt text has problems decrypting line breaks (file encrypt fine though bc used buffered reader) problem explained in readme file");
		problemsLabel1.setFont(new Font("Tahoma", Font.PLAIN, 7));
		problemsLabel1.setBounds(648, 557, 551, 39);
		panel.add(problemsLabel1);
		
		
	}
}