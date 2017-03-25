package report6;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.ArrayList;
import java.io.*;

public class ReservationGUI extends JFrame {
	
	// JButton ���� ����
	private JButton validTime = new JButton("���డ�ɽð�");
	private JButton reservSituation = new JButton("�溰�����Ȳ");
	private JButton reserv = new JButton("����");
	private JButton cancel = new JButton("���");
	
	// JComboBox�� JTextField ���� 
	// ���� Room514, Room515, Room516 3���� �ִٰ� ����9
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
	
	// RoomReservation���� ���� ���Ͽ� ����Ǿ��ִ� ������ �ҷ��´�.
	private static RoomReservation dataFile = new RoomReservation();
	private static ArrayList <ReservationRecord> reservList = dataFile.Get_Real_Records(); 
	
	public ReservationGUI() {
		
		// ȣ��, ����, �ð�, ������, �޸� ���� Label�� JComboBox, JTextField �߰�
		// GridLayout(2,5) 2�� 5��
		JPanel p1 = new JPanel();
		p1.setLayout(new GridLayout(2, 5));

		p1.add(new JLabel("ȣ��"));
		p1.add(new JLabel("����"));
		p1.add(new JLabel("�ð�"));
		p1.add(new JLabel("������"));
		p1.add(new JLabel("�޸�"));
		p1.add(roomNum);
		p1.add(reservDay);
		p1.add(reservTime);
		p1.add(reservName);
		p1.add(reservMemo);

		// "���� ��Ȳ"�̶�� JTextField ����
		JPanel p2 = new JPanel(new BorderLayout());
		p2.add(new JTextField("���� ��Ȳ"));
    
		// JTable ����
		DefaultTableModel model = new DefaultTableModel();
		model.setColumnCount(5);
		p3 = new JTable(model);	
		
		// RoomReservation(datafile)�� ����Ǿ��ִ� ��������� JTable�� ǥ��
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
 
		// ���డ�ɽð� ��ư ����
    	JPanel p4 = new JPanel(new BorderLayout());
    	p4.add(validTime);
    
    	// ���� ������ JComboBox, �����Ȳ��ư JButton
    	// FlowLayout���� ���ڰ��� 5, �ٰ��� 10
    	JPanel p5 = new JPanel();
    	p5.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 10));
    	p5.add(situation);
    	p5.add(reservSituation);
    	
    	// ����, ��ҹ�ư ����
    	// GridLayout(2,1) 2�� 1��
    	JPanel p6 = new JPanel();
    	p6.setLayout(new GridLayout(2,1));
    	p6.add(reserv);
    	p6.add(cancel);
    	
    	// reserv, cancel�� Ŭ���ϸ� ReservListener, CancelListener�� ���� ����
    	reserv.addActionListener(new ReservListener(p3, roomNum, reservDay, reservTime, reservName, reservMemo));
    	cancel.addActionListener(new CancelListener(p3));
    	
    	// validTime, reservSituation�� Ŭ���ϸ�  ValidTimeListener�� ReservationStatus�� ���� ����
    	validTime.addActionListener(new ValidTimeListener(reservList));
    	reservSituation.addActionListener(new ReservationStatus(reservList, situation));
    	
    	// p1�� NORTH, p2�� CENTER�� ��ġ & Grouping
    	JPanel p7 = new JPanel(new BorderLayout());
    	p7.add(p1, BorderLayout.NORTH);
    	p7.add(p2, BorderLayout.CENTER);
    	add(p7);
    	
    
    	// p7�� NORTH, p3�� CENTER�� ��ġ & Grouping
    	JPanel p8 = new JPanel(new BorderLayout());
    	p8.add(p7, BorderLayout.NORTH);
    	p8.add(p3, BorderLayout.CENTER);
    	add(p8);
    
    	// p4�� CENTER, p5�� EAST�� ��ġ & Grouping
    	JPanel p9 = new JPanel(new BorderLayout());
    	p9.add(p4, BorderLayout.CENTER);
    	p9.add(p5, BorderLayout.EAST);
    	add(p9);
    
    	// p8�� CENTER, p6�� EAST�� ��ġ & Grouping    	
    	JPanel p10 = new JPanel(new BorderLayout());
    	p10.add(p8, BorderLayout.CENTER);
    	p10.add(p6, BorderLayout.EAST);
    	add(p10);
    
    	// p10�� CENTER, p9�� SOUTH�� ��ġ & Grouping
    	JPanel p = new JPanel(new BorderLayout());
    	p.add(p10, BorderLayout.CENTER);
    	p.add(p9, BorderLayout.SOUTH);
    	add(p);
    }
	public ArrayList<ReservationRecord> Get_Chart_List(){
		return reservList;
	}

	//���� ��ư�� ������ ���ο� ��������� JTable�� ǥ���ϰ� ������� ������ ����
	class ReservListener implements ActionListener{
		JTable table;
		JComboBox roomNum, reservDay, reservTime;
		JTextField reservName, reservMemo;
		
		// ���ο� ���� ������ ����
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

			// ��������� String �迭�� ����
			String[] input = new String [5];
			input[0] = (String)roomNum.getSelectedItem();
			input[1] = (String)reservDay.getSelectedItem();
			input[2] = (String)reservTime.getSelectedItem();
			input[3] = reservName.getText();
			input[4] = reservMemo.getText();
					
			// ReservationRecord�� ����
			ReservationRecord newReservation = new ReservationRecord(input);
					
			// ���� ����Ʈ�� �߰� , ���� ���� ���� ��� �߰� X
			boolean conflict = false;
			for(int i = 0; i<table.getRowCount(); i++){
				if(table.getValueAt(i, 0).equals(input[0]) && table.getValueAt(i, 1).equals(input[1]) && table.getValueAt(i, 2).equals(input[2])){
						System.out.println("Your new reservation conflicted");
						conflict = true;
				}
			}
			// �������� ������ JTable ���� ����Ʈ�� �߰� & �������� ����
			if(!conflict){
				
				// JTable ���� ����Ʈ�� �߰�
				DefaultTableModel model = (DefaultTableModel) table.getModel();
				model.addRow(input);
					
				// reservList�� �߰�
				reservList.add(newReservation);
					
					
				// ���� ���� ����
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
	
	//���� ���v�� ������ ���ο� ��������� JTable�� ǥ���ϰ� ������� ������ ����
	class CancelListener implements ActionListener{
		JTable table;
			
		CancelListener(JTable table){
			this.table = table;
		}
			
		public void actionPerformed(ActionEvent e){
				
			// datafile���� ������ ��������� line�� �����
			int row = table.getSelectedRow();
			File file = new File("roomreserve-norm.data");
			String dummy = "";
				
			// ���� ������ �����Ѵ� (���ؼ� ���ﶧ �ʿ�)
			// ���ǽ� ��ȣ
			String delRoom = table.getValueAt(row, 0).toString();
			// ����
			String delDay = table.getValueAt(row, 1).toString().toLowerCase();
			// �ð�
			int delTime = (Integer.parseInt(table.getValueAt(row, 2).toString())-1);
			
			try{
				BufferedReader search = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
					
				String searchLine = search.readLine();
				int position = -1;
					
				// ������ line�� ��ġ ã��
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
				
				// ��ġ�ϴ� ���� ������ line�� ã�� ������ ���
				if(position == -1){
					System.out.println("There is no statement");
				}
				else{
					
					// ������ ��������� �����ϰ� ������ �����Ѵ�.
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
					
					// ���� datafile�� ������ ������.
					FileWriter fw = new FileWriter("roomreserve-norm.data");
					fw.write(dummy);
					
					fw.close();
					br.close();
				}
			}
			catch(Exception ex){
				ex.printStackTrace();
			}
			
			// JTable���� ������ ������ ����
			if(row == -1){
				return;
			}
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			model.removeRow(row);
			
			
			// reservList�� ��� �ִ��� ã�Ƴ���.
			int where = -1;
			for(int i = 0; i<reservList.size(); i++){
				if((reservList.get(i).room_num.equals(delRoom))
						&&(reservList.get(i).day.equals(delDay))
						&& (reservList.get(i).time == delTime )){
					where = i;
				}
			}
				
			// reservList���� �����
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
			valid.setTitle("AvailableTime_20131790_�Ǽ���");
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
