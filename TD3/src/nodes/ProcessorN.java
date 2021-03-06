package nodes;

import data.MessageProcessor;
import data.MessageQueue;

public class ProcessorN extends Processor {

	
	private int numThreads;
		
    public ProcessorN(int numThreads, MessageQueue q, MessageProcessor p,
            String name) {
        super(q, p, name);
    
        this.numThreads = numThreads;
    }

    @Override
    public void init() {
    	for (int i=0; i<numThreads; i++) {
    		new Thread(this).start();
    	}
        
        
    }
}
