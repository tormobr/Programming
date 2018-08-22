import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Scanner;

class TypeTest{
	static JTextField input;
	static int wordsTyped = 0;
	static int correctWords = 0;

	public static void main(String[] args){
        JFrame hax = new JFrame("Simple TypeTest.");
        hax.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        hax.setSize(400, 300);
        hax.setResizable(false);
        
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
        JTextArea textArea = new JTextArea(5, 30);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
        textArea.setText("hallo jeg heter tormod og dette er en liten typetester. Ha det gøy of skriv så rask du klarer");
        panel.add(textArea, c);


        c.gridx = 0;
        c.gridy = 2;
        input = new JTextField(10);
        input.addKeyListener(new KeyPress());
        panel.add(input, c);


        hax.setVisible(true);


	}

	static class KeyPress implements KeyListener{

		public void keyReleased(KeyEvent e){
			int keyCode = e.getKeyCode();
			if(keyCode == 32 ){
				if(!(input.getText().trim().equals(""))){
					wordsTyped ++;
					System.out.println(wordsTyped);
				}
				input.setText("");

			}
		}

		public void keyPressed(KeyEvent e){

		}
		public void keyTyped(KeyEvent e){

		}
	}
}