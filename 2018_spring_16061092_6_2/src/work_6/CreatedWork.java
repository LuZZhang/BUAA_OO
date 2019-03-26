package work_6;

import java.io.*;

public class CreatedWork {
	public static int CreatedObjnum = 0;//被创建的监控对象数目
	public static int CreatedWorknum = 0;//被创建的作业数目
	public static MonitorWork[] createdWorks = new MonitorWork[125];//被创建的每个不同作业
	public static File[] createdObj = new File[15];//被创建的不同监控对象
	
	public static void creatWork(MonitorWork Work) {
		//createdWorks[CreatedWorknum] = Work;
		//CreatedWorknum++; 
		int flag=0,i=0;
		//判断是否有相同对象
		for(i=0;i<CreatedObjnum;i++) {
			if(createdObj[i].equals(Work.road)) {
				flag=1;//有重复对象
				break;
			}
		}
		if(flag==0) {//与之前监控对象不重复
			if(CreatedObjnum<10) {
				if(Work.road.exists()) {
					createdObj[i]=Work.road;
					CreatedObjnum++;
					System.out.println("创建新的监控对象");
					flag=0;
					for(i=0;i<CreatedWorknum;i++) {
						if(createdWorks[i].road.equals(Work.road)&&createdWorks[i].Trig.equals(Work.Trig)&&createdWorks[i].task.equals(Work.task)) {
							flag=1;//有重复作业
							break;
						}
					}
					if(flag==0) {
						createdWorks[CreatedWorknum] = Work;
						CreatedWorknum++;
						System.out.println("创建新的监控作业 ");
						//Thread td =new MonitorThread(Work);//创建监控线程
						Main.td[Main.tdnum] = new MonitorThread(Work);//创建监控线程
						Main.tdnum++;
					}
				}
				else {
					System.out.println("该路径不存在："+Work.road);
				}
			} 
			else {
				System.out.println("监控对象超过10个，不处理后面的啦");
			}
		}
		else {//有重复对象
			//判断是否有相同作业
			flag=0;
			for(i=0;i<CreatedWorknum;i++) {
				if(createdWorks[i].road.equals(Work.road)&&createdWorks[i].Trig.equals(Work.Trig)&&createdWorks[i].task.equals(Work.task)) {
					flag=1;//有重复作业
					break;
				}
			}
			if(flag==0) {
				createdWorks[CreatedWorknum] = Work;
				CreatedWorknum++;
				System.out.println("创建新的监控作业 ");
				Main.td[Main.tdnum] = new MonitorThread(Work);//创建监控线程
				Main.tdnum++;
			}
		}
	}

}
