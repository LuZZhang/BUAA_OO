#include <stdio.h>
#define N 20
//�ַ���s��c�ַ��ָ���С�ַ���t 
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
//�������ʽ 
void input(int a[],int n){
    char s[200]="1 2 3 4";
    char t[N][20];
    int m,i;
    printf("����һ�����������ʽ��ϵ��\n");
    fflush(stdin);//������뻺�� 
    gets(s);
    m=split(s,t,' ');//��s��' 'Ϊ�ָ�Ϊ���� 
    for(i=0;i<n-m;i++) a[i]=0;
    for(i=n-m;i<n;i++){
        sscanf(t[i-n+m],"%d",&a[i]);
    }
}
 
//�������ʽ 
void printit(int a[],int n){
    int i=0;
    while(a[i]==0 && i<n-1) i++;
    for(;i<n;i++)
        printf("%d  ",a[i]);
}
 
/*void menu(){  //��ʾ���˵� 
    printf("============================================\n");
    printf("1.����ʽ�ӷ�\n");
    printf("2.����ʽ����\n");
    printf("3.����ʽ�˷�\n");
    printf("0.�˳�\n");
    printf("��ѡ��");
}*/
//����ʽ���
void addit(int a[],int b[],int c[],int n){
    int i;
    for(i=0;i<2*n;i++) c[i]=0;
    for(i=0;i<n;i++)
        c[n+i]=a[i]+b[i];
}
/*����ʽ��� 
void subit(float a[],float b[],float c[],int n){
    int i;
    for(i=0;i<2*n;i++) c[i]=0;
    for(i=0;i<n;i++)
        c[n+i]=a[i]-b[i];
}
//����ʽ��� 
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
    //printf("����ʽ���㣬��������<=%d��\n",N);
    //while(1){
        //menu();
        fflush(stdin);//������뻺�� 
        //scanf("%d",&x);
        //switch(x){
            //case 1:
                printf("�����һ������ʽ��");
                input(a,N);
                printf("����ڶ�������ʽ��");
                input(b,N);
                addit(a,b,c,N);  //����ʽ��� 
                printf("��ӵĽ����:\n");
                printit(c,2*N);  //������ 
                printf("\n");
                //break;
            /*case 2:
                printf("�����һ������ʽ��");
                input(a,N);
                printf("����ڶ�������ʽ��");
                input(b,N);
                subit(a,b,c,N);  //����ʽ��� 
                printf("����Ľ����:\n");
                printit(c,2*N);  //������ 
                printf("\n");
                break;
            case 3:
                printf("�����һ������ʽ��");
                input(a,N);
                printf("����ڶ�������ʽ��");
                input(b,N);
                mulit(a,b,c,N);  //����ʽ��� 
                printf("��˵Ľ����:\n");
                printit(c,2*N);  //������ 
                printf("\n");
                break;
        }*/ 
        //if (x==0) break;
    //}
}
