package work_11;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

interface deIterator<E> {
    /**
     * @Overview: 双向迭代器
     */

    /**
     * @effect: \result == if the iterator has next element;
     */
    public boolean hasNext();

    /**
     * @modifies: \this;
     * @effects: \result == the next element of the iterator;
     */
    E next();

    /**
     * @effects: \result == if the iterator has previous element;
     */
    public boolean hasPrevious();

    /**
     * @modifies: \this;
     * @effects: \result == the previous element of the iterator;
     */
    E previous();
}

public class TaxiNSch extends TaxiSch{
	/**
     * @Overview:可追踪出租车，能行走关闭的边，能以迭代方式访问历次服务记录，还能迭代访问服务轨迹。
     */
	private static final int SIZE = 80;
	
	private static final int LEFT = 1;
    private static final int RIGHT = 2;
    private static final int UP = 3;
    private static final int DOWN = 4;
    
    private ArrayList<Record> records;//每次记录
    
    class Record{
    	/**
         * @Overview:记录类，存储可追踪出租车每次服务的记录。
         */
    	private PassengerReq cr;
    	private int taxilocation;
    	private String tocr;
    	private String todes;
    	
    	Record(PassengerReq cr1,int a,String b,String c){
    		/**
             * @REQUIRES:
             *      cr1!=null && a!=null && b!=null && c!=null;
             * @MODIFIES:
             *      \this.cr;
             *      \this.taxilocation;
             *      \this.tocr;
             *      \this.todes;
             * @EFFECTS:
             *      \this.cr == cr1;
             *      \this.taxilocation == a;
             *      \this.tocr == b;
             *      \this.todes == c;
             */
    		this.cr = cr1;
    		this.taxilocation = a;
    		this.tocr = b;
    		this.todes = c;
    	}
    	
    	public boolean repOK() {
            /**
             * @EFFECTS:
             *      \result == cr!=null && taxilocation>=0 && taxilocation<6400 && tocr!=null && todes!=null;
             */
            return cr!=null && taxilocation>=0 && taxilocation<6400 &&
                    tocr!=null && todes!=null;
        }
    	
    	public deIterator<Point> getPoint() {//提供一个能迭代访问轨迹点的迭代器
            /**
             * @EFFECTS:
             *      \result==new PointManager(\this);
             */
            return new PointManager(this);
        }
    	
    	class PointManager implements deIterator{
            /**
             * @Overview:点管理类，存储一次服务中出租车行驶的轨迹点。
             */
            private ArrayList<Point> points;
            private int n;

            PointManager(Record record) {
                /**
                 * @REQUIRES:record!=null;
                 * @MODIFIES:\this.n;\this.points;
                 * @EFFECTS:
                 *      \this.n == 0;
                 *      \this.points == 出租车在从抢单位置到服务完该次请求所经过的所有的Point序列;
                 */
                this.points = new ArrayList<>();
                n = 0;
                points.add(new Point(record.taxilocation/SIZE
                        ,record.taxilocation%SIZE));//出租车抢单时所处位置
                
                //接单:从taxilocation到(seti,setj)
                SPFA spfa = new SPFA();
                int dis = spfa.distance(taxilocation, cr.seti*80+cr.setj,1);
                int[] road = spfa.getnext();//road为最短路径表示临接数组
                int m = taxilocation;//startpoint
                for(int i=0;i<dis;i++) {
                	m = road[m]; //当前结点位置
                	points.add(new Point(m/SIZE,m%SIZE));
                }
                //服务:从(seti,setj)到(aimi,aimj)
                dis = spfa.distance(cr.seti*80+cr.setj, cr.aimi*80+cr.aimj,1);
                road = spfa.getnext();//road为最短路径表示临接数组
                m = cr.seti*80+cr.setj;
                for(int i=0;i<dis;i++) {
                	m = road[m]; //当前结点位置
                	points.add(new Point(m/SIZE,m%SIZE));
                }
                records.add(record);
            }

            public boolean repOK() {
                /**
                 * @EFFECTS:
                 *      \result==points!=null;
                 */
                return points!=null;
            }

