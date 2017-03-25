package report6;

import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;

public class RoomReservation {
	private static String[][][] rooms = new String[3][8][7]; // 최대 3개의 강의실과 입력받을 8x7칸의 강의실 내용을 저장할 String rooms를 만든다.
	private static String[] room_name = new String[3]; // 최대 3개의 강의실이름을 입력받을 String room_name을 만든다.
	private static int number_of_rooms = 0; // 현재 강의실의 갯수를 알려줄 int형 number_of_rooms를 만든다.
	private static ArrayList <ReservationRecord> records = new ArrayList<ReservationRecord>();
	private static ArrayList <ReservationRecord> realRecords = new ArrayList<ReservationRecord>();
	
	public void showReservation(String reservationFileName){ // 예약 사항을 보여줄 메소드를 만든다.
		// 파일의 내용을 불러온다.
		Scanner input = null;
		File file = new File(reservationFileName);
		// 파싱된 예약사항(ReservationRecord)을 저장해줄 ArrayList를 만든다.
		String[] data = new String[5]; // 파싱할 정보를 저장해줄 String array
		try{
			input = new Scanner(file);
		}
		catch(Exception e){
			// 파일을 읽지 못했을 경우
			System.out.println("Unknwon File");
		}
		while(input.hasNext()){
			String line = input.nextLine();
			// 예약 사항 파싱
			if(!line.startsWith("//") && line.length()!=0){ // 주석처리로 된 문장이나 빈줄을 제외시킨다.
				String[] tokens = line.split(":"); // 구분자 ':'를 기준으로 문장을 나눈다.
				if(tokens[0] != null && tokens[1] != null && tokens[2] != null && tokens[3] != null ){
					// 내용이 비어있는 곳이 있는 경우를 제외시킨다. (입력 형식의 오류)
					if(tokens.length==5){
						// 강의실 이름, 요일, 시간, 예약자명, 메모가 모두 있는 경우
						data[0] = tokens[0].trim();
						data[1] = tokens[1].trim();
						data[2] = tokens[2].trim();
						data[3] = tokens[3].trim();
						data[4] = tokens[4].trim();
						// record 생성 후 arraylist인 records에 저장
						ReservationRecord record = new ReservationRecord(data);
						records.add(record);
					}
					else if(tokens.length == 4){
						// 강의실 이름, 요일, 시간, 예약자명이 있는 경우 (메모 입력 X)
						data[0] = tokens[0].trim();
						data[1] = tokens[1].trim();
						data[2] = tokens[2].trim();
						data[3] = tokens[3].trim();
						data[4] = null;
						// record 생성 후 arraylist인 records에 저장
						ReservationRecord record = new ReservationRecord(data);
						records.add(record);
					}
					else{
						//비어있는 내용이 있을 경우 오류 표시 (입력 형식이 잘못된 경우)
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
		// records에 있는 record가 요일check, 시간check가 정상일때 , 시간 중복X, 강의실 3개 초과X일때 저장
		for(int i = 0; i<records.size(); i++){
			// 요일의 위치를 찾아 저장
			int day_index = records.get(i).get_Day_Index();
			// 강의실이 저장되어있는 위치를 찾아 저장
			if(records.get(i).day_Check()){ // 요일이 정상적으로 입력된 경우
				if(records.get(i).time_Check()){ // 시간이 정상적으로 입력된 경우
					int room_index = get_Room_Index(records.get(i));
					if(room_index != -1){// room이 존재하거나 존재하지 않을 경우 3개를 넘지 않을 때
						if(rooms[room_index][records.get(i).time][day_index] == null){ // 예약이 비어있으면 내용을 저장한다.
							rooms[room_index][records.get(i).time][day_index] = records.get(i).name;
							
							// 실제 저장되는 예약 사항을 저장
							realRecords.add(records.get(i));
						}
						else{
							// 예약이 이미 되어있는 경우 충돌 요일 & 시간을 표시한다.
							System.out.println("Conflict hour -- "+records.get(i).day + " "+ (records.get(i).time+1));
						}
					}
				}
			}
		}
		
		// 강의실 시간표를 출력해준다.
		Display(rooms, room_name, number_of_rooms);
	}
	// 강의실의 위치를 구하는 메소드
	public int get_Room_Index(ReservationRecord record){
		if( number_of_rooms == 0){ // 저장된 강의실이 아예 없을 때
			room_name[number_of_rooms] = record.room_num;
			number_of_rooms++; // 방의 갯수가 1개 증가
			return (number_of_rooms-1); // 이 강의실은 room_name[0]에 저장된 강의실
		}
		else{ // 저장된 강의실이 있을 경우
			for(int i = 0; i<number_of_rooms; i++){
			// 저장된 강의실이 있고, 자신의 강의실이 있다면 저장되어있는 강의실의 위치를 반환
				if(record.room_num.equals(room_name[i])){
					return i;
				}
			}
			// 나의 강의실이 저장되어있지 않고, 강의실의 갯수가 2개이하라면
			if(number_of_rooms<3){
				room_name[number_of_rooms] = record.room_num;
				number_of_rooms++; // 방의 갯수가 증가
				return (number_of_rooms-1);
			}
				// 나의 강의실이 저장되어있지 않고, 강의실의 갯수가 3개라면 강의실이 초과되었다고 표시
			else if( number_of_rooms>=3){
				System.out.println("The number of rooms is exceeded");
				return -1;
			}
			
			return -1;
		}
	}
	// 강의실 시간표를 출력해주는 메소드
		public void Display(String [][][] rooms, String[] room_name, int number_of_rooms ){
			String[] days = {"Sun","Mon","Tue","Wed","Thr","Fri","Sat"};
			for( int i = 0; i<number_of_rooms ; i++){
			// 강의실 이름을 출력
			System.out.println("Room Name :: "+ room_name[i]);
			// 요일 출력
			for( int j = 0; j< days.length; j++){
				System.out.print("\t" + days[j]);
			}
			System.out.println();
			System.out.println("------------------------------------------------------------");
			// 입력된 내용이 없을경우 '\t'을 입력, 있을 경우 이름 +'\t'입력
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
				// 한시간을 출력하면 다음시간으로 띄어준다.
				System.out.println("");
			}
		}
	}
		
		// 예약사항을 저장한 ArrayList를 전달
		public ArrayList<ReservationRecord> Get_Real_Records(){
			return realRecords;
		}
}