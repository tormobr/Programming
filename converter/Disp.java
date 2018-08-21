import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

class Disp extends JFrame{

	private JLabel mainTitle;
	private JLabel item1;
	private JLabel item2;
	private JLabel item3;
	private JButton convertButton;
	private JTextField number;
	private JTextField binResult;
	private JTextField hexResult;
	private JTextField octResult;

	public Disp(){
		super("Simple converter");

		JPanel panel = new JPanel(new GridBagLayout());
		getContentPane().add(panel, BorderLayout.WEST);
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(10,10,10,10);

		mainTitle = new JLabel("Welcome!");
		mainTitle.setFont(new Font("Serif", Font.BOLD, 30));
		item1 = new JLabel("bin: ");
		item2 = new JLabel("hex: ");
		item3 = new JLabel("oct: ");
		convertButton = new JButton("Convert!");
		convertButton.addActionListener(new Action());
		number = new JTextField(10);
		binResult = new JTextField(15);
		hexResult = new JTextField(15);
		octResult = new JTextField(15);
		binResult.setEditable(false);
		hexResult.setEditable(false);
		octResult.setEditable(false);

		c.gridx = 0;
		c.gridy = 0;
		panel.add(mainTitle, c);

		c.gridx = 0;
		c.gridy = 1;
		panel.add(number, c);

		c.gridx = 1;
		c.gridy = 1;
		panel.add(convertButton, c);


		c.gridx = 0;
		c.gridy = 2;
		panel.add(item1, c);


		c.gridx = 0;
		c.gridy = 3;
		panel.add(item2, c);


		c.gridx = 0;
		c.gridy = 4;
		panel.add(item3, c);

		c.gridx = 1;
		c.gridy = 2;
		panel.add(binResult, c);

		c.gridx = 1;
		c.gridy = 3;
		panel.add(hexResult, c);

		c.gridx = 1;
		c.gridy = 4;
		panel.add(octResult, c);

	}

	class Action implements ActionListener{
		public void actionPerformed(ActionEvent evt){

			String hax = number.getText();
			if(hax.length() == 0){
				return;
			}


			try {Integer.parseInt(hax); 
	    	} catch(NumberFormatException e) {return;}

			String binary = converter.decToBin((Integer.parseInt(hax)));
			String hexa = converter.decToHex((Integer.parseInt(hax)));
			String octa = converter.decToOct((Integer.parseInt(hax)));
			binResult.setText(binary);
			hexResult.setText(hexa);
			octResult.setText(octa);
		}
	}

}