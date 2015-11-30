package RCM;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.*;
import javax.swing.border.EtchedBorder;

public class RMOSGUI extends JPanel{
	RMOSGUI(){
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.RED, Color.black),"EcoRe Monitoring Station"));
		this.setBackground(Color.white);
	}

}