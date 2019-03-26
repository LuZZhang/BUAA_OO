package work_13;

import java.util.ArrayList;
   
public class RequQue {
	/**
	 * @Overview:请求队列类，实现添加和删除请求队列里的请求,得到一些返回值,设置一些属性值等功能。
	 */
	private static ArrayList<Requ> queue = new ArrayList<Requ>();
	private static ArrayList<Integer> valid = new ArrayList<Integer>();
	
	public boolean repOK() {
		/**@REQUIRES: None;
		@MODIFIES: None;
		@EFFECTS: \result == invariant(this);
		*/
		if(getQueue()==null) return false;
		else if(getValid()==null) return false;
		return true;
	}
	
	public void addReq(Requ Rq) { 
		/** @REQUIRES: (Rq != null);
		@MODIFIES: queue,valid;
		@EFFECTS: queue.size == old(queue.size) + 1 && queue.contains(Rq) == true;
		*         valid.size == old(valid.size) + 1 && queue.contains(1) == true;
		@ */
		getQueue().add(Rq); 
		getValid().add(1);   
	} 
	public void subReq(int i){
		/** @REQUIRES: (i>=0 && i<queue.size && i<valid.size);
		@MODIFIES: queue,valid;
		@EFFECTS: queue.size == old(queue.size) - 1 && queue.contains(old(queue).get(i)) == false;
		*         valid.size == old(valid.size) - 1 ;
		@ */
		getQueue().remove(i);
		getValid().remove(i);
	}
	public int RQnum() {
		/** @REQUIRES: None;
		@MODIFIES: None;
		@EFFECTS: /result == queue.size;
		@ */
		return getQueue().size();
	} 
	public Requ nowRq(int i) {
		/** @REQUIRES: (i>=0 && i<queue.size);
		@MODIFIES: None;
		@EFFECTS: /result == queue.get(i);
		@ */
		return getQueue().get(i);
	}
	public void setvalid_none(int i) {
		/** @REQUIRES: (i>=0 && i<valid.size);
		@MODIFIES: valid;
		@EFFECTS: valid.get(i) == 0;
		@ */
		getValid().set(i, 0);
	}
	public int nowvalid(int i) {
		/** @REQUIRES: (i>=0 && i<valid.size);
		@MODIFIES: None;
		@EFFECTS: /result == valid.get(i);
		@ */
		return getValid().get(i);
	}
	public void setvalid_two(int i) {
		/** @REQUIRES: (i>=0 && i<valid.size);
		@MODIFIES: valid;
		@EFFECTS: valid.get(i) == 2;
		@ */
		getValid().set(i, 2);
	}
	
	//用于测试
	public void setquenone() {
		/** @REQUIRES: None;
		@MODIFIES: queue,valid;
		@EFFECTS: queue.size == 0 && valid.size == 0;
		@ */
		getQueue().clear();
		getValid().clear();
	}

	public static ArrayList<Requ> getQueue() {
		/** @REQUIRES: None;
		@MODIFIES: None;
		@EFFECTS: /result == queue;
		@ */
		return queue;
	}

	public static void setQueue(ArrayList<Requ> queue1) {
		/** @REQUIRES: (queue.size >=0);
		@MODIFIES: this.queue;
		@EFFECTS: this.queue == queue1;
		@ */
		RequQue.queue = queue1;
	}

	public static ArrayList<Integer> getValid() {
		/** @REQUIRES: None;
		@MODIFIES: None;
		@EFFECTS: /result == valid;
		@ */
		return valid;
	}

	public static void setValid(ArrayList<Integer> valid1) {
		/** @REQUIRES: None;
		@MODIFIES: this.valid;
		@EFFECTS: this.valid == valid1;
		@ */
		RequQue.valid = valid1;
	}
	
	

}

