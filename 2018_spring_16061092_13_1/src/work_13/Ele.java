package work_13;   

public class Ele implements OpenDoor{
	/**
	 * @Overview: 电梯类，实现电梯运行和开关门时间计算。
	 */
	public boolean repOK() {
		/** @REQUIRES: None;
		@MODIFIES: None;
		@EFFECTS: /result == true;
		@ */
		return true;
	}
	
	public double run(int nowfl,int aimfl,double nowT) {
		/** @REQUIRES: (nowfl>=1&&nowfl<=10);
		 * (aimfl>=1&&aimfl<=10);
		 * (nowT>=0);
		@MODIFIES: None;
		@EFFECTS: /result == abs(nowfl-aimfl)*0.5 + nowT;
		@ */
		double finT;
		finT = (double)(Math.abs(nowfl-aimfl))*0.5+nowT;
		return finT; 
	}
	public double OpenDo(double nowT) {
		/** @REQUIRES: (nowT>=0);
		@MODIFIES: None;
		@EFFECTS: /result == nowT + 1;
		@ */
		return nowT+1;
	}
}
 