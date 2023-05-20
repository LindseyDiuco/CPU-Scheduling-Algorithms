
public class Completion {
	private Process process;
	private int start, end;
	
	public Completion(Process p, int start, int end) {
		this.process = p;
		this.start = start;
		this.end = end;
	}
	
	public int getStart() {
		return this.start;
	}
	
	public int getEnd() {
		return this.end;
	}
	
	public void incrementEnd () {
		this.end++;
	}
	
	public Process getProcess () {
		return this.process;
	}
	
	public void setProcess (Process process) {
		this.process = process;
	}

	public String toString() {
		String toPrint = "";
		toPrint += " " + start + " ";

		toPrint += "[ " + "P" + process.getParams()[0] + " ]";

		return toPrint;
	}
}
