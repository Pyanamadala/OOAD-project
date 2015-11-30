package RCM;

import java.awt.*;

import javax.swing.*;

class MainPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	MainPanel(){
		this.setLayout(new GridLayout(1,2));
		add(new RCMGUI());
		add(new RMOSGUI());
	}
}


public class EcoRe {

	public static void main(String[] args) {
		JFrame frEcore=new JFrame("EcoRe");
		frEcore.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frEcore.getContentPane().add(new MainPanel(), BorderLayout.CENTER);
		JLabel lblheader = new JLabel("Welcome to EcoRe Recycling Station!!",
				SwingConstants.CENTER);
		lblheader.setFont(new Font("Serif", Font.BOLD, 40));
		frEcore.getContentPane().setBackground(Color.WHITE);
		frEcore.getContentPane().add(lblheader, BorderLayout.NORTH);
		frEcore.setVisible(true);
		frEcore.pack();

	}

}
