package work_13;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

public class RequQueTest { 
	
	private static Requ requ1 = new Requ(3,0,0);//[ER,3,0]
	private static Requ requ2 = new Requ(6,1,1);//[FR,6,UP,1]
	private static Requ requ3 = new Requ(2,-1,3);//[FR,2,DOWN,3]
	
	private static RequQue requque = new RequQue();
	
	private static ArrayList<Requ> queue1 ;
	private static ArrayList<Integer> valid1;
	
	@Before
	public void setUp() throws Exception{
		requque.setquenone();
		requque.addReq(requ1);
		requque.addReq(requ2);
	}
	
	
	@Test
	public void testrepOK() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		
		assertTrue(requque.repOK());
		queue1 = RequQue.getQueue();
		valid1 = RequQue.getValid();
		RequQue.setQueue(null);//
		assertFalse(requque.repOK());
		RequQue.setQueue(queue1);//
		assertTrue(requque.repOK());
		RequQue.setValid(null);//
		assertFalse(requque.repOK());
		RequQue.setValid(valid1);
		assertTrue(requque.repOK());		
		
	}
	
	@Test
	public void testAddReq() {
		requque.addReq(requ3);
		assertEquals(3,requque.RQnum());
	}

	@Test
	public void testSubReq() {
		requque.subReq(0);
		assertEquals(1,requque.RQnum());
	}
	
	

	

}
