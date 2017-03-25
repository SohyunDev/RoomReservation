package report6;

import java.util.*;

public class ReservationChart{
	
	// 방 Room514, Room515, Room516을 String[] 배열에 순서대로 지정
	private String[] roomName = {"Room514", "Room515", "Room516"};
	// 3개의 강의실의 에약을 저장할 3차원 배열을 생성
	private String[][][] roomSituation = new String[3][8][7];
	
	ReservationChart(ArrayList<ReservationRecord> reservList){
		
		// roomSituation 초기화
		for(int i = 0; i<roomSituation.length; i++){
			for(int j = 0; j<roomSituation[0].length; j++){
				for(int k = 0; k<roomSituation[0][0].length; k++){
					roomSituation[i][j][k] = null;
				}
			}
		}
		
		// 예약 내역 저장
		for(int i = 0; i<reservList.size(); i++){
			int roomIndex = GetRoomIndex(reservList.get(i).room_num);
			int dayIndex = reservList.get(i).get_Day_Index();
			if(roomSituation[roomIndex][reservList.get(i).time][dayIndex] == null){
				roomSituation[roomIndex][reservList.get(i).time][dayIndex] = reservList.get(i).name;
			}
		}
	}
	
	public String[][][] GetChart(){
		return roomSituation;
	}
	
	public int GetRoomIndex(String room){
		int index = -1;
		for(int i = 0; i<roomName.length; i++){
			if(roomName[i].equals(room)){
				index = i;
			}
		}
		if(index == -1){
			System.out.println("can't get index");
		}
		return index;
	}
}