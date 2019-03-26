package work_13;

public class New_sch {
	/**
	 * @Overview:调度器调度类，实现对请求队列的调度。
	 */
	private static RequQue RQ = new RequQue();
	//private static OpenDoor ev = new Ele();
	
	public boolean repOK() {
		/**@REQUIRES: None;
		@MODIFIES: None;
		@EFFECTS: \result == invariant(this);
		*/
		if(getRQ()==null) return false;
		return true;
	}
	
	public void Schedule() {
		/** @REQUIRES: None;
		@MODIFIES: RQ,System.out;
		@EFFECTS: 调度器调度请求队列,完成判断捎带,同质请求,主请求等一系列工作，且请求完成后输出相应信息;
		@ */
		OpenDoor ev = new Ele();
		double T_now = 0,T = 0;
		int pre_stage = 1,aim_stage = 1;
		while(getRQ().RQnum()!=0) {//当请求队列不为空 
			//得到主请求
			int falg = 0;
			Requ main_req = null; 
			int m=0;
			for(m=0;m<getRQ().RQnum();m++) {
				if(getRQ().nowvalid(m)==2) {//若当前队列里有主请求
					main_req = getRQ().nowRq(m); 
					falg =1;//break
				} 
			}
			if(falg==0) {//若当前队列里无主请求
				m=0;
				getRQ().setvalid_two(m);//将队列里第一个设为主请求
				main_req = getRQ().nowRq(m);
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
					for(int j=0;j<getRQ().RQnum();j++) {
						//若valid
						T = getRQ().nowRq(j).getT();
						if(T<T_now&&(getRQ().nowRq(j).getReqnum()!=-1||getRQ().nowvalid(j)==2)&&getRQ().nowRq(j).getAimstage()==i) {
							//执行该请求
							System.out.print(getRQ().nowRq(j));
							System.out.printf("/(%d,UP,%.1f)%n", i, T_now);
							//删同质
							flag =1;
							////开关门后更新时间
							dele_same(j,T_now+1,i);
							getRQ().subReq(j);
							j--;
						}
					}
					if(flag==1) T_now = ev.OpenDo(T_now);
				}//执行完包括主请求的可捎带请求
				for(int i=0;i<getRQ().RQnum();i++) {
					//T_now=T_now+0.5;
					if(getRQ().nowRq(i).getT()<T_now-1&&getRQ().nowRq(i).getAimstage()>aim_stage&&getRQ().nowRq(i).getReqnum()==0) {
						//将其作为当前主请求
						getRQ().setvalid_two(i);
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
					for(int j=0;j<getRQ().RQnum();j++) {
						//若valid 
						T = getRQ().nowRq(j).getT();
						if(T<T_now&&(getRQ().nowRq(j).getReqnum()!=1||getRQ().nowvalid(j)==2)&&getRQ().nowRq(j).getAimstage()==i) {
							System.out.print(getRQ().nowRq(j));
							System.out.printf("/(%d,DOWN,%.1f)%n", i, T_now);
							//删同质
							flag =1;
							////开关门后需要更新时间
							dele_same(j,T_now+1,i);
							getRQ().subReq(j);
							j--;
						} 
					}
					if(flag==1) T_now = ev.OpenDo(T_now);
				}//执行完包括主请求的可捎带请求
				for(int i=0;i<getRQ().RQnum();i++) {
					//T_now=T_now+0.5;
					if(getRQ().nowRq(i).getT()<T_now-1&&getRQ().nowRq(i).getAimstage()<aim_stage&&getRQ().nowRq(i).getReqnum()==0) {
						//将其作为当前主请求
						getRQ().setvalid_two(i);
						break;
					}
				}
			}
			else {//STILL
				T_now = ev.OpenDo(T_now); 
				System.out.print(getRQ().nowRq(m));
				System.out.printf("/(%d,STILL,%.1f)%n", aim_stage, T_now);
				dele_same(m,T_now,aim_stage);
				getRQ().subReq(m);
			}
		}
	}
	public void dele_same(int i,double T_now,int aim_stage) {
		/** @REQUIRES: (i>=0&&i<RQ.queue.length);
		@MODIFIES: RQ,System.out;
		@EFFECTS: 删除RQ队列T_now时刻,队列第i个请求后的同质请求,并输出相应结果。;
		@ */
		for(int j=i+1;j<getRQ().RQnum();j++) { 
			if((getRQ().nowRq(j).getAimstage()==aim_stage)&&(getRQ().nowRq(j).getT()<=T_now)&&(getRQ().nowRq(j).getReqnum()==getRQ().nowRq(i).getReqnum())) {
				getRQ().setvalid_none(j);
				if(getRQ().nowRq(j).getReqnum()==0) {
					System.out.printf("#SAME[ER,%d,%.0f]%n",getRQ().nowRq(j).getAimstage() ,getRQ().nowRq(j).getT());
				}
				else if(getRQ().nowRq(j).getReqnum()==1) {
					System.out.printf("#SAME[FR,%d,UP,%.0f]%n",getRQ().nowRq(j).getAimstage() ,getRQ().nowRq(j).getT());
				}
				else {
					System.out.printf("#SAME[FR,%d,DOWN,%.0f]%n",getRQ().nowRq(j).getAimstage() ,getRQ().nowRq(j).getT());
				}
				getRQ().subReq(j);
				j--;
			}
			
		}
		
	}

	public static RequQue getRQ() {
		/** @REQUIRES: None;
		@MODIFIES: None;
		@EFFECTS: /result == RQ;
		@ */
		return RQ;
	}

	public static void setRQ(RequQue rQ) {
		/** @REQUIRES: rQ!=null;
		@MODIFIES: this.RQ;
		@EFFECTS: this.RQ == rQ;
		@ */
		RQ = rQ;
	}
}
