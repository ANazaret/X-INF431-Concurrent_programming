package search;

import java.util.concurrent.LinkedBlockingQueue;

public class Printer extends Thread {
    LinkedBlockingQueue<String> in;
    int items;
    int num;
    public static final String EOF = "--EOF--";

    public Printer(LinkedBlockingQueue<String> in) {
    	items = 0;
    	this.in = in;
    	num = Integer.MAX_VALUE;
    }
    public Printer(LinkedBlockingQueue<String> in, int num) {
    	items = 0;
    	this.in = in;
    	this.num = num;
    }
    public void printStatus() {
    	System.out.format("Thread \"%s\" processed %d items.\n",
                this.getName(), this.items);
    }
    
   
    
    public void run() {
    	String line;
    	
    	try {
			line = in.take();
		} catch (InterruptedException e) {
			return;
		}
    	
    	while (!line.equals(EOF) && items < num) {
    		try {
				System.out.println(line);
				items ++;
				line = in.take();
			} catch (InterruptedException e) {
				break;
			}
    		
    	} 
    	
    }
}