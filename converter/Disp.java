import java.awt.*;
import javax.swing.*;

class Disp extends JFrame{

	private JLabel item1;
	private JLabel item2;
	private JLabel item3;
	private JButton item4;
	private JTextField item5;

	public Disp(){
		super("simple converter");

		JPanel panel = new JPanel(new GridBagLayout());
		getContentPane().add(panel, BorderLayout.WEST);
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(10,10,10,10);

		item1 = new JLabel("dec: ");
		item2 = new JLabel("bin: ");
		item3 = new JLabel("hex: ");
		item4 = new JButton("Convert!");
		item5 = new JTextField(20);



		c.gridx = 0;
		c.gridy = 0;

		panel.add(item5, c);

		c.gridx = 1;
		c.gridy = 0;

		panel.add(item4, c);


		c.gridx = 0;
		c.gridy = 1;
	
		panel.add(item1, c);


		c.gridx = 0;
		c.gridy = 2;
		panel.add(item2, c);


		c.gridx = 0;
		c.gridy = 3;
		panel.add(item3, c);

	}

}