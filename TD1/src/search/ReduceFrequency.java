package search;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

public class ReduceFrequency extends Thread {
	public static final String EOF = "--EOF--";
	private LinkedBlockingQueue<String> in;
    public Map<String,Integer> count;
    private int items;

    public ReduceFrequency(LinkedBlockingQueue<String> in) {
    	this.in= in;
    	count = new HashMap<>();
    	items = 0;
    }

    public void printStatus() {
    	System.out.format("Thread \"%s\" processed %d items\n",
                this.getName(), this.items);
    }

    @Override
    public void run() {
    	String match;
    	
    	try {
    		match = in.take();
		} catch (InterruptedException e) {
			return;
		}
    	
    	while (!match.equals(EOF) ) {
    		try {

    			//Count
    			if (count.containsKey(match)) 
					count.replace(match, count.get(match) + 1);
				else 
					count.put(match, 1);
				
				items ++;
				match = in.take();
				
			} catch (InterruptedException e) {
				break;
			}
    		
    	} 
    }
}
