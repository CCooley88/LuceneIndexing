//CKulig BU CS 622 HW2 10/20
package lucene;

public class StopWatch {
	public StopWatch(int numberOfRuns) {
		this.numberOfRuns = numberOfRuns;
	}
	private long totalTime;
	private int numberOfRuns;
	private long start;
	
	public void start() {
		start = System.nanoTime();
	}
	
	public void stop() {
		totalTime+= System.nanoTime() - start;
	}
	
	public long averageTime() {
		return totalTime/numberOfRuns;
	}

}
