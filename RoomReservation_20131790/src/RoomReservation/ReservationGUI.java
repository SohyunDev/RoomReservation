package report6;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.ArrayList;
import java.io.*;

public class ReservationGUI extends JFrame {
	
	// JButton 변수 생성
	private JButton validTime = new JButton("예약가능시간");
	private JButton reservSituation = new JButton("방별예약상황");
	private JButton reserv = new JButton("예약");
	private JButton cancel = new JButton("취소");
	
	// JComboBox와 JTextField 생성 
	// 방은 Room514, Room515, Room516 3개가 있다고 가정9
	public static JTable p3;
	private static String[] room = {"Room514","Room515", "Room516"};
	public static JComboBox<String> roomNum = new JComboBox<String>(room);
	private static String[] day = {"sun","mon","tue","wed","thr","fri","sat"};
	public static JComboBox<String> reservDay = new JComboBox<String>(day);
	private static String[] time = {"1", "2", "3", "4", "5", "6", "7", "8"};
	public static JComboBox<String> reservTime = new JComboBox<String>(time);
	private JComboBox<String> situation = new JComboBox<String>(room);
	public JTextField reservName = new JTextField();
	public JTextField reservMemo = new JTextField();
	
	// RoomReservation에서 예약 파일에 저장되어있던 내용을 불러온다.
	private static RoomReservation dataFile = new RoomReservation();
	private static ArrayList <ReservationRecord> reservList = dataFile.Get_Real_Records(); 
	
