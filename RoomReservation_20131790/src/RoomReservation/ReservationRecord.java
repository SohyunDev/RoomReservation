package report6;

public class ReservationRecord {
	// ������ ���ǽ� �̸�, ����, �ð�, ������, �޸� ������ش�.
	String room_num, day, name, memo;
	int time;
		
	public ReservationRecord(String[] data){
	// �Է� ���� �����͸� ����
		this.room_num = data[0];
		this.day = data[1].toLowerCase();
		this.time = Integer.parseInt(data[2])-1;
		this.name = data[3];
		this.memo = data[4];
	}
	// ������ ���������� �ԷµǾ����� Ȯ���ϴ� �޼ҵ� 
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
	// �ð��� ���������� �Է� (1~8����)�Ǿ����� Ȯ���ϴ� �޼ҵ�
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
	// ������ ��ġ�� ��ȯ���ִ� �޼ҵ�
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
