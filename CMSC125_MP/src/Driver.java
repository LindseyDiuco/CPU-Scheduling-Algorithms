// CPU Scheduling Driver

import java.io.*;
import java.util.*;

public class Driver {

	@SuppressWarnings("unchecked")

	public static void main(String[] args) throws FileNotFoundException {
		//inputReader
		System.out.println("CPU Scheduling Algorithm Calculator\n");
		//Read console input
		InputReader inputReader = new InputReader();
		inputReader.csvReader();
		ArrayList<Process> processes = inputReader.getProcess();
		int timeQuantum = inputReader.getTimeQuantum();
		
		//Algorithms

		ArrayList<Process> fcfs = (ArrayList<Process>) processes.clone();
		ArrayList<Process> sjf = (ArrayList<Process>) processes.clone();
		ArrayList<Process> srtf = (ArrayList<Process>) processes.clone();
		ArrayList<Process> nonPreemptivePriority = (ArrayList<Process>) processes.clone();
		ArrayList<Process> preemptivePriority = (ArrayList<Process>) processes.clone();
		ArrayList<Process> roundRobin = (ArrayList<Process>) processes.clone();
		
		int maxTime = 0;
		
		for (Process p : processes) {
			maxTime += p.getParams()[2];
		}
		
		//First Come First Serve (FCFS) CPU Scheduling
		System.out.println("\t\t\t\t\t1.) First Come First Serve (FCFS) CPU Scheduling\n");

		int time = 0;
		int end = 0;
		boolean new_process = true;
		ArrayList<Completion> fcfsTurns = new ArrayList<Completion>();
		
		while (!fcfs.isEmpty()) {
			if (time == end) {
				new_process = true;
			}
			if (new_process) {
				Process fcfs_process = fcfs.get(0);
				
				for (Process process : fcfs) {
					if (process.earlierArrival(fcfs_process)) {
						fcfs_process = process;
					}
				}
				
				if (fcfs_process.getParams()[1] <= time) {
					end = time + fcfs_process.getParams()[2];
					processes.get(fcfs_process.getParams()[0] - 1).setStart(time);
					processes.get(fcfs_process.getParams()[0] - 1).setEnd(end);
					fcfsTurns.add(new Completion (fcfs_process, time, end));
					fcfs.remove(fcfs_process);
					new_process = false;
				}
			}
			time++;
		}
		
		Display fcfsTable = new Display (processes);
		System.out.println(fcfsTable);
		
		System.out.println("\tGantt Chart:");
		System.out.print("\t"+ fcfsTurns.get(0));
		for (int i = 1; i < fcfsTurns.size(); i++) {
			if (fcfsTurns.get(i).getStart() != fcfsTurns.get(i-1).getEnd()) {
				System.out.print(fcfsTurns.get(i-1).getEnd() + " ");
			}
			System.out.print(fcfsTurns.get(i));
		}
		
		System.out.print(" " + fcfsTurns.get(fcfsTurns.size()-1).getEnd());
		System.out.print("\n-----------------------------------------------------------------------------------------------------------------------------------------\n");

		
		//Shortest Job First CPU Scheduling
		System.out.println("\n\n\t\t\t\t\t\t2.) Shortest Job First CPU Scheduling\n");

		time = 0;
		end = 0;
		new_process = true;
		ArrayList<Completion> sjfTurns = new ArrayList<Completion>();
		
		while (!sjf.isEmpty()) {
			if (time == end) {
				new_process = true;
			}
			if (new_process) {
				Process sjf_process = sjf.get(0);
				for (int i = 0; i < sjf.size(); i++) {
					if (sjf.get(i).getParams()[1] <= time) {
						sjf_process = sjf.get(i);
						break;
					}
				}
				
				for (Process process : sjf) {
					if (process.getParams()[1] <= time) {
						if (process.shorterBurst(sjf_process)) {
							if (process.equalBurst(sjf_process)) {
								if (process.getParams()[1] < sjf_process.getParams()[1]) {
									sjf_process = process;
								}
							}
							else {
								sjf_process = process;
							}
						}
					}
				}
				
				if (sjf_process.getParams()[1] <= time) { 
					end = time + sjf_process.getParams()[2];
					processes.get(sjf_process.getParams()[0] - 1).setStart(time);
					processes.get(sjf_process.getParams()[0] - 1).setEnd(end);
					sjfTurns.add(new Completion (sjf_process, time, end));
					sjf.remove(sjf_process);
					new_process = false;
				}
			}
			time++;
		}
		
		Display sjfTable = new Display(processes);
		System.out.println(sjfTable);
		
		System.out.println("\tGantt Chart:\n");
		System.out.print("\t" + sjfTurns.get(0));
		for (int i = 1; i < sjfTurns.size(); i++) {
			if (sjfTurns.get(i).getStart() != sjfTurns.get(i-1).getEnd()) {
				System.out.print(sjfTurns.get(i-1).getEnd() + " ");
			}
			System.out.print(sjfTurns.get(i));
		}
		System.out.print(" " + sjfTurns.get(sjfTurns.size()-1).getEnd());
		System.out.print("\n-----------------------------------------------------------------------------------------------------------------------------------------\n");

		
		//Shortest Remaining Time First (SRTF) CPU Scheduling
		System.out.println("\n\n\t\t\t\t\t3.) Shortest Remaining Time First (SRTF) CPU Scheduling\n");
		
		time = 0;
		boolean new_turn = true;
		boolean first = false;
		Process before = null;
		int tempStart = 0;
		int tempEnd = 0;
		int active = 0;
		ArrayList<Completion> srtfTurns = new ArrayList<Completion>();
		ArrayList<Integer> bursts = new ArrayList<Integer>();
		for (Process p : processes) {
			bursts.add(p.getParams()[2]);
		}
		Process srtf_process = null;
		
		while (!srtf.isEmpty()) {
			while (active == 0) { //check if there are processes active
				for (Process p : srtf) {
					if (p.getParams()[1] <= time) {
						active++;
					}
				}
				if (active == 0) {
					time++;
				}
			}
			active = 0;
			
			if (new_turn) {
				srtf_process = srtf.get(0);
				for (int i = 0; i < srtf.size(); i++) {
					if (srtf.get(i).getParams()[1] <= time) {
						srtf_process = srtf.get(i);
						break;
					}
				}
			}
			
			for (Process process : srtf) { //compare active process found with other active processes to find shortest burst
				if (process.getParams()[1] <= time) {
					if (process.shorterBurst(srtf_process) && !(process.equals(srtf_process))) {
						if (process.equalBurst(srtf_process)) {
							if (process.getTimeQueued() < srtf_process.getTimeQueued()) {
								srtf_process = process;
								new_turn = true;
							}
						}
						else {
							srtf_process = process;
							new_turn = true;
						}
					}
				}
			}
			
			if (new_turn) {
				if (first) { //save the previous turn
					before.setTimeQueued(time);
					Completion tempTurn = new Completion(before, tempStart, tempEnd);
					srtfTurns.add(tempTurn);
				}
				if (srtf_process.getParams()[1] <= time) { //if process is available, start the processing of the turn
					before = new Process (srtf_process.getParams(), time);
					tempEnd = time + 1;
					tempStart = time;
					if (processes.get(srtf_process.getParams()[0] - 1).isFirst()) { 
						processes.get(srtf_process.getParams()[0] - 1).setStart(time);
						processes.get(srtf_process.getParams()[0] - 1).setFirst(false);
					}
					new_turn = false;
					first = true;
				}
				srtf.get(srtf.indexOf(srtf_process)).incrementPreemptiveBurst();
				if (srtf.get(srtf.indexOf(srtf_process)).getParams()[2] == 0) { //if the process has finished, start a new turn and remove the process
					new_turn = true;
					processes.get(srtf_process.getParams()[0] - 1).setEnd(time+1);
					srtf.remove(srtf_process);
				}
			}
			else { //if it is still a running turn, increment the temporary end and the preemptive burst of the running process
				srtf.get(srtf.indexOf(srtf_process)).incrementPreemptiveBurst();
				if (srtf.get(srtf.indexOf(srtf_process)).getParams()[2] == 0) {
					new_turn = true;
					processes.get(srtf_process.getParams()[0] - 1).setEnd(time+1);
					srtf.remove(srtf_process);
					tempEnd++;
				}
				else {
					tempEnd++;
				}
			}
			
			time++;
		}
		
		Completion lastTurn = new Completion(before, tempStart, tempEnd);
		srtfTurns.add(lastTurn);
		
		for (Process p : processes) {
			p.setBurst(bursts.get(processes.indexOf(p)));
		}
		
		for (Process p : processes) {
			p.setTimeQueued(p.getParams()[1]);
		}
		
		Display srtfTable = new Display(processes);
		System.out.println(srtfTable);
				
		System.out.println("\tGantt Chart:\n");
		System.out.print("\t" + srtfTurns.get(0));
		for (int i = 1; i < srtfTurns.size(); i++) {
			if (srtfTurns.get(i).getStart() != srtfTurns.get(i-1).getEnd()) {
				System.out.print(srtfTurns.get(i-1).getEnd() + " ");
			}
			System.out.print(srtfTurns.get(i));
		}

		System.out.print(" " + srtfTurns.get(srtfTurns.size()-1).getEnd());
		System.out.print("\n-----------------------------------------------------------------------------------------------------------------------------------------\n");

		//Preemptive Priority CPU Scheduling
		System.out.println("\n\n\t\t\t\t\t\t4.) Priority (Preemptive) CPU Scheduling\n");
		
		time = 0;
		new_turn = true;
		first = false;
		before = null;
		tempStart = 0;
		tempEnd = 0;
		active = 0;
		ArrayList<Completion> preemptivePriorityTurns = new ArrayList<Completion>();

		Process preemptivePriority_process = null;
		
		while (!preemptivePriority.isEmpty()) {
			while (active == 0) { //check if there are processes active
				for (Process p : preemptivePriority) {
					if (p.getParams()[1] <= time) {
						active++;
					}
				}
				if (active == 0) {
					time++; //increment time until there is a process active
				}
			}
			active = 0;
			
			if (new_turn) { //find an active process
				preemptivePriority_process = preemptivePriority.get(0);
				for (int i = 0; i < preemptivePriority.size(); i++) {
					if (preemptivePriority.get(i).getParams()[1] <= time) {
						preemptivePriority_process = preemptivePriority.get(i);
						break;
					}
				}
			}
			
			for (Process p : preemptivePriority) { //compare active process found with other active processes to find shortest burst
				if (p.getParams()[1] <= time) {
					if (p.higherPriority(preemptivePriority_process) && !(p.equals(preemptivePriority_process))) {
						if (p.equalPriority(preemptivePriority_process)) {
							if (p.getTimeQueued() < preemptivePriority_process.getTimeQueued()) {
								preemptivePriority_process = p;
								new_turn = true;
							}
						}
						else {
							preemptivePriority_process = p;
							new_turn = true;
						}
					}
				}
			}
			
			if (new_turn) {
				if (first) {
					before.setTimeQueued(time);
					Completion tempTurn = new Completion(before, tempStart, tempEnd);
					preemptivePriorityTurns.add(tempTurn);
				}
				if (preemptivePriority_process.getParams()[1] <= time) {
					before = new Process (preemptivePriority_process.getParams(), time);
					tempEnd = time + 1;
					tempStart = time;
					if (processes.get(preemptivePriority_process.getParams()[0] - 1).isFirst()) {
						processes.get(preemptivePriority_process.getParams()[0] - 1).setStart(time);
						processes.get(preemptivePriority_process.getParams()[0] - 1).setFirst(false);
					}
					new_turn = false;
					first = true;
				}
				preemptivePriority.get(preemptivePriority.indexOf(preemptivePriority_process)).incrementPreemptiveBurst();
				if (preemptivePriority.get(preemptivePriority.indexOf(preemptivePriority_process)).getParams()[2] == 0) {
					new_turn = true;
					processes.get(preemptivePriority_process.getParams()[0] - 1).setEnd(time+1);
					preemptivePriority.remove(preemptivePriority_process);
				}
			}
			else {
				preemptivePriority.get(preemptivePriority.indexOf(preemptivePriority_process)).incrementPreemptiveBurst();
				if (preemptivePriority.get(preemptivePriority.indexOf(preemptivePriority_process)).getParams()[2] == 0) {
					new_turn = true;
					processes.get(preemptivePriority_process.getParams()[0] - 1).setEnd(time+1);
					preemptivePriority.remove(preemptivePriority_process);
					tempEnd++;
				}
				else {
					tempEnd++;
				}
			}
			
			time++;
		}
		
		Completion last_turn = new Completion(before, tempStart, tempEnd);
		preemptivePriorityTurns.add(last_turn);
		
		for (Process p : processes) {
			p.setBurst(bursts.get(processes.indexOf(p)));
		}
		
		Display preemptivePriorityTable = new Display(processes);
		System.out.println(preemptivePriorityTable);
		
		System.out.println("\tGantt Chart:\n");
		System.out.print("\t" + preemptivePriorityTurns.get(0));
		for (int i = 1; i < preemptivePriorityTurns.size(); i++) {
			if (preemptivePriorityTurns.get(i).getStart() != preemptivePriorityTurns.get(i-1).getEnd()) {
				System.out.print(preemptivePriorityTurns.get(i-1).getEnd() + " ");
			}
			System.out.print(preemptivePriorityTurns.get(i));
		}
		System.out.print(" " + preemptivePriorityTurns.get(preemptivePriorityTurns.size()-1).getEnd());
		
		for (Process process : processes) {
			process.setFirst(true);
		}
		
		for (Process p : processes) {
			p.setTimeQueued(p.getParams()[1]);
		}
		System.out.print("\n-----------------------------------------------------------------------------------------------------------------------------------------\n");

		//Non-preemptive Priority CPU Scheduling
		System.out.println("\n\n\t\t\t\t\t\t5.) Priority (Non-preemptive) CPU Scheduling\n");
		
		time = 0;
		end = 0;
		new_process = true;
		ArrayList<Completion> nonPreemptivePriorityTurns = new ArrayList<Completion>();
		
		while (time < maxTime) {
			if (time == end) {
				new_process = true;
			}
			if (new_process) {
				Process higherPrio = nonPreemptivePriority.get(0);
				for (int i = 0; i < nonPreemptivePriority.size(); i++) {
					if (nonPreemptivePriority.get(i).getParams()[1] <= time) {
						higherPrio = nonPreemptivePriority.get(i);
						break;
					}
				}
				
				for (Process process : nonPreemptivePriority) {
					if (process.getParams()[1] <= time) {
						if (process.higherPriority(higherPrio)) {
							if (process.equalPriority(higherPrio)) {
								if (process.getParams()[1] < higherPrio.getParams()[1]) {
									higherPrio = process;
								}
							}
							else {
								higherPrio = process;
							}
						}
					}
				}
				
				if (higherPrio.getParams()[1] <= time) {
					end = time + higherPrio.getParams()[2];
					processes.get(higherPrio.getParams()[0] - 1).setStart(time);
					processes.get(higherPrio.getParams()[0] - 1).setEnd(end);
					nonPreemptivePriorityTurns.add(new Completion (higherPrio, time, end));
					nonPreemptivePriority.remove(higherPrio);
					new_process = false;
				}
			}
			time++;
		}
		
		Display nonPreemptivePriorityTable = new Display(processes);
		System.out.println(nonPreemptivePriorityTable);
		
		System.out.println("\tGantt Chart:\n");
		System.out.print("\t" + nonPreemptivePriorityTurns.get(0));
		for (int i = 1; i < nonPreemptivePriorityTurns.size(); i++) {
			if (nonPreemptivePriorityTurns.get(i).getStart() != nonPreemptivePriorityTurns.get(i-1).getEnd()) {
				System.out.print(nonPreemptivePriorityTurns.get(i-1).getEnd() + " ");
			}
			System.out.print(nonPreemptivePriorityTurns.get(i));
		}
		System.out.print(" " + nonPreemptivePriorityTurns.get(nonPreemptivePriorityTurns.size()-1).getEnd());
		
		for (Process process : processes) {
			process.setFirst(true);
		}
		System.out.print("\n-----------------------------------------------------------------------------------------------------------------------------------------\n");

		
		//Round Robin CPU Scheduling
		System.out.println("\n\n\t\t\t\t\t\t\t6.) Round Robin CPU Scheduling\n");
		time = 0;
		end = 0;
		new_process = true;
		ArrayList<Completion> rrTurns = new ArrayList<Completion>();

		
		while (!roundRobin.isEmpty()) {
			if (time == end) {
				new_process = true;
			}
			if (new_process) {
				Process first_come = roundRobin.get(0);
				
				for (Process p : roundRobin) {
					if (p.getTimeQueued() < first_come.getTimeQueued()) {
						first_come = p;
					}
				}
				
				if (first_come.getParams()[1] <= time) {
					if (first_come.getParams()[2] <= timeQuantum) {
						end = time + first_come.getParams()[2];
						if (processes.get(first_come.getParams()[0] - 1).isFirst()) {
							processes.get(first_come.getParams()[0] - 1).setStart(time);
							processes.get(first_come.getParams()[0] - 1).setFirst(false);
						}
						processes.get(first_come.getParams()[0] - 1).setEnd(end);
						rrTurns.add(new Completion (first_come, time, end));
						roundRobin.remove(first_come);
						new_process = false;
					}
					else {
						end = time + timeQuantum;
						rrTurns.add(new Completion(first_come, time, end));
						if (processes.get(first_come.getParams()[0] - 1).isFirst()) {
							processes.get(first_come.getParams()[0] - 1).setStart(time);
							processes.get(first_come.getParams()[0] - 1).setFirst(false);
						}
						processes.get(first_come.getParams()[0] - 1).decrementBurst(timeQuantum);
						processes.get(first_come.getParams()[0] - 1).setTimeQueued(end);
						new_process = false;
					}
				}
			}
			time++;
		}
		
		for (Process p : processes) {
			p.setBurst(bursts.get(processes.indexOf(p)));
		}
		
		Display rrTable = new Display(processes);
		System.out.println(rrTable);
		
		System.out.println("\tGantt Chart:\n");
		System.out.print("\t" + rrTurns.get(0));
		for (int i = 1; i < rrTurns.size(); i++) {
			if (rrTurns.get(i).getStart() != rrTurns.get(i-1).getEnd()) {
				System.out.print(rrTurns.get(i-1).getEnd() + " ");
			}
			System.out.print(rrTurns.get(i));
		}
		System.out.print(" " + rrTurns.get(rrTurns.size()-1).getEnd());
		System.out.print("\n-----------------------------------------------------------------------------------------------------------------------------------------\n");


	}

}
