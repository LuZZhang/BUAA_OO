package oo;
 
import java.math.BigInteger;
import java.util.Scanner;
import java.util.regex.Pattern;
 
public class Main {
	private static RequQue RQ = new RequQue();
	private static Scanner input;
	public static void main(String[] args) {
		String floorUP = "^([(]FR,[+]?[0]{0,10}[1-9],UP,[+]?(([0]{0,10}[1-9][0-9]{0,9})|([0]{0,11}))[)])$";
		String floorDOWN = "^([(]FR,[+]?[0]{0,10}([2-9]|10),DOWN,[+]?(([0]{0,10}[1-9][0-9]{0,9})|([0]{0,11}))[)])$";
		String elevReq = "^([(]ER,[+]?[0]{0,10}([1-9]|10),[+]?(([0]{0,10}[1-9][0-9]{0,9})|([0]{0,11}))[)])$";
		String END = "^RUN$";
		String first_one = "[(]FR,[+]?[0]{0,10}1,UP,[+]?[0]{0,10}0[)]";
		int m = 1;
		int i=0;
		double T=0; 
		double newTimeMax = 0;
		String getsIni=null;
		input = new Scanner(System.in);
		//��ȡ����
		while(i<100) {

		if(input.hasNextLine()) { 
			getsIni = input.nextLine();
		}
		 
		String gets = getsIni.replaceAll(" ","");//消除指令间空格
		
		if(Pattern.matches(floorUP,gets)) {
			String[] eachpart = gets.split("[,]");//用逗号分开
		    m = Integer.parseInt(eachpart[1]);//楼层数
			String newstring = eachpart[3].substring(0, eachpart[3].length()-1);//T
			//�ж������Ƿ�С��4�ֽ�
			BigInteger a = new BigInteger(newstring);//a���������ʱ����
			BigInteger border = new BigInteger("4294967295");//���߽�
			if(a.compareTo(border)>0) {
				System.out.println("INVALID["+gets+"]");
				continue;
			}
			T = Double.parseDouble(newstring);
			if((T<newTimeMax)||((!Pattern.matches(first_one, gets))&&i==0)) {
				System.out.println("INVALID["+gets+"]");
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
				System.out.println("INVALID["+gets+"]");
				continue;
			}
			T = Double.parseDouble(newstring);
			//System.out.println(T);
			if((T<newTimeMax)||i==0) {
				System.out.println("INVALID["+gets+"]");
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
				System.out.println("INVALID["+gets+"]");
				continue;
			}
			T = Double.parseDouble(newstring);
			if((T<newTimeMax)||i==0) {
				System.out.println("INVALID["+gets+"]");
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
			System.out.println("INVALID["+gets+"]");
			continue;
		}  
		newTimeMax = T;
		i++;//有效请求个数
		}/////////////////////////////////////////////////判断输入结束
		New_sch sch = new New_sch();
		sch.Schedule();
	}

}
