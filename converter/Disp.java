import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

class Disp extends JFrame{

	private JLabel item1;
	private JLabel item2;
	private JLabel item3;
	private JButton item4;
	private JTextField item5;
	private JTextField item6;
	JTextField item7;
	JTextField item8;

	public Disp(){
		super("simple converter");

		JPanel panel = new JPanel(new GridBagLayout());
		getContentPane().add(panel, BorderLayout.WEST);
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(10,10,10,10);

		item1 = new JLabel("bin: ");
		item2 = new JLabel("hex: ");
		item3 = new JLabel("oct: ");
		item4 = new JButton("Convert!");
		item4.addActionListener(new Action());
		item5 = new JTextField(10);
		item6 = new JTextField(10);
		item7 = new JTextField(10);
		item8 = new JTextField(10);




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

		c.gridx = 1;
		c.gridy = 1;
		panel.add(item6, c);

		c.gridx = 1;
		c.gridy = 2;
		panel.add(item7, c);

		c.gridx = 1;
		c.gridy = 3;
		panel.add(item8, c);

	}
	class Action implements ActionListener{
		public void actionPerformed(ActionEvent evt){
			String hax = item5.getText();
			String binary = converter.decToBin((Integer.parseInt(hax)));
			String hexa = converter.decToHex((Integer.parseInt(hax)));
			String octa = converter.decToOct((Integer.parseInt(hax)));
			item6.setText(binary);
			item7.setText(hexa);
			item8.setText(octa);
		}
	}

}