package oo;
 
public class Requ {
	    //Ŀ��¥��    
		private int aimstage;
		//�������
		private int reqnum;//DOWN=-1;UP=1;������=0;
		//����ʱ��
		private int T;
		public Requ(int a,int b,int c){
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
		public int getT() {
			return T;
		}


}
