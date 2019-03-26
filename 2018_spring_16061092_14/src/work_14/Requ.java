package work_14;
 
public class Requ { 
		/**
		 * @Overview:请求类,定义请求。
		 */
		//rep
		private int aimstage;//目标楼层
		private int reqnum;//请求代号//DOWN=-1;UP=1;电梯类请求=0;
		private double T;//请求发出时间
		
		public boolean repOK() {
			/** @REQUIRES: None;
			@MODIFIES: None;
			@EFFECTS: \result == invariant(this);
			*/
			if(aimstage<1||aimstage>10) return false;
			else if(reqnum<-1||reqnum>1) return false;
			else if(T<0) return false;
			return true;
		}
		
		public Requ(int a,int b,double c){
			/** @REQUIRES: a>0&&a<11;
			 *             b==-1||b==0||b==1;
			 *             c>=0;
			@MODIFIES: \this;
			@EFFECTS: \this.aimstage == a;
			 *        \this.reqnum == b;
			 *        \this.T == c;
			 */
			if(a<1||a>10||b<-1||b>1||c<0) {
				return ;
			}
			aimstage = a;
			reqnum = b; 
			T = c; 
		}
		
		public int getReqnum() {
			/** @REQUIRES: None;
			@MODIFIES: None;
			@EFFECTS: \result == \this.reqnum;
			*/
			return reqnum;
		}
		
		public int getAimstage() {
			/** @REQUIRES: None;
			@MODIFIES: None;
			@EFFECTS: \result == \this.aimstage;
			*/
			return aimstage;
		} 
		
		public double getT() {
			/** @REQUIRES: None;
			@MODIFIES: None;
			@EFFECTS: \result == \this.T;
			*/
			return T;
		}
		
		@Override
		public String toString() {
			/** @REQUIRES: reqnum == -1||reqnum == 0||reqnum == 1;
			@MODIFIES: None;
			@EFFECTS: reqnum == 0 ==> \result == "[ER," + aimstage + ","  + (long)T + "]";
			 *        reqnum ==-1 ==> \result == "[FR," + aimstage + ",DOWN,"  + (long)T + "]";
			 *        reqnum == 1 ==> \result == "[FR," + aimstage + ",UP,"  + (long)T + "]";
			 */
			if(!(reqnum==-1||reqnum==0||reqnum==1)) {
				return "error";
			}
			if(reqnum==0) return "[ER," + aimstage + ","  + (long)T + "]";//[FR,%d,UP,%.0f]
			else if(reqnum==-1) return "[FR," + aimstage + ",DOWN,"  + (long)T + "]"; 
			else return "[FR," + aimstage + ",UP,"  + (long)T + "]"; 
		}


}
