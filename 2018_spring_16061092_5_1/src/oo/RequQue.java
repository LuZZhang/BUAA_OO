package oo;

import java.util.ArrayList;
  
public class RequQue {
	private  ArrayList<Requ> queue = new ArrayList<Requ>();
	private  ArrayList<Integer> valid = new ArrayList<Integer>();
	public void addReq(Requ Rq) { 
		queue.add(Rq);
		valid.add(1); 
	}
	public void subReq(int i){
		queue.remove(i); 
		valid.remove(i);
	}
	public int RQnum() {
		return queue.size();
	} 
	public int vanum() {
		return valid.size();
	} 
	public Requ nowRq(int i) {
		return queue.get(i);
	}
	public void setvalid_none(int i) {
		valid.set(i, 0);
	}
	public int nowvalid(int i) {
		return valid.get(i);
	}
	public void setvalid_two(int i) {
		valid.set(i, 2);
	}

}

