package report6;


import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RoomStatus extends JFrame{

	private JButton[][] buttons = new JButton[8][7];
	private Object selRoomName;
	private Object selDay;
	private Object selTime;
	
	public RoomStatus(ArrayList<ReservationRecord> reservList, String situation) {
		ReservationChart getChart = new ReservationChart(reservList);
		String[][][] chart = getChart.GetChart();
		selRoomName = (Object)situation;
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
		
		// 예약 내역 표시를 위한 버튼 생성
		JPanel p3 = new JPanel();
		p3.setLayout(new GridLayout(8,7));
		int reservRoomIndex = getChart.GetRoomIndex(situation);
		for(int i = 0; i<chart[reservRoomIndex].length; i++){
			for(int j = 0; j<chart[reservRoomIndex][0].length; j++){
				if(chart[reservRoomIndex][i][j] == null){
					buttons[i][j] = new JButton("");
					p3.add(buttons[i][j]);
					
				}
				else{

					buttons[i][j] = new JButton(chart[reservRoomIndex][i][j]);
					p3.add(buttons[i][j]);
				}
			}
		}
		
		for(int i = 0; i<8; i++){
			for(int j = 0; j<7; j++){
				buttons[i][j].addActionListener(new ButtonListener());
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
	
	// 예약 상황의 버튼을 누르면 빈 칸을 예약 내용의 JComboBox로 변경
	
	class ButtonListener implements ActionListener{
		Object[] day = {"sun","mon","tue","wed","thr","fri","sat"};
		Object[] time = {"1", "2", "3", "4", "5", "6", "7", "8"};

		public void actionPerformed(ActionEvent e){
			for(int i = 0; i<8; i++){
				for(int j = 0; j<7; j++){
					if(e.getSource() == buttons[i][j]){
						
						// 누른 버튼의 요일, 시간 내용 저장
						selTime = time[i];
						selDay = day[j];
						
						// 버튼이 비어있는경우 -> 예약 내용으로 바꾸기
						if(buttons[i][j].getText() == ""){
							ReservationGUI.roomNum.setSelectedItem(selRoomName);
							ReservationGUI.reservDay.setSelectedItem(selDay);
							ReservationGUI.reservTime.setSelectedItem(selTime);
						}
						
						// 버튼이 예약되어있는 경우 -> 예약 취소할 table 선택
						else{
							for(int k = 0; k<ReservationGUI.p3.getRowCount(); k++){
								if(ReservationGUI.p3.getValueAt(k,0).equals(selRoomName)
										&& ReservationGUI.p3.getValueAt(k, 1).equals(selDay)
										&& ReservationGUI.p3.getValueAt(k, 2).equals(selTime)){
									ReservationGUI.p3.setRowSelectionInterval(k, k);
								}
							}
						}
					}
				}
			}
		}
	}
}
