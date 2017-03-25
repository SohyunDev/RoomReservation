package report6;

 import javax.swing.*;


public class Test {

	public static void main(String[] args) {
		RoomReservation res = new RoomReservation();
		res.showReservation("roomreserve-norm.data");
		ReservationGUI gui = new ReservationGUI();
		gui.setTitle("RoomReservation_20131790_±Ç¼ÒÇö");
		gui.setSize(700, 500);
		gui.setLocationRelativeTo(null);
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gui.setVisible(true);
	}

}