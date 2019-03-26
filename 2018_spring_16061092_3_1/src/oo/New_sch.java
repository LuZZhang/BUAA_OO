package oo;

public class New_sch extends Schl{
	private static RequQue RQ = new RequQue();
	private static OpenDoor ev = new Ele();
	public void Schedule() {
		double T_now = 0,T = 0;
		int pre_stage = 1,aim_stage = 1;
		while(RQ.RQnum()!=0) {//当请求队列不为空
			//得到主请求
			int falg = 0;
			Requ main_req = null;
			int m=0;
			for(m=0;m<RQ.RQnum();m++) {
				if(RQ.nowvalid(m)==2) {
					main_req = RQ.nowRq(m);
					falg =1;//break
				} 
			}
			if(falg==0) {
				m=0;
				RQ.setvalid_two(m);
				main_req = RQ.nowRq(m);
			}
			T_now = Math.max(T_now, main_req.getT());
			pre_stage = aim_stage;//主请求还未执行时所在楼层
			aim_stage = main_req.getAimstage();//主请求目标楼层
			if(aim_stage>pre_stage) {//UP
				int flag = 0;
				for(int i=pre_stage+1;i<=aim_stage;i++) {
					T_now = T_now + 0.5;
					//检索捎带
					flag = 0;
					//检测有没有第i层的可捎带请求
					for(int j=0;j<RQ.RQnum();j++) {
						//若valid
						T = RQ.nowRq(j).getT();
						if(T<T_now&&(RQ.nowRq(j).getReqnum()!=-1||RQ.nowvalid(j)==2)&&RQ.nowRq(j).getAimstage()==i) {
							//执行该请求
							System.out.print(RQ.nowRq(j));
							System.out.printf("/(%d,UP,%.1f)%n", i, T_now);
							//删同质
							flag =1;
							////开关门后更新时间
							super.dele_same(j,T_now+1,i);
							RQ.subReq(j);
							j--;
						}
					}
					if(flag==1) T_now = ev.OpenDo(T_now);
				}//执行完包括主请求的可捎带请求
				for(int i=0;i<RQ.RQnum();i++) {
					//T_now=T_now+0.5;
					if(RQ.nowRq(i).getT()<T_now-1&&RQ.nowRq(i).getAimstage()>aim_stage&&RQ.nowRq(i).getReqnum()==0) {
						//将其作为当前主请求
						RQ.setvalid_two(i);
						break;
					}
				}
			}
			else if(aim_stage<pre_stage){//DOWN
				int flag = 0;
				for(int i=pre_stage-1;i>=aim_stage;i--) {
					T_now = T_now + 0.5;
					//检索捎带
					flag = 0;
					//检测有没有第i层的可捎带请求
					for(int j=0;j<RQ.RQnum();j++) {
						//若valid
						T = RQ.nowRq(j).getT();
						if(T<T_now&&(RQ.nowRq(j).getReqnum()!=1||RQ.nowvalid(j)==2)&&RQ.nowRq(j).getAimstage()==i) {
							System.out.print(RQ.nowRq(j));
							System.out.printf("/(%d,DOWN,%.1f)%n", i, T_now);
							//删同质
							flag =1;
							////开关门后需要更新时间
							super.dele_same(j,T_now+1,i);
							RQ.subReq(j);
							j--;
						}
					}
					if(flag==1) T_now = ev.OpenDo(T_now);
				}//执行完包括主请求的可捎带请求
				for(int i=0;i<RQ.RQnum();i++) {
					//T_now=T_now+0.5;
					if(RQ.nowRq(i).getT()<T_now-1&&RQ.nowRq(i).getAimstage()<aim_stage&&RQ.nowRq(i).getReqnum()==0) {
						//将其作为当前主请求
						RQ.setvalid_two(i);
						break;
					}
				}
			}
			else {//STILL
				T_now = ev.OpenDo(T_now); 
				System.out.print(RQ.nowRq(m));
				System.out.printf("/(%d,STILL,%.1f)%n", aim_stage, T_now);
				super.dele_same(m,T_now,aim_stage);
				RQ.subReq(m);
			}
		}
	}
}
