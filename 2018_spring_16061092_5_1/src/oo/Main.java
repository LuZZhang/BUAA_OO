package oo;
 
public class Main {
	//static Main_Sch Sch_th = new Main_Sch(thread_ALS_1,thread_ALS_2,thread_ALS_3);
	static Thread thread_ALS_1 = new New_sch();
	static Thread thread_ALS_2 = new New_sch();
	static Thread thread_ALS_3 = new New_sch();
	static Thread thread_wait = new WaitQue(thread_ALS_1,thread_ALS_2,thread_ALS_3);
	static Main_Sch Sch_th = new Main_Sch(thread_ALS_1,thread_ALS_2,thread_ALS_3,thread_wait);
	static Thread thread_Sch = new Thread(Sch_th);
	public static void main(String[] args) {
		thread_Sch.start();
		thread_ALS_1.start();
		thread_ALS_2.start();
		thread_ALS_3.start(); 
		thread_wait.start();
	}

}
