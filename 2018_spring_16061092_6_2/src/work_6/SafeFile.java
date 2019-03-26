package work_6;

import java.io.*;

public class SafeFile {
	public static long oldtime;
	public static int testi = 0;
	public static File[] Finded = new File[100];
	public static int alli = 0;
	public static File[] Findedall = new File[100];
	public static long alllen = 0;
	public static File[] Findedallall = new File[100];
	public static int allalli=0;
	public static long allalllen = 0;
	public static synchronized boolean addmkdir(File dir){//根据绝对路径创建目录
		//File dir = new File("F:\\oo\\dir");
		if(!dir.exists()&&dir.getParentFile().exists()) {
			dir.mkdirs();
			return true;
		}
		else {
			return false;
		}
		  
	}
	
	public static synchronized boolean addfile(File file) {
		try {
			//File file = new File("F:\\oo\\dir\\file1.txt");
			file.createNewFile();
			return true;
		}catch(IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static synchronized boolean delefile(File file) {
		//File file = new File("F:\\oo\\dir\\file1.txt");
		if(file.exists()) {
			file.delete();
			return true;
		}
		else {
			return false;
		}
		
	}
	
	public static synchronized boolean rename(File oldname,File newname) {
		//File file = new File("F:\\oo\\dir\\file1.txt");
		if(oldname.exists()&&!newname.exists()) {
			oldname.renameTo(newname);
			return true;
		}
		else {
			return false;
		}
		
	}
	
	public static synchronized boolean move(File file,File newroad) {
		if(file.exists()&&!newroad.exists()) {
			file.renameTo(newroad);
			return true;
		}
		else {
			return false;
		}
	}
	
	public static synchronized boolean changetime(File file) {
		if(file.exists()) {
			file.setLastModified(System.currentTimeMillis());
			return true;
		}
		else {
			return false;
		}
	}
	
	public static synchronized boolean changesize(File fileName,String content) {
		fileName.setLastModified(System.currentTimeMillis());
		try {
			// 打开一个随机访问文件流，按读写方式
			RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
			// 文件长度，字节数
			long fileLength = randomFile.length();
			// 将写文件指针移到文件尾。
			randomFile.seek(fileLength);
			randomFile.writeBytes(content+"\r\n");
			randomFile.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	
	public static synchronized File[] allfile(File file) {//返回目录下所有的文件，不包含目录
		File[] files = new File[10000];
		int i = 0;
		for(File ss:file.listFiles()) {
			//System.out.println(ss);
			if(ss.isFile()) {
				//System.out.println(ss);
				files[i] = ss;
				i++;
			}
		}
		return files;
	}
	
	public static synchronized int allFilenum(File file) {//返回目录下文件个数，不包括子目录
		int i = 0;
		for(File ss:file.listFiles()) {
			if(ss.isFile()) i++;
		}
		return i;
	}
	
	public synchronized static File getFa(File file) {
		return file.getParentFile();
	}
	

	
	public static synchronized void findFile(File root, String name) {//获得目录下所有同名文件
		if (root.exists() && root.isDirectory()) {
			//System.out.println("efs");
			for (File file : root.listFiles()) {
				if (file.isFile() && file.getName().equals(name)) {
					Finded[testi] = file;
					testi++;
					//System.out.println(testi);//这里输出文件名！
				} else if (file.isDirectory()) {
					findFile(file, name);
				}
			}
		}
	} 
	
	public static synchronized void findall(File root) {//找到file目录下的所有文件
		if (root.exists() && root.isDirectory()) {
			for (File file : root.listFiles()) {
				if (file.isFile() ) {
					Findedall[alli] = file;
					alli++;
					alllen = alllen+file.length();
					//System.out.println(file);//这里输出文件名！
				} else if (file.isDirectory()) {
					findall(file);
				}
			}
		}
	}
	
	public static synchronized void findallall(File root) {//找到file目录下所有文件，包括目录
		if (root.exists() && root.isDirectory()) {
			for (File file : root.listFiles()) {
				if (file.isFile() ) {
					Findedallall[allalli] = file;
					allalli++;
					//System.out.println(file);//这里输出文件名！
				} else if (file.isDirectory()) {
					Findedallall[allalli] = file;
					allalli++;
					findall(file);
				}
			}
		}
	}
	
	public static synchronized long getlastmodi(File file) {
		return file.lastModified();
	} 
	
	public static synchronized String getname(File file) {
		return file.getName();
	}
	
	public static synchronized long getlen(File file) {
		return file.length();
	}
	
	public static synchronized boolean ifexist(File file) {
		if(file.exists()) return true;
		else return false;
	}
	
	public static synchronized boolean ifequ(File file1,File file2) {
		return file1.equals(file2);
	}
	
	public static synchronized boolean isfile(File file) {
		return file.isFile();
	}
	




}
