package search;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Pattern;


public class MapFrequency extends Thread {
    private BufferedReader data;
    private String[] queries;
    private Pattern[] patterns;
    private LinkedBlockingQueue<String> result;
    private int processedItems;
    private int matchesFound;
    static public final String EOF = "--EOF--";
    
    public MapFrequency(BufferedReader data, String[] queries, LinkedBlockingQueue<String> result) {
      this.data = data;
      this.queries = queries;
      this.result = result;
      
      processedItems = 0;
      matchesFound = 0;
      
      patterns = new Pattern[queries.length];
      
      for (int i =0; i< queries.length; i++) {
    	  patterns[i] = Pattern.compile(queries[i]);
      }
    }
    
    

    public void printStatus() {
    	System.out.format("Thread \"%s\" processed %d items and found %d matching results.\n",
                this.getName(), this.processedItems, this.matchesFound);
    }

    @Override
    public void run() {
    	String line;
    	
    	try {
    		line = data.readLine();
    	}
    	catch (IOException e) {
			result.add(EOF);
			return;
		}
    	while (line != null && !Thread.interrupted()) {
    		/*for (String q : queries) {
    			if (line.indexOf(q) >= 0) {
    				result.add(q);
    				matchesFound += 1;
    			}
    		}*/
    		
    		for (Pattern p : patterns) {
    			if (p.matcher(line).matches()) {
    				result.add(p.toString());
    				matchesFound += 1;
    			}
    		}
    		
    		processedItems ++;
    		
    		try {
				line = data.readLine();
			} catch (IOException e) {
				break;
			}
    	}
    	

		result.add(EOF);
    }
}
