package work_7;


public class Main {
	static Thread taxis[] = new TaxiSch[100] ;
//	static Thread passreqs = new PassengerReq();
	static Thread inputreq = new InputReq();
	//static TaxiGUI gui = new TaxiGUI();
	static Map map = new Map();
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//将地图载入可视化对象
	//	gui.LoadMap(Map.map, 80);
		Integer i = 0;
		for(i=0;i<100;i++) {
				taxis[i]=new TaxiSch(i);
				taxis[i].setUncaughtExceptionHandler(new ExceptionSch());
			    taxis[i].start();
		}
        inputreq.setUncaughtExceptionHandler(new ExceptionSch());
		inputreq.start();
		 
	}
 
}
