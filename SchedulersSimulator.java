import java.util.*;

class Process {
    String name;
    String color;
    int arrivalTime;
    int burstTime;
    int priorityNumber;
    public Process(String name, String color, int arrivalTime, int burstTime, int priorityNumber) {
        this.name = name;
        this.color = color;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priorityNumber = priorityNumber;
    }
    void getProcessInfo(){
        System.out.println("Enter the process name: ");
        Scanner scanner = new Scanner(System.in);
        name = scanner.nextLine();
        System.out.println("Enter the process color: ");
        color = scanner.nextLine();
        System.out.println("Enter the process arrival time: ");
        arrivalTime = scanner.nextInt();
        System.out.println("Enter the process burst time: ");
        burstTime = scanner.nextInt();
        System.out.println("Enter the process priority number: ");
        priorityNumber = scanner.nextInt();
    }
}

public class SchedulersSimulator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of processes: ");
        int numProcesses = scanner.nextInt();
        System.out.print("Enter the Round Robin Time Quantum: ");
        int timeQuantum = scanner.nextInt();
        System.out.print("Enter context switching time: ");
        int contextSwitching = scanner.nextInt();
        ArrayList<Process> processes = new ArrayList<>();
        for (int i = 0; i < numProcesses; i++) {
            Process process = new Process("", "", 0, 0, 0);
            process.getProcessInfo();
            processes.add(process);
        }
    }
}
