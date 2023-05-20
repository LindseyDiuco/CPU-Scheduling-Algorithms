import java.text.DecimalFormat;
import java.util.ArrayList;

public class Display {
	private ArrayList<Process> processes;
	
	public Display (ArrayList<Process> processes) {
		this.processes = processes;
	}
	
	public String toString() {
		String toPrint = "";
		int completionTime, responseTime, waitingTime, turnAroundTime = 0;
		double sumResponse = 0;
		double sumWaiting = 0;
		double sumTurnaround = 0;
		int count = 0;
		toPrint += "-----------------------------------------------------------------------------------------------------------------------------------------\n";
		toPrint += "|\tProcess\t\t|\tCompletion Time\t\t|\tResponse Time\t|\tWaiting Time\t|\tTurnaround Time\t\t|\n";
		toPrint += "-----------------------------------------------------------------------------------------------------------------------------------------\n";

		for (Process process : processes) {
			completionTime = process.getEnd();
			responseTime = process.getStart() - process.getParams()[1];
			turnAroundTime = process.getEnd() - process.getParams()[1];
			waitingTime = responseTime + (turnAroundTime - responseTime - process.getParams()[2]);
			sumResponse += responseTime;
			sumWaiting += waitingTime;
			sumTurnaround += turnAroundTime;
			count++;
			
			toPrint += "|\tP" + process.getParams()[0] + "\t\t|\t" + completionTime + "\t\t\t|\t" + responseTime + "\t\t|\t" + waitingTime + "\t\t|\t" + turnAroundTime + "\t\t\t|\n";
		}
		DecimalFormat df = new DecimalFormat("#.####");
		double aveResponse = Double.parseDouble(df.format(sumResponse/count));
		double aveWaiting = Double.parseDouble(df.format(sumWaiting/count));
		double aveTurnaround = Double.parseDouble(df.format(sumTurnaround/count));

		toPrint += "-----------------------------------------------------------------------------------------------------------------------------------------\n";
		toPrint += "|\tAverage\t\t|\t\t\t\t|\t" + aveResponse + "\t\t|\t" + aveWaiting + "\t\t|\t" + aveTurnaround + "\t\t\t|\n";
		toPrint += "-----------------------------------------------------------------------------------------------------------------------------------------";

		return toPrint;
	}
	
}
