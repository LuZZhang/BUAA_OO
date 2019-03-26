package work_14;

public class New_sch {
	/**
	 * @Overview:调度器调度类，实现对请求队列的调度。
	 */
	//rep
	private static RequQue RQ = new RequQue();
	
	public boolean repOK() {
		/**@REQUIRES: None;
		@MODIFIES: None;
		@EFFECTS: \result == invariant(this);
		*/
		if(RQ==null) return false;
		return true;
	}
	
	public void Schedule() {
		/** @REQUIRES: None;
		@MODIFIES: RQ,System.out;
		@EFFECTS: 
		*      (RQ.size != 0) ==> (main_req.nowvalid == 2 && pre_stage == (\old)aim_stage && aim_stage == main_req.aimstage) &&
		*                         ((aim_stage > pre_stage && flag == 0 && ((\all int i = pre_stage,i<=aim_stage)
		*                                                      ==> (T_now == (\old)T_now + 0.5 && flag == 0) && ((\all int j = 0,j<RQ.size)
		*                                                                                                                ==>(T == RQ(j).T && T<T_now && (RQ(j).Reqnum != -1 || RQ(j).valid == 2) && RQ(j).aimstage == i) 
		*                                                                                                                &&(print(RQ(j)/(i,T_now)) && flag ==1 && RQ.size == (\old)RQ.size - (\old)RQ.same_req.number - 1 && RQ.contains(RQ(j).same_req) == false && RQ.contains((\old)RQ(j)) == false && j == (\old)j - 1) &&
		*                                                                                                                (j == (\old)j + 1)
		*                                                                                                       ) &&
		*                                                                                                       (flag == 1 && T_now == (\old)T_now + 1) &&
		*                                                                       (i == (\old)i + 1)
		*                                                                 ) &&
		*                                                             	  ( (\all int i=0,i<RQ.size)
		*                                                                  ==> (RQ(i).T<T_now-1 && RQ(i).aimstage>aim_stage && RQ(i).Reqnum ==0) &&(RQ(i).valid == 2) &&(out of loop) ||
		*                                                                      i == (\old)i + 1
		*                                                                 )
		*                          ) ||
		*                          ((aim_stage<pre_stage && flag == 0 && ((\all int i = pre_stage && i>= aimstage)
		*                                                      ==> (T_now == (\old)T_now + 0.5 && flag == 0) && ((\all int j  =0,j<RQ.size)
		*                                                                                                                ==>(T == RQ(j).T && T<T_now && (RQ(j).Reqnum != 1 || RQ(j).valid == 2) && RQ(j).aimstage == i)
		*                                                                                                                &&(print(RQ(j)/(i,T_now)) && flag ==1 && RQ.size == (\old)RQ.size - (\old)RQ.same_req.number - 1 && RQ.contains(RQ.(j).same_req) == false && RQ.contains((\old)RQ(j)) == false && j == (\old)j - 1)&&
		*                                                                                                                (j == (\old)j + 1)
		*                                                                                                       ) &&
		*                                                                                                       (flag == 1 && T_now == (\old)T_now + 1)
		*                                                                       (i == (\old)i - 1)
		*                                                                ) &&
		*                                                                ( (\all int i=0,i<RQ.size)
		*                                                                 ==> (RQ(i)).T<T_now-1 && RQ(i).aimstage>aim_stage && RQ(i).Reqnum ==0) &&(RQ(i).valid == 2) &&(out of loop) ||
		*                                                                     i == (\old)i + 1
		*                                                                )
		*                                                                
		*                           ) ||
		*                           (
		*                              (aim_stage==pre_stage&& T_now == (\old)T_now + 1 && print(RQ(m)/(aim_stage,Tnow)) && RQ.size == (\old)RQ.size - (\old)RQ.same_req.number - 1&& RQ.contains(RQ.(j).same_req) == false && RQ.contains((\old)RQ(j)) == false && j == (\old)j - 1)
		*                           )
		*                          );
		*/
		Ele ev = new Ele();
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
		 *             (T_now >= 0);
		 *             (1<= aim_stage <= 10);
		@MODIFIES: RQ,System.out;
		@EFFECTS: 
		*       (\all int j=i+1;j<RQ.size) ==> ((RQ(j).aimstage==aim_stage && RQ(j).T<=T_now && RQ(j).Reqnum==RQ(i).Reqnum) && (RQ(j).valid==0) && (RQ(j).Reqnum==0 && print ""#SAME[ER,%d,%.0f]%n",RQ(j).aimstage ,RQ(j).T") && (RQ.size == (\old)RQ.size - 1 && j == (\old)j - 1) ||
		*                                       (RQ(j).aimstage==aim_stage && RQ(j).T<=T_now && RQ(j).Reqnum==RQ(i).Reqnum) && (RQ(j).valid==0) && (RQ(j).Reqnum==0 && print ""#SAME[FR,%d,UP,%.0f]%n",RQ(j).aimstage,RQ(j).T") && (RQ.size == (\old)RQ.size - 1 && j == (\old)j - 1) ||
		*                                       (RQ(j).aimstage==aim_stage && RQ(j).T<=T_now && RQ(j).Reqnum==RQ(i).Reqnum) && (RQ(j).valid==0) && (RQ(j).Reqnum==0 && print ""#SAME[FR,%d,DOWN,%.0f]%n",RQ(j).aimstage,RQ(j).T") && (RQ.size == (\old)RQ.size - 1 && j == (\old)j - 1)) &&
		*                                      j == (\old)j + 1; 
		*/
		if((i<0)||(i>=getRQ().RQnum())||(T_now<0)||(aim_stage<1)||(aim_stage>10)) {
			return;
		}
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
		@EFFECTS: \result == RQ;
		*/
		return RQ;
	}

	public static void setRQ(RequQue rQ) {
		/** @REQUIRES: rQ != null;
		@MODIFIES: \this.RQ;
		@EFFECTS: \this.RQ == rQ;
		*/
		if(RQ==null) {
			return ;
		}
		RQ = rQ;
	}


}
