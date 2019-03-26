package oo;

public class New_sch extends Thread{
	protected  RequQue RQ = new RequQue();//请求队列
	private  OpenDoor ev = new Ele();
	//private Schl schl = new Schl();
	volatile static long Tstart;//第一条请求发出时的系统时间
	volatile protected int sumrunt;//累计运动量
	volatile protected double T;//请求产生时刻的电梯系统时间
	volatile protected double t;//刚执行完时刻的电梯系统时间
	private long st = 0;//系统时间
	volatile int elenum;
	volatile static boolean ifstop = false;
	volatile int floornext = 1;
	volatile int aim_stage = 1;
	double T_now = 0;
	int pre_stage = 1;
	Requ main_req;
	int main_req_fx; 
	public void run() {
		while(true) {//当请求队列不为空
			//得到主请求
			while(RQ.RQnum()!=0) {
			floornext=0;//电梯队列不为空
			//System.out.println("#"+elenum+"电梯当前队列数： "+RQ.RQnum()+"上次执行完毕电梯系统时间："+T_now);//上次执行完毕电梯系统时间
			//System.out.println("Tstart： "+Tstart);
			int falg = 0;
			main_req = null;//主请求
			int m=0;
			st = System.currentTimeMillis();//当前系统时间
			long time,time1,time2;
			time = st - Tstart;
			time1 = time/100;
			time2 = time - time1;
			if(time2>=50){
				time1 = time1 + 1;
			}
			double real_T_now = time1/(double)10;//当前时刻电梯系统时间,真时间
		//	System.out.println("#"+elenum+"电梯当前队列数： "+RQ.RQnum()+"当前系统时间"+real_T_now);
			for(m=0;m<RQ.RQnum();m++) {
				//System.out.println(RQ.RQnum()+" "+RQ.vanum());
				if(RQ.nowvalid(m)==2) {
					main_req = RQ.nowRq(m);
					falg =1;//break
				} 
			}
			if(falg==0) {
				m=0;
				RQ.setvalid_two(m);
				main_req = RQ.nowRq(m);
			}//判断结束主请求
			if(main_req.getT()>T_now) {//若主请求发出时间大于上个请求执行完后的时间，用真时间
				T_now = real_T_now;
			}
			//T_now = Math.max(T_now, main_req.getT());
			pre_stage = aim_stage;//主请求还未执行时所在楼层,即当前所在楼层
			aim_stage = main_req.getAimstage();//主请求目标楼层
			if(aim_stage>pre_stage) {//UP
				main_req_fx = 1;
				int flag = 0;
				for(int i=pre_stage+1;i<=aim_stage;i++) {
					//sumrunt++;//运动量
					T_now = T_now + 3;
					floornext = i;//下一个即将到达的楼层
					try {
						sleep(3000);//上一层楼
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					sumrunt++;//累计运动量
					floornext = i + 1;//可能会是21
					//检索捎带
					flag = 0;
					//检测有没有第i层的可捎带请求
					for(int j=0;j<RQ.RQnum();j++) {
						//若valid
						//T = RQ.nowRq(j).getT();
						if((RQ.nowRq(j).getReqnum()!=-1||RQ.nowvalid(j)==2)&&RQ.nowRq(j).getAimstage()==i) {
							//执行该请求
							System.setOut(Main_Sch.ps);
							System.out.printf("%d:[%s,%.1f]/(#%d,%d,UP,%d,%.1f)%n",System.currentTimeMillis(),RQ.nowRq(j).reqstring,RQ.nowRq(j).getT(), elenum, i, sumrunt,T_now);
							//删同质
							flag =1;
							////开关门后更新时间
							dele_same(j,T_now+6,i);//删同质
							RQ.subReq(j);
							j--;
						}
					}
					if(flag==1) {
						T_now = ev.OpenDo(T_now);
						//System.out.println("开关门");
						//floornext = i + 1;
						try {
							sleep(6000);//开关门
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}//执行完包括主请求的可捎带请求
				for(int i=0;i<RQ.RQnum();i++) {
					//T_now=T_now+0.5;
					if(RQ.nowRq(i).getT()<T_now-6&&RQ.nowRq(i).getAimstage()>aim_stage&&RQ.nowRq(i).getReqnum()==0) {
						//将其作为当前主请求
						RQ.setvalid_two(i);
						break;
					}
				}
			}
			else if(aim_stage<pre_stage){//DOWN
				main_req_fx = -1;
				int flag = 0;
				for(int i=pre_stage-1;i>=aim_stage;i--) {
					//sumrunt++;//累计运动量
					T_now = T_now + 3;
					floornext = i;
					try {
						sleep(3000);//下一层楼
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					sumrunt++;//累计运动量
					floornext = i - 1;
					//检索捎带
					flag = 0;
					//检测有没有第i层的可捎带请求
					for(int j=0;j<RQ.RQnum();j++) {
						//若valid
						//T = RQ.nowRq(j).getT();
						if((RQ.nowRq(j).getReqnum()!=1||RQ.nowvalid(j)==2)&&RQ.nowRq(j).getAimstage()==i) {
							System.setOut(Main_Sch.ps);
							System.out.printf("%d:[%s,%.1f]/(#%d,%d,DOWN,%d,%.1f)%n",System.currentTimeMillis(),RQ.nowRq(j).reqstring,RQ.nowRq(j).getT(), elenum, i, sumrunt,T_now);
							//删同质
							flag =1;
							////开关门后需要更新时间
							dele_same(j,T_now+6,i);//
							RQ.subReq(j);
							j--;
						}
					}
					if(flag==1) {
						T_now = ev.OpenDo(T_now);
						
						try {
							sleep(6000);//开关门
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}//执行完包括主请求的可捎带请求
				for(int i=0;i<RQ.RQnum();i++) {
					//T_now=T_now+0.5;
					if(RQ.nowRq(i).getT()<T_now-6&&RQ.nowRq(i).getAimstage()<aim_stage&&RQ.nowRq(i).getReqnum()==0) {
						//将其作为当前主请求
						RQ.setvalid_two(i);
						break;
					}
				}
			}
			else {//STILL
				floornext=0;
				main_req_fx = 0;
				T_now = ev.OpenDo(T_now);
				try {
					sleep(6000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.setOut(Main_Sch.ps);
				System.out.printf("%d:[%s,%.1f]/(#%d,%d,STILL,%d,%.1f)%n",System.currentTimeMillis(),RQ.nowRq(m).reqstring,RQ.nowRq(m).getT(), elenum, aim_stage, sumrunt,T_now);
				dele_same(m,T_now,aim_stage);
				RQ.subReq(m);
			}
		} //请求队列为空
		floornext = -1;//WFS
		if(ifstop) {
			break;
		}
	}//线程结束
		//System.out.println("ele end");
	}
	public void dele_same(int i,double T_now,int aim_stage) {
		for(int j=i+1;j<RQ.RQnum();j++) {
			if((RQ.nowRq(j).getAimstage()==aim_stage)&&(RQ.nowRq(j).getT()<=T_now)&&(RQ.nowRq(j).getReqnum()==RQ.nowRq(i).getReqnum())) {
				RQ.setvalid_none(j);
				System.setOut(Main_Sch.ps);
				System.out.println("#"+System.currentTimeMillis()+":SAME"+RQ.nowRq(j));
				RQ.subReq(j);
				j--;
			}
			
		}
		
	}
}
