import java.util.*;

class Process {
    String name;
    String color;
    int arrivalTime;
    int burstTime;
    int priorityNumber;
    int quantumTime;

    int AGFactor = 0;
    public Process(String name, String color, int arrivalTime, int burstTime, int priorityNumber, int quantumTime) {
        this.name = name;
        this.color = color;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priorityNumber = priorityNumber;
        this.quantumTime = quantumTime;
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


class AGSchedule {
    int time = 0;
    int oldTimeQuantum;
    ArrayList<Process> processes;
    Queue<Process> readyQueue;
    Process processInCPU;


    public AGSchedule(ArrayList<Process> processes, int timeQuantum) {
        this.processes = processes;
        this.time=timeQuantum;
    }

    int minimumAGFactor(){
        int i,min = processes.get(0).AGFactor;
        for (i=1;i<processes.size();i++){
            if(processes.get(i).AGFactor < min){
                min = processes.get(i).AGFactor;
            }
        }
        return i;
    }

    void execute(){
        while (true) {
            if (processes.get(minimumAGFactor()).AGFactor < processInCPU.AGFactor) {
                for (int i = 0; i < processes.size(); i++) {
                    if (time >= processes.get(i).arrivalTime ) {
                        if (processInCPU == null){
                            processInCPU = processes.get(i);
                            oldTimeQuantum=processInCPU.quantumTime;
                            processInCPU.quantumTime /= 2 ;
                            time += processInCPU.quantumTime;
                            break;
                        }
                        if (processes.get(i).AGFactor < processInCPU.AGFactor) {
                            processInCPU.burstTime -= processInCPU.quantumTime;
                            oldTimeQuantum += processInCPU.quantumTime;
                            processInCPU.quantumTime = oldTimeQuantum;
                            readyQueue.add(processInCPU);
                            oldTimeQuantum=processInCPU.quantumTime;
                            processInCPU=processes.get(i);
                            processInCPU.quantumTime /= 2 ;
                            time += processInCPU.quantumTime;
                            processes.remove(i);
                            break;
                        } else {
                            readyQueue.add(processes.get(i));
                        }
                    }
                }
            }
            if(processInCPU.quantumTime == 0){

            } else if(processInCPU.burstTime==0){

            } else {
                time += 1;
            }

            if(processes.size() == 0 && readyQueue.size() ==0) {
                break;
            }

        }

    }
    static int random(){
        return (int) (Math.random() * 20);
    }

    static int AGFactor(Process process){
        int rund = random();
        if(rund == 10){
            return process.priorityNumber + process.arrivalTime + process.burstTime;
        }
        else if(rund < 10){
            return rund + process.arrivalTime + process.burstTime;
        }
        else{
            return 10 + process.burstTime + process.arrivalTime;
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
            Process process = new Process("", "", 0, 0, 0, 0);
            System.out.println("----------Process "+(i+1)+"----------");
            process.getProcessInfo();
            process.AGFactor = AGSchedule.AGFactor(process);
            processes.add(process);
        }

        AGSchedule agSchedule=new AGSchedule(processes, timeQuantum);
//        System.out.println("--------------------------SJF--------------------------");
//        SJF sjf = new SJF(processes, contextSwitching);
//        sjf.execute();
//        System.out.println("-------------------------------------------------------");
    }
}
