import java.util.*;

public class Recurrence {
	public static void main(String[] args) {
		//int k = Integer.parseInt(args[0]);
		int k = 4;
		int i,j;
		int n=1<<k;
		// test somme de n entiers tirés aléatoirement
		Random r = new Random();
		int [][] a = new int[n][k+1];
		
		for (i=0; i<n; i++) {
			for (j=0; j<k; j++) {
				a[i][j] = 0;
			}
			a[i][0] = i%4+1;
		}
		
		for (i=0; i<n; i++) {
			a[i][0] = i%4+1;
		}
		
		for (i=0; i<n; i++) {
			new Multred(i, a, k).start();;
		}
		
		for (i=0; i<n; i++) {
			while(a[i][k-1] ==0) {};
		}
		
		i=2;
		
		System.out.println(a[n-1][k]);
		//test y(i+1)=2y(i)+a(i) où a est un tableau aléatoire
		
	}
}