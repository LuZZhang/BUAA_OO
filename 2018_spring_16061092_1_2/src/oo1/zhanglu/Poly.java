package oo1.zhanglu;


public class Poly {
	public int[] terms = new int [1000000];//����
    public Poly add(Poly q) {
    	int i;
    	Poly p = new Poly();
    	for(i=0;i<1000000;i++) {
    		p.terms[i] = q.terms[i];
    	}
    	for(i=0;i<1000000;i++) {
    		if(terms[i]!=0) { //ֻҪϵ������0���͵ü���ȥ
				p.terms[i] = terms[i]+p.terms[i];
			}
    	}
    	return p;
    }
    public Poly sub(Poly q) {   
    	int i;
    	Poly p = new Poly();
    	for(i=0;i<1000000;i++) {
    		p.terms[i]=-q.terms[i];
    		if(terms[i]!=0) {
				p.terms[i] = terms[i]+p.terms[i];
			}
    	}
    	return p;
    }
	public void Polyset(int coeff, int degree) {
		// TODO Auto-generated method stub
		terms[degree] = coeff;
	}
}



