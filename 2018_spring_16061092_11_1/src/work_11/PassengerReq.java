package work_11;

import java.awt.Point;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import work_11.TaxiNSch.Record;

public class PassengerReq extends Thread{ 
	/** 
     * @Overview:模拟乘客叫车和调度器调度。
     */
	//public String CR;
	public int seti = 0;
	public int setj = 0;
	public int aimi = 0;
	public int aimj = 0;
	public long settime = 0;
	public RobbingMess[] robbingalllist = new RobbingMess[100];//总抢单队列
	public int[] robbingnowlist = new int[100];//当前抢单队列，只关注哪辆车
	public int robbingallnum = 0;//总抢单队列数
	public int robbingnownum = 0;//当前抢单队列数 
	//ArrayList<Record> records;//每次记录
	public int[] diedaip = new int[40];
	public int[][] diedai = new int[40][300];
	
	public boolean repOK() {
		/**@REQUIRES: None;
		@MODIFIES: None;
		@Effects: \result == invariant(this);
		*/
		if (robbingalllist==null||robbingnowlist==null||seti<0||seti>79||aimi<0||aimi>79||setj<0||setj>79||aimj<0||aimj>79||settime<0||robbingallnum<0||robbingnownum<0)
			return false;
		return true;
	}
	
	public PassengerReq(int a,int b,int c,int d, long e) {
		/**@REQUIRES: (\all integer a;0 <= a <=79);
		 * (\all integer b;0 <= a <=79);
		 * (\all integer c;0 <= a <=79);
		 * (\all integer d;0 <= a <=79);
		 * (\all long e;e > 0);
		@MODIFIES: seti,setj,aimi,aimj,settime;
		@EFFECTS: 为属性赋值;
		@ */

		seti = a;
		setj = b;
		aimi = c;
		aimj = d;
		settime = e;//请求发出时间，以百毫秒为单位
	} 
	
