package work_10;


public class ExceptionSch implements Thread.UncaughtExceptionHandler{
	 /**
     * @Overview:捕捉线程产生的异常。
     */
	public boolean repOK() {
		/**@REQUIRES: None;
		@MODIFIES: None;
		@Effects: \result == invariant(this);
		*/
		return true;
	}
	public void uncaughtException(Thread t, Throwable e) {  
		/**@REQUIRES: t != null;
		 *             e != null;
		@MODIFIES: None;
		@EFFECTS: 线程运行过程中出现异常 ==> e.printStackTrace();
		*/
        e.printStackTrace();
    } 
}  