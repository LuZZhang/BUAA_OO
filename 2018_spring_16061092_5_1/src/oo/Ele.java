package oo;   

public class Ele implements OpenDoor{  
	
	public double run(int nowfl,int aimfl,double nowT) {
		double finT;
		finT = (double)(Math.abs(nowfl-aimfl))*3+nowT;
		return finT;
	} 
	public double OpenDo(double nowT) {
		return nowT+6;
	}
} 
 