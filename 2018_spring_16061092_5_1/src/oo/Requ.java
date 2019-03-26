package oo;
 
public class Requ { 
		private int aimstage;
		private int reqnum;//DOWN=-1;UP=1;
		private double T; 
		String reqstring;
		public Requ(int a,int b,double c,String s){
			aimstage = a;
			reqnum = b;
			T = c; 
			reqstring = s.replace("(", "");
			reqstring = reqstring.replace(")", "");
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
			return "[" + reqstring + "," + T + "]";
		}


}
