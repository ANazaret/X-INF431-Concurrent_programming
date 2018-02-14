package data;

import java.util.LinkedList;

import util.PixelBuffer;

public class TileDistorter implements MessageProcessor {
    private final PixelDistorter distorter;
    private final int threshold;

    public TileDistorter(int parallelism, PixelDistorter distorter,
            int threshold) {
        this.distorter = distorter;
        this.threshold = threshold;
    }

    public TileDistorter(int parallelism, PixelDistorter distorter) {
        this(parallelism, distorter, 64);
    }

    private Message processTile(TileMessage msg) {
        
    	
    	TileMessage newmsg = new TileMessage(msg.channel, new PixelBuffer(msg.buf.pixels.clone(), msg.buf.width, msg.buf.height), msg.x, msg.y, msg.w, msg.h);
    	
    	
    	
    	for (int x=msg.x; x<msg.x+msg.w; x++) {
    		for (int y=msg.y; y<msg.y+msg.h; y++) {
    			newmsg.buf.pixels[y * newmsg.buf.width + x] =distorter.process(newmsg.buf, x, y); 
    			
    		}
    	}
    	
        return newmsg;
    }

    @Override
    public Message process(Message msg) {
        if (msg instanceof TileMessage)
            return this.processTile((TileMessage) msg);
        else {
            throw new IllegalArgumentException("TileDistorter: invalid message");
        }
    }
    
    
    class Job extends java.util.concurrent.RecursiveAction {
    	private PixelBuffer buf;
    	private int x,y,w,h;
    	public Job(PixelBuffer buf, int x, int y, int w, int h) {
    		this.buf=buf;
    		this.x=x;
    		this.y=y;
    		this.w=w;
    		this.h=h;
    	}
		@Override
		protected void compute() {
			if (w*h > threshold) {
				LinkedList<Job> list = new LinkedList<>();
				
				list.push(new Job(buf, x, y, w/2, h/2));
				list.push(new Job(buf, x+w/2, y, w-w/2, h/2));
				list.push(new Job(buf, x, y+h/2, w/2, h-h/2));
				list.push(new Job(buf, x+w/2, y+h/2, w-w/2, h-h/2));
				
				invokeAll(list);
			} else {
				for (int i=x; i<x+w; i++) {
		    		for (int j=y; j<y+h; j++) {
		    			buf.pixels[j * buf.width+ i] =distorter.process(buf, i, j); 
		    			
		    		}
		    	}
			}
		}
    	
    }

}
