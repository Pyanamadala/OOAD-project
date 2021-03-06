package RCM;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.*;

public class RCMGUI extends JFrame {
	private static final int FRAME_WIDTH = 1500;
	private static final int FRAME_HEIGHT = 1000;
	private Random random = new Random();
	private Set<String> recyclableItems;
	private JButton[] Btn;
	private Box panelItems;
	private String item;
	private boolean isAnyOtherBtnDisabled = false;
	private JPanel panelDisp2;
	private DefaultTableModel data;
	private int row = 0;
	private String[][] displayList = new String[10][3];
	private JRadioButton cashBtn;
	private JRadioButton couponBtn;
	private JPanel paymentMode;
	private JPanel panelPay, panelDisp1;
	private JPanel panelRe;
	private JPanel cardPanel;
	private CardLayout cardLayout;
	private JLabel displayLabel, labelWt;
	private JTable table;
	private double weight, totAmount;
	private String weightUnits;
	private JRadioButton wtInKgBtn, wtInLbBtn;
	private String itemStatus; 
	private String amountMode=" ";
	RCM rcm;

	public RCMGUI() {
		super("Recycling Machine");
		rcm = new RCM(1);
		Container container = getContentPane();

		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();

		createItemButtons();

		JLabel queryLabel = new JLabel(
				"Items that are recyclable and their prices per pound are as follows :");
		queryLabel.setFont(new Font("Serif", Font.BOLD, 30));

		JTextArea queryText = new JTextArea("");
		queryText.setFont(new Font("Serif", Font.BOLD, 30));
		queryText.setPreferredSize(new Dimension(400, 200));
		queryText
				.append("\n 1.Paper          -  $1/lb \n 2.Glass           -  $2/lb \n 3.Aluminium  -  $4/lb\n");

		JLabel itemLabel = new JLabel("Item Type");
		itemLabel.setForeground(Color.BLACK);
		itemLabel.setFont(new Font("Serif", Font.BOLD, 30));

		JButton acceptBtn = new JButton("Accept Item");
		acceptBtn.setPreferredSize(new Dimension(140, 40));
		acceptBtn.setIcon(new ImageIcon("acceptImg.png"));

		JLabel backGrnd = new JLabel();
		backGrnd.setPreferredSize(new Dimension(700, 700));
		backGrnd.setIcon(new ImageIcon("recycling_cc_674x906.png"));

		JButton homeBtn = new JButton("Home");
		homeBtn.setPreferredSize(new Dimension(90, 40));
		homeBtn.setIcon(new ImageIcon("homeImg.png"));

		panelRe = new JPanel(new GridLayout(0, 1));
		panelRe.add(panelItems);

		displayLabel = new JLabel("");
		displayLabel.setFont(new Font("Serif", Font.PLAIN, 25));

		JPanel panelDisp = new JPanel(new GridLayout(2, 1));
		panelDisp1 = new JPanel();
		panelDisp2 = new JPanel();
		panelDisp.add(panelDisp1);
		panelDisp.add(panelDisp2);

		JPanel cardRe = new JPanel(new GridLayout(1, 2));
		cardRe.add(panelRe);
		cardRe.add(panelDisp);

		JPanel cardQu = new JPanel(new BorderLayout());
		cardQu.add(queryLabel, BorderLayout.PAGE_START);
		cardQu.add(queryText);

		JPanel cardHome = new JPanel();
		cardHome.add(backGrnd);
		cardHome.setPreferredSize(new Dimension(800, 400));

		JButton goHomeBtn = new JButton("GoHome");
		goHomeBtn.setPreferredSize(new Dimension(90, 40));
		goHomeBtn.setIcon(new ImageIcon("homeImg.png"));

		JPanel homePanel = new JPanel();
		homePanel.add(goHomeBtn);
		cardQu.add(homePanel, BorderLayout.PAGE_END);

		cardLayout = new CardLayout();
		cardPanel = new JPanel(cardLayout);

		JLabel label = new JLabel("Welcome to EcoRe Recycling Station!!",
				SwingConstants.CENTER);
		label.setFont(new Font("Serif", Font.BOLD, 40));

		labelWt = new JLabel();
		labelWt.setFont(new Font("Serif", Font.PLAIN, 25));

		JButton moreBtn = new JButton();
		moreBtn.setIcon(new ImageIcon("moreImg.png"));
		moreBtn.setPreferredSize(new Dimension(130, 40));

		JButton cancelBtn = new JButton();
		cancelBtn.setIcon(new ImageIcon("cancelImg.png"));
		cancelBtn.setPreferredSize(new Dimension(100, 40));

		JButton doneBtn = new JButton();
		doneBtn.setIcon(new ImageIcon("doneImg.png"));
		doneBtn.setPreferredSize(new Dimension(100, 40));

		JPanel reBtnPanel = new JPanel();
		reBtnPanel.add(acceptBtn);
		reBtnPanel.add(cancelBtn);
		reBtnPanel.add(doneBtn);

		BtnForweightInLbOrKg();

		panelDisp1.add(labelWt);
		panelRe.add(reBtnPanel);

		createCashCouponBtns();

		JPanel cardDone = new JPanel(new BorderLayout());

		cardDone.add(paymentMode, BorderLayout.CENTER);
		// cardDone.add(panelPay, BorderLayout.PAGE_START);

		cardPanel.add(cardHome, "Home");
		cardPanel.add(cardQu, "Query");
		cardPanel.add(cardRe, "Recycle");
		cardPanel.add(cardDone, "Done");

		JButton adminBtn = new JButton();
		adminBtn.setIcon(new ImageIcon("adminImg.png"));
		adminBtn.setPreferredSize(new Dimension(100, 40));

		JButton recycleBtn = new JButton();
		recycleBtn.setPreferredSize(new Dimension(100, 40));
		recycleBtn.setIcon(new ImageIcon("recycleImg.png"));

		JButton queryBtn = new JButton();
		queryBtn.setPreferredSize(new Dimension(100, 40));
		queryBtn.setIcon(new ImageIcon("queryImg.png"));

		panel1.add(label);
		panel2.add(adminBtn);
		panel2.add(recycleBtn);
		panel2.add(queryBtn);
		cardHome.add(panel2, BorderLayout.PAGE_END);

		recycleBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(cardPanel, "Recycle");
			}
		});
		queryBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(cardPanel, "Query");
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
					if (wtInLbBtn.isSelected()) {
						weight = generateRandWt();
						weightUnits = "Pounds";
					} else if (wtInKgBtn.isSelected()) {
						weight = generateRandWt() * 0.453592;
						weightUnits = "Kgs";
					}
					System.out.println(item);
					System.out.println(weight);
					itemStatus = rcm.isItemAccepted(item, weight);
					System.out.println(itemStatus);
					if ((itemStatus.toUpperCase()).equals("INVALID")) {
						JOptionPane
								.showMessageDialog(
										null,
										"The item you are trying to recycle is not accepted as it is not recyclable.",
										"Error", JOptionPane.ERROR_MESSAGE);
					} else if ((itemStatus.toUpperCase()).equals("NO_CASH")) {
						if (amountMode.equals("COUPONS"))
							ifItemAccepted();
						else {
							int userChoice = JOptionPane.showConfirmDialog(
									null,
									"There is no more cash in the machine.Would you like to avail coupons?",
									"Question", JOptionPane.YES_NO_OPTION);
							if (userChoice == JOptionPane.YES_OPTION) {
								amountMode = "COUPONS";
								ifItemAccepted();
							} else if (userChoice == JOptionPane.NO_OPTION) {
								cardLayout.show(cardPanel, "Done");
							}
						}
					} else if ((itemStatus.toUpperCase()).equals("FULL")) {
						int userChoice = JOptionPane
								.showConfirmDialog(
										null,
										"Cannot accept items.Machine capacity reached.Vend out?",
										"Question", JOptionPane.YES_NO_OPTION);
						if (userChoice == JOptionPane.YES_OPTION) {
							cardLayout.show(cardPanel, "Done");
						} else if (userChoice == JOptionPane.NO_OPTION) {
							JOptionPane
									.showMessageDialog(null,
											"Please Collect your items from the slot.Have a nice day!");
							cardLayout.show(cardPanel, "Home");
						}
					} else {
						amountMode = "CASH";
						ifItemAccepted();
					}
				}
				item = " ";
			}
		});
		homeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(cardPanel, "Home");
			}
		});
		goHomeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(cardPanel, "Home");
			}
		});
		cancelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displayList = new String[10][3];
				labelWt.setText("");
				displayLabel.setText("");
				data = (DefaultTableModel) table.getModel();
				data.setRowCount(0);
				cardLayout.show(cardPanel, "Home");
			}
		});
		moreBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(cardPanel, "Recycle");
			}
		});
		doneBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JLabel totalValue = new JLabel("Total amount is $"
						+ rcm.getCumulativeAmount());
				totalValue.setFont(new Font("Serif", Font.BOLD, 25));
				totalValue.setForeground(Color.BLUE);

				panelPay = new JPanel();
				panelPay.add(totalValue);
				cardLayout.show(cardPanel, "Done");
				cardDone.add(panelPay, BorderLayout.PAGE_START);

			}
		});
		container.add(panel1, BorderLayout.PAGE_START);
		container.add(cardPanel, BorderLayout.CENTER);

		setVisible(true);
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
	}

	private void BtnForweightInLbOrKg() {
		wtInKgBtn = new JRadioButton("Weight in kgs");
		wtInLbBtn = new JRadioButton("Weight in lbs");
		wtInLbBtn.setSelected(true);
		Font font = new Font(("SansSerif"), Font.PLAIN, 20);
		wtInKgBtn.setFont(font);
		wtInLbBtn.setFont(font);
		ButtonGroup BtnGrp = new ButtonGroup();
		BtnGrp.add(wtInKgBtn);
		BtnGrp.add(wtInLbBtn);
		JLabel wtUnitLabel = new JLabel("Select to display ");
		wtUnitLabel.setFont(font);
		JPanel wtUnitPanel = new JPanel();
		wtUnitPanel.add(wtUnitLabel);
		wtUnitPanel.add(wtInLbBtn);
		wtUnitPanel.add(wtInKgBtn);
		panelItems.add(Box.createVerticalStrut(40));
		panelItems.add(wtUnitPanel);
		panelItems.add(Box.createVerticalStrut(50));
	}

	private double generateRandWt() {
		int maxWt = 10;
		int minWt = 4;
		return (random.nextInt(maxWt - minWt + 1) + minWt);
	}

	private void createItemButtons() {
		HashMap<String, Double> h = rcm.displayItems();
		recyclableItems = h.keySet();
		panelItems = Box.createVerticalBox();
		Btn = new JButton[recyclableItems.size()];
		Iterator<String> iterator = recyclableItems.iterator();
		for (int i = 0; i < Btn.length; i++) {
			Btn[i] = new JButton(iterator.next());
			Btn[i].setPreferredSize(new Dimension(400, 80));
			panelItems.add(Box.createVerticalStrut(25));
			panelItems.add(Box.createHorizontalStrut(25));
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
	}

	private void createTableToDisplay(String item, double weight) {
		String[] columns = { "ItemType", "Weight", "Total Value($)" };
		data = new DefaultTableModel(0, 0);
		table = new JTable();
		table.setFont(new Font("Serif", Font.PLAIN, 16));
		data.setColumnIdentifiers(columns);
		table.setModel(data);
		panelDisp2.setLayout(new BorderLayout());
		panelDisp2.add(table.getTableHeader(), BorderLayout.PAGE_START);
		panelDisp2.add(table, BorderLayout.CENTER);
		table.setShowGrid(false);
	}

	private void createCashCouponBtns() {
		cashBtn = new JRadioButton("Cash");
		couponBtn = new JRadioButton("Coupon");
		Font font = new Font(("SansSerif"), Font.BOLD, 25);
		cashBtn.setFont(font);
		couponBtn.setFont(font);

		JLabel modeOfPayment = new JLabel("Select the mode of payment :");
		modeOfPayment.setFont(new Font("Serif", Font.BOLD, 25));

		JButton vendBtn = new JButton("Vend");
		vendBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cashBtn.isSelected()) {
					rcm.endSession("CASH");
					JOptionPane.showMessageDialog(null,
							"Please collect your cash from the Slot");
					cardLayout.show(cardPanel, "Home");
				} else if (couponBtn.isSelected()) {
					rcm.endSession("COUPON");
					JOptionPane.showMessageDialog(null,
							"Please collect your coupons from the Slot");
					cardLayout.show(cardPanel, "Home");
				} else {
					JOptionPane.showMessageDialog(null,
							"Please select cash or coupon");
				}
				displayList = new String[10][3];
				labelWt.setText("");
				displayLabel.setText("");
				data = (DefaultTableModel) table.getModel();
				data.setRowCount(0);

			}
		});

		paymentMode = new JPanel(new GridLayout(0, 1));
		paymentMode.add(modeOfPayment);
		paymentMode.add(cashBtn);
		paymentMode.add(couponBtn);
		JPanel vendPanel = new JPanel();
		vendPanel.add(vendBtn);
		paymentMode.add(vendPanel);

		JButton goBackBtn = new JButton("Go Back");
		vendPanel.add(goBackBtn);
		goBackBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(cardPanel, "Recycle");
			}
		});

		ButtonGroup group = new ButtonGroup();
		group.add(cashBtn);
		group.add(couponBtn);
	}

	private void ifItemAccepted() {
		double price = rcm.calculateAmount(item, weight);
		labelWt.setText("Weight of the " + item + " item : " + weight
				+ weightUnits);
		panelDisp1.add(displayLabel);
		displayLabel.setText("Value of " + item + " item = $" + price);
		createTableToDisplay(item, weight);

		for (int j = 0; j < 3; j++) {
			displayList[row][j] = new String();
		}
		displayList[row][0] = item;
		displayList[row][1] = Double.toString(weight);
		displayList[row][2] = Double.toString(price);
		for (int i = 0; i <= row; i++) {
			for (int j = 0; j < 3; j++) {
			}
		}
		for (int k = 0; k <= row; k++) {
			data.addRow(new Object[] { displayList[k][0], displayList[k][1],
					displayList[k][2] });
		}
		String cumulative = String.valueOf(rcm.addItem(amountMode, item,
				weight, price));
		data.addRow(new Object[] { "", "Total", cumulative });
		row = row + 1;
		cardLayout.show(cardPanel, "Recycle");

	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				RCMGUI gUI = new RCMGUI();
				gUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
		});
	}

}
