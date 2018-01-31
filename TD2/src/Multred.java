public class Multred extends Thread {
	int k;
	int pos;
	int a[][];

	Multred(int pos, int[][] a, int k) {
		this.pos = pos;
		this.a = a;
		this.k = k;
	}
  
  
 
	public void run() {
		int next;
		for (int i=1; i<=k; i++) {
			next = pos + (1<<(i-1));
			System.out.println(String.format("Thread %d : valeur %d, etape %d, next %d a.length %d", pos,a[pos][0],i,next, a.length));
			if (next<a.length) {
				//On attend
				while (a[next][i-1]==0) {System.out.println(String.format("Thread %d attends le %d a l'etape %d car %d", pos, next, i-1, a[next][i-1]));}
					
				
				a[pos][i] = a[pos][i-1]*a[next][i-1];
			} else {
				a[pos][i] = a[pos][i-1];
			}
			
			
		}
		
	}
  
}