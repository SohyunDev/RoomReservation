package report6;

import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;

public class RoomReservation {
	private static String[][][] rooms = new String[3][8][7]; // �ִ� 3���� ���ǽǰ� �Է¹��� 8x7ĭ�� ���ǽ� ������ ������ String rooms�� �����.
	private static String[] room_name = new String[3]; // �ִ� 3���� ���ǽ��̸��� �Է¹��� String room_name�� �����.
	private static int number_of_rooms = 0; // ���� ���ǽ��� ������ �˷��� int�� number_of_rooms�� �����.
	private static ArrayList <ReservationRecord> records = new ArrayList<ReservationRecord>();
	private static ArrayList <ReservationRecord> realRecords = new ArrayList<ReservationRecord>();
	
	public void showReservation(String reservationFileName){ // ���� ������ ������ �޼ҵ带 �����.
		// ������ ������ �ҷ��´�.
		Scanner input = null;
		File file = new File(reservationFileName);
		// �Ľ̵� �������(ReservationRecord)�� �������� ArrayList�� �����.
		String[] data = new String[5]; // �Ľ��� ������ �������� String array
		try{
			input = new Scanner(file);
		}
		catch(Exception e){
			// ������ ���� ������ ���
			System.out.println("Unknwon File");
		}
		while(input.hasNext()){
			String line = input.nextLine();
			// ���� ���� �Ľ�
			if(!line.startsWith("//") && line.length()!=0){ // �ּ�ó���� �� �����̳� ������ ���ܽ�Ų��.
				String[] tokens = line.split(":"); // ������ ':'�� �������� ������ ������.
				if(tokens[0] != null && tokens[1] != null && tokens[2] != null && tokens[3] != null ){
					// ������ ����ִ� ���� �ִ� ��츦 ���ܽ�Ų��. (�Է� ������ ����)
					if(tokens.length==5){
						// ���ǽ� �̸�, ����, �ð�, �����ڸ�, �޸� ��� �ִ� ���
						data[0] = tokens[0].trim();
						data[1] = tokens[1].trim();
						data[2] = tokens[2].trim();
						data[3] = tokens[3].trim();
						data[4] = tokens[4].trim();
						// record ���� �� arraylist�� records�� ����
						ReservationRecord record = new ReservationRecord(data);
						records.add(record);
					}
					else if(tokens.length == 4){
						// ���ǽ� �̸�, ����, �ð�, �����ڸ��� �ִ� ��� (�޸� �Է� X)
						data[0] = tokens[0].trim();
						data[1] = tokens[1].trim();
						data[2] = tokens[2].trim();
						data[3] = tokens[3].trim();
						data[4] = null;
						// record ���� �� arraylist�� records�� ����
						ReservationRecord record = new ReservationRecord(data);
						records.add(record);
					}
					else{
						//����ִ� ������ ���� ��� ���� ǥ�� (�Է� ������ �߸��� ���)
						System.out.println("Irregular reservation line");
					}
				}
			}
		}
		for(int i = 0; i< 3; i++){
			for(int j = 0; j< 8; j++){
				for(int k = 0; k<7; k++){
					this.rooms[i][j][k]=null;
				}
			}
		}
		for(int i = 0; i<3; i++){
			this.room_name[i] = null;
		}
		this.number_of_rooms = 0;
		// records�� �ִ� record�� ����check, �ð�check�� �����϶� , �ð� �ߺ�X, ���ǽ� 3�� �ʰ�X�϶� ����
		for(int i = 0; i<records.size(); i++){
			// ������ ��ġ�� ã�� ����
			int day_index = records.get(i).get_Day_Index();
			// ���ǽ��� ����Ǿ��ִ� ��ġ�� ã�� ����
			if(records.get(i).day_Check()){ // ������ ���������� �Էµ� ���
				if(records.get(i).time_Check()){ // �ð��� ���������� �Էµ� ���
					int room_index = get_Room_Index(records.get(i));
					if(room_index != -1){// room�� �����ϰų� �������� ���� ��� 3���� ���� ���� ��
						if(rooms[room_index][records.get(i).time][day_index] == null){ // ������ ��������� ������ �����Ѵ�.
							rooms[room_index][records.get(i).time][day_index] = records.get(i).name;
							
							// ���� ����Ǵ� ���� ������ ����
							realRecords.add(records.get(i));
						}
						else{
							// ������ �̹� �Ǿ��ִ� ��� �浹 ���� & �ð��� ǥ���Ѵ�.
							System.out.println("Conflict hour -- "+records.get(i).day + " "+ (records.get(i).time+1));
						}
					}
				}
			}
		}
		
		// ���ǽ� �ð�ǥ�� ������ش�.
		Display(rooms, room_name, number_of_rooms);
	}
	// ���ǽ��� ��ġ�� ���ϴ� �޼ҵ�
	public int get_Room_Index(ReservationRecord record){
		if( number_of_rooms == 0){ // ����� ���ǽ��� �ƿ� ���� ��
			room_name[number_of_rooms] = record.room_num;
			number_of_rooms++; // ���� ������ 1�� ����
			return (number_of_rooms-1); // �� ���ǽ��� room_name[0]�� ����� ���ǽ�
		}
		else{ // ����� ���ǽ��� ���� ���
			for(int i = 0; i<number_of_rooms; i++){
			// ����� ���ǽ��� �ְ�, �ڽ��� ���ǽ��� �ִٸ� ����Ǿ��ִ� ���ǽ��� ��ġ�� ��ȯ
				if(record.room_num.equals(room_name[i])){
					return i;
				}
			}
			// ���� ���ǽ��� ����Ǿ����� �ʰ�, ���ǽ��� ������ 2�����϶��
			if(number_of_rooms<3){
				room_name[number_of_rooms] = record.room_num;
				number_of_rooms++; // ���� ������ ����
				return (number_of_rooms-1);
			}
				// ���� ���ǽ��� ����Ǿ����� �ʰ�, ���ǽ��� ������ 3����� ���ǽ��� �ʰ��Ǿ��ٰ� ǥ��
			else if( number_of_rooms>=3){
				System.out.println("The number of rooms is exceeded");
				return -1;
			}
			
			return -1;
		}
	}
	// ���ǽ� �ð�ǥ�� ������ִ� �޼ҵ�
		public void Display(String [][][] rooms, String[] room_name, int number_of_rooms ){
			String[] days = {"Sun","Mon","Tue","Wed","Thr","Fri","Sat"};
			for( int i = 0; i<number_of_rooms ; i++){
			// ���ǽ� �̸��� ���
			System.out.println("Room Name :: "+ room_name[i]);
			// ���� ���
			for( int j = 0; j< days.length; j++){
				System.out.print("\t" + days[j]);
			}
			System.out.println();
			System.out.println("------------------------------------------------------------");
			// �Էµ� ������ ������� '\t'�� �Է�, ���� ��� �̸� +'\t'�Է�
			for(int j = 0; j< 8; j++){
				System.out.print(j+1+"\t");
				for(int k = 0; k<7; k++){
					if(rooms[i][j][k]==null){
						System.out.print("\t");
					}
					else{
						System.out.print(rooms[i][j][k]+"\t");
					}
				}
				// �ѽð��� ����ϸ� �����ð����� ����ش�.
				System.out.println("");
			}
		}
	}
		
		// ��������� ������ ArrayList�� ����
		public ArrayList<ReservationRecord> Get_Real_Records(){
			return realRecords;
		}
}