package work_14;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class New_schTest {
	
	private static Requ requ1 = new Requ(9,0,0);//4s+1s
    private static Requ requ2 = new Requ(9,0,1);//
    private static Requ requ3 = new Requ(10,0,1);
	private static Requ requ4 = new Requ(9,1,1);//1s
	private static Requ requ5 = new Requ(9,1,2);
    private static Requ requ6 = new Requ(2,-1,3);//1s
	private static Requ requ7 = new Requ(2,-1,4);
	private static Requ requ8 = new Requ(1,0,4);
	private static Requ requ9 = new Requ(1,1,4);
	private static Requ requ10 = new Requ(3,-1,5);
	private static Requ requ11 = new Requ(4,0,5);
	private static Requ requ12 = new Requ(1,1,5);
	
	private static RequQue requque = new RequQue();
	private static New_sch schl = new New_sch();
	
	private static RequQue rq1 = new RequQue();

	@Before
	public void setUp() throws Exception {
		requque.setquenone();
		requque.addReq(requ1);
		requque.addReq(requ2);
		requque.addReq(requ3);
		requque.addReq(requ4);
		requque.addReq(requ5);
		requque.addReq(requ6);
		requque.addReq(requ7);
		requque.addReq(requ8);
		requque.addReq(requ9);
		requque.addReq(requ10);
		requque.addReq(requ11);
		requque.addReq(requ12);
		
	}
	
	@Test
	public void testrepOK() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
		/*assertTrue(schl.repOK());
		rq1 = New_sch.getRQ();
		New_sch.setRQ(null);
		assertFalse(schl.repOK());
		New_sch.setRQ(rq1);
		assertTrue(schl.repOK());*/
	}
	
	@Test
	public void testSchedule() {
		schl.Schedule();
	}

	@Test
	public void testDele_same() {
		schl.dele_same(0, 4, 9);
		assertEquals(requque.RQnum(),11);
		schl.dele_same(4, 10, 2);
		assertEquals(requque.RQnum(),10);
		schl.dele_same(6, 15, 1);
		assertEquals(requque.RQnum(),9);
		schl.dele_same(-1, 0, 1);
	} 

}
