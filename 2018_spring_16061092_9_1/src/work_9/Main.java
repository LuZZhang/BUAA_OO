package work_9;

public class Main {
	static Thread taxis[] = new TaxiSch[100] ; 
//	static Thread passreqs = new PassengerReq();
	static Thread inputreq = new InputReq();
	static Thread spfa = new SPFA();
	static TaxiGUI gui = new TaxiGUI();
	static Map map = new Map();
	public static void main(String[] args) {
		/**@ Requires: None;
		@ Modifies: None;
		@ Effects:  Normal behavior
		*			开启100个出租车线程和输入线程;
		*/ 
		// TODO Auto-generated method stub
		//将地图载入可视化对象
		gui.LoadMap(Map.map, 80);
		Integer i = 0;
		spfa.start();
		for(i=0;i<100;i++) {
				taxis[i]=new TaxiSch(i);
				taxis[i].setUncaughtExceptionHandler(new ExceptionSch());
			    taxis[i].start();
		}
        inputreq.setUncaughtExceptionHandler(new ExceptionSch());
		inputreq.start();
		 
	}
 
}
