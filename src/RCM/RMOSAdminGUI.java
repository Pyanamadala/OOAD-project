package RCM;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.*;

public class RMOSAdminGUI extends JFrame {
	private Box panelLeft;
	private CardLayout cardLayout= new CardLayout();
	private JPanel cardPanel;
	private boolean isLogInSuccessful=false;
	public RMOSAdminGUI(){
	}
	public boolean getIsLogInSuccessful(){
		return isLogInSuccessful;
	}
	public void setIsLogInSuccessful(boolean stat ){
		isLogInSuccessful=stat;
	}
	public JPanel logInPage(){
		
		Font font = new Font(("SansSerif"), Font.PLAIN, 20);
		
		panelLeft=Box.createVerticalBox();
		
		JLabel userNameLabel= new JLabel("Username");
		userNameLabel.setFont(font);
		JTextArea userNameArea= new JTextArea("");
		userNameArea.setFont(font);
		userNameArea.setPreferredSize(new Dimension(200,35));
		
		JPanel userNamePanel = new JPanel();
		userNamePanel.add(userNameLabel);
		userNamePanel.add(userNameArea);
		
		JLabel passwrdLabel = new JLabel("Password");
		passwrdLabel.setFont(font);
		JTextArea passwrdArea= new JTextArea("");
		passwrdArea.setFont(font);
		passwrdArea.setPreferredSize(new Dimension(200,35));
		
		JPanel passwrdPanel = new JPanel();
		passwrdPanel.add(passwrdLabel);
		passwrdPanel.add(passwrdArea);
		
		panelLeft.add(Box.createVerticalStrut(50));
		panelLeft.add(Box.createVerticalStrut(25));
		panelLeft.add(userNamePanel);
		panelLeft.add(Box.createVerticalStrut(25));
		panelLeft.add(passwrdPanel);
		panelLeft.add(Box.createVerticalStrut(25));
		
		JButton logInBtn= new JButton("Log In");
		panelLeft.add(logInBtn);
		panelLeft.add(Box.createVerticalStrut(30));
		
		JPanel panelToHold = new JPanel();
		panelToHold.add(panelLeft);
		
		logInBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				   if(userNameArea.getText().equals("ADMIN") && passwrdArea.getText().equals("ADMIN")){
					   isLogInSuccessful=true;
					}
				   else{
					JOptionPane.showMessageDialog(null,"The username password combination is invalid.Please try again.","Error",JOptionPane.ERROR_MESSAGE);
					userNameArea.setText("");
					passwrdArea.setText("");
				}
			}
		});
		
		return panelToHold;
		
	}
	public JPanel operationsPage(){

		cardPanel = new JPanel(cardLayout);
		JButton addBtn = new JButton("Add");
		
		JPanel cardAdminLoggedIn = new JPanel();
		cardAdminLoggedIn.setBackground(Color.ORANGE);
		cardAdminLoggedIn.add(addBtn);
		cardPanel.add(cardAdminLoggedIn,"AdminLoggedIn");
		return cardPanel;
		
	}

}