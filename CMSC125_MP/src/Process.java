
public class Process {
	private int[] parameters; //process ID, arrival, burst, prio
	private int start, end;
	private boolean first;
	private int timeQueued;
	
	public Process(int[] parameters, int start) {
		this.parameters = parameters;
		this.start = start;
		this.end = start;
		this.first = true;
		this.timeQueued = parameters[1];
	}
	
	public int getTimeQueued() {
		return timeQueued;
	}

	public void setTimeQueued(int timeQueued) {
		this.timeQueued = timeQueued;
	}
	
	public void setBurst (int burst) {
		this.parameters[2] = burst;
	}

	public void decrementBurst (int burst) {
		this.parameters[2] -= burst;
	}
	
	public void setParams (int[] parameters) {
		this.parameters = parameters;
	}
	
	public int[] getParams() {
		return this.parameters;
	}
	
	public void incrementPreemptiveBurst () {
		this.parameters[2] -= 1;
	}
	
	public int getStart() {
		return this.start;
	}
	
	public void setStart(int start) {
		this.start = start;
	}
		
	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public boolean isFirst() {
		return first;
	}

	public void setFirst(boolean first) {
		this.first = first;
	}

	public boolean earlierArrival (Process p2) {
		if (this.getParams()[1] < p2.getParams()[1]) {
			return true;
		}
		else if (this.getParams()[1] == p2.getParams()[1]) {
			return false;
		}
		else {
			return false;
		}
	}
	
	public boolean shorterBurst (Process p2) {
		if (this.getParams()[2] < p2.getParams()[2]) {
			return true;
		}
		else if (this.getParams()[2] == p2.getParams()[2]) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean equalBurst (Process p2) {
		if (this.getParams()[2] == p2.getParams()[2]) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean higherPriority (Process p2) {
		if (this.getParams()[3] < p2.getParams()[3]) {
			return true;
		}
		else if (this.getParams()[3] == p2.getParams()[3]) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean equalPriority (Process p2) {
		if (this.getParams()[3] == p2.getParams()[3]) {
			return true;
		}
		else {
			return false;
		}
	}
	
	@Override
    public Process clone() {
        Process cloned = null;
        try {
            cloned = (Process) super.clone();
            cloned.setParams(this.parameters.clone());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
 
        return cloned;
    }
}
