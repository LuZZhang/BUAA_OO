package work_13;
 
public class Requ {   
	    //目标楼层
		private int aimstage;
		//请求代号
		private int reqnum;//DOWN=-1;UP=1;电梯类请求=0;
		//请求发出时间
		private double T;
		public Requ(int a,int b,double c){
			aimstage = a;
			reqnum = b; 
			T = c; 
		}
		public int getReqnum() {
			return reqnum;
		}
		public int getAimstage() {
			return aimstage;
		} 
		public double getT() {
			return T;
		}
		@Override
		public String toString() {
			if(reqnum==0) return "[ER," + aimstage + ","  + (long)T + "]";//[FR,%d,UP,%.0f]
			else if(reqnum==-1) return "[FR," + aimstage + ",DOWN,"  + (long)T + "]"; 
			else return "[FR," + aimstage + ",UP,"  + (long)T + "]"; 
		}


}
