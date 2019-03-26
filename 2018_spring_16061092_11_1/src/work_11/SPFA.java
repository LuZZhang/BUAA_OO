package work_11;

import java.util.ArrayList;

public class SPFA extends Thread{
	/** 
     * @Overview:更新map，基于新map寻找最短路径，每500ms更新流量。
     */
    
    public int[] result;         //用于得到第s个顶点到其它顶点之间的最短距离
    public int[] last;
    public int[] next = new int[6400];
    static edge[] A = new edge[20000];//用于保存图上的边  
    public static int p;//图上边的个数的二倍
    static edge[] NA = new edge[20000];//用于保存图上的边_New  
    public static int Np;//图上边的个数的二倍_New
    private int biaoji[][] = new int[120][10000];//用于标记是否需要增加流量
    public static int[][] liu_ri = new int[3][10000];//用于记录该点与其右边点的流量
    public static int[][] liu_do = new int[3][10000];//用于记录该点与其下边点的流量
    
    public boolean repOK() {
		/**@REQUIRES: None; 
		@MODIFIES: None;
		@Effects: \result == invariant(this);
		*/
		if (result==null||last==null||next==null||A==null||p<0||biaoji==null||liu_ri==null||liu_do==null)
			return false;
		return true;
	}
    
    //内部类，用于存放图的具体边数据
    class edge {
        public int a;  //边的起点
        public int b;  //边的终点
        public int value;   //边的权值
        public int liuliang;//边的流量
        edge(int a, int b, int value) {
        	/** @REQUIRES: (\all integer a; 0 <= a <= 6399);
        	 *              (\all integer b; 0 <= b <= 6399);
        	 *              (\all integer value; 0 <= value);
        	@MODIFIES: a,b,value;
        	@EFFECTS: this.a == a;
             *          this.b == b;
             *          this.value == value;
        	@ */
            this.a = a;
            this.b = b;
            this.value = value;
            this.liuliang = 0;
        } 
    }
    
    public boolean ifexist(int a,int b) {
    	SPFA test = new SPFA();
    	p=0;
    	for(int x=0,i=0;x<80;x++) {
	        	for(int y=0;y<80;y++) {
	        		i = 80*x+y;
	        		if(y<79&&(Map.map[x][y]==1||Map.map[x][y]==3)) {
	        			//System.out.println(A[p]);
	            		A[p]=test.new edge(i,i+1,1);
	            		p++;
	            		A[p]=test.new edge(i+1,i,1);
	            		p++;
	            	}
	        		if(x<79&&(Map.map[x][y]==2||Map.map[x][y]==3)) {
	        			A[p]=test.new edge(i,i+80,1);
	        			p++;
	        			A[p]=test.new edge(i+80,i,1);
	        			p++;
	        		}
	        	}
	    }
    	for(int i=0;i<p;i++) {
    		if(A[i].a==a&&A[i].b==b) {
    			return true;
    		}
    	}
    	return false;
    }
    
