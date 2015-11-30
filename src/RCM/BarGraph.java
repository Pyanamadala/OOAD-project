package RCM;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JPanel;
public class BarGraph extends JFrame { 
	RCMUsageStatistics rus= new RCMUsageStatistics();
	public BarGraph(){
		super ("Bar Chart");		
		setPreferredSize(new Dimension(800,800));
		setLocationRelativeTo(null);		
		BarChart chart = new BarChart();	
		
		chart.addBar(rus.getNoOfTimesLastEmptied("2015-11-23","2015-11-26"));
		
		getContentPane().setBackground(Color.CYAN  ); 
		getContentPane().add(chart);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	public static void main(String[] args)
	{
		new BarGraph();		
	}	
	
}
	class BarChart extends JPanel{
	private Color[] col= new Color[]{Color.red,Color.magenta,Color.blue,Color.green};
	String title=" Bar Graph ";
	
		private Map<Integer, Integer> bars =
	            new LinkedHashMap<Integer, Integer>();
		public BarChart()
		{
			setPreferredSize(new Dimension(500,500));
			
		}
		public void addBar(HashMap<Integer,Integer>noOfTimesEmptied)
		{
			System.out.println(noOfTimesEmptied);
			for(int key:noOfTimesEmptied.keySet())
			bars.put(key,noOfTimesEmptied.get(key));	
			repaint();
			
		   
		}

		@Override
		protected void paintComponent(Graphics g)
		{
			 Font titleFont = new Font("SansSerif", Font.BOLD, 20);
			    FontMetrics titleFontMetrics = g.getFontMetrics(titleFont);
			    Font labelFont = new Font("SansSerif", Font.PLAIN, 18);
			    FontMetrics labelFontMetrics = g.getFontMetrics(labelFont);

			    int titleWidth = titleFontMetrics.stringWidth(title);
			    int y = titleFontMetrics.getAscent();
			    int x = (getWidth() - titleWidth) / 2;
			    g.setFont(titleFont);
			    g.drawString(title, x, y);

			int max = Integer.MIN_VALUE;
			int min= Integer.MAX_VALUE;
			
			for (Integer value : bars.values())
			{
				max = Math.max(max, value);
				min=Math.max(min,value);
			}
		    int top = titleFontMetrics.getHeight();
		    int bottom = labelFontMetrics.getHeight();
		    y = getHeight() - labelFontMetrics.getDescent();
		    g.setFont(labelFont);


			int width = (getWidth() / bars.size()) - 250;
			int xOffset = 10;
			int i=0;
			for (int id : bars.keySet())
			{
				int value = bars.get(id);
				int height = (int)
	                            ((getHeight()-15) * ((double)value / max));
				g.setColor(col[i]);
				g.fillRect(xOffset, getHeight() - height-30, width, height);
				g.setColor(Color.black);
				g.drawRect(xOffset, getHeight() - height-30, width, height);
				int labelWidth = labelFontMetrics.stringWidth("Rcm"+id);
			      x = i * width + (width - labelWidth) / 2;
			      g.drawString("Rcm"+id, x, y);
				xOffset += (width + 2);
				i=i+1;
			}
		}
	}

