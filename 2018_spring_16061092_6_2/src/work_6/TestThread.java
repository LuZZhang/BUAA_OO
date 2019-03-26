package work_6;

import java.io.*;

public class TestThread extends Thread{
public boolean addFile(String file) {
		File file1 = new File(file);
		if(SafeFile.addfile(file1)) {
			return true;
		}
		else {
			return false;
		} 
	}
	
	public boolean rename(String from,String to) {
		File old = new File(from);
		File newfile = new File(to);
		if(SafeFile.rename(old, newfile)) {
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean delete(String src) {
		File file = new File(src);
		if(SafeFile.delefile(file)) {
			return true;
		}
		else{
			return false;
		}
	} 
	
	public boolean move(String from,String to) {
		File oldroad = new File(from);
		File newroad = new File(to);
		if(SafeFile.move(oldroad, newroad)) {
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean changeSize(String file) {
		File file1 = new File(file);
		if(SafeFile.changesize(file1, "asdwa")) {
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean changeTime(String file) {
		File file1 = new File(file);
		if(SafeFile.changetime(file1)) {
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean testcase() {
		try {
			sleep(3);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!rename("F:\\test\\TextFile\\a\\a_a\\a_x2.txt","F:\\test\\TextFile\\a\\a_a\\a_x20.txt"))return false;
		try {
			sleep(3);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//if(!changeTime("F:\\OO\\dir\\file2.txt")) return false;
		try {
			sleep(3);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//if(!changeSize("F:\\OO\\dir\\src\\file.txt"))return false;
		try {
			sleep(3);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*if(!move("F:\\OO\\dir\\new.txt","F:\\OO\\dir\\src\\new.txt")) {
			System.out.println("???");
			return false;
		}*/
		//if(!rename("F:\\OO\\dir\\file1.txt", "F:\\OO\\dir\\file3.txt")) return false;
		//if(!changeSize("F:\\OO\\dir\\src\\file.txt"))return false;
	  // if(!delete("F:\\OO\\dir\\ss.txt")) return false;
		return true;
	}
	
	public void run() {
		try {
			sleep(3);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!testcase()) {
			System.out.println("测试有错误");
		}//输出相关提示信息，或者进行额外的必要处理。
	}

}
