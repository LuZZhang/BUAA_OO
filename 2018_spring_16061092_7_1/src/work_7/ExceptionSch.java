package work_7;


public class ExceptionSch implements Thread.UncaughtExceptionHandler{
	public void uncaughtException(Thread t, Throwable e) {  
        e.printStackTrace();
    } 
} 