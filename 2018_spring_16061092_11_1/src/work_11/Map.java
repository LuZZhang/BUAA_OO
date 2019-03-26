package work_11;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Map {
	 /** 
     * @Overview:创建地图信息，包括图上的边的连通性,流量。
     */

	
	public static int[][] map =new int[100][100]; 
	public static int[][] map_ini = new int[100][100];
	
	public boolean repOK() {
		/**@REQUIRES: None;
		@MODIFIES: None;
		@Effects: \result == invariant(this); 
		*/
		if (map==null)
			return false;
		return true;
	}
	public Map() {
		/**@REQUIRES: (读入的地图信息没有问题);
		@MODIFIES: map;
		@EFFECTS: normal behavior
		*		   扫描过程中出现非法访问操作 ==> exceptional_behavior();
		*/ 
		//读取文件地图
		try {  
            File file = new File("C:\\Users\\LuZhang\\Desktop\\map.txt"); // "D:\\oo_pro\\互测\\others_7\\b243501a\\map .txt"
          // 
            if(file.isDirectory()) {
            	System.out.println("图有问题");
            	System.exit(1);
            }
            if(file.exists()) {
            	// 读取文件，并且以utf-8的形式写出去  
            BufferedReader bufread;  
            String read;  
            bufread = new BufferedReader(new FileReader(file)); 
            int i=0;//行数
            while ((read = bufread.readLine()) != null) {//每一行  
               // System.out.println(read);  //第i行
                for(int j=0;j<80;j++) {
                	try {
                	map[i][j]=Integer.parseInt(read.substring(j,j+1));
                	map_ini[i][j]=map[i][j];
                	//0:无1:与右边有连接2:与下边有连接3:与右边下边均有连接
                	if(map[i][j]==0||map[i][j]==1||map[i][j]==2||map[i][j]==3) {
                		
                	}
                	else {
                		System.out.println("图有问题");
                		System.exit(1);
                	}
                	}catch(Exception e) {
                		System.out.println("图有问题");
                		System.exit(1);
                	}
                }
                i++;//行数增加
            }
            if(i!=80) {
            	System.out.println("图有问题");
            	System.exit(1);
            }
            bufread.close(); 
            }
            else {
            	System.out.println("map路径不存在，请检查更正之后重新测试~");
            	System.exit(1);
            }
            //map_ini = map;
        } catch (FileNotFoundException ex) {  
            ex.printStackTrace();  
        } catch (IOException ex) {  
            ex.printStackTrace();  
        }catch(Exception e) { 
        	
        }
		//构造图 
		
		
	}
	
	public static synchronized boolean modify(int i,int j,int res) {//改变地图文件的边
		/** @REQUIRES: (\all integer i;0 <= i <= 79);
		 *               (\all integer j;0 <= j <= 79);
		 *              (\all integer res;0 <= res <= 3);
		@MODIFIES: map;
		@EFFECTS: 修改地图map的边;
		@THREAD_REQUIRES: \locked(this);
		@THREAD_EFFECTS: \locked();
		@ */
		try {
		map[i][j]=res;
		}catch(Exception e) {
			return false;
		}
		return true;
	}
	
	public static synchronized boolean modiliuliang(int i,int j,int ii,int jj,int liuliang) {
		/** @REQUIRES: (\all integer i;0 <= i <= 79);
		 *              (\all integer j;0 <= j <= 79);
		 *              (\all integer ii;0 <= ii <= 79);
		 *              (\all integer jj;0 <= jj <= 79);
		 *              (\all integer liuliang;0 <= liuliang);
		@MODIFIES: SPFA.A;
		@EFFECTS: 利用for循环寻找是否有(i,j)到(ii,jj)的边，如果有该边，则设该边流量为liuliang;
		@THREAD_REQUIRES: \locked(this);
		@THREAD_EFFECTS: \locked();
		@ */
        try {
		for(int  i1 = 0; i1 < SPFA.p ; i1++) {
			//
			if(SPFA.A[i1].a == 80*i + j&&SPFA.A[i1].b == 80*ii + jj) {
				//有这条边
				SPFA.A[i1].liuliang = liuliang;
			}
			
		}
        }catch(Exception e) {
        	return false;
        }
		return true;
	}
	

}
