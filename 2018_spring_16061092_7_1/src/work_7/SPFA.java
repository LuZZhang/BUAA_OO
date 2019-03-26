package work_7;

import java.util.ArrayList;


public class SPFA {
    
    public int[] result;         //用于得到第s个顶点到其它顶点之间的最短距离
    public int[] last;
    public int[] next = new int[6400];
    //内部类，用于存放图的具体边数据
    class edge {
        public int a;  //边的起点
        public int b;  //边的终点
        public int value;   //边的权值
        
        edge(int a, int b, int value) {
            this.a = a;
            this.b = b;
            this.value = value;
        } 
    }
    /*
     * 参数n:给定图的顶点个数
     * 参数s:求取第s个顶点到其它所有顶点之间的最短距离
     * 参数edge:给定图的具体边
     * 函数功能：如果给定图不含负权回路，则可以得到最终结果，如果含有负权回路，则不能得到最终结果
     */
    public boolean getShortestPaths(int s, int p,edge[] A) {//
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
        while(list.size() != 0) {
            int a = list.get(0);   //获取数组队中第一个元素
            list.remove(0);         //删除数组队中第一个元素
            for(int i = 0;i < p;i++) {
                //当list数组队的第一个元素等于边A[i]的起点时
            //	System.out.println(i);
                if(a == A[i].a && result[A[i].b] > result[A[i].a] + 1) { 
                    result[A[i].b] = result[A[i].a] + 1;
                    last[A[i].b]=a;//上一个
                    if(!used[A[i].b]) {
                        list.add(A[i].b);
                        num[A[i].b]++;
                        //if(num[A[i].b] > n)
                         //   return false;
                        used[A[i].b] = true;   //表示边A[i]的终点b已进入数组队
                    }
                }
            }
            used[a] = false;        //顶点a出数组对
        }
        return true;
    }
    
    public  int distance(int startpoint,int endpoint){//
    //	Map map = new Map();//绝对不能去掉
        SPFA test = new SPFA();
      //  int startpoint = 34;//qi点3556,3634
      // int endpoint = 255;//终点        
       // System.out.println("sta:"+startpoint+"end:"+endpoint);
        edge[] A = new edge[20000];
       // System.out.println("请输入具体边的数据：");
        int p=0;
        for(int x=0,i=0;x<80;x++) {
        	for(int y=0;y<80;y++) {
        		i = 80*x+y;
        		if(y<79&&(Map.map[x][y]==1||Map.map[x][y]==3)) {
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
     //   System.out.println(p);
    //    System.out.println(A[1]);
        if(test.getShortestPaths(startpoint,p, A)) {
            // for(int i = 0;i < test.result.length;i++)
              //   System.out.print(test.result[i]+" ");
            // System.out.println("距离为"+test.result[endpoint]);
             int j=0,i=endpoint;
             while(j!=startpoint) {
            	 j=test.last[i];//i的上一个是j
            	 this.next[j]=i;//j的下一个是i
            	 i=j;
             }//这是一个从终点到起点的路径
             /*for(int m=startpoint,mm=0;mm<k;mm++) {
            	 System.out.println(test.next[m]);
            	 m=test.next[m];
             }*/
             //System.out.println(k);
            // System.out.println(test.next[0]);
             
             return test.result[endpoint];
        } else
             System.out.println("给定图存在负环，没有最短距离");
        return 0;
    }
    
    public  int[] getnext() {
    	return this.next;
    }
}