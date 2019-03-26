package oo;

public class WaitQue extends Thread{
	RequQue waitqueue = new RequQue();//等待队列
	volatile static boolean ifstop = false;
	New_sch ele_1;
    New_sch ele_2;
	New_sch ele_3;
	public WaitQue(Thread e1,Thread e2,Thread e3) {
		ele_1=(New_sch)e1;
		ele_2=(New_sch)e2;
		ele_3=(New_sch)e3;
	}
	public void run() { 
		while(true) {//一直扫描
			while(waitqueue.RQnum()!=0) {//当等待队列不为空
				//System.out.println("waiting");
				//不为空，就要看什么时候想办法把他扔出去
				for(int i=0;i<waitqueue.RQnum();i++) {
					//针对每一个wait队列里的请求
					//分为UP和DOWN
					//UP
					//System.out.println(waitqueue.RQnum());
					if(waitqueue.nowRq(i).getReqnum()==1) {//UP
						Requ Rq = waitqueue.nowRq(i);//
						//判断是否可以被捎带
						New_sch carry_sch = null;//捎带电梯
						New_sch now_sch = null;//当前电梯
						int now_min = 1000000000;//当前最小运动量
						int flag = 0;//判断是否有可以稍带的电梯
						for(int k=1;k<=3;k++) {
							if(k==1) now_sch = ele_1;
							else if(k==2) now_sch = ele_2;
							else now_sch = ele_3;
							//now_sch是当前的电梯
							synchronized(now_sch) {
							if(now_sch.main_req_fx==1&&Rq.getAimstage()>=now_sch.floornext&&Rq.getAimstage()<=now_sch.aim_stage) {
								//可以被捎带
								if(now_sch.sumrunt<=now_min) {//这里的now_min是可以被捎带电梯的最小运动量																			
									carry_sch = now_sch;//选择它作为捎带电梯
								    now_min = carry_sch.sumrunt;
								    flag = 1;//第一波将flag置为1的机会
								}
								
							}
							}
						}
						if(flag==0) {//如果没有可以稍带的电梯,此时carry_sch=null
							//那就判断是否有WFS的电梯
							now_min = 1000000000;//最大运动量重置为1000000000
							for(int k=0;k<=3;k++) {
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
						if(flag==1) {//恭喜，该请求已经被踢出wait队列
							//Object ele_1;
							synchronized(carry_sch) {
								carry_sch.RQ.addReq(Rq);
							}
							
							//System.out.println("被踢出wait队列");
							waitqueue.subReq(i);
							i--;
						}//否则留在wait队列
						
					}
					//DOWN
					else {//DOWN
						//System.out.println("有一个DOWN的wait请求");
						Requ Rq = waitqueue.nowRq(i);//
						//判断是否可以被捎带
						New_sch carry_sch = null;//捎带电梯
						New_sch now_sch = null;//当前电梯
						int now_min = 1000000000;//当前最小运动量
						int flag = 0;//判断是否有可以稍带的电梯
						for(int k=1;k<=3;k++) {
							if(k==1) now_sch = ele_1;
							else if(k==2) now_sch = ele_2;
							else now_sch = ele_3;
							//now_sch是当前的电梯
							synchronized(now_sch) {
							if(now_sch.main_req_fx==-1&&Rq.getAimstage()>=now_sch.aim_stage&&Rq.getAimstage()<=now_sch.floornext) {
								//可以被捎带
								if(now_sch.sumrunt<=now_min) {//这里的now_min是可以被捎带电梯的最小运动量																			
									carry_sch = now_sch;//选择它作为捎带电梯
								    now_min = carry_sch.sumrunt;
								    flag = 1;//第一波将flag置为1的机会
								}
								
							}
							}
						}
						if(flag==0) {//如果没有可以稍带的电梯,此时carry_sch=null
							//那就判断是否有WFS的电梯
							now_min = 1000000000;//最大运动量重置为1000000000
							for(int k=0;k<=3;k++) {
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
						if(flag==1) {//恭喜，该请求已经被踢出wait队列
							//Object ele_1;
							synchronized(carry_sch) {
								carry_sch.RQ.addReq(Rq);
							}
							
							//System.out.println("被踢出wait队列"+carry_sch.elenum);
							waitqueue.subReq(i);
							i--;
						}//否则留在wait队列
					}
				}
			}
		if(ifstop) {
			New_sch.ifstop = true;
			break;
		}
		}//线程结束 
	}

}
