package work_10;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.*;

public class InputReq extends Thread{
	/** 
     * @Overview:输入请求类，拥有处理输入的功能。
     */
	static String getsIni = null;
	private static Scanner input;
	static String req = "^(\\[CR[,][(][0-9]{1,2}[,][0-9]{1,2}[)][,][(][0-9]{1,2}[,][0-9]{1,2}\\)\\])$";//
	static String end = "END";
	static String open = "^(\\[OPEN[,][(][0-9]{1,2}[,][0-9]{1,2}[)][,][(][0-9]{1,2}[,][0-9]{1,2}\\)\\])$";//
	static String close = "^(\\[CLOSE[,][(][0-9]{1,2}[,][0-9]{1,2}[)][,][(][0-9]{1,2}[,][0-9]{1,2}\\)\\])$";
	static String loadfile = "^(Load .+)$";
	private static final int STOP =0;
    private static final int WAIT =1;
    private static final int ORDER =2;
    private static final int SERVE =3;
    PassengerReq[] reqlists =new PassengerReq[300];
    
    public boolean repOK() {
    	/**@REQUIRES: None;
		@MODIFIES: None;
		@Effects: \result == invariant(this);
		*/
		if (input==null||reqlists==null)
			return false;
		return true;
    }
	
	public void run() {//
		/** @REQUIRES: None;
		@MODIFIES: System.out, getsIni, SPFA.A, Map.map, Main.taxis,reqlists; 
		@EFFECTS: normal behavior
		*          响应控制台输出的请求。
		*          sleep出现异常 ==> exceptional_behavior (e);
		@THREAD_REQUIRES: \locked(this);
		@THREAD_EFFECTS: \locked();
		@ */
		System.out.println("Input start!");
		//读入请求
		int i=0;
		long st=0;
		input = new Scanner(System.in);
		int nowpointi = 0;//请求发出地横坐标
		int nowpointj = 0;//请求发出地纵坐标
		int aimpointi = 0;//目标地点横坐标
		int aimpointj = 0;//目标第点纵坐标
		int modifai = 0;//修改道路开关的第一个坐标的横坐标
		int modifaj = 0;//修改道路开关的第一个坐标的纵坐标
		int modifbi = 0;//修改道路开关的第二个坐标的横坐标
		int modifbj = 0;//修改道路开关的第二个坐标的纵坐标
		long nowt=0;//请求发出时间
		int reqnums=0;
		int flag=0;
		
		while(i<300) {
			flag=0;
			if(input.hasNextLine()) { 
				getsIni = input.nextLine();	
				st = System.currentTimeMillis();//输入时的系统时间
			}
			String gets =getsIni.replaceAll(" ","");//;//消除指令间空格[sasa,(12,32),(52,33)]
			if(Pattern.matches(loadfile, getsIni)&&i==0) {//只能初始化的时候load file
			//	String fileroad = getsIni.substring(5);//fileroad即为路径
				//System.out.println(fileroad);
				File fileroad = new File(getsIni.substring(5));
				if(fileroad.exists()) {
					BufferedReader bufread = null;
					String read;
					int i1=0;//行数
					 try {
						bufread = new BufferedReader(new FileReader(fileroad));
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} 
					 try {
						 int i2=0;
						while ((read = bufread.readLine()) != null) {//每一行
							if(i1==0&&!(Pattern.matches("#No 9 Test File#", read))){
								System.out.println("文件格式错误");
								break;
							}
							if(Pattern.matches("#map", read)) {
								int map_ini[][] = new int[100][100];
								while ((read = bufread.readLine()) != null&&!Pattern.matches("#end_map", read)) {
									for(int j=0;j<80;j++) {
					                	try {
					                	map_ini[i2][j] = Integer.parseInt(read.substring(j,j+1));
					                	//0:无1:与右边有连接2:与下边有连接3:与右边下边均有连接
					                	if(map_ini[i2][j]==0||map_ini[i2][j]==1||map_ini[i2][j]==2||map_ini[i2][j]==3) {
					                		
					                	}
					                	else {
					                		System.out.println("图有问题");
					                	}
					                	}catch(Exception e) {
					                		System.out.println("图有问题");
					                		break;
					                	}
					                } 
					                i2++;//行数增加
					                if(i2>80) {
					                	System.out.println("图有问题");
					                	break;
					                }
					                //若图没有问题
					                for(int hang=0;hang<80;hang++) {
					                	for(int lie=0;lie<80;lie++) {
					                		Map.map[hang][lie] = map_ini[hang][lie];
					                	}
					                }
								}
							}
							i2=0;
							if(Pattern.matches("#light", read)) {
								int lig_ini[][] = new int[100][100];
								while ((read = bufread.readLine()) != null&&!Pattern.matches("#end_light", read)) {
									for(int j=0;j<80;j++) {
					                	try {
					                		lig_ini[i2][j]=Integer.parseInt(read.substring(j,j+1));
					                	//0:无1:与右边有连接2:与下边有连接3:与右边下边均有连接
					                	if(lig_ini[i2][j]==0||lig_ini[i2][j]==1) {
					                		
					                	}
					                	else {
					                		System.out.println("图有问题");
					                	}
					                	}catch(Exception e) {
					                		System.out.println("图有问题");
					                		break;
					                	}
					                } 
					                i2++;//行数增加
					                if(i2>80) {
					                	System.out.println("图有问题");
					                	break;
					                }
					                for(int hang=0;hang<80;hang++) {
					                	for(int lie=0;lie<80;lie++) {
					                		Light.map_lig[hang][lie] = lig_ini[hang][lie];
					                	}
					                }
								}
							}
							if(Pattern.matches("#flow", read)) {
								while ((read = bufread.readLine()) != null&&!Pattern.matches("#end_flow", read)) {
									if(Pattern.matches("[(][0-9]{1,2}[,][0-9]{1,2}[)][ ][(][0-9]{1,2}[,][0-9]{1,2}\\) [0-9]{1,10}", read)) {
										String[] eachpart1 = read.split("[ ]");//用逗号分开
										String[] eachpart2 = eachpart1[0].split(",");
										//System.out.println(eachpart1[0]);
										String[] eachpart3 = eachpart1[1].split(",");
										modifai=Integer.parseInt(eachpart2[0].substring(1));
										modifaj=Integer.parseInt(eachpart2[1].substring(0, eachpart2[1].length()-1));
										modifbi=Integer.parseInt(eachpart3[0].substring(1));
										modifbj=Integer.parseInt(eachpart3[1].substring(0, eachpart3[1].length()-1));
										int value = Integer.parseInt(eachpart1[2]);
										//若该边存在
										if((modifai<80&&modifaj<80&&modifbi<80&&modifbj<80)&&modifai==modifbi&&modifaj==modifbj+1&&(Map.map[modifbi][modifbj]==1||Map.map[modifbi][modifbj]==3)) {
												System.out.println("该边存在,成功修改流量初值");
												Map.modiliuliang(modifai, modifaj, modifbi, modifbj, value);
										}
										else if((modifai<80&&modifaj<80&&modifbi<80&&modifbj<80)&&modifai==modifbi+1&&modifaj==modifbj&&(Map.map[modifbi][modifbj]==2||Map.map[modifbi][modifbj]==3)) {
												System.out.println("该边存在,成功修改流量初值");
												Map.modiliuliang(modifai, modifaj, modifbi, modifbj, value);
										}
										else if((modifai<80&&modifaj<80&&modifbi<80&&modifbj<80)&&modifai==modifbi&&modifaj==modifbj-1&&(Map.map[modifai][modifaj]==1||Map.map[modifai][modifaj]==3)) {
												System.out.println("该边存在,成功修改流量初值");
												Map.modiliuliang(modifai, modifaj, modifbi, modifbj, value);
										}
										else if((modifai<80&&modifaj<80&&modifbi<80&&modifbj<80)&&modifai==modifbi-1&&modifaj==modifbj&&(Map.map[modifai][modifaj]==2||Map.map[modifai][modifaj]==3)) {
												System.out.println("该边存在,成功修改流量初值");
												Map.modiliuliang(modifai, modifaj, modifbi, modifbj, value);
										}
										else {
											System.out.println("该边不存在，无法修改流量初值");
										}
									}
								}
							}
							if(Pattern.matches("#taxi", read)) {
								while ((read = bufread.readLine()) != null&&!Pattern.matches("#end_taxi", read)) {
									if(Pattern.matches("[0-9]{1,2}[ ][0-3][ ][0-9]{1,10}[ ][(][0-9]{1,2}[,][0-9]{1,2}[)]", read)) {
										String[] eachpart1 = read.split("[ ]");//用空格分开
										String[] eachpart2 = eachpart1[3].split(",");
										int No = Integer.parseInt(eachpart1[0]);
										int Status = Integer.parseInt(eachpart1[1]);
										int Credit = Integer.parseInt(eachpart1[2]);
										int x = Integer.parseInt(eachpart2[0].substring(1));
										int y = Integer.parseInt(eachpart2[1].substring(0, eachpart2[1].length()-1));
										//System.out.println(No+" "+Status+" "+Credit+" "+x+" "+y);
										//对编号为No的出租车进行初始化。
										TaxiSch moditaxi = (TaxiSch) Main.taxis[No];//moditaxi为修改的出租车
										moditaxi.setnowi(x);
										moditaxi.setnowj(y);
										moditaxi.setcredit(Credit);
										if(Status==STOP) {
											moditaxi.switchto(0, 0, 0);
										}
										else if(Status==WAIT) {
											moditaxi.switchto(1, 0, 0);
										}
										else if(Status==ORDER) {
											moditaxi.switchto(2, x, y);
											moditaxi.setlingyi(1);
										}
										else {//SERVE
											moditaxi.switchto(3, x, y);
											//moditaxi.setlingyi(1);
										}
										
									}else {
										//System.out.println("???");
									}
								}
							}
							if(Pattern.matches("#request", read)) {
								while ((read = bufread.readLine()) != null&&!Pattern.matches("#end_request", read)) {
									if(Pattern.matches(req, read)) {
										//System.out.println("??!");
										try {
											String[] eachpart = read.split("[,]");//用逗号分开
											//String cr = eachpart[0].substring(1);
											nowpointi=Integer.parseInt(eachpart[1].substring(1));
											nowpointj=Integer.parseInt(eachpart[2].substring(0, eachpart[2].length()-1));
											aimpointi=Integer.parseInt(eachpart[3].substring(1));
											aimpointj=Integer.parseInt(eachpart[4].substring(0, eachpart[4].length()-2));
											nowt=st/100;//百毫秒为单位，请求发出时刻
											//System.out.println("nowpointi"+nowpointi+"nowpointj"+nowpointj+"aimpointi"+aimpointi+"aimpointj"+aimpointj);
										    if(nowpointi<80&&nowpointj<80&&aimpointi<80&&aimpointj<80&&!(nowpointi==aimpointi&&nowpointj==aimpointj)) {//大小满足条件
										    	//判断是否同质
										    	//如果有重复的
										    //	System.out.println("aaa");
										    	if(reqnums==0) {//当请求队列为空时，还没有得到有效请求的时候，第一条肯定不同质
										    		Thread newreq = new PassengerReq(nowpointi,nowpointj,aimpointi,aimpointj,nowt);
									    			//加到请求队列数组里
									    			reqlists[reqnums]=(PassengerReq) newreq;
									    			reqnums++;
									    			//System.out.println(reqnums);
									    			i++;
									    			newreq.setUncaughtExceptionHandler(new ExceptionSch());
									    			newreq.start();
										    	}else {
										    	//	System.out.println("bbb");
											    	for(int i11=0;i11<reqnums;i11++) {
											    	//	System.out.println(nowt+" "+reqlists[i1].settime);
											    		if(reqlists[i11].seti==nowpointi&&reqlists[i11].setj==nowpointj&&reqlists[i11].aimi==aimpointi&&reqlists[i11].aimj==aimpointj&&reqlists[i11].settime==nowt) {//
											    			//是同质请求
											    			System.out.println("与之前请求同质");
											    			flag=1;
											    			break;
											    		}
											    	}
											    //	System.out.println("ccc");
											    	if(flag==0) {
											    		Thread newreq = new PassengerReq(nowpointi,nowpointj,aimpointi,aimpointj,nowt);
											    			//加到请求队列数组里
											    			reqlists[reqnums]=(PassengerReq) newreq;
											    			reqnums++;
											    			//System.out.println(reqnums);
											    			i++;
											    			newreq.setUncaughtExceptionHandler(new ExceptionSch());
											    			newreq.start();
											    	}
										    	}
										    }else {//大小不满足条件
										    	System.out.println("输入坐标大小不满足条件");
										    }
										}catch(Exception e) {
											System.out.println("无效输入");
										}
									}
								}
							}
							 i1++;
						 }
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					 try {
						bufread.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
				}else {
					System.out.println(fileroad+"路径不存在");
				}
			}
			else if(Pattern.matches(req, gets)) {//满足正则表达式
				try {
				String[] eachpart = gets.split("[,]");//用逗号分开
				//String cr = eachpart[0].substring(1);
				nowpointi=Integer.parseInt(eachpart[1].substring(1));
				nowpointj=Integer.parseInt(eachpart[2].substring(0, eachpart[2].length()-1));
				aimpointi=Integer.parseInt(eachpart[3].substring(1));
				aimpointj=Integer.parseInt(eachpart[4].substring(0, eachpart[4].length()-2));
				nowt=st/100;//百毫秒为单位，请求发出时刻
				//System.out.println("nowpointi"+nowpointi+"nowpointj"+nowpointj+"aimpointi"+aimpointi+"aimpointj"+aimpointj);
			    if(nowpointi<80&&nowpointj<80&&aimpointi<80&&aimpointj<80&&!(nowpointi==aimpointi&&nowpointj==aimpointj)) {//大小满足条件
			    	//判断是否同质
			    	//如果有重复的
			    //	System.out.println("aaa");
			    	if(reqnums==0) {//当请求队列为空时，还没有得到有效请求的时候，第一条肯定不同质
			    		Thread newreq = new PassengerReq(nowpointi,nowpointj,aimpointi,aimpointj,nowt);
		    			//加到请求队列数组里
		    			reqlists[reqnums]=(PassengerReq) newreq;
		    			reqnums++;
		    			//System.out.println(reqnums);
		    			i++;
		    			newreq.setUncaughtExceptionHandler(new ExceptionSch());
		    			newreq.start();
			    	}else {
			    	//	System.out.println("bbb");
				    	for(int i1=0;i1<reqnums;i1++) {
				    	//	System.out.println(nowt+" "+reqlists[i1].settime);
				    		if(reqlists[i1].seti==nowpointi&&reqlists[i1].setj==nowpointj&&reqlists[i1].aimi==aimpointi&&reqlists[i1].aimj==aimpointj&&reqlists[i1].settime==nowt) {//
				    			//是同质请求
				    			System.out.println("与之前请求同质");
				    			flag=1;
				    			break;
				    		}
				    	}
				    //	System.out.println("ccc");
				    	if(flag==0) {
				    		Thread newreq = new PassengerReq(nowpointi,nowpointj,aimpointi,aimpointj,nowt);
				    			//加到请求队列数组里
				    			reqlists[reqnums]=(PassengerReq) newreq;
				    			reqnums++;
				    			//System.out.println(reqnums);
				    			i++;
				    			newreq.setUncaughtExceptionHandler(new ExceptionSch());
				    			newreq.start();
				    	}
			    	}
			    }else {//大小不满足条件
			    	System.out.println("输入坐标大小不满足条件");
			    }
			}catch(Exception e) {
				System.out.println("无效输入");
			}
			}
			else if(Pattern.matches(end, gets)) {
				break;//输入结束
			}
			else if(Pattern.matches(open, gets)) {
				//只是输入符合格式
				String[] eachpart = gets.split("[,]");//用逗号分开
				modifai=Integer.parseInt(eachpart[1].substring(1));
				modifaj=Integer.parseInt(eachpart[2].substring(0, eachpart[2].length()-1));
				modifbi=Integer.parseInt(eachpart[3].substring(1));
				modifbj=Integer.parseInt(eachpart[4].substring(0, eachpart[4].length()-2));
				if(modifai<80&&modifaj<80&&modifbi<80&&modifbj<80) {
					if(modifai==modifbi&&modifaj==modifbj+1) {
						if(Map.map[modifbi][modifbj]==0) {
							if(Map.modify(modifbi, modifbj, 1)){
								System.out.println("加入该边成功");
								Main.gui.SetRoadStatus(new Point(modifai,modifaj), new Point(modifbi,modifbj), 1);
							}
						}
						else if(Map.map[modifbi][modifbj]==2) {
							if(Map.modify(modifbi, modifbj, 3)){
								System.out.println("加入该边成功");
								Main.gui.SetRoadStatus(new Point(modifai,modifaj), new Point(modifbi,modifbj), 1);
							}
						}
					}
					if(modifai==modifbi+1&&modifaj==modifbj) {
						if(Map.map[modifbi][modifbj]==0) {
							if(Map.modify(modifbi, modifbj, 2)){
								System.out.println("加入该边成功");
								Main.gui.SetRoadStatus(new Point(modifai,modifaj), new Point(modifbi,modifbj), 1);
							}
						}
						else if(Map.map[modifbi][modifbj]==1) {
							if(Map.modify(modifbi, modifbj, 3)) {
								System.out.println("加入该边成功");
								Main.gui.SetRoadStatus(new Point(modifai,modifaj), new Point(modifbi,modifbj), 1);
							}
						}
					}
					if(modifai==modifbi&&modifaj==modifbj-1) {
						if(Map.map[modifai][modifaj]==0) {
							if(Map.modify(modifai, modifaj, 1)){
								System.out.println("加入该边成功");
								Main.gui.SetRoadStatus(new Point(modifai,modifaj), new Point(modifbi,modifbj), 1);
							}
						}
						else if(Map.map[modifai][modifaj]==2) {
							if(Map.modify(modifai, modifaj, 3)){
								System.out.println("加入该边成功");
								Main.gui.SetRoadStatus(new Point(modifai,modifaj), new Point(modifbi,modifbj), 1);
							}
						}
					}
					if(modifai==modifbi-1&&modifaj==modifbj) {
						if(Map.map[modifai][modifaj]==0) {
							if(Map.modify(modifai, modifaj, 2)){
								System.out.println("加入该边成功");
								Main.gui.SetRoadStatus(new Point(modifai,modifaj), new Point(modifbi,modifbj), 1);
							}
						}
						else if(Map.map[modifai][modifaj]==1) {
							if(Map.modify(modifai, modifaj, 3)){
								System.out.println("加入该边成功");
								Main.gui.SetRoadStatus(new Point(modifai,modifaj), new Point(modifbi,modifbj), 1);
							}
						}
					}
				}
			}
			else if(Pattern.matches(close, gets)) {
				//只是输入符合格式
				String[] eachpart = gets.split("[,]");//用逗号分开
				modifai=Integer.parseInt(eachpart[1].substring(1));
				modifaj=Integer.parseInt(eachpart[2].substring(0, eachpart[2].length()-1));
				modifbi=Integer.parseInt(eachpart[3].substring(1));
				modifbj=Integer.parseInt(eachpart[4].substring(0, eachpart[4].length()-2));
				if(modifai<80&&modifaj<80&&modifbi<80&&modifbj<80) {
					if(modifai==modifbi&&modifaj==modifbj+1) {
						if(Map.map[modifbi][modifbj]==1) {
							if(Map.modify(modifbi, modifbj, 0)) {
								System.out.println("删除该边成功");
								Main.gui.SetRoadStatus(new Point(modifai,modifaj), new Point(modifbi,modifbj), 0);
							}
						}
						else if(Map.map[modifbi][modifbj]==3) {
							if(Map.modify(modifbi, modifbj, 2)) {
								System.out.println("删除该边成功");
								Main.gui.SetRoadStatus(new Point(modifai,modifaj), new Point(modifbi,modifbj), 0);
							}
						}
					}
					if(modifai==modifbi+1&&modifaj==modifbj) {
						if(Map.map[modifbi][modifbj]==2) {
							if(Map.modify(modifbi, modifbj, 0)) {
								System.out.println("删除该边成功");
								Main.gui.SetRoadStatus(new Point(modifai,modifaj), new Point(modifbi,modifbj), 0);
							}
						}
						else if(Map.map[modifbi][modifbj]==3) {
							if(Map.modify(modifbi, modifbj, 1)) {
								System.out.println("删除该边成功");
								Main.gui.SetRoadStatus(new Point(modifai,modifaj), new Point(modifbi,modifbj), 0);
							}
						}
					}
					if(modifai==modifbi&&modifaj==modifbj-1) {
						if(Map.map[modifai][modifaj]==1) {
							if(Map.modify(modifai, modifaj, 0)) {
								System.out.println("删除该边成功");
								Main.gui.SetRoadStatus(new Point(modifai,modifaj), new Point(modifbi,modifbj), 0);
							}
						}
						else if(Map.map[modifai][modifaj]==3) {
							if(Map.modify(modifai, modifaj, 2)){
								System.out.println("删除该边成功");
								Main.gui.SetRoadStatus(new Point(modifai,modifaj), new Point(modifbi,modifbj), 0);
							}
						}
					}
					if(modifai==modifbi-1&&modifaj==modifbj) {
						if(Map.map[modifai][modifaj]==2) {
							if(Map.modify(modifai, modifaj, 0)){
								System.out.println("删除该边成功");
								Main.gui.SetRoadStatus(new Point(modifai,modifaj), new Point(modifbi,modifbj), 0);
							}
						}
						else if(Map.map[modifai][modifaj]==3) {
							if(Map.modify(modifai, modifaj, 1)){
								System.out.println("删除该边成功");
								Main.gui.SetRoadStatus(new Point(modifai,modifaj), new Point(modifbi,modifbj), 0);
							}
						}
					}
				}
			}
			else {
				System.out.println("输入不匹配");
			}
		}
	}

}
