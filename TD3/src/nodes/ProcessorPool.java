package nodes;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import data.Message;
import data.MessageProcessor;
import data.MessageQueue;

public class ProcessorPool extends Processor {

	private final ExecutorService exec;
	
	
    public ProcessorPool(int concur, MessageQueue q, MessageProcessor p,
            String name) {
        super(q, p, name);
        this.exec = Executors.newFixedThreadPool(concur);
        
    }

    @Override
    public void run() {
        
    	while (true) {
    		Message msg = queue.remove();
    		exec.execute( new Runnable() {
				
				@Override
				public void run() {
					processMessage(msg);				
				}
			});
    	}
    }

}
