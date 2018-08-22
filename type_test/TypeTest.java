import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.util.Scanner;
import java.io.File;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

class TypeTest{
	static JTextField input;
	static int wordsTyped = 0;
	static int correctWords = 0;
	static int charsTyped = 0; 
	static String[] textArray = new String[50];
	static JTextPane textArea;

	public static void main(String[] args){
		JFrame hax = new JFrame("Simple TypeTest.");
		hax.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		hax.setSize(800, 300);
		//hax.setResizable(false);

		JPanel panel = new JPanel(new GridBagLayout());
		hax.add(panel, BorderLayout.NORTH);
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(10,10,10,10);

		c.gridx = 0;
		c.gridy = 0;
		JLabel title = new JLabel("TypeTester");
		title.setFont(new Font("Serif", Font.BOLD, 30));
		panel.add(title, c);



		c.gridx = 0;
		c.gridy = 1;
		EmptyBorder eb = new EmptyBorder(new Insets(10, 10, 10, 10));
		textArea = new JTextPane();
		textArea.setBorder(eb);
		textArea.setPreferredSize( new Dimension(400, 200));
		textArea.setFont(new Font("Serif", Font.PLAIN, 16));
		textArea.setEditable(false);
		
		//textArea.setLineWrap(true);
		//textArea.setWrapStyleWord(true);
		
		readFile("words.txt");
		addText(textArray, textArea);


		panel.add(textArea, c);



		c.gridx = 0;
		c.gridy = 2;
		input = new JTextField(10);
		input.setFont(new Font("Serif", Font.BOLD, 18));
		input.addKeyListener(new KeyPress());
		panel.add(input, c);


		hax.setVisible(true);


	}
	private static void readFile(String fileName){
		Scanner scanner;
		try{
		scanner = new Scanner(new File(fileName));
		}
		catch(Exception e){return;}

		int index = 0;
		while(scanner.hasNextLine()){
			Scanner scanner2 = new Scanner(scanner.nextLine());
			while(scanner2.hasNext() && index < 50){
				String word = scanner2.next();
				textArray[index] = word;
				index ++;

			}
		}

	}

	private static void addText(String[] s, JTextPane tf){
		for(int i = 0; i < s.length; i++){
			if(s[i] != null){
				tf.setText(tf.getText() + " " + s[i]);
			}
		}
	}

	static class KeyPress implements KeyListener{

		public void keyReleased(KeyEvent e){

			SimpleAttributeSet attrs = new SimpleAttributeSet();
			StyleConstants.setForeground(attrs, Color.green);

			StyledDocument sdoc = textArea.getStyledDocument();

			input.setText(input.getText().trim());
			int keyCode = e.getKeyCode();
			String in = input.getText().trim();
			if(keyCode == 32 ){
				if(!(in.equals(""))){
					
					if(in.equals(textArray[wordsTyped])){
						charsTyped += in.length()+1;
						sdoc.setCharacterAttributes(0, charsTyped, attrs, false);
						input.setText("");
						wordsTyped ++;

					}
					else{
						System.out.println("wrong!");
					}
				}

			}
		}

		public void keyPressed(KeyEvent e){

		}
		public void keyTyped(KeyEvent e){

		}
	}
}