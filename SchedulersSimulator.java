import java.util.*;

class Process {
    String name;
    String color;
    int arrivalTime;
    int burstTime;
    int priorityNumber;
    int finishTime;
    boolean isFinished;
    int turnAroundTime;
    int waitingTime;

    int currenttime ;
    public Process(String name, String color, int arrivalTime, int burstTime, int priorityNumber) {
        this.name = name;
        this.color = color;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priorityNumber = priorityNumber;
        finishTime = 0;
        isFinished = false;
        turnAroundTime = 0;
        waitingTime = 0;
    }
    void getProcessInfo(){
        System.out.print("process name, color, arrival time, burst time, priority number: ");
        Scanner scanner = new Scanner(System.in);
        name = scanner.next();
        color = scanner.next();
        arrivalTime = scanner.nextInt();
        burstTime = scanner.nextInt();
        priorityNumber = scanner.nextInt();
    }
    void setFinishTime(int finishTime){
        this.finishTime = finishTime;
    }
    void setIsFinished(boolean isFinished){
        this.isFinished = isFinished;
    }
    void setTurnAroundTime(int turnAroundTime){
        this.turnAroundTime = turnAroundTime;
    }
    void setWaitingTime(int waitingTime){
        this.waitingTime = waitingTime;
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
    void sortFinishTime(){
        Collections.sort(processes, new Comparator<Process>() {
            @Override
            public int compare(Process o1, Process o2) {
                return o1.finishTime - o2.finishTime;
            }
        });
    }
    void sortArrival(){
        Collections.sort(processes, new Comparator<Process>() {
            @Override
            public int compare(Process o1, Process o2) {
                return o1.arrivalTime - o2.arrivalTime;
            }
        });
    }
    void sortArrivalBurst(){
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
    void execute() {
        sortArrivalBurst();
        processes.get(0).setFinishTime(processes.get(0).arrivalTime + processes.get(0).burstTime);
        processes.get(0).setIsFinished(true);
        for(int i=1;i<processes.size();i++){
            ArrayList<Process> waitingQueue = new ArrayList<>();
            int finishTime = processes.get(i-1).finishTime;
            for(int j=0;j<processes.size();j++){
                if(!processes.get(j).isFinished && processes.get(j).arrivalTime <= finishTime){
                    waitingQueue.add(processes.get(j));
                }
            }
            //sort the waiting queue according to their burst time and if equal then according to their arrival time
            Collections.sort(waitingQueue, new Comparator<Process>() {
                @Override
                public int compare(Process o1, Process o2) {
                    if(o1.burstTime == o2.burstTime){
                        return o1.arrivalTime - o2.arrivalTime;
                    }
                    return o1.burstTime - o2.burstTime;
                }
            });
            for(int j=0,k=i;j<waitingQueue.size();j++){
                processes.set(k++, waitingQueue.get(j));
            }
            processes.get(i).setIsFinished(true);
            if(processes.get(i).arrivalTime > processes.get(i-1).finishTime){
                processes.get(i).setFinishTime(processes.get(i).arrivalTime + processes.get(i).burstTime);
            }
            else{
                processes.get(i).setFinishTime(processes.get(i-1).finishTime + processes.get(i).burstTime);
            }
        }
        for(int i=0;i<processes.size();i++){
            processes.get(i).setTurnAroundTime(processes.get(i).finishTime+ (contextSwitching*i) - processes.get(i).arrivalTime);
        }
        for(int i=0;i<processes.size();i++){
            processes.get(i).setWaitingTime(processes.get(i).turnAroundTime - processes.get(i).burstTime);
        }
        sortFinishTime();
        //Print the processes in the first column and their waiting time in the second column and their turn around time in the third column
        System.out.println("Process\t\tWaiting Time\t\tTurn Around Time");
        for(int i=0;i<processes.size();i++){
            System.out.println(processes.get(i).name+"\t\t\t\t"+processes.get(i).waitingTime+"\t\t\t\t\t"+processes.get(i).turnAroundTime);
        }
        //print the average waiting time and average turn around time
        int totalWaitingTime = 0;
        int totalTurnAroundTime = 0;
        for(int i=0;i<processes.size();i++){
            totalWaitingTime += processes.get(i).waitingTime;
            totalTurnAroundTime += processes.get(i).turnAroundTime;
        }
        System.out.println("Average Waiting Time: "+(totalWaitingTime/processes.size()));
        System.out.println("Average Turn Around Time: "+(totalTurnAroundTime/processes.size()));
    }
}
class PriorityScheduling
{
    ArrayList<Process> processes;
    public PriorityScheduling(ArrayList<Process> processes)
    {
        this.processes = processes;
    }
    // Sort according  priority time
    // if equal according to  arrival time

    void sortProcesses()
    {
        Collections.sort(processes, new Comparator<Process>() {
            @Override
            public int compare(Process o1, Process o2) {
                // if priority is equal , the process with the smallest arrivaltime
                // should be positioned before the other
                if(o1.priorityNumber == o2.priorityNumber){
                    return o1.arrivalTime - o2.arrivalTime;
                }
                // If the priority numbers are different, compare the priority numbers.
                // processes with higher priority numbers will be positioned after
                // processes with lower priority numbers.
                return o1.priorityNumber - o2.priorityNumber;
            }
        });
    }
    void execute()
    {
        sortProcesses();
        System.out.println("----------Processes execution order----------");
        for(int i = 0; i < processes.size(); i++){
            System.out.print(processes.get(i).name+" ");
        }
        System.out.println();

        int totalturnaroundtime=0;
        int averageturnaroundtime =0;
        int totalwatingtime=0;
        int averagewaitingtime=0;
        processes.get(0).currenttime = processes.get(0).burstTime;
        //current[i]=burst[i]+current[i-1]
        for(int i = 1; i < processes.size(); i++){
            processes.get(i).currenttime = processes.get(i).burstTime + processes.get(i - 1).currenttime;
        }
        //turnaround=current-arrival
        System.out.println();
        System.out.println("----------Turnaround time for each process----------");
        for (int i = 0; i < processes.size(); i++) {
            System.out.println( processes.get(i).currenttime - processes.get(i).arrivalTime);
            processes.get(i).turnAroundTime = processes.get(i).currenttime - processes.get(i).arrivalTime;
            totalturnaroundtime += processes.get(i).turnAroundTime;

        }
        averageturnaroundtime = totalturnaroundtime / processes.size();
        //waiting=turnaround-burst
        System.out.println();
        System.out.println("----------Waiting time for each process----------");

        for (int i = 0; i < processes.size(); i++) {
            System.out.println(processes.get(i).turnAroundTime - processes.get(i).burstTime);
            processes.get(i).waitingTime = processes.get(i).turnAroundTime - processes.get(i).burstTime;
            totalwatingtime += processes.get(i).waitingTime;
        }
        averagewaitingtime = totalwatingtime  / processes.size();

        //print the average waiting time
        System.out.println();

        System.out.println("Average waiting time: "+(averagewaitingtime));
        //print the average turnaround time
        System.out.println();
        System.out.println("Average turnaround time: "+(averageturnaroundtime));

    }
    // Example: Apply aging to increase the priority of a process over time
    void Aging(ArrayList<Process> processes) {
        int aginglimit = 0;
        int sumofpriority =0;
        for (int i=0;i<processes.size();i++)
        {

            sumofpriority+=processes.get(i).priorityNumber;
        }
        aginglimit=sumofpriority % processes.size();
        for (Process process : processes) {
            // if the process has been waiting for a long time
            if (process.waitingTime > aginglimit) {
                // Increase the priority (lower the priority value)
                process.priorityNumber--;
                System.out.println("Aging: Increased priority for process " + process.name);
            }
        }
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

        System.out.println("--------------------------priority scheduler--------------------------");
        PriorityScheduling priority = new PriorityScheduling(processes);
        priority.execute();
        priority.Aging(processes);
        System.out.println("-------------------------------------------------------");
    }
}