	public static void writetotxt(String content) {   
		/**@REQUIRES: (\all String content);
		@MODIFIES: writer;
		@EFFECTS: 将content写入到passmess.txt中;
		@ */
        try {      
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件     
            FileWriter writer = new FileWriter("passmess.txt", true);     
            writer.write(content);     
            writer.close();     
        } catch (IOException e) {     
            e.printStackTrace();     
        }     
    }
	
	
	public void run() {
		/** @REQUIRES: None;
		@MODIFIES: writer,System.out,robbingallnum,robbingalllist,robbingnownum,robbingnowlist,taxibest.status,gui;
		@EFFECTS: 乘客发出请求,在发出请求7.5s内循环扫描有没有可以抢单的车.
		*          7.5s窗口结束后,从当前抢单列表中找到最佳出租车，派单给它.
		*          否则输出"派单失败，请重新叫车";
		@ */
		//Main.gui.RequestTaxi(new Point(seti,setj), new Point(aimi,aimj));
		writetotxt("<-----乘客在"+settime*100+"时刻发出从"+"("+seti+","+setj+")"+"到"+"("+aimi+","+aimj+")的请求----->");
		System.out.println("Passreq run!");
		while(System.currentTimeMillis()<=settime*100+7500) {//当系统时间小于settime*100+3000
			
			for(int i=0;i<100;i++) {//循环扫描有没有可以抢单的车
				TaxiSch newtaxi = (TaxiSch)Main.taxis[i];//newtaxi是第i号出租车
				//System.out.println(i+" "+newtaxi.getnowi()+" "+seti);
				//System.out.println("juedui"+Math.abs(newtaxi.getnowi()-seti));
				//该车已经参与抢单则不能再抢
				int ifbreak=0;
				for(int ii=0;ii<robbingnownum;ii++) {
					if(robbingnowlist[ii]==i) {
						ifbreak=1;
						break;
					}
				}
				if(ifbreak==1) break;
				if(Math.abs(newtaxi.getnowi()-seti)<=2&&Math.abs(newtaxi.getnowj()-setj)<=2&&newtaxi.getnowstatus()==1){
					//成功抢单，加入当前抢单队列和总抢单队列
					//加信用度
					newtaxi.addcredit(1);
					RobbingMess robmess = new RobbingMess(i,newtaxi.getnowi(),newtaxi.getnowj(),newtaxi.getnowstatus(),newtaxi.getcre());
					System.out.println("总抢单单号"+robbingallnum+" "+i);
					//System.out.println();
					robbingalllist[robbingallnum] = robmess;//总抢单队列里加入该单
					robbingallnum++;
					int ifhavei =0;
				//	System.out.println("当前抢单数"+robbingnownum);
					for(int j=0;j<robbingnownum;j++) {
						if(robbingnowlist[j]==i) {//当前抢单队列里有该车
							ifhavei=1;
						}
					}
					if(ifhavei==0) {//当前抢单队列里没有该车
						robbingnowlist[robbingnownum] = i;//加入该车
						robbingnownum++;//当前抢单车数
					}
				}
			}
			int j=0;
			for(int i=0;i<robbingnownum;i++) {//循环扫描有没有退出抢单列表的车
				TaxiSch newtaxi = (TaxiSch)Main.taxis[robbingnowlist[i]];//抢单列表第i个车
				if(newtaxi.getnowstatus()!=1) {
					//退出抢单列表
					System.out.println(robbingnowlist[i]+"退出抢单列表");
				}else {//无需退出当前抢单列表
					robbingnowlist[j]=robbingnowlist[i];
					j++;
				}
			}//扫描完毕
			robbingnownum=j;
		}//叫车窗口关闭
		//选择信用度最高，距离最近的派车
		System.out.println("抢单窗口关闭时抢单成功车辆"+robbingnownum);
		int robbingnownumbers=robbingnownum;
		int ifalloced=0;
		TaxiSch taxibest=null;
		//输出当前请求队列里抢单车辆的信息
		if(robbingnownum>0) {
			writetotxt("--------乘客在抢单时间窗内所有参与抢单的出租车信息--------");
		}
		for(int i=0;i<robbingnownum;i++) {
			TaxiSch taxii = (TaxiSch) Main.taxis[robbingnowlist[i]];
			writetotxt("<---单号:"+i+"车辆编号:"+robbingnowlist[i]+"车辆位置:("+taxii.getnowi()+","+taxii.getnowj()+")"+"车辆状态:"+taxii.getnowstatustostring()+"车辆信用信息"+taxii.getcre()+"--->");
		}//仅仅为了输出
		while(robbingnownumbers>0) {//当剩余抢单车辆为0时退出抢单
			robbingnownumbers=robbingnownum;
			System.out.println("剩余抢单车辆"+robbingnownumbers);
			int nowmaxcred=0;//当前最高信用度
			int nowbest = 0;
			for(int i=0;i<robbingnownum;i++) {
				TaxiSch taxinow = (TaxiSch) Main.taxis[robbingnowlist[i]];
				taxibest = (TaxiSch) Main.taxis[nowbest];
				if(taxinow.getnowstatus()!=1) {
					robbingnownumbers--;
				}
				else {
					if(taxinow.getcre()>nowmaxcred) {
					nowmaxcred = taxinow.getcre();
					nowbest = robbingnowlist[i];
					}
					else if(taxinow.getcre()==nowmaxcred) {
						SPFA spfa = new SPFA();
						if(spfa.distance(taxinow.getnowi()*80+taxinow.getnowj(), seti*80+setj,taxinow.ID)<spfa.distance(taxibest.getnowi()*80+taxibest.getnowj(), seti*80+setj,taxibest.ID)) {
							//比当前最佳车距离乘客出发点更近
							nowbest= robbingnowlist[i];
							System.out.println(nowbest);
						}
					}
				}
				
			}
			taxibest = (TaxiSch) Main.taxis[nowbest];
			//循环结束得到最佳车
			if(taxibest.getnowstatus()==1) {//该车处于等待服务状态
				taxibest.switchto(2,seti,setj);//派单给他
				System.out.println("被派单给"+nowbest);
				if(taxibest.ID<30) {
					//可追踪出租车
					 
				}
				ifalloced=1;
				break;
			}
		}
		if(ifalloced==1) {//被派单
			//taxibest进入接单状态
			while(true) {
				if(taxibest.getnowstatus()==3) {//进入
					taxibest.switchto(3, aimi, aimj);
				}
			}
			
		}else {
			System.out.println("派单失败，请重新叫车");
		}
	}

}
