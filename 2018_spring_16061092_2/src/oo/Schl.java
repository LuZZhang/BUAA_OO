package oo;

import java.math.BigInteger;
import java.util.Scanner;
import java.util.regex.*;
 
public class Schl {
	private static RequQue RQ = new RequQue();
	private static Scanner input;
	public static void main(String[] args) {
		String floorUP = "^([(]FR,[+]?[0]{0,10}[1-9],UP,[+]?(([0]{0,10}[1-9][0-9]{0,9})|([0]{0,11}))[)])$";//[(]FR,[+]?[0]{0,10}[1-9],UP,[+]?[0]{0,10}[0-(pow(2,31)-1)][)]
		String floorDOWN = "^([(]FR,[+]?[0]{0,10}([2-9]|10),DOWN,[+]?(([0]{0,10}[1-9][0-9]{0,9})|([0]{0,11}))[)])$";
		String elevReq = "^([(]ER,[+]?[0]{0,10}([1-9]|10),[+]?(([0]{0,10}[1-9][0-9]{0,9})|([0]{0,11}))[)])$";//[(]ER,[+]?[0]{0,10}[1-10],[+]?[0]{0,10}[0-(pow(2,31)-1)][)]
		String END = "^RUN$";
		int m = 1;
		int i=0,T=0;
		int newTimeMax = 0;
		String getsIni=null;
		input = new Scanner(System.in);
		//��ȡ����
		while(true) {

		if(input.hasNextLine()) { 
			getsIni = input.nextLine();
		}
		 
		String gets = getsIni.replaceAll(" ","");//������пո�
		
		if(Pattern.matches(floorUP,gets)) {
			String[] eachpart = gets.split("[,]");//���ŷֿ�
			m = Integer.parseInt(eachpart[1]);//Ŀ��¥��
			String newstring = eachpart[3].substring(0, eachpart[3].length()-1);//T
			//�ж������Ƿ�С��4�ֽ�
			BigInteger a = new BigInteger(newstring);//a���������ʱ����
			BigInteger border = new BigInteger("4294967295");//���߽�
			if(a.compareTo(border)>0) {
				System.out.println("ERROR");
				System.out.println("#Time out");
				continue;
			}
			T = Integer.parseInt(newstring);
			if((T<newTimeMax)||(T!=0&&i==0)) {
				System.out.println("ERROR");
				System.out.println("#Time disorder");
				continue;
			}
			Requ Rq = new Requ(m,1,T);
			//���뵽�������
			RQ.addReq(Rq);
		}
		
		else if(Pattern.matches(floorDOWN,gets)) {
			String[] eachpart = gets.split("[,]");//���ŷֿ�
			m = Integer.parseInt(eachpart[1]);//Ŀ��¥��
			String newstring = eachpart[3].substring(0, eachpart[3].length()-1);//T
			//�ж������Ƿ�С��4�ֽ�
			BigInteger a = new BigInteger(newstring);//a���������ʱ����
			BigInteger border = new BigInteger("4294967295");//���߽�
			if(a.compareTo(border)>0) {
				System.out.println("ERROR");
				System.out.println("#Time out");
				continue;
			}
			T = Integer.parseInt(newstring);
			//System.out.println(T);
			if((T<newTimeMax)||(T!=0&&i==0)) {
				System.out.println("ERROR");
				System.out.println("#Time disorder");
				continue;
			}
			Requ Rq = new Requ(m,-1,T);
			//���뵽�������
			RQ.addReq(Rq);
		}  
		
		else if(Pattern.matches(elevReq,gets)) {
			String[] eachpart = gets.split("[,]");//���ŷֿ�
			m = Integer.parseInt(eachpart[1]);//Ŀ��¥��
			String newstring = eachpart[2].substring(0, eachpart[2].length()-1);//T
			//�ж������Ƿ�С��4�ֽ�
			BigInteger a = new BigInteger(newstring);//a���������ʱ����
			BigInteger border = new BigInteger("4294967295");//���߽�
			if(a.compareTo(border)>0) {
				System.out.println("ERROR");
				System.out.println("#Time out");
				continue;
			}
			T = Integer.parseInt(newstring);
			if((T<newTimeMax)||(T!=0&&i==0)) {
				System.out.println("ERROR");
				System.out.println("#Time disorder");
				continue;
			}
			Requ Rq = new Requ(m,0,T);
			//���뵽�������
			RQ.addReq(Rq);
		}
		
		else if(Pattern.matches(END,gets)) {
			break;
		}
		else {
			System.out.println("ERROR");
			System.out.println("#Matching Error");
			continue;
		}
		newTimeMax = T;
		i++;//��Ч�������
		if(i==100) {
			System.out.println("#Now 100");
			System.exit(0);
		}
		}//������� 
		Schl sch = new Schl();
		sch.Schedule();
	}
	public void Schedule() {
		Ele Ev = new Ele();
		double nowT=0,Tnew;
		int nowstage=1,aimstage=1,T=0;
		while(RQ.RQnum()!=0) {//��ǰ�������в�Ϊ��
			//�ж��Ƿ���֮ǰ��ͬ������
			if(RQ.nowvalid()==0) {
				//��ͬ������
				System.out.println("#Invalid Req");
				RQ.subReq();
				continue;
			}
			//����ͬ������
			else {
				aimstage = RQ.nowRq(0).getAimstage();
				T = RQ.nowRq(0).getT();//��ǰ��������󷢳�ʱ��
				if(T>nowT) nowT = T;
				Tnew = Ev.run(nowstage, aimstage, nowT);//����һ�ε���run
				if(nowstage==aimstage) System.out.println("("+aimstage+",STILL,"+(nowT+1)+")");
				else if(nowstage>aimstage) System.out.println("("+aimstage+",DOWN,"+Tnew+")");
				else System.out.println("("+aimstage+",UP,"+Tnew+")"); 
				Tnew = Ev.OpenDo(Tnew);//����һ�ο�����
			}
			for(int j=0;j<RQ.RQnum();j++) {
				if((RQ.nowRq(j).getAimstage()==aimstage)&&(RQ.nowRq(j).getT()<=Tnew)&&(RQ.nowRq(j).getReqnum()==RQ.nowRq(0).getReqnum())) {
					RQ.setvalid(j);
				}
			} 
			nowT = Tnew;
			nowstage = aimstage;
			RQ.subReq();
		}
	}
}