	public ReservationGUI() {
		
		// 호실, 요일, 시간, 예약자, 메모에 대한 Label과 JComboBox, JTextField 추가
		// GridLayout(2,5) 2행 5열
		JPanel p1 = new JPanel();
		p1.setLayout(new GridLayout(2, 5));

		p1.add(new JLabel("호실"));
		p1.add(new JLabel("요일"));
		p1.add(new JLabel("시간"));
		p1.add(new JLabel("예약자"));
		p1.add(new JLabel("메모"));
		p1.add(roomNum);
		p1.add(reservDay);
		p1.add(reservTime);
		p1.add(reservName);
		p1.add(reservMemo);

		// "예약 상황"이라고 JTextField 생성
		JPanel p2 = new JPanel(new BorderLayout());
		p2.add(new JTextField("예약 상황"));
    
		// JTable 생성
		DefaultTableModel model = new DefaultTableModel();
		model.setColumnCount(5);
		p3 = new JTable(model);	
		
		// RoomReservation(datafile)에 저장되어있는 예약사항을 JTable에 표시
		for(int i = 0; i<reservList.size(); i++){
			String[] input = new String[5];
			for(int j=0; j<input.length; j++){
				input[j] = null;
			}
			input[0] = reservList.get(i).room_num;
			input[1] = reservList.get(i).day;
			input[2] = String.valueOf(reservList.get(i).time + 1);
			input[3] = reservList.get(i).name;
			input[4] = reservList.get(i).memo;
			model.addRow(input);
		}
 
		// 예약가능시간 버튼 생성
    	JPanel p4 = new JPanel(new BorderLayout());
    	p4.add(validTime);
    
    	// 방을 선택할 JComboBox, 예약상황버튼 JButton
    	// FlowLayout으로 글자간격 5, 줄간격 10
    	JPanel p5 = new JPanel();
    	p5.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 10));
    	p5.add(situation);
    	p5.add(reservSituation);
    	
    	// 예약, 취소버튼 생성
    	// GridLayout(2,1) 2행 1열
    	JPanel p6 = new JPanel();
    	p6.setLayout(new GridLayout(2,1));
    	p6.add(reserv);
    	p6.add(cancel);
    	
    	// reserv, cancel를 클릭하면 ReservListener, CancelListener가 각각 실행
    	reserv.addActionListener(new ReservListener(p3, roomNum, reservDay, reservTime, reservName, reservMemo));
    	cancel.addActionListener(new CancelListener(p3));
    	
    	// validTime, reservSituation을 클릭하면  ValidTimeListener와 ReservationStatus가 각각 실행
    	validTime.addActionListener(new ValidTimeListener(reservList));
    	reservSituation.addActionListener(new ReservationStatus(reservList, situation));
    	
    	// p1을 NORTH, p2를 CENTER로 배치 & Grouping
    	JPanel p7 = new JPanel(new BorderLayout());
    	p7.add(p1, BorderLayout.NORTH);
    	p7.add(p2, BorderLayout.CENTER);
    	add(p7);
    	
    
    	// p7을 NORTH, p3를 CENTER로 배치 & Grouping
    	JPanel p8 = new JPanel(new BorderLayout());
    	p8.add(p7, BorderLayout.NORTH);
    	p8.add(p3, BorderLayout.CENTER);
    	add(p8);
    
    	// p4를 CENTER, p5를 EAST로 배치 & Grouping
    	JPanel p9 = new JPanel(new BorderLayout());
    	p9.add(p4, BorderLayout.CENTER);
    	p9.add(p5, BorderLayout.EAST);
    	add(p9);
    
    	// p8를 CENTER, p6를 EAST로 배치 & Grouping    	
    	JPanel p10 = new JPanel(new BorderLayout());
    	p10.add(p8, BorderLayout.CENTER);
    	p10.add(p6, BorderLayout.EAST);
    	add(p10);
    
    	// p10을 CENTER, p9를 SOUTH로 배치 & Grouping
    	JPanel p = new JPanel(new BorderLayout());
    	p.add(p10, BorderLayout.CENTER);
    	p.add(p9, BorderLayout.SOUTH);
    	add(p);
    }
	public ArrayList<ReservationRecord> Get_Chart_List(){
		return reservList;
	}

	//예약 버튼을 누르면 새로운 예약사항을 JTable에 표시하고 예약사항 파일을 갱신
	class ReservListener implements ActionListener{
		JTable table;
		JComboBox roomNum, reservDay, reservTime;
		JTextField reservName, reservMemo;
		
		// 새로운 예약 사항을 저장
		ReservListener(JTable table, JComboBox roomNum, JComboBox reservDay, 
				JComboBox reservTime, JTextField reservName, JTextField reservMemo){
			this.table = table;
			this.roomNum = roomNum;
			this.reservDay = reservDay;
			this.reservTime = reservTime;
			this.reservName = reservName;
			this.reservMemo = reservMemo;
		}
			
			
		public void actionPerformed(ActionEvent e){

			// 예약사항을 String 배열에 저장
			String[] input = new String [5];
			input[0] = (String)roomNum.getSelectedItem();
			input[1] = (String)reservDay.getSelectedItem();
			input[2] = (String)reservTime.getSelectedItem();
			input[3] = reservName.getText();
			input[4] = reservMemo.getText();
					
			// ReservationRecord에 저장
			ReservationRecord newReservation = new ReservationRecord(input);
					
			// 에약 리스트에 추가 , 같은 것이 있을 경우 추가 X
			boolean conflict = false;
			for(int i = 0; i<table.getRowCount(); i++){
				if(table.getValueAt(i, 0).equals(input[0]) && table.getValueAt(i, 1).equals(input[1]) && table.getValueAt(i, 2).equals(input[2])){
						System.out.println("Your new reservation conflicted");
						conflict = true;
				}
			}
			// 같은것이 없으면 JTable 예약 리스트에 추가 & 예약파일 갱신
			if(!conflict){
				
				// JTable 예약 리스트에 추가
				DefaultTableModel model = (DefaultTableModel) table.getModel();
				model.addRow(input);
					
				// reservList에 추가
				reservList.add(newReservation);
					
					
				// 예약 파일 갱신
				BufferedWriter bw;
				try {
					bw = new BufferedWriter(new FileWriter("roomreserve-norm.data",true));
					PrintWriter pw = new PrintWriter(bw,true);
					String line = input[0]+":"+input[1]+":"+input[2]+":"+input[3]+":"+input[4];
					pw.println();
					pw.write(line);
					pw.flush();
					pw.close();
				} 
				catch (IOException e1) {
					e1.printStackTrace();
				}
			}		
				
		}
	}
	
	//예약 버틍을 누르면 새로운 예약사항을 JTable에 표시하고 예약사항 파일을 갱신
	class CancelListener implements ActionListener{
		JTable table;
			
		CancelListener(JTable table){
			this.table = table;
		}
			
		public void actionPerformed(ActionEvent e){
				
			// datafile에서 선택한 예약사항의 line을 지운다
			int row = table.getSelectedRow();
			File file = new File("roomreserve-norm.data");
			String dummy = "";
				
			// 지울 내용을 저장한다 (비교해서 지울때 필요)
			// 강의실 번호
			String delRoom = table.getValueAt(row, 0).toString();
			// 요일
			String delDay = table.getValueAt(row, 1).toString().toLowerCase();
			// 시간
			int delTime = (Integer.parseInt(table.getValueAt(row, 2).toString())-1);
			
			try{
				BufferedReader search = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
					
				String searchLine = search.readLine();
				int position = -1;
					
				// 제거할 line의 위치 찾기
				for(int i = 0; searchLine != null; i++){
					if (!searchLine.startsWith("//") && searchLine.length()!=0){
						String[] tokens = searchLine.split(":");
						String[] data = new String[5];
						if(tokens.length == 5){
							data[0] = tokens[0].trim();
							data[1] = tokens[1].trim().toLowerCase();
							data[2] = tokens[2].trim();
							data[3] = tokens[3].trim();
							data[4] = tokens[4].trim();
						}
						else if(tokens.length == 4){
							data[0] = tokens[0].trim();
							data[1] = tokens[1].trim().toLowerCase();
							data[2] = tokens[2].trim();
							data[3] = tokens[3].trim();
						}
						
						if((data[0].equals(delRoom)) 
								&& (data[1].equals(delDay))
								&& (Integer.parseInt(data[2])-1 == delTime)){
							position = i;
						}
					}
					searchLine = search.readLine();
				}
				search.close();
				
				// 일치하는 예약 사항의 line을 찾지 못했을 경우
				if(position == -1){
					System.out.println("There is no statement");
				}
				else{
					
					// 제거할 예약사항을 제외하고 내용을 저장한다.
					String line;
					BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
											
					for(int i=0; i<position; i++){
						line = br.readLine();
						dummy += (line+ "\r\n");
					}
					String delData = br.readLine();
					while((line = br.readLine()) != null){
						dummy += (line+ "\r\n");
					}
					
					// 원래 datafile에 내용을 덮어씌운다.
					FileWriter fw = new FileWriter("roomreserve-norm.data");
					fw.write(dummy);
					
					fw.close();
					br.close();
				}
			}
			catch(Exception ex){
				ex.printStackTrace();
			}
			
			// JTable에서 선택한 예약을 제거
			if(row == -1){
				return;
			}
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			model.removeRow(row);
			
			
			// reservList의 어디에 있는지 찾아낸다.
			int where = -1;
			for(int i = 0; i<reservList.size(); i++){
				if((reservList.get(i).room_num.equals(delRoom))
						&&(reservList.get(i).day.equals(delDay))
						&& (reservList.get(i).time == delTime )){
					where = i;
				}
			}
				
			// reservList에서 지우기
			if(where == -1){
				System.out.println("Can't find reservList");
			}
			else{
				reservList.remove(where);
			}
		}
	}
		
	class ValidTimeListener implements ActionListener{
		AvailableTime valid;
		ArrayList<ReservationRecord> reservList;
		ValidTimeListener(ArrayList<ReservationRecord> reservList){
			this.reservList = reservList;
		}
		@Override
		public void actionPerformed(ActionEvent e){
			valid = new AvailableTime(reservList); 
			valid.setTitle("AvailableTime_20131790_권소현");
			valid.setSize(700, 500);
			valid.setLocationRelativeTo(null);
			valid.setVisible(true);
		}
	}
	
	class ReservationStatus implements ActionListener{
		RoomStatus status;
		ArrayList<ReservationRecord> reservList;
		JComboBox situation;
		ReservationStatus(ArrayList<ReservationRecord> reservList, JComboBox situation){
			this.reservList = reservList;
			this.situation = situation;
		}
		@Override
		public void actionPerformed(ActionEvent e){
			status = new RoomStatus(reservList, this.situation.getSelectedItem().toString());
			String name = situation.getSelectedItem().toString()+" Reservation Status";
			status.setTitle(name);
			status.setSize(700, 500);
			status.setLocationRelativeTo(null);
			status.setVisible(true);
		}
	}
}
