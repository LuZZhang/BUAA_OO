package oo;  

public class Ele { 
	
	public double run(int nowfl,int aimfl,double nowT) {
		double finT;
		finT = (double)(Math.abs(nowfl-aimfl))*0.5+nowT;
		return finT;
	}
	   
	public double OpenDo(double nowT) {
		return nowT+1;
	}

}
  