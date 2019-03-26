package oo;
 
public class Schl {
	private static RequQue RQ = new RequQue();
	private static Ele Ev = new Ele();
	private double nowT=0,Tnew;
	private int nowstage=1,aimstage=1;
	private double T=0;
	public void Schedule() {
		Schl sch = new Schl();
		while(RQ.RQnum()!=0) {//当请求队列不为空
			//若该请求为之前请求的同质请求
			if(RQ.nowvalid(0)==0) {
				sch.output_same(0);
				continue;
			}
			//不是之前请求的同质请求
			else {
				aimstage = RQ.nowRq(0).getAimstage();
				T = RQ.nowRq(0).getT();//当前请求发生时刻
				if(T>nowT) nowT = T;
				Tnew = Ev.run(nowstage, aimstage, nowT);//电梯运行后时刻（还未执行开关门动作）
				sch.output_carried();
				Tnew = Ev.OpenDo(Tnew);//加上开关门用时
			}
			//排除该请求后的所有同质请求
			sch.dele_same(0,Tnew,aimstage);//Tnew是执行结束后时刻
			nowT = Tnew;
			nowstage = aimstage;
			RQ.subReq(0);
		}
	}
	public void output_carried() {
		String now_state = new String();
		if(nowstage==aimstage) {
			now_state = "STILL";
			System.out.printf("(%d,%s,%.1f)%n", aimstage, now_state, nowT+1);
		}
		else if(nowstage>aimstage) {
			now_state = "DOWN";
			System.out.printf("(%d,%s,%.1f)%n", aimstage, now_state, Tnew);
		}
		else {
			now_state = "UP";
			System.out.printf("(%d,%s,%.1f)%n", aimstage, now_state, Tnew);
		}
	}
	public void output_same(int i) {
		if(RQ.nowRq(0).getReqnum()==0) {
			System.out.printf("#SAME[ER,%d,%.0f]%n",RQ.nowRq(i).getAimstage() ,RQ.nowRq(i).getT());
		}
		else if(RQ.nowRq(0).getReqnum()==1) {
			System.out.printf("#SAME[FR,%d,UP,%.0f]%n",RQ.nowRq(i).getAimstage() ,RQ.nowRq(i).getT());
		}
		else {
			System.out.printf("#SAME[FR,%d,DOWN,%.0f]%n",RQ.nowRq(i).getAimstage() ,RQ.nowRq(i).getT());
		}
		RQ.subReq(0);
	}
	public void dele_same(int i,double T_now,int aim_stage) {
		for(int j=i+1;j<RQ.RQnum();j++) {
			if((RQ.nowRq(j).getAimstage()==aim_stage)&&(RQ.nowRq(j).getT()<=T_now)&&(RQ.nowRq(j).getReqnum()==RQ.nowRq(i).getReqnum())) {
				RQ.setvalid_none(j);
				if(RQ.nowRq(j).getReqnum()==0) {
					System.out.printf("#SAME[ER,%d,%.0f]%n",RQ.nowRq(j).getAimstage() ,RQ.nowRq(j).getT());
				}
				else if(RQ.nowRq(j).getReqnum()==1) {
					System.out.printf("#SAME[FR,%d,UP,%.0f]%n",RQ.nowRq(j).getAimstage() ,RQ.nowRq(j).getT());
				}
				else {
					System.out.printf("#SAME[FR,%d,DOWN,%.0f]%n",RQ.nowRq(j).getAimstage() ,RQ.nowRq(j).getT());
				}
				RQ.subReq(j);
				j--;
			}
			
		}
		
	}
}
