#include <stdio.h>
#define N 20
//字符串s按c字符分隔成小字符串t 
int split(char *s,char t[][20],char c){
    int i=0,k=0,ii;
    while(s[i]!=0){
        while(s[i]==c && s[i]!=0) i++;
        if(s[i]==0) break;
        ii=i;
        while(s[i]!=c && s[i]!=0){
            t[k][i-ii]=s[i];
            i++;
        } 
        t[k][i-ii]=0;
        k++;
    }
    return k;
}
//输入多项式 
void input(int a[],int n){
    char s[200]="1 2 3 4";
    char t[N][20];
    int m,i;
    printf("请在一行内输入多项式各系数\n");
    fflush(stdin);//清除输入缓冲 
    gets(s);
    m=split(s,t,' ');//把s以' '为分隔为数组 
    for(i=0;i<n-m;i++) a[i]=0;
    for(i=n-m;i<n;i++){
        sscanf(t[i-n+m],"%d",&a[i]);
    }
}
 
//输出多项式 
void printit(int a[],int n){
    int i=0;
    while(a[i]==0 && i<n-1) i++;
    for(;i<n;i++)
        printf("%d  ",a[i]);
}
 
/*void menu(){  //显示主菜单 
    printf("============================================\n");
    printf("1.多项式加法\n");
    printf("2.多项式减法\n");
    printf("3.多项式乘法\n");
    printf("0.退出\n");
    printf("请选择：");
}*/
//多项式相加
void addit(int a[],int b[],int c[],int n){
    int i;
    for(i=0;i<2*n;i++) c[i]=0;
    for(i=0;i<n;i++)
        c[n+i]=a[i]+b[i];
}
/*多项式相减 
void subit(float a[],float b[],float c[],int n){
    int i;
    for(i=0;i<2*n;i++) c[i]=0;
    for(i=0;i<n;i++)
        c[n+i]=a[i]-b[i];
}
//多项式相乘 
void mulit(float a[],float b[],float c[],int n){
    int i,j;
    for(i=0;i<2*n;i++) c[i]=0;
    for(i=0;i<n;i++)
    for(j=0;j<n;j++)
        c[i+j+1]+=a[i]*b[j];
}*/
int main(){
    int a[N],b[N],c[N*2];
    //int x;
    //N=20;
    //printf("多项式运算，运算项数<=%d项\n",N);
    //while(1){
        //menu();
        fflush(stdin);//清除输入缓冲 
        //scanf("%d",&x);
        //switch(x){
            //case 1:
                printf("输入第一个多项式，");
                input(a,N);
                printf("输入第二个多项式，");
                input(b,N);
                addit(a,b,c,N);  //多项式相加 
                printf("相加的结果是:\n");
                printit(c,2*N);  //输出结果 
                printf("\n");
                //break;
            /*case 2:
                printf("输入第一个多项式，");
                input(a,N);
                printf("输入第二个多项式，");
                input(b,N);
                subit(a,b,c,N);  //多项式相减 
                printf("相减的结果是:\n");
                printit(c,2*N);  //输出结果 
                printf("\n");
                break;
            case 3:
                printf("输入第一个多项式，");
                input(a,N);
                printf("输入第二个多项式，");
                input(b,N);
                mulit(a,b,c,N);  //多项式相乘 
                printf("相乘的结果是:\n");
                printit(c,2*N);  //输出结果 
                printf("\n");
                break;
        }*/ 
        //if (x==0) break;
    //}
}
