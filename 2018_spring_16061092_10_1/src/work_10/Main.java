package work_10;

import java.util.Random;

public class Main {
	/** 
     * @Overview:实现带有红绿灯的出租车系统。
     */
	static Thread taxis[] = new TaxiSch[100] ; 
//	static Thread passreqs = new PassengerReq();
	static Thread inputreq = new InputReq();
	static Thread spfa = new SPFA();
	static TaxiGUI gui = new TaxiGUI();
	static Map map = new Map();
	static Light light = new Light();
	public static int wait_lig = 0;
	
	public boolean repOK() {
		/**@ REQUIRES: None;
		@ MODIFIES: None;
		@ Effects: \result == invariant(this);
		*/
		if (taxis==null||inputreq==null||spfa==null||gui==null||map==null||light==null||wait_lig<500||wait_lig>1000)
			return false;
		return true;
	}
	
	public static void main(String[] args) {
		/**@ Requires: None;
		@ Modifies: None;
		@ Effects:  Normal behavior
		*			将地图载入可视化对象,开启红绿灯控制线程,100个出租车线程,spfa线程和输入线程;
		*/ 
		Main eg = new Main();
		//设置初始随机等待时间
		Random rand = new Random();
		wait_lig = 500+rand.nextInt(500);//以ms为单位,初始等待时间间隔
		//将地图载入可视化对象
		gui.LoadMap(Map.map, 80);
		light.start();
		Integer i = 0;
		for(i=0;i<100;i++) {
				taxis[i]=new TaxiSch(i);
				taxis[i].setUncaughtExceptionHandler(new ExceptionSch());
			    taxis[i].start();
		}
		spfa.setUncaughtExceptionHandler(new ExceptionSch());
		spfa.start();
        inputreq.setUncaughtExceptionHandler(new ExceptionSch());
		inputreq.start();
		
		//System.out.println(eg.light.repOK());
		//System.out.println(eg.repOK());
	}
 
}