    public void run() {
    	/** @REQUIRES: None;
    	@MODIFIES: biaoji, A,NA,p,Np;
    	@EFFECTS: 每500ms重新计算一次Map.map上边的流量;
    	@THREAD_REQUIRES: \locked(this);
    	@THREAD_EFFECTS: \locked();
    	@ */
    	//每200ms计算一次道路流量
    	long nowtime = System.currentTimeMillis();
    	while(true) {
    		//将biaoji数组清0
    		for(int i=0;i<10000;i++) {
    			for(int j = 0;j<120;j++) {
    				biaoji[j][i]= 0;
    			}
    			
    		}
    		//将流量数组清零
    		for(int i=0;i<p;i++) {
    			A[i].liuliang=0;
    		}
    		//将流量数组清零
    		for(int i=0;i<Np;i++) {
    			NA[i].liuliang=0;
    		}
    		for(int i=0;i<6400;i++) {
    			for(int j=0;j<3;j++) {
    				liu_ri[j][i] = 0;//
    				liu_do[j][i] = 0;
    			}
    			
    		}
    		
    		while(System.currentTimeMillis()<=nowtime+500) {
    			SPFA test = new SPFA();
      	        p=0;
      	        Np=0;
      	        //对于可追踪出租车
      	      for(int x=0,i=0;x<80;x++) {
    	        	for(int y=0;y<80;y++) {
    	        		i = 80*x+y;
    	        		if(y<79&&(Map.map_ini[x][y]==1||Map.map_ini[x][y]==3)) {
    	        			//System.out.println(A[p]);
    	            		NA[Np]=test.new edge(i,i+1,1);
    	            		Np++;
    	            		NA[Np]=test.new edge(i+1,i,1);
    	            		Np++;
    	            	}
    	        		if(x<79&&(Map.map_ini[x][y]==2||Map.map_ini[x][y]==3)) {
    	        			NA[Np]=test.new edge(i,i+80,1);
    	        			Np++;
    	        			NA[Np]=test.new edge(i+80,i,1);
    	        			Np++;
    	        		}
    	        	}
    	        }
      	      
      	    for(int i = 0;i < Np;i += 2) {
				//边为A[i]
				//对于每个出租车
				for(int j = 0;j < 30;j++) {
					TaxiNSch newtaxi = (TaxiNSch) Main.taxis[j];
					if(newtaxi.getnowi()*80+newtaxi.getnowj()==NA[i].a) {//要是map上有的边
						biaoji[j][i/2]=1;//将标记设为1,符合从该边经过的前提条件，但还不一定抵达b点
					}
					if(newtaxi.getnowi()*80+newtaxi.getnowj()==NA[i].b&&biaoji[j][i/2]==1&&ifexist(NA[i].a,NA[i].b)) {
						//从a边经过到达b点
						NA[i].liuliang++;//该边的流量++
						NA[i+1].liuliang++;
						if(Math.abs(NA[i].a-NA[i].b)==1) {
							liu_ri[1][Math.min(NA[i].a, NA[i].b)] ++;
							//System.out.println("cg"+Math.min(A[i].a, A[i].b));
						}else if(Math.abs(NA[i].a-NA[i].b)==80) {
							liu_do[1][Math.min(NA[i].a, NA[i].b)] ++;
						}
					}
					if(newtaxi.getnowi()*80+newtaxi.getnowj()==NA[i].b) {
						biaoji[j][i/2]=2;//将标记设为2,符合从该边经过的前提条件，但还不一定抵达b点
					}
					if(newtaxi.getnowi()*80+newtaxi.getnowj()==NA[i].a&&biaoji[j][i/2]==2&&ifexist(NA[i].a,NA[i].b)) {
						//从b边经过到达a点
						NA[i].liuliang++;//该边的流量++
						NA[i+1].liuliang++;
						if(Math.abs(NA[i].a-NA[i].b)==1) {
							liu_ri[1][Math.min(NA[i].a, NA[i].b)] ++;
						}else if(Math.abs(NA[i].a-NA[i].b)==80) {
							liu_do[1][Math.min(NA[i].a, NA[i].b)] ++;
						}
					}
				}
			}
      	      //对于普通出租车:
      	    p=0;
      	        for(int x=0,i=0;x<80;x++) {
      	        	for(int y=0;y<80;y++) {
      	        		i = 80*x+y;
      	        		if(y<79&&(Map.map[x][y]==1||Map.map[x][y]==3)) {
      	        			//System.out.println(A[p]);
      	            		A[p]=test.new edge(i,i+1,1);
      	            		p++;
      	            		A[p]=test.new edge(i+1,i,1);
      	            		p++;
      	            	}
      	        		if(x<79&&(Map.map[x][y]==2||Map.map[x][y]==3)) {
      	        			A[p]=test.new edge(i,i+80,1);
      	        			p++;
      	        			A[p]=test.new edge(i+80,i,1);
      	        			p++;
      	        		}
      	        	}
      	        }
    			//所有的偶数可以代表所有的边
    			for(int i = 0;i < p;i += 2) {
    				//边为A[i]
    				//对于每个出租车
    				for(int j = 30;j < 100;j++) {
    					TaxiSch newtaxi = (TaxiSch) Main.taxis[j];
    					if(newtaxi.getnowi()*80+newtaxi.getnowj()==A[i].a) {
    						biaoji[j][i/2]=1;//将标记设为1,符合从该边经过的前提条件，但还不一定抵达b点
    					}
    					if(newtaxi.getnowi()*80+newtaxi.getnowj()==A[i].b&&biaoji[j][i/2]==1) {
    						//从a边经过到达b点
    						A[i].liuliang++;//该边的流量++
    						A[i+1].liuliang++;
    						if(Math.abs(A[i].a-A[i].b)==1) {
    							liu_ri[0][Math.min(A[i].a, A[i].b)] ++;
    							//System.out.println("cg"+Math.min(A[i].a, A[i].b));
    						}else if(Math.abs(A[i].a-A[i].b)==80) {
    							liu_do[0][Math.min(A[i].a, A[i].b)] ++;
    						}
    					}
    					if(newtaxi.getnowi()*80+newtaxi.getnowj()==A[i].b) {
    						biaoji[j][i/2]=2;//将标记设为2,符合从该边经过的前提条件，但还不一定抵达b点
    					}
    					if(newtaxi.getnowi()*80+newtaxi.getnowj()==A[i].a&&biaoji[j][i/2]==2) {
    						//从b边经过到达a点
    						A[i].liuliang++;//该边的流量++
    						A[i+1].liuliang++;
    						if(Math.abs(A[i].a-A[i].b)==1) {
    							liu_ri[0][Math.min(A[i].a, A[i].b)] ++;
    						}else if(Math.abs(A[i].a-A[i].b)==80) {
    							liu_do[0][Math.min(A[i].a, A[i].b)] ++;
    						}
    					}
    				}
    			}
    			//计算流量
    		}
    		nowtime = nowtime +500;
    	}
    }
    /*
     * 参数n:给定图的顶点个数
     * 参数s:求取第s个顶点到其它所有顶点之间的最短距离
     * 参数edge:给定图的具体边
     * 函数功能：如果给定图不含负权回路，则可以得到最终结果，如果含有负权回路，则不能得到最终结果
     */
    public boolean getShortestPaths(int s, int p,edge[] A) {//
    	/** @REQUIRES: (\all integer s; 0 <= s <= 6399);
    	 *              (\all integer p; 0 <= p <= 19999);
    	 *              (\all integer i; 0 <= i <= p);
    	@MODIFIES: result, last;
    	@EFFECTS: 将以s为顶点的最短路径且流量最小的路径存储在last数组中。
    	@*/
        ArrayList<Integer> list = new ArrayList<Integer>();
        result = new int[6400];
        boolean[] used = new boolean[6400];
        int[] num = new int[6400];
        last = new int[6400];//用于得到最短路径
        for(int i = 0;i < 6400;i++) {
            result[i] = Integer.MAX_VALUE;//s到i的最短距离
            used[i] = false;
            num[i]=0;
            last[i]=i;//初始化时，令下一个结点为他自己
        }
        result[s] = 0;     //第s个顶点到自身距离为0
        used[s] = true;    //表示第s个顶点进入数组队
        num[s] = 1;       //表示第s个顶点已被遍历一次
        list.add(s);      //第s个顶点入队
        int flow[] = new int[20000];
        for(int i=0;i<20000;i++) {
        	flow[i] = Integer.MAX_VALUE;//将当前流量最小值设为int型最大值
        }
        while(list.size() != 0) {
            int a = list.get(0);   //获取数组队中第一个元素
            list.remove(0);         //删除数组队中第一个元素
            for(int i = 0;i < p;i++) {
                //当list数组队的第一个元素等于边A[i]的起点时
            //	System.out.println(i);
                if(a == A[i].a && (result[A[i].b] > result[A[i].a] + 1 || (result[A[i].b] == result[A[i].a] + 1 && A[i].liuliang< flow[A[i].b]))) { 
                    flow[A[i].b]=A[i].liuliang;//将到当前边最小值更新
                	result[A[i].b] = result[A[i].a] + 1;
                    last[A[i].b]=a;//上一个
                    if(!used[A[i].b]) {
                        list.add(A[i].b);
                        num[A[i].b]++;
                        used[A[i].b] = true;   //表示边A[i]的终点b已进入数组队
                    }
                }
            }
            used[a] = false;        //顶点a出数组对
        }
        return true;
    }
    
