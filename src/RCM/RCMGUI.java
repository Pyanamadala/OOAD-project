package RCM;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.table.*;

public class RCMGUI extends JPanel {
	private static final int FRAME_WIDTH = 1500;
	private static final int FRAME_HEIGHT = 1000;
	private static final int rcmid=2;
	private Random random = new Random();
	private Set<String> recyclableItems;
	private JButton[] Btn;
	private Box panelItems;
	private String item,mode;
	private boolean isAnyOtherBtnDisabled = false;
	private JPanel panelDisp2;
	private DefaultTableModel data;
	private int row = 0;
	private String[][] displayList = new String[10][3];
	private JRadioButton cashBtn;
	private JRadioButton couponBtn;
	private JPanel paymentMode;
	private JPanel panelPay, panelDisp1,panel2;
	private JPanel panelRCM;
	private JPanel cardPanel;
	private CardLayout cardLayout;
	private JLabel displayLabel, labelWt;
	private JTable table;
	private double weight;
	private String weightUnits="Pounds";
	private JButton KgBtn,LbBtn;
	private String itemStatus; 
	private String amountMode=" ";
	boolean rcmstatus;
	RCM rcm;
    double price;
	public RCMGUI() {
		rcm = new RCM(rcmid);
		rcmstatus = rcm.checkRCMActivated();
		JPanel panel1 = new JPanel();
		
		panel2 = new JPanel();
		panel2.setBackground(Color.WHITE);
		
		BtnForweightInLbOrKg();
        
		createItemButtons();
		

		JButton acceptBtn = new JButton("");
		acceptBtn.setIcon(new ImageIcon("acceptImg.png"));
		acceptBtn.setOpaque(false);
	    acceptBtn.setContentAreaFilled(false);
	    acceptBtn.setBorderPainted(false);

		JLabel backGrnd = new JLabel();
		backGrnd.setPreferredSize(new Dimension(700, 700));
		backGrnd.setIcon(new ImageIcon("recycling_cc_674x906.png"));


		panelRCM = new JPanel(new GridLayout(0, 1));
		this.setBorder(
	            BorderFactory.createTitledBorder(
	                    BorderFactory.createEtchedBorder(
	                            EtchedBorder.RAISED, Color.GRAY
	                            , Color.DARK_GRAY),"Recycling Station"));
		panelRCM.setBackground(Color.WHITE);
		
		panelRCM.add(panelItems);

		displayLabel = new JLabel("");
		displayLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));

		panelDisp1 = new JPanel();
		panelDisp2 = new JPanel();
		panelDisp1.setBackground(Color.WHITE);
		panelDisp2.setBackground(Color.WHITE);
				

		JPanel cardHome = new JPanel();
		//cardHome.add(backGrnd);
		cardHome.setPreferredSize(new Dimension(800, 400));
		cardHome.setBackground(Color.WHITE);

		JButton goHomeBtn = new JButton("GoHome");
		goHomeBtn.setPreferredSize(new Dimension(90, 40));
		goHomeBtn.setIcon(new ImageIcon("homeImg.png"));
		
		cardLayout = new CardLayout();
		cardPanel = new JPanel(cardLayout);
		cardPanel.setBackground(Color.WHITE);

		labelWt = new JLabel();
		labelWt.setFont(new Font("SansSerif", Font.PLAIN, 20));

		JButton cancelBtn = new JButton();
		cancelBtn.setIcon(new ImageIcon("cancelImg.png"));
		cancelBtn.setOpaque(false);
	    cancelBtn.setContentAreaFilled(false);
	    cancelBtn.setBorderPainted(false);

		JButton doneBtn = new JButton();
		doneBtn.setIcon(new ImageIcon("doneImg.png"));
		doneBtn.setOpaque(false);
	    doneBtn.setContentAreaFilled(false);
	    doneBtn.setBorderPainted(false);

		JPanel reBtnPanel = new JPanel();
		reBtnPanel.setBackground(Color.WHITE);
		reBtnPanel.add(acceptBtn);
		reBtnPanel.add(cancelBtn);
		reBtnPanel.add(doneBtn);
		
		panelPay=new JPanel(new GridLayout(0,1));
		panelPay.setBackground(Color.WHITE);
		panelPay.add(labelWt);
		panelRCM.add(reBtnPanel);
		reBtnPanel.add(panelDisp1);
		reBtnPanel.add(panelDisp2);
		
        cardPanel.add(cardHome, "Home");
		cardPanel.add(panelRCM, "Recycle");
		

		JButton recycleBtn = new JButton();
		recycleBtn.setIcon(new ImageIcon("recycleImg.png"));
		recycleBtn.setOpaque(false);
	    recycleBtn.setContentAreaFilled(false);
	    recycleBtn.setBorderPainted(false);
		panel2.add(recycleBtn);
		cardHome.add(panel2, BorderLayout.PAGE_END);
		

		recycleBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rcm.recycle();
				cardLayout.show(cardPanel, "Recycle");
			}
		});
		acceptBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				for (int i = 0; i < Btn.length; i++) {
					if (Btn[i].isEnabled() == false) {
						item = Btn[i].getText();
						Btn[i].setEnabled(true);
					}
				}
				if (item.equals(" "))
					JOptionPane.showMessageDialog(null,
							"Please select the item type!");
				else {
					weight = generateRandWt();
					itemStatus = rcm.isItemAccepted(item, weight);
					if ((itemStatus.toUpperCase()).equals("INVALID")) {
						JOptionPane.showMessageDialog(
										null,
										"The item you are trying to recycle is not accepted as it is not recyclable.",
										"Error", JOptionPane.ERROR_MESSAGE);
					} else if ((itemStatus.toUpperCase()).equals("NO_CASH")) {
						
						if (amountMode.equals("COUPON"))
							ifItemAccepted();
						else {
							int userChoice = JOptionPane.showConfirmDialog(
									null,
									"There is no more cash in the machine.Would you like to avail coupons?",
									"Question", JOptionPane.YES_NO_OPTION);
							if (userChoice == JOptionPane.YES_OPTION) {
								amountMode = "COUPON";
								ifItemAccepted();
							} else if (userChoice == JOptionPane.NO_OPTION) {
								
								cardLayout.show(cardPanel, "Home");
							}
						}
					} else if ((itemStatus.toUpperCase()).equals("FULL")) {
						int userChoice = JOptionPane
								.showConfirmDialog(
										null,
										"Cannot accept items.Machine capacity reached.Vend out?",
										"Question", JOptionPane.YES_NO_OPTION);
						if (userChoice == JOptionPane.YES_OPTION) {
							if (amountMode=="CASH"){
						
							JOptionPane.showMessageDialog(null,"Total amount is $"+ rcm.getCumulativeAmount()+"\nTotal weight of items recycled = "+rcm.getCumulativeWeight()+weightUnits);
							createCashCouponDialog();
							recycleBtn.setEnabled(false);
							}
							cardLayout.show(cardPanel, "Home");
						} else if (userChoice == JOptionPane.NO_OPTION) {
							JOptionPane
									.showMessageDialog(null,
											"Please Collect your items from the slot.Have a nice day!");
							resetFields();
						}
					} else {
						amountMode = "CASH";
						ifItemAccepted();
					}
				}
				item = " ";
			}
		});
		cancelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetFields();
			}
		});
		doneBtn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,"Total amount is $"+ rcm.getCumulativeAmount()+"\nTotal weight of items recycled = "+rcm.getCumulativeWeight()+weightUnits);
				createCashCouponDialog();
				if (mode=="CASH") {
					rcm.endSession("CASH");
					JOptionPane.showMessageDialog(null,
							"Please collect your cash from the Slot");
				} else if (mode=="COUPON") {
					rcm.endSession("COUPON");
					JOptionPane.showMessageDialog(null,
							"Please collect your coupons from the Slot");
			}
				resetFields();
		  }
		});
		createTableToDisplay();
		this.setLayout(new BorderLayout());
		panel1.setBackground(Color.WHITE);
		add(panel1, BorderLayout.PAGE_START);
		add(cardPanel, BorderLayout.CENTER);
		setOpaque(false);
		this.setBackground(Color.WHITE);
		setVisible(true);
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
	}

	private void BtnForweightInLbOrKg() {
		
		KgBtn = new JButton();
		KgBtn.setIcon(new ImageIcon("kg.png"));
		KgBtn.setOpaque(false);
	    KgBtn.setContentAreaFilled(false);
	    KgBtn.setBorderPainted(false);
	    
		LbBtn = new JButton();
		LbBtn.setIcon(new ImageIcon("lb.png"));
		LbBtn.setOpaque(false);
	    LbBtn.setContentAreaFilled(false);
	    LbBtn.setBorderPainted(false);
	    
		KgBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
			KgBtn.setEnabled(false);
			weightUnits="Kgs";
			LbBtn.setEnabled(true);
			}
		});
		LbBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
			LbBtn.setEnabled(false);
			weightUnits="Pounds";
			KgBtn.setEnabled(true);
			}
		});
		
		Font font = new Font(("SansSerif"), Font.PLAIN, 20);
		
		JLabel wtUnitLabel = new JLabel("Select");
		wtUnitLabel.setFont(font);
		JPanel wtUnitPanel = new JPanel();
		wtUnitPanel.add(wtUnitLabel);
		wtUnitPanel.setBackground(Color.WHITE);
		wtUnitPanel.add(LbBtn);
		wtUnitPanel.add(KgBtn);
		panel2.add(wtUnitPanel);
	}

	private double generateRandWt() {
		int maxWt = 8;
		int minWt = 4;
		return (random.nextInt(maxWt - minWt + 1) + minWt)*0.834;
	}

	private void createItemButtons() {
		HashMap<String, Double> h = rcm.displayItems();
		recyclableItems = h.keySet();
		panelItems = Box.createVerticalBox();
		Btn = new JButton[recyclableItems.size()];
		Iterator<String> iterator = recyclableItems.iterator();
		for (int i = 0; i < Btn.length; i++) {
			Btn[i] = new JButton(iterator.next());
			Btn[i].setPreferredSize(new Dimension(200, 35));
			panelItems.add(Box.createVerticalStrut(15));
			panelItems.add(Box.createHorizontalStrut(45));
			panelItems.add(Btn[i]);
			Btn[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JButton source = (JButton) e.getSource();
					for (int i = 0; i < Btn.length; i++) {
						if (source == Btn[i]) {
							Btn[i].setEnabled(false);
							isAnyOtherBtnDisabled = true;
						}
						if (isAnyOtherBtnDisabled == true) {
							for (i = 0; i < Btn.length; i++) {
								if (Btn[i] != source) {
									Btn[i].setEnabled(true);
									isAnyOtherBtnDisabled = false;
								}
							}
						}

					}
				}
			});
		}
		panelItems.add(Box.createVerticalStrut(20));
	}

	private void createTableToDisplay() {
		String[] columns = { "ItemType", "Weight", "ItemValue($)" };
		data = new DefaultTableModel(0, 0);
		table = new JTable();
		table.setFont(new Font("Serif", Font.PLAIN, 16));
		data.setColumnIdentifiers(columns);
		table.setModel(data);
		TableColumn column = null;
	    for (int i = 0; i < 3; i++) {
	        column = table.getColumnModel().getColumn(i);
	            column.setPreferredWidth(150); 
	    }    
	}

	private void ifItemAccepted() {
		if(data.getRowCount()>1){
		data.removeRow(data.getRowCount() - 1);}
		panelDisp2.setLayout(new BorderLayout());
		panelDisp2.setBackground(Color.WHITE);
		panelDisp2.add(table.getTableHeader(), BorderLayout.PAGE_START);
		panelDisp2.add(table, BorderLayout.CENTER);
		table.setShowGrid(false);
		table.setShowVerticalLines(true);
		price = rcm.calculateAmount(item, weight);
		if(weightUnits=="Kgs"){
			weight = weight * 0.453592;	
		}
		labelWt.setText("Weight of the " + item + " item : " + weight
				+ weightUnits);
		panelPay.add(displayLabel);
		panelDisp1.add(panelPay);
		displayLabel.setText("Value of " + item + " item = $" + price);
		data.addRow(new Object[]{item,weight,price });
	
		String cumulative = String.valueOf(rcm.addItem(item,
				weight, price));
		data.addRow(new Object[] { "", "Total", cumulative });
		
		cardLayout.show(cardPanel, "Recycle");

	}
	private void createCashCouponDialog(){
		if(amountMode=="CASH"){
		Object[] options={"CASH","COUPON"};
		int m=JOptionPane.showOptionDialog(null,"Mode of payment.","Select an option",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
		if (m==JOptionPane.YES_OPTION){
			mode="CASH";
		}
		else{
			mode="COUPON";
		}
		}
	else{
		rcm.endSession("COUPON");
		JOptionPane.showMessageDialog(null,
				"Please collect your coupons from the Slot");
		resetFields();
}
	}
	private void resetFields(){
		displayList = new String[10][3];
		KgBtn.setEnabled(true);
		LbBtn.setEnabled(true);
		labelWt.setText("");
		displayLabel.setText("");
		data = (DefaultTableModel) table.getModel();
		data.setRowCount(0);
		cardLayout.show(cardPanel, "Home");
		rcm=new RCM(rcmid);
	}


}