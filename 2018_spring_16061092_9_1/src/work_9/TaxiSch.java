package work_9;

import java.awt.Point;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.ArrayList;

public class TaxiSch extends Thread implements Information{
	public int ID;//车牌号从0到99
	private int lingyi = 0;//是否是灵异初始化为接单状态或者是服务状态
    private int status=0;//停止：0，等待服务：1，接单：2，服务：3
    private int nowi=40;//初始化出租车当前位置 
    private int nowj=40;
    private int creditnumber;//信用度
    private int aimi;
    private int aimj;
   // private String CR;
    private static final int LEFT = 1;
    private static final int RIGHT = 2;
    private static final int UP = 3;
    private static final int DOWN = 4;
    private static final int STOP =0;
    private static final int WAIT =1;
    private static final int ORDER =2;
    private static final int SERVE =3;
	public TaxiSch(int i) {
		/** @ REQUIRES: (\all integer i; 0 <= i <= 99);
		@ MODIFIES: ID;
		@ EFFECTS: this.ID = i;
		@ */
		setID(i);
	} 
	
	public synchronized void setID(int i) {
		/** @ REQUIRES: (\all integer i; 0 <= i <= 99);
		@ MODIFIES: this.ID;
		@ EFFECTS: this.ID = i;
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked;
		@ */
		ID = i;
	}
	
	public synchronized int getcre() {
		/** @ REQUIRES: None;
		@ MODIFIES: None;
		@ EFFECTS: \result = this.creditnumber;
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		@ */
		return creditnumber;
	}
	public synchronized int getnowi() {
		/** @ REQUIRES: None;
		@ MODIFIES: None;
		@ EFFECTS: \result = this.nowi;
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		@ */
		return nowi;
	}
	public synchronized int getnowj() {
		/** @ REQUIRES: None;
		@ MODIFIES: None;
		@ EFFECTS: \result = this.nowj;
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		@ */
		return nowj;
	}
	public synchronized int getnowstatus() {
		/** @ REQUIRES: None;
		@ MODIFIES: None;
		@ EFFECTS: \result = this.status;
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		@ */
		return status;
	}
	
	public synchronized String getnowstatustostring() {
		/** @ REQUIRES: None;
		@ MODIFIES: None;
		@ EFFECTS: 根据出租车当前所处状态返回不同的值;
		 *          IF this.getnowstatus()==SERVE THEN \result =  "SERVE";
		 *          ELSE IF this.getnowstatus()==WAIT THEN \result  = "WAIT";
		 *          ELSE IF this.getnowstatus()==ORDER THEN \result = "ORDER";
		 *          ELSE THEN \result = "STOP";
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		@ */
		if(this.getnowstatus()==SERVE) {
			return "SERVE";
		}
		else if(this.getnowstatus()==WAIT) {
			return "WAIT";
		}
		else if(this.getnowstatus()==ORDER) {
			return "ORDER";
		}
		else {
			return "STOP";
		}
	}
	
	public synchronized String writeforonetaxi() {
		/** @ REQUIRES: None;
		@ MODIFIES: None;
		@ EFFECTS: \result = "查询时刻:"+System.currentTimeMillis()+"出租车当前坐标:("+this.getnowi()+","+this.getnowj()+")当前所处状态:"+this.getnowstatustostring();
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		@ */
		return "查询时刻:"+System.currentTimeMillis()+"出租车当前坐标:("+this.getnowi()+","+this.getnowj()+")当前所处状态:"+this.getnowstatustostring();
	}
	
	public synchronized ArrayList<Integer> writeforalltaxis(int status) {
		/** @ REQUIRES: (\all integer status; 0 <= status <= 3);
		@ MODIFIES: sta_taxi;
		@ EFFECTS:    对于编号0-99的车，若有车正处于status状态，则将该车的编号存入sta_taxi动态数组;
		 *             \result = sta_taxi;
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		@ */
		ArrayList<Integer> sta_taxi = new ArrayList<Integer>();
		for(int i=0;i<100;i++) {
			TaxiSch taxiii = (TaxiSch) Main.taxis[i];
			if(taxiii.getnowstatus()==status) {
				sta_taxi.add(i);
			}
		}
		return sta_taxi;//满足条件的车辆编号动态数组
	}
	
	public synchronized static void writetotxt(String content) {
		/** @ REQUIRES: (\all String content);
		@ MODIFIES: writer;
		@ EFFECTS: normal behavior
		将content写入文件名为passmess.txt的文件中;
		写入过程中出现非法操作 ==> exceptional_behavior();
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		@ */
        try {     
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件     
            FileWriter writer = new FileWriter("passmess.txt", true);     
            writer.write(content);     
            writer.close();     
        } catch (IOException e) {     
            e.printStackTrace();     
        }     
    }
	