            public boolean hasNext() {
                /**
                 * @EFFECTS:
                 *      \result==n<points.size();
                 */
                return n<points.size();
            }
            public Point next() {
                /**
                 * @MODIFIES:\this.n;
                 * @EFFECTS:
                 *      如果中途不产生异常，则\result==points.get(n++);
                 *      如果中途出现异常，吞掉该异常并且\result==null;
                 */
                try {
                    return points.get(n++);
                } catch (Exception e) {
                    return null;
                }
            }
            public boolean hasPrevious() {
                /**
                 * @EFFECTS:
                 *      \result==(n-1>=0);
                 */
                return n-1>=0;
            }
            public Point previous() {
                /**
                 * @MODIFIES:\this.n;
                 * @EFFECTS:
                 *      如果中途不产生异常，则\result==points.get(--n);
                 *      如果中途出现异常，吞掉该异常并且\result==null;
                 */
                try {
                    return points.get(--n);
                } catch (Exception e) {
                    return null;
                }
            }
        }
    	
    	public void jilu(PassengerReq cr) {
    		
    	}
    	
    }
    
    public boolean repOK() {
    	/**
         * @EFFECTS:
         *      \result==super.repOK() && records!=null;
         */
        return super.repOK() && records!=null;
    }

	public TaxiNSch(int i) {
		/** @REQUIRES: (\all integer i; 0 <= i <= 29);
		@MODIFIES: ID;
		@EFFECTS: /this.ID == i;
		@ */
		super.setID(i);
		// TODO Auto-generated constructor stub
	}
	
	public void wander_new() {
		/** @REQUIRES: None;
		@MODIFIES: nowi,nowj,lnowj,lnowj;
		@EFFECTS: normal behavior
		* 出租车选择流量最小的边之后，判断是否需要等红灯。
		@ */
		Random rand = new Random();
		ArrayList<Integer> A = new ArrayList<Integer>();
		int x= this.getnowi();
		int y= this.getnowj();
		int[] liuliangs = new int[10];//记录每条有路的边的流量
		for(int i=0;i<10;i++) {
			liuliangs[i] = -1;//初始化流量为-1,-1的含义为没有这条边
		}
	//	System.out.println(x+" "+y);
		/*for(int i=0;i<80;i++) {
			for(int j=0;j<80;j++) {
				if(Map.map_ini[i][j]!=Map.map[i][j]) {
					System.out.println("1");
				}
			}
		}*/
		
		if(y>0&&(Map.map_ini[x][y-1]==1||Map.map_ini[x][y-1]==3)) {
			liuliangs[LEFT] = SPFA.liu_ri[1][80*x + y - 1];
		}
		if(y<79&&(Map.map_ini[x][y]==1||Map.map_ini[x][y]==3)) {
			liuliangs[RIGHT] = SPFA.liu_ri[1][80*x + y];
		}
		if(x>0&&(Map.map_ini[x-1][y]==2||Map.map_ini[x-1][y]==3)) {
			liuliangs[UP] = SPFA.liu_do[1][80*x + y - 80];
		}
		if(x<79&&(Map.map_ini[x][y]==2||Map.map_ini[x][y]==3)) {
			liuliangs[DOWN] = SPFA.liu_do[1][80*x + y];
		}
		int nowmin = 2147483647;
		for(int i = 1;i < 5;i ++) {
			//System.out.println("liuliang"+liuliangs[i]);
			if(liuliangs[i] < nowmin && liuliangs[i]!=-1) {
				nowmin = liuliangs[i];
			}
		}
		//System.out.println("nowmin"+nowmin);
		//得到的nowmin为最小流量，此时还不知道有几个最小流量
		for(int i = 1;i < 5;i ++) {
			if(liuliangs[i] == nowmin) {
				A.add(i);
			}
		}
		//System.out.println("A.size"+A.size());
        int index = rand.nextInt(A.size());
        int num = A.get(index);//num为出租车当前选择的方向
        //然后再判断要不要等红灯
        if(ifwait(num)) {
        	//不需要等红灯
        }else {
        	//需要等红灯
        	int nowlig_status = Light.status;
        	int a=1;
        	while(Light.status == nowlig_status) {
        		System.out.print("");
        	} 	//System.out.println("???");
        }
        try {
        	int anowi=0 ,anowj=0;
	        if(num == LEFT) {
	        	anowi = x;
	        	anowj = y-1;
	        }
	        if(num == RIGHT) {
	        	anowi = x;
	        	anowj = y+1;
	        }
	        if(num == UP) {
	        	anowi = x-1;
	        	anowj = y;
	        }
	        if(num == DOWN) {
	        	anowi = x+1;
	        	anowj = y;
	        }
	        setlnowi(getnowi());//更新lnow
			setlnowj(getnowj());
	        setnowi(anowi);//更新now
	        setnowj(anowj);
        
        }catch(Exception e) {
        	System.out.println("出租车出行故障");
        }
	}

	

}
