package work_11;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Light extends Thread{
	 /** 
     * @Overview:模拟红绿灯间隔变换。
     */

	public static int[][] map_lig = new int[100][100]; //红绿灯地图
	
	public static int status;
	public static final int GREEN = 1;//东西绿，即左右绿
	public static final int RED = 2;
	 
	public boolean repOK() {
		/**@REQUIRES: None;
		@MODIFIES: None;
		@Effects: \result == invariant(this);
		*/
		if (map_lig==null||status<1||status>2)
			return false;
		return true;
	}
	
	Light() {
		/**@REQUIRES: (读入的红绿灯地图信息没有问题);
		@MODIFIES: map_lig;
		@EFFECTS: normal behavior
		*		   扫描过程中出现非法访问操作 ==> exceptional_behavior();
		*/ 
		//读取文件地图
				try {  
					 File file = new File("D:\\oo_pro\\BUAA_OO-master\\OO_10\\OO10\\trafficLight.txt");
		          //  File file_lig = new File("D:\\oo_pro\\BUAA_OO-master\\OO_10\\OO10\\trafficLight");
		            if(file.isDirectory()) {
		            	System.out.println("红绿灯图有问题");
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
		                	map_lig[i][j]=Integer.parseInt(read.substring(j,j+1));
		                	//0:无1:与右边有连接2:与下边有连接3:与右边下边均有连接
		                	if(map_lig[i][j]==0) {
		                		
		                	}else if(map_lig[i][j]==1) {
		                		//判断是否是交叉口
		                		//ArrayList<Integer> A = new ArrayList<Integer>();
		                		int aa=0;
		                		if(j>0&&(Map.map[i][j]==1||Map.map[i][j]==3)) {
		                			aa++;
		                		}
		                		if(j<79&&(Map.map[i][j]==1||Map.map[i][j]==3)) {
		                			aa++;
		                		}
		                		if(i>0&&(Map.map[i][j]==2||Map.map[i][j]==3)) {
		                			aa++;
		                		}
		                		if(i<79&&(Map.map[i][j]==2||Map.map[i][j]==3)) {
		                			aa++;
		                		}
		                		if(aa<3) {
		                			map_lig[i][j] = 0;
		                		}
		                	}
		                	else {
		                		System.out.println("红绿灯图有问题");
		                		System.exit(1);
		                	}
		                	}catch(Exception e) {
		                		System.out.println("红绿灯图有问题");
		                		System.exit(1);
		                	}
		                }
		                i++;//行数增加
		            }
		            if(i!=80) {
		            	System.out.println("红绿灯图有问题");
		            	System.exit(1);
		            }
		            bufread.close(); 
		            }
		            else {
		            	System.out.println("map_light路径不存在，请检查更正之后重新测试~");
		            	System.exit(1);
		            }
		        } catch (FileNotFoundException ex) {  
		            ex.printStackTrace();  
		        } catch (IOException ex) {  
		            ex.printStackTrace();  
		        }catch(Exception e) { 
		        	
		        }
				
	}
	public void run() {
		/** @ REQUIRES: None;
    	@ MODIFIES: status,gui;
    	@ EFFECTS: normal behavior:
    	* 每隔一定的时间改变一次红绿灯方向
    	@ THREAD_REQUIRES: \locked(this);
    	@ THREAD_EFFECTS: \locked();
    	@ */
		//初始化它们都为绿灯
		status = GREEN;
		while(true) {
			if(status == GREEN) {
				status = RED;
				//Main.gui.SetLightStatus(p, status);
			}else if(status == RED){
				status = GREEN;
			}else {
				System.out.println("红绿灯变换出错,程序退出");
				System.exit(1);
			}
			for(int i=0;i<80;i++) {
				for(int j=0;j<80;j++) {
					if(map_lig[i][j]==0) {
						
						
					//	Main.gui.SetLightStatus(new Point(i,j), 0);
					}else {
					//	Main.gui.SetLightStatus(new Point(i,j), status);
					}
				}
			}
			try {
				sleep(Main.wait_lig);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