	public int getguistatus() {
		/**@ REQUIRES: None;
		@ MODIFIES: None;
		@ EFFECTS: IF this.getnowstatus()==0 THEN \result = 0;
		 *          ELSE IF this.getnowstatus()==1 THEN \result = 2;
		 *          ELSE \result = 1;
		@ */
		int guistatus;
		if(this.getnowstatus()==0) {
			guistatus=0;
		}
		else if(this.getnowstatus()==1) {
			guistatus=2;
		}
		else {
			guistatus=1;
		}
		return guistatus;
	}
	
	

	public void run() {
		/**@ Requires: None;
		@ Modifies: System.out, nowi, nowj, aimi, aimj, status, writer, creditnumber, lingyi;
		@ Effects: normal behavior
		*		   进行车的运动,在控制台和passmess.txt文件分别输出相应信息。
	    *		 sleep出现异常 ==> exceptional_behavior (e);
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		*/
		//随机处理出租车初始位置
		Random random = new Random();
		setnowi(random.nextInt(80));
		setnowj(random.nextInt(80));
		//setnowi(40);
	    //setnowj(40);
		//Point point = new Point(getnowi(),getnowj());
		Main.gui.SetTaxiStatus(this.ID,new Point(getnowi(),getnowj()) , getguistatus());
		//Map map = new Map();
		this.switchto(1,0,0);//一开始切入到等待服务状态
		while(true) {
			long nowtime = System.currentTimeMillis();
			if(status==WAIT) {
				while(status==WAIT&&System.currentTimeMillis()<nowtime+20000) {
					//可以抢单，随意走
					try {
						sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Main.gui.SetTaxiStatus(this.ID,new Point(getnowi(),getnowj()) , getguistatus());
					//写一个使出租车在等待服务状态随机走的算法
					Random rand = new Random();
					ArrayList<Integer> A = new ArrayList<Integer>();
					int x= this.getnowi();
					int y= this.getnowj();
					if(y>0&&(Map.map[x][y-1]==1||Map.map[x][y-1]==3)) {
						A.add(LEFT);
					}
					if(y<79&&(Map.map[x][y]==1||Map.map[x][y]==3)) {
						A.add(RIGHT);
					}
					if(x>0&&(Map.map[x-1][y]==2||Map.map[x-1][y]==3)) {
						A.add(UP);
					}
					if(x<79&&(Map.map[x][y]==2||Map.map[x][y]==3)) {
						A.add(DOWN);
					}
                    int index = rand.nextInt(A.size());
                    int num = A.get(index);
                    try {
                    if(num == LEFT) {
                    	this.setnowi(x);
                    	this.setnowj(y-1);
                    }
                    if(num == RIGHT) {
                    	this.setnowi(x);
                    	this.setnowj(y+1);
                    }
                    if(num == UP) {
                    	this.setnowi(x-1);
                    	this.setnowj(y);
                    }
                    if(num == DOWN) {
                    	this.setnowi(x+1);
                    	this.setnowj(y);
                    }
                    }catch(Exception e) {
                    	System.out.println("出租车出行故障");
                    }
					
				}
				if(status==1) this.switchto(0,0,0);
			}
			
			if(status==ORDER) {
				//System.out.println(nowi+" "+nowj+" "+aimi+" "+aimj);
				//接单状态，以最短路径去接单
				TaxiSch.writetotxt("--------------被派单车辆的运行信息-------------车辆编号:"+this.ID+"派单时的车辆位置坐标:("+this.getnowi()+","+this.getnowj()+")"+"派单时刻:"+System.currentTimeMillis()+"乘客位置坐标:"+"("+this.aimi+","+this.aimj+")");
				SPFA spfa = new SPFA();
				int dis = spfa.distance(nowi*80+nowj, aimi*80+aimj);
				//此时dis为最短路径长度
				int[] road = spfa.getnext();//road为最短路径表示临接数组
				int m=nowi*80+nowj;//startpoint
				for(int i=0;i<dis;i++) {
					int disi = spfa.distance(nowi*80+nowj, aimi*80+aimj);///每次判断
					road = spfa.getnext();
					m = road[m]; //当前结点位置
					try {
					sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					this.setnowi(m/80);
					this.setnowj(m%80);
					Main.gui.SetTaxiStatus(this.ID,new Point(getnowi(),getnowj()) , getguistatus());
					TaxiSch.writetotxt("<---"+this.ID+"车接单状态经停位置坐标"+"("+this.getnowi()+","+this.getnowj()+")到达时刻"+System.currentTimeMillis()+"--->");
					//System.out.println("经停信息"+this.getnowi()+" "+this.getnowj());
				}
				TaxiSch.writetotxt("<---"+this.ID+"号车到达乘客位置的时刻:"+System.currentTimeMillis()+"--->");
				//System.out.println("m:"+m);
				System.out.println("到达乘客上车地点");
				//理论上nowi,nowj应该一直更新
				nowi = aimi;
				nowj = aimj;
				if(lingyi==1) {
					//System.out.println("!!!");
					this.switchto(1, 0, 0);
					this.setlingyi(0);
				}
				else {
					this.switchto(3, -1, -1);//出租车到达乘客叫车地,让乘客知道,让乘客传入正确的switchto
				}
			}
			
			if(status==SERVE&&aimi!=-1) {
				//滴滴打鬼
				/*if(nowi==aimi&&nowj==aimj) {
					System.out.println("一次灵异改状态完成");
					this.switchto(1, 0, 0);
				}*/
				//System.out.println(Thread.currentThread()+" "+this.ID+"进入");
				TaxiSch.writetotxt("<---"+this.ID+"号车响应乘客请求,到达目的地坐标:("+this.aimi+","+this.aimj+")--->");
				SPFA spfa1 = new SPFA();
				try {
					//this.switchto(0, 0, 0);
					synchronized(this) {
					this.status=0;
					}
					sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}//到达乘客所在地睡眠一秒
				Main.gui.SetTaxiStatus(this.ID,new Point(getnowi(),getnowj()) , getguistatus());
				int dis = spfa1.distance(nowi*80+nowj, aimi*80+aimj);
				int[] road = spfa1.getnext();
				int m=nowi*80+nowj;
				for(int i=0;i<dis;i++) {
					//判断该边是否还存在
					int disi = spfa1.distance(nowi*80+nowj, aimi*80+aimj);///每次判断
					road = spfa1.getnext();
					m = road[m];
					try {
					sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					this.setnowi(m/80);
					this.setnowj(m%80);
					Main.gui.SetTaxiStatus(this.ID,new Point(getnowi(),getnowj()) , getguistatus());
					//System.out.println("经停信息"+this.getnowi()+" "+this.getnowj());
					TaxiSch.writetotxt("<---"+this.ID+"车服务状态经停位置坐标"+"("+this.getnowi()+","+this.getnowj()+")到达时刻"+System.currentTimeMillis()+"--->");
				}
				TaxiSch.writetotxt("<---"+this.ID+"号车到达乘客目的地的时刻"+System.currentTimeMillis()+"--->");
				//到达目的地
				this.addcredit(3);//到达目的地后信用度+3
				System.out.println(this.ID+"到达目的地");
				nowi=aimi;
				nowj=aimj;
				this.switchto(0, 0, 0);//进入停止状态
			}
			
			if(status==STOP) {
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				this.switchto(1,0,0);
			}
			
		}
		
	}
	
	public synchronized void setnowi(int i) {
		/** @ REQUIRES: (\all integer i; 0 <= i <= 79);
		@ MODIFIES: nowi;
		@ EFFECTS: nowi = i;
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		@ */
		nowi = i;
	}
	
	public synchronized void setnowj(int j) {
		/** @ REQUIRES: (\all integer j; 0 <= j <= 79);
		@ MODIFIES: nowj;
		@ EFFECTS: nowj = j;
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		@ */
		nowj = j;
	}

	public synchronized void switchto(int statusnew,int x,int y) {
		/** @ REQUIRES: (\all integer aimi; 0 <= aimi <= 79);
		 *               (\all integer aimj; 0 <= aimj <= 79);
		 *               (\all integer statusnew; 0 <= statusnew <= 3);
		@ MODIFIES: aimi, aimj, statusnew;
		@ EFFECTS: status=statusnew;
		 *          aimi=x;
		 *          aimj=y;
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		@ */
		status=statusnew;
		aimi=x;
		aimj=y;
	}

	
	public synchronized void addcredit(int i) {
		/** @ REQUIRES: (\all integer i; 0 <= i);
		@ MODIFIES: creditnumber;
		@ EFFECTS: creditnumber+=i;
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		@ */
		creditnumber+=i;
	}
	
	public synchronized void setcredit(int i) {
		/** @ REQUIRES: (\all integer i; 0 <= i);
		@ MODIFIES: creditnumber;
		@ EFFECTS: creditnumber = i;
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		@ */
		creditnumber = i;
	}
	public synchronized void setlingyi(int i) {
		/** @ REQUIRES: (\all integer i; 0 <= i <= 1);
		@ MODIFIES: lingyi;
		@ EFFECTS: lingyi = i;
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		@ */
		lingyi = i;
	}

}
