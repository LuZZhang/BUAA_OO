package work_10;

public class RobbingMess {//抢单信息
	/**
     * @Overview:抢单信息类。
     */ 
	public int taxinumber;//抢单车号
	public int nowi;//抢单时车辆位置  
	public int nowj;
	public int status;//车辆状态
	public int creditnum;//车辆信用信息
	
	public boolean repOK() {
		/**@REQUIRES: None;
		@MODIFIES: None;
		@Effects: \result == invariant(this);
		*/
		if (taxinumber<0||taxinumber>99||nowi<0||nowj>79||status<0||status>3||creditnum<0)
			return false;
		return true;
	}
	public RobbingMess(int a,int b,int c,int d,int e) {
		/** @REQUIRES: (\all integer a; 0 <= a <= 99);
		*               (\all integer b; 0 <= b <= 79);
		*               (\all integer c; 0 <= c <= 79);
		*               (\all integer d; 0 <= d <= 3);
		*               (\all integer e; 0 <= e);
		@MODIFIES: taxinumber,nowi,nowj,status,creditnumber;
		@EFFECTS:  taxinumber = a;
		*			nowi = b;
		*			nowj = c;
		*			status = d;
		*			creditnumber = e;
		@ */
		taxinumber = a;
		nowi = b;
		nowj = c;
		status = d;
		creditnum = e;
	}

}
 