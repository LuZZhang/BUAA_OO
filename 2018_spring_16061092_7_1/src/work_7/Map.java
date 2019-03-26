package work_7;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Map {
	
	public static int[][] map =new int[80][80];
	 
	
	public Map() {
		//读取文件地图
		try {  
            File file = new File("D:\\\\oo_pro\\\\互测\\\\others_7\\\\b243501a\\\\map .txt");  
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
                	//0:无1:与右边有连接2:与下边有连接3:与右边下边均有连接
                	if(map[i][j]==0) {
                		
                	}
                	else if(map[i][j]==1) {
                		
                	}
                	else if(map[i][j]==2) {
                		
                	}
                	else if(map[i][j]==3) {
                		
                	}
                	else {
                		System.out.println("图有问题");
                	}
                	}catch(Exception e) {
                		System.out.println("图有问题");
                		break;
                	}
                }
                i++;//行数增加
            }
            if(i!=80) {
            	System.out.println("图有问题");
            }
            bufread.close();  
        } catch (FileNotFoundException ex) {  
            ex.printStackTrace();  
        } catch (IOException ex) {  
            ex.printStackTrace();  
        }//构造图 
		
		
		
	}
	
	
	

}
