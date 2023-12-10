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
        System.out.print("Enter the process name: ");
        Scanner scanner = new Scanner(System.in);
        name = scanner.nextLine();
        System.out.print("Enter the process color: ");
        color = scanner.nextLine();
        System.out.print("Enter the process arrival time: ");
        arrivalTime = scanner.nextInt();
        System.out.print("Enter the process burst time: ");
        burstTime = scanner.nextInt();
        System.out.print("Enter the process priority number: ");
        priorityNumber = scanner.nextInt();
    }
}

//Non-Preemptive Shortest-Job First (SJF) (using context switching)
class SJF{
    ArrayList<Process> processes;
    int contextSwitching;
    public SJF(ArrayList<Process> processes, int contextSwitching) {
        this.processes = processes;
        this.contextSwitching = contextSwitching;
    }
    //Sort the processes according to their arrival time and if equal then according to their burst time
    void sortProcesses(){
        Collections.sort(processes, new Comparator<Process>() {
            @Override
            public int compare(Process o1, Process o2) {
                if(o1.arrivalTime == o2.arrivalTime){
                    return o1.burstTime - o2.burstTime;
                }
                return o1.arrivalTime - o2.arrivalTime;
            }
        });
    }
    void execute(){
        sortProcesses();
        System.out.println("----------Processes execution order----------");
        for(int i = 0; i < processes.size(); i++){
            System.out.print(processes.get(i).name+" ");
        }
        System.out.println();
        ArrayList<Integer>processesWaitingTime = new ArrayList<>();
        ArrayList<Integer>processesTurnaroundTime = new ArrayList<>();
        //print the waiting time for each process
        System.out.println("----------Waiting time for each process----------");
        int time = 0;
        for(int i = 0; i < processes.size(); i++){
            System.out.print((time - processes.get(i).arrivalTime)+" ");
            processesWaitingTime.add(time - processes.get(i).arrivalTime);
            time += processes.get(i).burstTime + contextSwitching;
        }
        //print the turnaround time for each process
        //turnaround time = waiting time + burst time
        System.out.println();
        System.out.println("----------Turnaround time for each process----------");
        time = 0;
        for(int i = 0; i < processes.size(); i++){
            System.out.print((time - processes.get(i).arrivalTime + processes.get(i).burstTime)+" ");
            processesTurnaroundTime.add(time - processes.get(i).arrivalTime + processes.get(i).burstTime);
            time += processes.get(i).burstTime + contextSwitching;
        }
        System.out.println();
        //print the average waiting time
        int sum = 0;
        for(int i = 0; i < processesWaitingTime.size(); i++){
            sum += processesWaitingTime.get(i);
        }
        System.out.println("Average waiting time: "+(sum/processesWaitingTime.size()));
        //print the average turnaround time
        sum = 0;
        for(int i = 0; i < processesTurnaroundTime.size(); i++){
            sum += processesTurnaroundTime.get(i);
        }
        System.out.println("Average turnaround time: "+(sum/processesTurnaroundTime.size()));
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
            System.out.println("----------Process "+(i+1)+"----------");
            process.getProcessInfo();
            processes.add(process);
        }
        System.out.println("--------------------------SJF--------------------------");
        SJF sjf = new SJF(processes, contextSwitching);
        sjf.execute();
        System.out.println("-------------------------------------------------------");
    }
}
