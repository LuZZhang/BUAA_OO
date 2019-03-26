package work_6;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MonitorThread extends Thread{
	private MonitorWork Moniwork;//监控作业
	private File[] oldfiles = new File[100];//目录下所有文件，不包含子目录
	private File[] newfiles = new File[100];
	private File[] oldsamefiles = new File[100];//目录下所有同名文件，包含
	private File[] newsamefiles = new File[100];
	private File[] oldallallfiles = new File[100];//目录下所有文件，包含
	//private File[] newallallfiles = new File[100];
	private File newname;
    private File newroad;
	private int oldfilenum = 0;
	private int newfilenum = 0;
	private int oldsamefilenum = 0;
	private int newsamefilenum = 0;
	private int oldallallfilenum = 0;
	//private int newallallfilenum = 0;
	private static int[] sumnum = new int[5];
	//private static int[] detnum = new int[5];
	
	public MonitorThread(MonitorWork work) {
		Moniwork = work;
	}
	
	public static void writetotxt(String fileName, String content) {     
        try {     
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件     
            FileWriter writer = new FileWriter(fileName, true);     
            writer.write(content);     
            writer.close();     
        } catch (IOException e) {     
            e.printStackTrace();     
        }     
    } 
	
	public void run() {
		File nowfile = Moniwork.road;
		System.out.println("success");
		int biaozhi=0;//判断是否成功触发
		long time = SafeFile.getlastmodi(Moniwork.road);//.lastModified();
		//System.out.println(Moniwork.road);
		if(!SafeFile.isfile(nowfile)) {//监控对象为目录
			SafeFile.alllen=0;
			SafeFile.alli=0;
			SafeFile.findall(nowfile);
			long oldsize = SafeFile.alllen;
			long newsize = 0;
			SafeFile.allalli=0;
			SafeFile.findallall(nowfile);
			if(SafeFile.allalli<=100) {
				oldallallfiles = SafeFile.Findedallall;//
			oldallallfilenum = SafeFile.allalli;
			}else {
				System.out.println("不支持超过100个文件");
				System.exit(0);
			}
			
			//监控对象是目录
			while(true) {
				try {
					sleep(3);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(Moniwork.Trig.equals("renamed")) {
					Moniwork.daihao = 0;
					biaozhi=1;
				}
				else if(Moniwork.Trig.equals("Modified")) {
					Moniwork.daihao=1;
					SafeFile.allalli=0;
					/*SafeFile.alllen*/
					if(SafeFile.ifexist(Moniwork.road)) {
						System.out.println(SafeFile.getlastmodi(nowfile)+" "+time);
						if(SafeFile.getlastmodi(nowfile)!=time) {//针对新增文件
							biaozhi=1;
						    System.out.println("目录最后修改时间变化了");
							break;
						}
					}
				}
				else if(Moniwork.Trig.equals("path-changed")) {
					Moniwork.daihao=2;
					biaozhi=1;
					  
				}
				else if(Moniwork.Trig.equals("size-changed")) {
					Moniwork.daihao=3;
					SafeFile.alli=0;
					SafeFile.alllen=0;
					SafeFile.findall(nowfile);//调用方法，更新findall
					//newallfiles = SafeFile.Findedall;//新的同名文件
					//newallfilenum = SafeFile.alli;
					newsize = SafeFile.alllen;
					System.out.println(newsize+"  "+oldsize+" "+SafeFile.getlastmodi(nowfile)+" "+time);
					if(newsize!=oldsize&&SafeFile.getlastmodi(nowfile)!=time) {//规模改变
						biaozhi=1;
						System.out.println("目录规模变化了");
						break;
					}
				}
				else { 
					System.out.println("没有该触发器");
					break;//结束线程
				}
				//
			}
			if(biaozhi==1) {
				if(Moniwork.task.equals("record-summary")) {
					synchronized(sumnum) {
					sumnum[Moniwork.daihao]++;
					writetotxt("summary.txt",Moniwork.Trig+"第"+sumnum[Moniwork.daihao]+"次被触发\n");
					System.out.println("记录summary");
					}
				}
				else if(Moniwork.task.equals("record-detail")) {
					synchronized(sumnum) {
					sumnum[Moniwork.daihao]++;
					}
					//writetotxt("detail.txt",Moniwork.Trig+"第"+detnum[Moniwork.daihao]+"次被触发");
					if(Moniwork.daihao==0) {//重命名
						writetotxt("detail.txt","<--"+Moniwork.Trig+"第"+sumnum[Moniwork.daihao]+"次被触发"+"目录中有文件发生了重命名"+"-->");
					}
					if(Moniwork.daihao==1) {//文件最后修改时间变化
						writetotxt("detail.txt","<--"+Moniwork.Trig+"第"+sumnum[Moniwork.daihao]+"次被触发  目录文件最后修改时间的变化："+time+"->"+nowfile.lastModified()+"-->");
					}
					if(Moniwork.daihao==2) {//路径变化
						writetotxt("detail.txt","<--"+Moniwork.Trig+"第"+sumnum[Moniwork.daihao]+"次被触发"+"目录中有文件发生了路径变化"+"-->");
					}
					if(Moniwork.daihao==3) {//文件规模变化
						writetotxt("detail.txt","<--"+Moniwork.Trig+"第"+sumnum[Moniwork.daihao]+"次被触发   目录文件的规模变化："+oldsize+"->"+newsize+"-->");
					}
					System.out.println("记录detail");
				}
				else if(Moniwork.task.equals("recover")) {
					if(Moniwork.daihao==0) {//重命名
							System.out.println("恢复过程发生错误");
					}
					else if(Moniwork.daihao==2) {//路径改变
							System.out.println("恢复过程发生错误");
					}
					else {
						System.out.println("触发器不符合");
						
					}
				}
				else {
					System.out.println("没有该任务");
				}
			}
		}
		if(SafeFile.isfile(nowfile)) {
			File father = SafeFile.getFa(Moniwork.road);
			if(SafeFile.allFilenum(SafeFile.getFa(Moniwork.road))<=100) {
				oldfiles = SafeFile.allfile(SafeFile.getFa(Moniwork.road));
				oldfilenum = SafeFile.allFilenum(SafeFile.getFa(Moniwork.road));
			}else {
				System.out.println("不支持超过100个文件");
				System.exit(0);//直接退出
			}
			
			long oldlen = SafeFile.getlen(Moniwork.road);
			String filename = SafeFile.getname(Moniwork.road);//.getName();
			long filelength = SafeFile.getlen(Moniwork.road);//.length();
			SafeFile.testi=0;
			SafeFile.findFile(father, SafeFile.getname(nowfile));//调用方法，更新finded
			if(SafeFile.testi<=100) {
				oldsamefiles = SafeFile.Finded;
			    oldfilenum = SafeFile.testi;
			}else {
				System.out.println("不支持超过100个文件");
				System.exit(0);
			} 
			
			while(true) {
				try {
					sleep(3);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(Moniwork.Trig.equals("renamed")) {
					Moniwork.daihao=0;
					if(SafeFile.getlastmodi(Moniwork.road)!=0)
						time = SafeFile.getlastmodi(Moniwork.road);
					//System.out.println("rename 线程");
					int flag = 0;//
					//break;
					//若触发器条件成立
					//在父目录下,当前扫描和上一次扫描，该文件消失，且新增了一个文件，且最后修改时间相同，有相同规模
					if(SafeFile.allFilenum(father)<=100) {
						newfiles = SafeFile.allfile(father);
					newfilenum = SafeFile.allFilenum(father);
					}else {
						System.out.println("不支持超过100个文件");
						break;
					}
					
					/*for(int i=0;i<newfilenum;i++) {
						//能不能找到该文件
						if(SafeFile.ifequ(newfiles[i], nowfile)) {//////
							flag=1;//找到该文件，肯定没有重命名
							break;
						}
					}*/
					if(SafeFile.ifexist(nowfile)) {
						flag=1;
					}
					if(flag==0) {
						//System.out.println("没有找到该文件");
						flag=0;
						//继续看该目录下有没有新增文件
						for(int i=0;i<newfilenum;i++) {
							flag=0;
							for(int j=0;j<oldfilenum;j++) {
								if(SafeFile.ifequ(newfiles[i],oldfiles[j])) {
									flag=1;
								}
							}
							if(flag==0) {//没有找到,即是新增文件
								
								if(SafeFile.getlastmodi(newfiles[i])==time) {
									newname = newfiles[i];
									//System.out.println(time);
									biaozhi=1;
									//System.out.println("被重命名啦");
									break;
								}
								else {
									//继续找啊
									System.out.println(SafeFile.getlastmodi(newfiles[i])+"   "+time);
									System.out.println("没有找到");
								}
							}
						}
						if(biaozhi==1) {
							break;
						}
						//得到该file的最后修改时间,
					}
					oldfiles = newfiles;
					oldfilenum = newfilenum;
					if(SafeFile.getlastmodi(Moniwork.road)!=0)
					time = SafeFile.getlastmodi(Moniwork.road);
					/*try {
						sleep(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
				}
				else if(Moniwork.Trig.equals("Modified")) {
					Moniwork.daihao=1;
					//System.out.println("Modified 线程");
					if(SafeFile.ifexist(Moniwork.road)) {
						if(SafeFile.getlastmodi(Moniwork.road)!=time) {
							biaozhi=1;
							//System.out.println("最后修改时间变化了");
							break;
						}
					}
					//break;
				}
				else if(Moniwork.Trig.equals("path-changed")) {
					Moniwork.daihao=2;
					if(SafeFile.getlastmodi(Moniwork.road)!=0)
						time = SafeFile.getlastmodi(Moniwork.road);
					SafeFile.testi=0;
					SafeFile.findFile(father, SafeFile.getname(nowfile));//调用方法，更新finded
					if(SafeFile.testi<=100) {
						newsamefiles = SafeFile.Finded;//新的同名文件
					    newsamefilenum = SafeFile.testi;//新的同名文件个数
					}else {
						System.out.println("不支持超过100个文件");
						break;
					}
					
					//File root = father;//得到父目录
					int flag=0;
					if(!SafeFile.ifexist(Moniwork.road)) {//如果当前路径下该文件不存在
						//System.out.println("该文件不存在");
						//SafeFile.testi=0;
						//SafeFile.findFile(root, filename);///注意！！！！！！！一次查找只能调用一次findfile方法，且找前需要把testi清零
						for(int k = 0;k<newsamefilenum;k++) {
							for(int ki=0;ki<oldsamefilenum;ki++) {
								if(SafeFile.ifequ(newsamefiles[k], oldsamefiles[ki])) {
									flag=1;
									break;
								}
							}
							if(flag==0) {
								System.out.println(SafeFile.getlastmodi(newsamefiles[k])+" "+time+" "+SafeFile.getlen(newsamefiles[k])+" "+filelength);
								if(SafeFile.getlastmodi(newsamefiles[k])==time&&SafeFile.getlen(newsamefiles[k])==filelength) {
									//System.out.println("wt");
									newroad = newsamefiles[k];
									biaozhi=1;
									break;
								}
							}
						}
						if(biaozhi==1) {
							break;
						}
						
					}
					oldsamefiles = newsamefiles;
					oldsamefilenum = newsamefilenum;
					if(SafeFile.getlastmodi(Moniwork.road)!=0)
						time = SafeFile.getlastmodi(Moniwork.road);
				}
				else if(Moniwork.Trig.equals("size-changed")) {
					Moniwork.daihao=3;
					//System.out.println("size-changed 线程");
					if(SafeFile.ifexist(Moniwork.road)) {
						if(SafeFile.getlastmodi(Moniwork.road)!=time&&SafeFile.getlen(Moniwork.road)!=oldlen) {
							biaozhi=1;
							//System.out.println("规模变化");
							break;
						}
					}
					//break;
				}
				else {//没有该触发器
					System.out.println("没有该触发器");
					break;//结束线程
				}
				
			}//扫描结束
			try {
				sleep(3);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//开始执行任务
			if(biaozhi==1) {
				if(Moniwork.task.equals("record-summary")) {
					synchronized(sumnum) {
					sumnum[Moniwork.daihao]++;
					writetotxt("summary.txt",Moniwork.Trig+"第"+sumnum[Moniwork.daihao]+"次被触发\n");
					System.out.println("记录summary");
					}
				}
				else if(Moniwork.task.equals("record-detail")) {
					synchronized(sumnum) {
					sumnum[Moniwork.daihao]++;
					}
					//writetotxt("detail.txt",Moniwork.Trig+"第"+detnum[Moniwork.daihao]+"次被触发\n");
					if(Moniwork.daihao==0) {//重命名
						writetotxt("detail.txt","<--"+Moniwork.Trig+"第"+sumnum[Moniwork.daihao]+"次被触发   文件名变化："+filename+"->"+SafeFile.getname(newname)+"  "+"文件最后修改时间变化："+time+"->"+time+"  "+"文件路径变化："+nowfile+"->"+nowfile+"  "+"文件规模变化："+oldlen+"->"+Moniwork.road.length()+"-->");//
						}
					if(Moniwork.daihao==1) {//文件最后修改时间变化
						writetotxt("detail.txt","<--"+Moniwork.Trig+"第"+sumnum[Moniwork.daihao]+"次被触发   文件名变化："+filename+"->"+filename+"  "+" 文件最后修改时间变化："+time+"->"+nowfile.lastModified()+"  "+"文件路径变化："+nowfile+"->"+nowfile+"  "+" 文件规模变化："+oldlen+"->"+Moniwork.road.length()+"-->");//
						}
					if(Moniwork.daihao==2) {//路径变化
						writetotxt("detail.txt","<--"+Moniwork.Trig+"第"+sumnum[Moniwork.daihao]+"次被触发   文件名变化："+filename+"->"+filename+"  "+"文件最后修改时间变化："+time+"->"+nowfile.lastModified()+"  "+" 文件路径变化："+nowfile+"->"+newroad+"  "+" 文件规模变化："+oldlen+"->"+Moniwork.road.length()+"-->");//
						}
					if(Moniwork.daihao==3) {//文件规模变化
						writetotxt("detail.txt","<--"+Moniwork.Trig+"第"+sumnum[Moniwork.daihao]+"次被触发   文件名变化："+filename+"->"+filename+"  "+"文件最后修改时间变化："+time+"->"+nowfile.lastModified()+"  "+"文件路径变化："+nowfile+"->"+nowfile+"  "+"文件规模变化："+oldlen+"->"+Moniwork.road.length()+"-->");//
						}
					try {
						sleep(5);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("记录detail");
				}
				else if(Moniwork.task.equals("recover")) {
					if(Moniwork.daihao==0) {//重命名
						if(SafeFile.move(newname, nowfile)) {
							System.out.println("已恢复命名");
						}else {
							System.out.println("命名恢复过程发生错误");
						}
						
					}
					else if(Moniwork.daihao==2) {//路径改变
						if(SafeFile.delefile(newroad)&&SafeFile.addfile(nowfile)) {
							System.out.println("已恢复路径");
						}else {
							System.out.println("路径恢复过程发生错误");
						}
					}
					else {
						System.out.println("触发器不符合");
					}
				}
				else {
					System.out.println("没有该任务");
				}
			}
		}
	}


	

}
