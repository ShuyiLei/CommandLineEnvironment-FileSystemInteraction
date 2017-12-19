package cs131.pa1.filter.concurrent;

public class PrintFilter extends ConcurrentFilter {
	public PrintFilter() {
		super();
	}
	
	public void process() {
		while(!isDone()) {
			try {
				processLine(input.take());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public String processLine(String line) {
		if(line!=stopper)
			System.out.println(line);
		else {
			done = true;

		}
		return null;
	}
}
