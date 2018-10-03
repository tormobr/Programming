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
	private JTextField input;
	private int wordsTyped = 0;
	private double correctWords = 0;
	private int charsTyped = 0; 
	private String[] textArray = new String[100];
	private double numberOfWords;
	private JTextPane textArea;
	private String fileName;
	private String fileString;
	private int wordStart = 0;
	private boolean error = false;
	private int errorDetected = -1;
	private JFrame res;
	private JFrame hax;
	public TypeTest(String file){
		this.fileName = file;
		//Setting up the main window
		hax = new JFrame("Simple TypeTest.");
		hax.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		hax.setSize(800, 400);
		//hax.setResizable(false);

		//adding panel to frame
		JPanel panel = new JPanel(new GridBagLayout());
		hax.add(panel, BorderLayout.NORTH);
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(10,10,10,10);

		//title
		c.gridx = 0;
		c.gridy = 0;
		JLabel title = new JLabel("TypeTester");
		title.setFont(new Font("Serif", Font.BOLD, 30));
		panel.add(title, c);


		//adding the textArea
		c.gridx = 0;
		c.gridy = 1;
		EmptyBorder eb = new EmptyBorder(new Insets(10, 10, 10, 10));
		textArea = new JTextPane();
		textArea.setBorder(eb);

		textArea.setPreferredSize( new Dimension(600, 250));
		textArea.setFont(new Font("Serif", Font.BOLD, 20));
		textArea.setEditable(false);

		panel.add(textArea, c);


		//adding the input field
		c.gridx = 0;
		c.gridy = 2;
		input = new JTextField(10);
		input.setFont(new Font("Serif", Font.BOLD, 25));
		input.addKeyListener(new KeyPress());
		panel.add(input, c);

		readFile("words.txt");
		addText(textArray, textArea);
		fileString = textArea.getText();

		hax.setVisible(true);
	}
	public static void main(String[] args){
		TypeTest tt = new TypeTest("words.txt");

	}

	//method for reading the file and inserting the words
	private void readFile(String fileName){
		Scanner scanner;
		try{scanner = new Scanner(new File(fileName));}
		catch(Exception e){return;}

		int index = 0;
		while(scanner.hasNextLine()){
			Scanner scanner2 = new Scanner(scanner.nextLine());
			while(scanner2.hasNext() && index < 100){
				String word = scanner2.next();
				textArray[index] = word.toLowerCase();

				index ++;

			}
		}
		numberOfWords = index;

	}

	//adds the words to the text area
	private void addText(String[] s, JTextPane tf){
		for(int i = 0; i < s.length; i++){
			if(s[i] != null){
				tf.setText(tf.getText() + " " + s[i]);
			}
		}
	}

	//checks if the input key matches with the one in the text and handles coloring
	private void correctKey(int keyCode){
		char key = Character.toLowerCase((char)keyCode);

		SimpleAttributeSet attrs = new SimpleAttributeSet();
		SimpleAttributeSet attrs2 = new SimpleAttributeSet();
		SimpleAttributeSet attrs3 = new SimpleAttributeSet();
		StyleConstants.setForeground(attrs, Color.decode("#228B22"));
		StyleConstants.setForeground(attrs2, Color.decode("#8B0000"));
		StyleConstants.setForeground(attrs3, Color.decode("#000000"));
		StyledDocument sdoc = textArea.getStyledDocument();
		
		if(charsTyped >= fileString.length() && !error){
			return;
		}
		if(keyCode == 8){
			if(charsTyped > wordStart){
				charsTyped --;
				sdoc.setCharacterAttributes(charsTyped+1, 1, attrs3, false);

				if(charsTyped <= errorDetected){
					error = false;
					errorDetected = -1;
				}
			}
		}

		else if(error || key != fileString.charAt(charsTyped)){
			error = true;
			sdoc.setCharacterAttributes(charsTyped, 1, attrs2, false);
			if(errorDetected == -1){
				errorDetected = charsTyped;
			}
		}
		else if(key == fileString.charAt(charsTyped)){
			sdoc.setCharacterAttributes(charsTyped, 1, attrs, false);
			
		}
		return;
	}

	//displays the results and restart option
	private void displayResults(double startTime){
		double endTime = System.currentTimeMillis();
		double timeUsed = (endTime - startTime)/1000;


		res = new JFrame("Simple TypeTest.");
		res.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		res.setSize(300, 100);
		JPanel panel = new JPanel(new GridBagLayout());
		res.add(panel, BorderLayout.NORTH);
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(10,10,10,10);

		JLabel percentage = new JLabel("correct word percentage: " + String.valueOf((correctWords/(numberOfWords) * 100)) + "%");
		c.gridx = 0;
		c.gridy = 0;
		panel.add(percentage, c);

		JLabel wpm = new JLabel("WPM: " + String.valueOf((correctWords*(60) / timeUsed)));
		c.gridx = 0;
		c.gridy = 1;
		panel.add(wpm, c);
		res.setVisible(true);

		JButton restart = new JButton("RESTART!");
		restart.addActionListener(new ButtonPress());
		c.gridx = 0;
		c.gridy = 2;
		panel.add(restart, c);
		res.setVisible(true);	
	}

	//restart button handler
	class ButtonPress implements ActionListener{
		public void actionPerformed(ActionEvent evt){
			TypeTest tt2 = new TypeTest("words.txt");
			hax.dispose();
			res.dispose();
		}
	}

	//keypress handler
	class KeyPress implements KeyListener{
		double startTime;
		boolean started = false;
		//when key is released
		public void keyReleased(KeyEvent e){
		}

		public void keyPressed(KeyEvent e){

			int keyCode = e.getKeyCode();
			if(keyCode < 65 || keyCode > 90){
				if(keyCode != 32 && keyCode != 8){
					return;
				}
			}
			//if first key then start timer
			if(started == false){
				started = true;
				startTime = System.currentTimeMillis();

			}

			if(keyCode != 8){
				charsTyped ++;
			}

			//input.setText(input.getText().trim());
			correctKey(keyCode);
			if(keyCode == 32 ){
				String in = input.getText().trim();
				//if the input from user is not empty 
				if(!(in.equals(""))){
					//if input is correct
					if(in.equals(textArray[wordsTyped])){

						input.setText("");
						wordStart += textArray[wordsTyped].length() + 1;
						wordsTyped ++;
						correctWords ++;
						if(wordsTyped < numberOfWords){
							SimpleAttributeSet set = new SimpleAttributeSet();
							StyleConstants.setUnderline(set, true);
							StyledDocument sdoc = textArea.getStyledDocument();
							sdoc.setCharacterAttributes(wordStart+1, textArray[wordsTyped].length(), set, false);
						}
					}
				}

				//when done, add result window to screen
				if(wordsTyped >= numberOfWords){
					displayResults(startTime);
					
				}



			}
		}
		public void keyTyped(KeyEvent e){
			//not used
		}
	}
}