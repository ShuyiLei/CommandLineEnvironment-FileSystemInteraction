package cs131.pa1.filter.concurrent;
import java.util.concurrent.LinkedBlockingQueue;

import cs131.pa1.filter.Filter;


public abstract class ConcurrentFilter extends Filter implements Runnable{
	
	protected LinkedBlockingQueue<String> input;
	protected LinkedBlockingQueue<String> output;
	protected static String stopper = "&";
	protected boolean done = false;
	
	@Override
	public void setPrevFilter(Filter prevFilter) {
		prevFilter.setNextFilter(this);
	}
	
	@Override
	public void setNextFilter(Filter nextFilter) {
		if (nextFilter instanceof ConcurrentFilter){
			ConcurrentFilter sequentialNext = (ConcurrentFilter) nextFilter;
			this.next = sequentialNext;
			sequentialNext.prev = this;
			if (this.output == null){
				this.output = new LinkedBlockingQueue<String>();
			}
			sequentialNext.input = this.output;
		} else {
			throw new RuntimeException("Should not attempt to link dissimilar filter types.");
		}
	}
	
	public Filter getNext() {
		return next;
	}
	
	public void process(){
		while (true){
			try {
				String line = input.take();
				if(line == stopper) {
					output.put(stopper);
					done = true;
					return;
				}
				String processedLine = processLine(line);
				if (processedLine != null){
					output.put(processedLine);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}	
	}
	
	@Override
	public boolean isDone() {
		return done;
	}
	
	protected abstract String processLine(String line);
	
	@Override
	public void run() {
		process();
	}
	
}
