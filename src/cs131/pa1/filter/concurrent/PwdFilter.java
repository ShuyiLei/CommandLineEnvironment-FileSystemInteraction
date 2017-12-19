package cs131.pa1.filter.concurrent;

public class PwdFilter extends ConcurrentFilter {
	public PwdFilter() {
		super();
	}
	
	public void process() {
		try {
			output.put(processLine(""));
			output.put(stopper);
			done = true;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	public String processLine(String line) {
		return ConcurrentREPL.currentWorkingDirectory;
	}
}
