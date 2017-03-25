package report6;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class AvailableTime extends JFrame {
	
	public AvailableTime(ArrayList<ReservationRecord> reservList){
		ReservationChart getChart = new ReservationChart(reservList);
		String[][][] chart = getChart.GetChart();
		// 요일에 대한 Label
		// GridLayout(1,7)
		
		JPanel p1 = new JPanel();
		p1.setLayout(new GridLayout(1,7));
		p1.add(new JLabel("SUN"));
		p1.add(new JLabel("MON"));
		p1.add(new JLabel("TUE"));
		p1.add(new JLabel("WED"));
		p1.add(new JLabel("THR"));
		p1.add(new JLabel("FRI"));
		p1.add(new JLabel("SAT"));
		
		
		// 시간에 대한 Label
		// GridLayout(9,1)
		
		JPanel p2 = new JPanel();
		p2.setLayout(new GridLayout(9,1));

		
		p2.add(new JLabel(""));
		p2.add(new JLabel("1"));
		p2.add(new JLabel("2"));
		p2.add(new JLabel("3"));
		p2.add(new JLabel("4"));
		p2.add(new JLabel("5"));
		p2.add(new JLabel("6"));
		p2.add(new JLabel("7"));
		p2.add(new JLabel("8"));

		// 예약 가능 표시를 위한 버튼 생성
		JPanel p3 = new JPanel();
		p3.setLayout(new GridLayout(8,7));

		for(int i = 0; i<chart[0].length; i++){
			for(int j = 0; j<chart[0][0].length; j++){
				if(chart[0][i][j] == null
						|| chart[1][i][j] == null 
						|| chart[2][i][j] == null){
					p3.add(new JButton("O"));
				}
				else{
					p3.add(new JButton("X"));
				}
			}
		}
		
		// p1와 p3를 Grouping
		JPanel p4 = new JPanel(new BorderLayout());
		p4.add(p1, BorderLayout.NORTH);
		p4.add(p3, BorderLayout.CENTER);
		add(p4);
		
		// p2과 p4를 Grouping
		JPanel p = new JPanel(new BorderLayout());
		p.add(p2, BorderLayout.WEST);
		p.add(p4, BorderLayout.CENTER);
		add(p);
	}
}
