package work_14;   

public class Ele {//implements OpenDoor{
	/**
	 * @Overview: 电梯类，实现电梯运行和开关门时间计算。
	 */
	public boolean repOK() {
		/** @REQUIRES: None;
		@MODIFIES: None;
		@EFFECTS: \result == true;
		*/
		return true;
	}
	
	@SuppressWarnings("null")
	public double run(int nowfl,int aimfl,double nowT) {
		/** @REQUIRES: (nowfl>=1&&nowfl<=10);
		 * (aimfl>=1&&aimfl<=10);
		 * (nowT>=0);
		@MODIFIES: None;
		@EFFECTS: \result == abs(nowfl-aimfl)*0.5 + nowT;
		*/
		double finT;
		if(nowfl<1||nowfl>10||aimfl<1||aimfl>10||nowT<0) {
			try {
				throw new Exception();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		finT = (double)(Math.abs(nowfl-aimfl))*0.5+nowT;
		return finT; 
	}
	
	public double OpenDo(double nowT) {
		/** @REQUIRES: (nowT>=0);
		@MODIFIES: None;
		@EFFECTS: \result == nowT + 1;
		*/
		if(nowT<0) {
			try {
				throw new Exception();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return nowT+1;
	}
}
 