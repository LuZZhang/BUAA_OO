package work_9;


public class ExceptionSch implements Thread.UncaughtExceptionHandler{
	public void uncaughtException(Thread t, Throwable e) {  
		/**@ REQUIRES: t != null;
		 *             e != null;
		@ MODIFIES: None;
		@ EFFECTS: 线程运行过程中出现异常 ==> e.printStackTrace();
		*/
        e.printStackTrace();
    } 
} 