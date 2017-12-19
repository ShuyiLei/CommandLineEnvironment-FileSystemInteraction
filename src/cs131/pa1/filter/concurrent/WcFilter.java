package cs131.pa1.filter.concurrent;

public class WcFilter extends ConcurrentFilter {
	private int linecount;
	private int wordcount;
	private int charcount;
	
	public WcFilter() {
		super();
	}
	
	public void process() {
		while (true){
			try {
				String line = input.take();
				if(line == stopper) {
					done = true;
					output.put(processLine(""));
					output.put(stopper);
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
	
	public String processLine(String line) {
		//prints current result if ever passed a null
		if(line == null) {
			return linecount + " " + wordcount + " " + charcount;
		}
		
		if(isDone()) {
			return linecount + " " + wordcount + " " + charcount;
//			if(emptyIn) {
//				return "0 0 0";
//			}
//			String[] wct = line.split(" ");
//			wordcount += wct.length;
//			String[] cct = line.split("|");
//			charcount += cct.length;
//			return ++linecount + " " + wordcount + " " + charcount;
		} else {
			linecount++;
			String[] wct = line.split(" ");
			wordcount += wct.length;
			String[] cct = line.split("|");
			charcount += cct.length;
			return null;
		}
	}
}