    public  int distance(int startpoint,int endpoint,int taxiid){//
    	/** @REQUIRES: (\all integer startpoint; 0 <= startpoint <= 6399);
    	 *              (\all integer endpoint; 0 <= endpoint <= 19999);
    	@MODIFIES: A,p,next;
    	@EFFECTS: 得到从startpoint到endpoint的最短路径
    	           IF 调用test.getShortestPaths(startpoint,p, A)方法成功  THEN \result == test.result[endpoint];
    	           ELSE \result == 0;
    	@ */
    //	Map map = new Map();//绝对不能去掉
        SPFA test = new SPFA();
        if(taxiid<100) {
        	if(test.getShortestPaths(startpoint,Np, NA)) {
                // for(int i = 0;i < test.result.length;i++)
                  //   System.out.print(test.result[i]+" ");
                // System.out.println("距离为"+test.result[endpoint]);
                 int j=0,i=endpoint;
                 while(j!=startpoint) {
                	 j=test.last[i];//i的上一个是j
                	 this.next[j]=i;//j的下一个是i
                	 i=j;
                 }
                 
                 return test.result[endpoint];
            } else
                 System.out.println("给定图存在负环，没有最短距离");
            return 0;
        }
       /* if(taxiid<130){
        	p=0;
  	        for(int x=0,i=0;x<80;x++) {
  	        	for(int y=0;y<80;y++) {
  	        		i = 80*x+y;
  	        		if(y<79&&(Map.map[x][y]==1||Map.map[x][y]==3)) {
  	        			//System.out.println(A[p]);
  	            		A[p]=test.new edge(i,i+1,1);
  	            		p++;
  	            		A[p]=test.new edge(i+1,i,1);
  	            		p++;
  	            	}
  	        		if(x<79&&(Map.map[x][y]==2||Map.map[x][y]==3)) {
  	        			A[p]=test.new edge(i,i+80,1);
  	        			p++;
  	        			A[p]=test.new edge(i+80,i,1);
  	        			p++;
  	        		}
  	        	}
  	        }
        	if(test.getShortestPaths(startpoint,p, A)) {
                // for(int i = 0;i < test.result.length;i++)
                  //   System.out.print(test.result[i]+" ");
                // System.out.println("距离为"+test.result[endpoint]);
                 int j=0,i=endpoint;
                 while(j!=startpoint) {
                	 j=test.last[i];//i的上一个是j
                	 this.next[j]=i;//j的下一个是i
                	 i=j;
                 }
                 
                 return test.result[endpoint];
            } else
                 System.out.println("给定图存在负环，没有最短距离");
            return 0;
        }*/
       return 0;
    }
    
    public  int[] getnext() {
    	/** @REQUIRES: None;
    	@MODIFIES: None;
    	@EFFECTS: \result == this.next;
    	@ */
    	return this.next;
    }
}
