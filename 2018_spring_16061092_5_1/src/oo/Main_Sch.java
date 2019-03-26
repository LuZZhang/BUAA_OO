package oo;

import java.util.Scanner;
import java.util.regex.Pattern; 
import java.io.PrintStream; 
import java.io.FileNotFoundException;

public class Main_Sch extends New_sch implements Runnable {
	//private static RequQue RQ = new RequQue();
	String floorUP = "^([(]FR,([1-9]|[1][0-9]),UP[)])$";
	String floorDOWN = "^([(]FR,([2-9]|[1][0-9]|[2][0]),DOWN[)])$";
	String elevReq = "^([(]ER,[#][1-3],([1-9]|[1][0-9]|[2][0])[)])$";
	String END = "^END$";
	New_sch ele_1;
    New_sch ele_2;
	New_sch ele_3;
	WaitQue waitque;
	int m = 1;//楼层数
	int i = 0;//请求次数
	RequQue main_rq = new RequQue();//主调度器的请求队列，主要为了存放等待的FR请求
	//double T=0; 
	//double newTimeMax = 0;
	String getsIni = null;
	private static Scanner input;
	static PrintStream ps;
	public Main_Sch(Thread e1,Thread e2,Thread e3,Thread wait) {
		ele_1=(New_sch)e1;
		ele_2=(New_sch)e2;
		ele_3=(New_sch)e3;
		waitque=(WaitQue)wait;
	}
	

