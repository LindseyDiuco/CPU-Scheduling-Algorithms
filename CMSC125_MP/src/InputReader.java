import java.io.*;
import java.util.*;

public class InputReader {
	ArrayList<Process> processes = new ArrayList<Process>();
	int timeQuantum;
	

	@SuppressWarnings("resource")
	public void csvReader() throws FileNotFoundException {
		System.out.print("Enter the absolute csv file path:\n> ");
		Scanner input = new Scanner(System.in);
		String path = input.nextLine();
		File file = new File(path);
		
		if (!file.isFile()) {
			System.out.println("The file does not exist.\n");
		}
		else if (!file.exists() || !path.substring(path.length()-3).equals("csv")) {
			System.out.println("This is not a csv file.\n");
			System.exit(0);
		}
		else {
			System.out.println("\nReading the CSV file...\n");
			Scanner sc = new Scanner(new File(path));
			String line;
			
			while (sc.hasNext()) {
				line = sc.nextLine();
				String[] line_entries = line.split(",");
				int[] int_entries = new int[line_entries.length];
				for (int i = 0; i < line_entries.length; i++) {
					try{
						int_entries[i] = Integer.parseInt(line_entries[i]);
					}
					catch(NumberFormatException e){
						System.out.println("The csv file input has a non-integer character.");
						e.printStackTrace();
					}
				}
				processes.add(new Process(int_entries, 0));
			}
			timeQuantumScanner();
		}
	}
	
	@SuppressWarnings("resource")
	public void timeQuantumScanner() {
		System.out.print("Enter time quantum (for Round Robin Scheduling):\n> ");
		Scanner input = new Scanner(System.in);
		timeQuantum = input.nextInt();
		System.out.println();
	}
	
	//setter
	public void setProcess(ArrayList<Process> processes) {
		this.processes = processes;
	}
	
	public void setTimeQuantum(int timeQuantum) {
		this.timeQuantum = timeQuantum;
	}
	
	//getter
	public ArrayList<Process> getProcess() {
		return processes;
	}
	
	public int getTimeQuantum() {
		return timeQuantum;
	}
}


