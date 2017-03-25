package report6;

public class ReservationRecord {
	// 저장할 강의실 이름, 요일, 시간, 예약자, 메모를 만들어준다.
	String room_num, day, name, memo;
	int time;
		
	public ReservationRecord(String[] data){
	// 입력 받은 데이터를 저장
		this.room_num = data[0];
		this.day = data[1].toLowerCase();
		this.time = Integer.parseInt(data[2])-1;
		this.name = data[3];
		this.memo = data[4];
	}
	// 요일이 정상적으로 입력되었는지 확인하는 메소드 
	public boolean day_Check(){
		String[] days = {"sun","mon","tue","wed","thr","fri","sat"};
		boolean ret = false;
		for (int i = 0; i<days.length; i++){
			if(day.equals(days[i])){
				ret = true;
			}
		}
		if(ret == false){
			System.out.println("Input Day is wrong");
		}
		return ret;
	}
	// 시간이 정상적으로 입력 (1~8교시)되었는지 확인하는 메소드
	public boolean time_Check(){
		boolean ret = false;
		if(0<=time && time<=7){
			ret = true;
		}
		if(ret == false){
			System.out.println("Input Time is wrong");
		}
		return ret;
	}
	// 요일의 위치를 반환해주는 메소드
	public int get_Day_Index(){
		int index = -1;
		String[] days = {"sun","mon","tue","wed","thr","fri","sat"};
		for (int i = 0; i<days.length; i++){
			if(day.equals(days[i])){
				index = i;
			}
		}
		return index;
	}
}
