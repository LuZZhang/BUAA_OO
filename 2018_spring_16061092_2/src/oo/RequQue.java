package oo;
 
import java.util.ArrayList;

public class RequQue {
	private static ArrayList<Requ> queue = new ArrayList<Requ>();
	private static ArrayList<Integer> valid = new ArrayList<Integer>();
	public void addReq(Requ Rq) {
		queue.add(Rq);
		valid.add(1);   
	}
	public void subReq(){
		queue.remove(0);
		valid.remove(0);
	}
	public int RQnum() {
		return queue.size();
	}
	public Requ nowRq(int i) {
		return queue.get(i);
	}
	public void setvalid(int i) {
		valid.set(i, 0);
	}
	public int nowvalid() {
		return valid.get(0);
	}

}

