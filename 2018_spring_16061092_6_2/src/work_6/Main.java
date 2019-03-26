package work_6;

import java.util.Scanner;
import java.util.regex.*;
import java.io.*;

public class Main {
	private static Scanner input;
	static String regex = "[ ]*IF[ ]*\\<(.+)\\>[ ]*\\<(.+)\\>[ ]*THEN[ ]*\\<(.+)\\>[ ]*";//正则表达式
	static String end = "END";
	public static Thread[] td = new MonitorThread[125];
	public static int tdnum = 0;
	
	public static File getMoniObj(String gets) {
		String[] each = gets.split("\\<|\\>");//
		File ss = new File(each[1]);
		return ss;
	}
	 
	public static String getTrig(String gets) {
		String[] each = gets.split("\\<|\\>");//
		return each[3];
	}
	
	public static String getTask(String gets) {
		String[] each = gets.split("\\<|\\>");//
		return each[5];
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//输入监控指令
		input = new Scanner(System.in);
		String gets = null;
		while(true){
			if(input.hasNextLine()) {
				gets = input.nextLine();//输入的一行
			} 
			//String gets = "  IF<a>   <s> THEN <aad>  ";//需要输入的指令
			//System.out.println(gets);
			if(Pattern.matches(regex,gets)) {
				System.out.println("监控对象："+getMoniObj(gets));
				System.out.println("触发条件："+getTrig(gets));
				System.out.println("任务："+getTask(gets));
				//获得作业
				//首先判断是否已经有该作业，若已经有，则忽略该作业，否则创建监控线程
			    MonitorWork nowThr = new MonitorWork(getMoniObj(gets),getTrig(gets),getTask(gets));
				CreatedWork.creatWork(nowThr);
			}
			else if(Pattern.matches(end, gets)) break;
			else {
				System.out.println("不匹配");
			}
	    }//输入结束
		//开启summary线程和detail线程
		for(int i=0;i<tdnum;i++) {
			td[i].start();
		}
		System.out.println("测试线程开启");
		Thread testhr = new TestThread();
		testhr.start();
		//开启测试线程
		
	}

}
