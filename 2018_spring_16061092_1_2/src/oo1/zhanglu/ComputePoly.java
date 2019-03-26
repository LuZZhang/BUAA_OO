package oo1.zhanglu;

import java.util.Scanner;
import java.util.ArrayList;  
import java.util.List;  
import java.util.regex.*;

public class ComputePoly {
	private static Poly[] polyList = new Poly[22];///////???????????�ĳ��˾�̬
	private static int opList[] = new int[22];//��ADD=1��SUB=-1;
	private static int num;
	private static Scanner input;
	private void compute() {
		//Poly p = polyList[0];//
		//Poly p1 = polyList[0];//
		Poly p = new Poly();
		Poly p1 = new Poly();
		Poly p2 = new Poly();
		int op =0;
		for(int i=0;i<num;i++) {///int i=1;i<num;i++
			p2 = polyList[i];
			op = opList[i];
			if(op==1) p1=p.add(p2);
			if(op==-1) p1=p.sub(p2);
			p = p1;
		}
		//������յõ��Ķ���ʽp
		//����õ����ǿն���ʽ�������0
		int judge = 0;
		for(int i=0;i<1000000;i++) {
			if(p.terms[i]!=0) {
				judge = 1;
			}
		}
		if(judge ==0) {
			System.out.println("0");
			System.exit(0);
		}
		System.out.print("{");
		int j=0;
		for(int i=0;i<1000000;i++) {
			if(p.terms[i]!=0) {
				if(j!=0) System.out.print(",");
				j++;
				System.out.print("("+p.terms[i]+","+i+")");
			}
		}
		System.out.println("}");
	}
	
	/** 
     * ʹ��������ʽ��ȡС�����е����� 
     * @param msg 
     * @return  
     */  
    public static List<String> extractMessageByRegular(String msg,String regx){  
        
        List<String> list=new ArrayList<String>();  
        Pattern p = Pattern.compile(regx); //����ƥ��С�����м���ַ�������ÿһ�
        Matcher m = p.matcher(msg);  
        while(m.find()){  
            list.add(m.group().substring(1, m.group().length()-1));  
        }  
        return list;  
    }  
	public static void main(String[] args) {
		input = new Scanner(System.in);
		String getsIni = input.nextLine();//getsIniΪ����ļ���ʽ�����������ո�
		String gets = getsIni.replaceAll(" ","");//������пո�
		String eg1 = "^([+-]?[{][(][+-]?[0-9]+[,][+-]?[0-9]+[)]([,][(][+-]?[0-9]+[,][+-]?[0-9]+[)])*[}]([+-][{][(][+-]?[0-9]+[,][+-]?[0-9]+[)]([,][(][+-]?[0-9]+[,][+-]?[0-9]+[)])*[}])*)+$";
		boolean isMatch = Pattern.matches(eg1,gets);
		if(isMatch==false) {
			System.out.println("ERROR");
			System.exit(0);
		}
		
		List<String> list2 = extractMessageByRegular(gets,"[}][+-][{]"); 
		//��ȡ����ʽ֮����������
		//opList[0]=1;
		String eg2 = "^([-].+)+$";
		boolean isMatch2 = Pattern.matches(eg2, gets);
		if(isMatch2==true) {
			opList[0]=-1;
		}
		else opList[0]=1;
		if(list2.size()>19) {
			System.out.println("ERROR");
			System.exit(0);
		}
		for (int i = 0;i < list2.size();i++) {
			String sss = list2.get(i);
			char c;
			c = sss.charAt(0);
			if(c=='+')  opList[i+1]=1;
			if(c=='-') opList[i+1]=-1;
		}
		//System.out.println(list2.size());////////////////////////
		//������ʽ��+��-����
		String[] eachPoly = gets.split("[}][+-][{]");
		int i = 0;//����ָ������ʽ�ĸ���
		//Ϊ����ʽ��ֵ
		for(String s:eachPoly){
			//if()
            List<String> list = extractMessageByRegular(s,"[(][+-]?[0-9]+[,][+-]?[0-9]+[)]");  //(([+]?[0-9]+)|(-0))
            //System.out.println("ok");//////////////////
            if(list.size()>50) {
            	System.out.println("ERROR");
    			System.exit(0);
            }
            Poly p = new Poly();
            int[] biaoji = new int[1000000];
            for (int j = 0; j < list.size(); j++) {  
                //System.out.println(list.get(j));  
                String[] eachterms = list.get(j).split("[,]");
                int k =0;
                int coeff=0,degree=0;
                //ERROR��һ������ʽ�ж���ʽ�ж�γ���ͬһָ��
                for (String ss:eachterms) {
                	if(ss.charAt(0)=='+'||ss.charAt(0)=='-') {
                		if(ss.length()>7) {
                			//System.out.println(ss);
                    		System.out.println("ERROR");
                			System.exit(0);
                    	}
                	}
                	else {
                		if(ss.length()>6) {
                    		System.out.println("ERROR");
                			System.exit(0);
                    	
                    	}
                	}
                	if(k==0) coeff=Integer.parseInt(ss);//���Ǿ���ĳһ���j�����ϵ��//System.out.print("ϵ����"+s);
                	else {
                		degree=Integer.parseInt(ss);//�����ĳһ���j�����ָ��//System.out.println("ָ����"+s);
                	}
                	k++;
                }
                //ϵ����ָ��������Χ����
                if(coeff>999999||coeff<-999999||degree<0||degree>999999) {
                	System.out.println("ERROR");
        			System.exit(0);
                }
                if(biaoji[degree]==1) {
                	System.out.println("ERROR");
        			System.exit(0);
                }
                biaoji[degree]=1;
                p.Polyset(coeff,degree);
                polyList[i]=p;
            } 

			i++;
        }
		num=i;//����ʽ������
		ComputePoly cp = new ComputePoly();
		cp.compute();
	}
	 

}
