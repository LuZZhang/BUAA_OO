package work_13;

import static org.junit.Assert.*;

import org.junit.Test;

public class EleTest {
	
	private static Ele ele = new Ele();
	
	
	@Test
	public void testrepOK() {
		assertEquals(true,ele.repOK());
	}

	@Test
	public void testRun() {
		assertEquals(1,ele.run(1, 3, 0),0);
	}

	@Test
	public void testOpenDo() {
		//fail("Not yet implemented");
		assertEquals(1,ele.OpenDo(0),0);
	}

}
