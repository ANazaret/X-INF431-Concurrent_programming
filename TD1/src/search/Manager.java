package search;
import java.io.BufferedReader;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.*;
import java.lang.Runtime;

public class Manager {
    public static LinkedBlockingQueue<String> simpleSearch(BufferedReader data, String query, int num)
        throws InterruptedException{
        LinkedBlockingQueue<String> result = new LinkedBlockingQueue<>();
        Searcher s = new Searcher(data, query, result);
        s.setName("searcher");
        s.start();
        Thread.sleep(100);
        s.printStatus();
        return result;
    }
    
    public static LinkedBlockingQueue<String> pollingSearch(BufferedReader data, String query, int num) 
    	throws InterruptedException{
            
    	LinkedBlockingQueue<String> result = new LinkedBlockingQueue<>();
        Searcher s = new Searcher(data, query, result);
        s.setName("searcher");
        s.start();
        while (s.isAlive()) {
        	Thread.sleep(100);
        	s.printStatus();	
        }
        
        s.printStatus();
        return result;
    	
    }
    
    public static LinkedBlockingQueue<String> waitingSearch(BufferedReader data, String query, int num)
            throws InterruptedException{
    	
            LinkedBlockingQueue<String> result = new LinkedBlockingQueue<>();
            Searcher s = new Searcher(data, query, result);
            s.setName("searcher");
            s.start();
            s.join();
            s.printStatus();
            return result;
        }
    
    
    public static LinkedBlockingQueue<String> pipelinedSearch(BufferedReader data, String query, int num)
            throws InterruptedException{
    	
            LinkedBlockingQueue<String> result = new LinkedBlockingQueue<>();
            Searcher s = new Searcher(data, query, result);
            Printer p = new Printer(result);
            s.setName("searcher");
            p.setName("printer");
            s.start(); p.start(); 
            s.join(); p.join();
            s.printStatus(); p.printStatus();
            return result;
        }
    
    public static LinkedBlockingQueue<String> interruptingSearch(BufferedReader data, String query, int num) throws InterruptedException{
    	
    	LinkedBlockingQueue<String> result = new LinkedBlockingQueue<>();
    	Searcher s = new Searcher(data, query, result);
    	Printer p = new Printer(result, num);
    	
    	s.setName("searcher");
    	p.setName("printer");
    	
    	s.start();
    	p.start();
    	
    	p.join();
    	s.interrupt();
    	s.join();
    	
    	s.printStatus(); p.printStatus();
        return result;
    	
    }
    
    
public static LinkedBlockingQueue<String> concurrentSearch(BufferedReader data, String query, int num) throws InterruptedException{
    	
    	LinkedBlockingQueue<String> result = new LinkedBlockingQueue<>();
    	Searcher s1 = new Searcher(data, query, result);
    	Searcher s2 = new Searcher(data, query, result);
    	Printer p = new Printer(result, num);
    	
    	s1.setName("searcher1");
    	s2.setName("searcher2");
    	p.setName("printer");
    	
    	s1.start();
    	s2.start();
    	p.start();
    	
    	p.join();
    	s1.interrupt();
    	s2.interrupt();
    	s1.join();
    	s2.join();
        
    	
    	s1.printStatus(); s2.printStatus(); p.printStatus();
        return result;
    	
    }

    public static LinkedBlockingQueue<String> search(BufferedReader data, String query, int num){
        try {
            //return simpleSearch(data, query, num);
        	//return pollingSearch(data, query, num);
        	//return waitingSearch(data, query, num);java.lang.Runtime.
        	//return pipelinedSearch(data, query, num);
        	//return interruptingSearch(data, query, num);
        	return concurrentSearch(data, query, num);
        	
        	
        } catch (InterruptedException e) {
            System.out.println("Search interrupted.");
            throw new RuntimeException("Unexpected search interruption");
        }
    }
    
    

    /*public static Map<String,Integer> count(BufferedReader data, String[] queries) throws InterruptedException {
        
    	LinkedBlockingQueue<String> result = new LinkedBlockingQueue<>();
    	MapFrequency mf1 = new MapFrequency(data, queries, result);
    	MapFrequency mf2 = new MapFrequency(data, queries, result);
    	ReduceFrequency rf = new ReduceFrequency(result);
    	
    	mf1.setName("map1");
    	mf2.setName("map2");
    	rf.setName("reduce");
    	
    	mf1.start();
    	mf2.start();
    	rf.start();
    	
    	mf1.join();
    	mf2.join();
    	rf.join();
    	
    	mf1.printStatus();
    	mf2.printStatus();
    	rf.printStatus();
    	
        return rf.count;
    }*/
    
    
public static Map<String,Integer> count(BufferedReader data, String[] queries) throws InterruptedException {
        
    	LinkedBlockingQueue<String> result = new LinkedBlockingQueue<>();
    	
    	
    	int nbProc = Runtime.getRuntime().availableProcessors();
    	int nbThread = Math.max(nbProc-1, 1);
    	MapFrequency[] mfs = new MapFrequency[nbThread];
    	
    	for (int i=0; i< nbThread; i++) {
    		mfs[i] =new MapFrequency(data, queries, result);
    		mfs[i].setName("map"+(i+1));
    		mfs[i].start();
    	}
    	
    	
    	ReduceFrequency rf = new ReduceFrequency(result);
    	rf.setName("reduce");
    	rf.start();
    	
    	for (int i=0; i< nbThread; i++) {
    		mfs[i].join();
    		mfs[i].printStatus();
    	}
    	
    	rf.join();
    	rf.printStatus();
    	
        return rf.count;
    }
}