	public void run() {
		try {
				ps = new PrintStream("result.txt");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		
		ele_1.elenum=1;
		ele_2.elenum=2;
		ele_3.elenum=3;
		//System.out.println(System.currentTimeMillis()+"in Main_Sch just after run");
		input = new Scanner(System.in);
        //String gets = Main.gets;//消除指令间空格
		long st=0;
		int out = 0;
		while(i<50) {
			if(out == 1) {
				break;
			}

			if(input.hasNextLine()) { 
				getsIni = input.nextLine();	
				st = System.currentTimeMillis();//输入时的系统时间
			}
			if(i==0) {//第一条请求
				New_sch.Tstart= st;
			}
			long time,time1,time2;
			time = st - New_sch.Tstart;
			time1 = time/100;
			time2 = time - time1;
			if(time2>=50){
				time1 = time1 + 1;
			}
			double timereal = time1/(double)10;//请求产生时刻电梯系统时间
			String gets = getsIni.replaceAll(" ","");//消除指令间空格
			//得到了一行指令
			String[] eachreq = gets.split("[;]");//用分号分开
			int reqnum = eachreq.length;//一行请求的数量
			//对该行的每一个请求（可能有效或者无效）
			Requ[] Rq=new Requ[10];
			int flagtz=0;
			for(int j=0;j<reqnum;j++) {
				//System.out.println(eachreq[j]+" 在 "+timereal+" 输入");
				//FLOORup
				flagtz=0;
				if(j>=10) {//对于十个以外的请求
					System.setOut(ps);
					st = System.currentTimeMillis();
					System.out.println(st+":INVALID["+eachreq[j]+","+timereal+"]");
					continue;
				}
				if(Pattern.matches(floorUP,eachreq[j])) {
					String[] eachpart = eachreq[j].split("[,]");//用逗号分开
				    m = Integer.parseInt(eachpart[1]);//楼层数
					Rq[j] = new Requ(m,1,timereal,eachreq[j]);//得到一个FR,up请求
					//判断该行之前有没有与它相同的请求
					for(int ii=0;ii<j;ii++) {
						if(Rq[ii].getAimstage()==Rq[j].getAimstage()&&Rq[ii].getReqnum()==1) {
							//System.out.println("同质");
							System.setOut(Main_Sch.ps);
							System.out.println("#"+System.currentTimeMillis()+":SAME"+Rq[j]);
							flagtz = 1;
							break;
						}
					}
					if(flagtz==1) continue;
					//判断是否可以被捎带
					New_sch carry_sch = null;//捎带电梯
					New_sch now_sch = null;//当前电梯
					int now_min = 1000000000;//当前最小运动量
					int flag = 0;//判断是否有可以稍带的电梯
					//判断能否被捎带
					for(int k=3;k>=1;k--) {
						if(k==1) now_sch = ele_1;
						else if(k==2) now_sch = ele_2;
						else now_sch = ele_3;
						//now_sch是当前的电梯
						synchronized(now_sch) {
						if(now_sch.main_req_fx==1&&Rq[j].getAimstage()>=now_sch.floornext&&Rq[j].getAimstage()<=now_sch.aim_stage) {
							//可以被捎带
							if(now_sch.sumrunt<=now_min) {//这里的now_min是可以被捎带电梯的最小运动量																			
								carry_sch = now_sch;//选择它作为捎带电梯
							    now_min = carry_sch.sumrunt;
							    flag = 1;//第一波将flag置为1的机会
							}
							
						}
						}
					}
					//if(flag==1) System.out.println(j+"UP捎带成功");
					if(flag==0) {//如果没有可以稍带的电梯,此时carry_sch=null
						//那就判断是否有WFS的电梯
						now_min = 1000000000;//最大运动量重置为1000000000
						for(int k=3;k>=1;k--) {
							if(k==1) now_sch = ele_1;
							else if(k==2) now_sch = ele_2;
							else now_sch = ele_3;
							synchronized(now_sch) {
							if(now_sch.RQ.RQnum()==0) {//处于WFS状态
								if(now_sch.sumrunt<=now_min) {
									carry_sch = now_sch;//选择它作为捎带电梯
								    now_min= carry_sch.sumrunt;
								    flag = 1;//第二波将flag置为1的机会
								}
							}
							}
						}
					}
					//有捎带或者WFS的电梯
					if(flag==1) {
						//将Rq加到carry_sch
						//Object ele_1;
						synchronized(carry_sch) {
							carry_sch.RQ.addReq(Rq[j]);
						}
						
					//	System.out.println(j+" UP捎带或WFS成功 "+carry_sch.elenum);
					}
					if(flag==0) {//没有捎带和WFS的电梯
						//加到waitque里去
						//System.out.println("加入waiting"+Rq[j]);
						synchronized(waitque) {
							waitque.waitqueue.addReq(Rq[j]);
						}
						
					}
					//RQ.addReq(Rq);
				}
				//FLOORdown
				else if(Pattern.matches(floorDOWN,eachreq[j])) {
					String[] eachpart = eachreq[j].split("[,]");//用逗号分开
					m = Integer.parseInt(eachpart[1]);//楼层数
					Rq[j] = new Requ(m,-1,timereal,eachreq[j]);
					//判断有没有同质
					for(int ii=0;ii<j;ii++) {
						if(Rq[ii].getAimstage()==Rq[j].getAimstage()&&Rq[ii].getReqnum()==-1) {
							//System.out.println("同质");
							System.setOut(Main_Sch.ps);
							System.out.println("#"+System.currentTimeMillis()+":SAME"+Rq[j]);
							flagtz = 1;
							break;
						}
					}
					if(flagtz==1) continue;
					//判断是否可以被捎带
					New_sch carry_sch = null;//捎带电梯
					New_sch now_sch = null;//当前电梯
					int now_min = 1000000000;//当前最小运动量
					int flag = 0;//判断是否有可以稍带的电梯
					for(int k=3;k>=1;k--) {
						if(k==1) now_sch = ele_1;
						else if(k==2) now_sch = ele_2;
						else now_sch = ele_3;
						//now_sch是当前的电梯
						synchronized(now_sch) {
						if(now_sch.main_req_fx==-1&&Rq[j].getAimstage()>=now_sch.aim_stage&&Rq[j].getAimstage()<=now_sch.floornext) {
							//可以被捎带
							if(now_sch.sumrunt<=now_min) {//这里的now_min是可以被捎带电梯的最小运动量																			
								carry_sch = now_sch;//选择它作为捎带电梯
							    now_min = carry_sch.sumrunt;
							    flag = 1;//第一波将flag置为1的机会
							}
							
						}
						}
					}
					//if(flag==1) System.out.println(j+"DOWN捎带成功");
					if(flag==0) {//如果没有可以稍带的电梯,此时carry_sch=null
						//那就判断是否有WFS的电梯
						now_min = 1000000000;//最大运动量重置为1000000000
						for(int k=3;k>=1;k--) {
							if(k==1) now_sch = ele_1;
							else if(k==2) now_sch = ele_2;
							else now_sch = ele_3;
							synchronized(now_sch) {
							if(now_sch.RQ.RQnum()==0) {//处于WFS状态
								if(now_sch.sumrunt<=now_min) {
									carry_sch = now_sch;//选择它作为捎带电梯
								    now_min= carry_sch.sumrunt;
								    flag = 1;//第二波将flag置为1的机会
								}
							}
							}
						} 
					}
					//有捎带或者WFS的电梯
					if(flag==1) {
						//将Rq加到carry_sch
						synchronized(carry_sch) {
							carry_sch.RQ.addReq(Rq[j]);
							//System.out.println(j+"DOWN捎带或WFS成功 "+carry_sch.elenum);
						}
						
						
					}
					if(flag==0) {//没有捎带和WFS的电梯
						//加到waitque里去
						//System.out.println("加入waiting"+Rq[j]);
						synchronized(waitque) {
							waitque.waitqueue.addReq(Rq[j]);
						}
						
					}
					
				}  
				//ELE
				else if(Pattern.matches(elevReq,eachreq[j])) {
					String[] eachpart = eachreq[j].split("[,]");//用逗号分开
					m = Integer.parseInt(eachpart[2].substring(0, eachpart[2].length()-1));//楼层数
					Rq[j] = new Requ(m,0,timereal,eachreq[j]);//
					int elenum = Integer.parseInt(eachpart[1].substring(1, eachpart[1].length()));
					//System.out.println(System.currentTimeMillis()+" 获得该电梯请求，即将给电梯");
					//ALS_th[elenum].run();
					//System.out.println(elenum+" 几号电梯");
					if(elenum==1) {
						//Object ele_1;
						synchronized(ele_1) {
							ele_1.RQ.addReq(Rq[j]);
						}
						
						//System.out.println(System.currentTimeMillis()+" 电梯号数："+elenum+" 队列数目："+ele_1.RQ.RQnum());
					}
					else if(elenum==2) {
						
						synchronized(ele_2) {
							ele_2.RQ.addReq(Rq[j]);
						}
						
						//System.out.println(System.currentTimeMillis()+" 电梯号数："+elenum+" 队列数目："+ele_2.RQ.RQnum());
						//System.out.println(System.currentTimeMillis()+" 2号电梯得到请求");
					}
					else {
						
						synchronized(ele_3) {
							ele_3.RQ.addReq(Rq[j]);
						}
						
						//System.out.println(System.currentTimeMillis()+" 电梯号数："+elenum+" 队列数目："+ele_3.RQ.RQnum());
					}
					//System.out.println(System.currentTimeMillis()+" 给了电梯请求");
					//RQ.addReq(Rq);
				}
				
				else if(Pattern.matches(END,eachreq[j])) {
					//结束,不再接受输入
					out = 1;
					//New_sch.ifstop = true;//输入END后剩下请求不接受，只处理之前请求
					WaitQue.ifstop = true;
					break;
				}
				else {
					System.setOut(ps);
					st = System.currentTimeMillis();
					System.out.println(st+":INVALID["+eachreq[j]+","+timereal+"]");
					//continue;
				}
			}
		
			i++;//请求次数
		}//超过50次请求后，电梯执行完队列已有请求后结束
		//New_sch.ifstop = true;
		WaitQue.ifstop = true;
	}

}
