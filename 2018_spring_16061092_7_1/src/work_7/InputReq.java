package work_7;

import java.util.Scanner;
import java.util.regex.*;

public class InputReq extends Thread{
	static String getsIni = null;
	private static Scanner input;
	static String req = "^(\\[CR[,][(][0-9]{1,2}[,][0-9]{1,2}[)][,][(][0-9]{1,2}[,][0-9]{1,2}\\)\\])$";//
	static String end = "END";
	 
	public void run() {//
		System.out.println("Input start!");
		//读入请求
		int i=0;
		long st=0;
		input = new Scanner(System.in);
		int nowpointi = 0;//请求发出地横坐标
		int nowpointj = 0;//请求发出地纵坐标
		int aimpointi = 0;//目标地点横坐标
		int aimpointj = 0;//目标第点纵坐标
		long nowt=0;//请求发出时间
		//String CR;//请求标识符
		PassengerReq[] reqlists =new PassengerReq[300];
		int reqnums=0;
		int flag=0;
		
		while(i<300) {
			flag=0;
			if(input.hasNextLine()) { 
				getsIni = input.nextLine();	
				st = System.currentTimeMillis();//输入时的系统时间
			}
			String gets =getsIni.replaceAll(" ","");//;//消除指令间空格[sasa,(12,32),(52,33)]
			if(Pattern.matches(req, gets)) {//满足正则表达式
				try {
				String[] eachpart = gets.split("[,]");//用逗号分开
				//String cr = eachpart[0].substring(1);
				nowpointi=Integer.parseInt(eachpart[1].substring(1));
				nowpointj=Integer.parseInt(eachpart[2].substring(0, eachpart[2].length()-1));
				aimpointi=Integer.parseInt(eachpart[3].substring(1));
				aimpointj=Integer.parseInt(eachpart[4].substring(0, eachpart[4].length()-2));
				nowt=st/100;//百毫秒为单位，请求发出时刻
				//System.out.println("nowpointi"+nowpointi+"nowpointj"+nowpointj+"aimpointi"+aimpointi+"aimpointj"+aimpointj);
			    if(nowpointi<80&&nowpointj<80&&aimpointi<80&&aimpointj<80&&!(nowpointi==aimpointi&&nowpointj==aimpointj)) {//大小满足条件
			    	//判断是否同质
			    	//如果有重复的
			    //	System.out.println("aaa");
			    	if(reqnums==0) {//当请求队列为空时，还没有得到有效请求的时候，第一条肯定不同质
			    		Thread newreq = new PassengerReq(nowpointi,nowpointj,aimpointi,aimpointj,nowt);
		    			//加到请求队列数组里
		    			reqlists[reqnums]=(PassengerReq) newreq;
		    			reqnums++;
		    			//System.out.println(reqnums);
		    			newreq.setUncaughtExceptionHandler(new ExceptionSch());
		    			newreq.start();
			    	}else {
			    	//	System.out.println("bbb");
				    	for(int i1=0;i1<reqnums;i1++) {
				    	//	System.out.println(nowt+" "+reqlists[i1].settime);
				    		if(reqlists[i1].seti==nowpointi&&reqlists[i1].setj==nowpointj&&reqlists[i1].aimi==aimpointi&&reqlists[i1].aimj==aimpointj&&reqlists[i1].settime==nowt) {//
				    			//是同质请求
				    			System.out.println("与之前请求同质");
				    			flag=1;
				    			break;
				    		}
				    	}
				    //	System.out.println("ccc");
				    	if(flag==0) {
				    		Thread newreq = new PassengerReq(nowpointi,nowpointj,aimpointi,aimpointj,nowt);
				    			//加到请求队列数组里
				    			reqlists[reqnums]=(PassengerReq) newreq;
				    			reqnums++;
				    			System.out.println(reqnums);
				    			newreq.setUncaughtExceptionHandler(new ExceptionSch());
				    			newreq.start();
				    	}
			    	}
			    }else {//大小不满足条件
			    	System.out.println("输入坐标大小不满足条件");
			    }
			}catch(Exception e) {
				System.out.println("无效输入");
			}
			}
			else if(Pattern.matches(end, gets)) {
				break;//输入结束
			}
			else {
				System.out.println("输入不匹配");
			}
		}
	}

}
