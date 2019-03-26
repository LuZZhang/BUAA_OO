package work_13;

import static org.junit.Assert.*;

import org.junit.Test;

public class RequTest {
	
	//aimstage,reqnum,T
	private static Requ requ1 = new Requ(3,0,0);//[ER,3,0]
	private static Requ requ2 = new Requ(6,1,1);//[FR,6,UP,1]
	private static Requ requ3 = new Requ(2,-1,3);//[FR,2,DOWN,3]

	

	@Test
	public void testToString() {
		
		assertEquals("[ER,3,0]",requ1.toString());
		assertEquals("[FR,6,UP,1]",requ2.toString());
		assertEquals("[FR,2,DOWN,3]",requ3.toString());
	}
	//before,after可以不写？
    //构造方法，get,set方法可以不写test
	//上面方法写错？
	//接口要不要写test

}
