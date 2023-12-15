import java.util.*;

class Process {
    String name;
    String color;
    int arrivalTime;
    int burstTime;
    int remainingBurstTime;
    int priorityNumber;
    int quantumTime;

    int AGFactor;

    boolean status = false;



    public Process(String name, String color, int arrivalTime, int burstTime, int priorityNumber,  int qn) {
        this.name = name;
        this.color = color;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priorityNumber = priorityNumber;
        this.quantumTime = qn;
        this.remainingBurstTime = burstTime;
    }

    void getProcessInfo() {
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

        this.remainingBurstTime = burstTime;

    }
}

//Non-Preemptive Shortest-Job First (SJF) (using context switching)
class SJF {
    ArrayList<Process> processes;
    int contextSwitching;

    public SJF(ArrayList<Process> processes, int contextSwitching) {
        this.processes = processes;
        this.contextSwitching = contextSwitching;
    }

    //Sort the processes according to their arrival time and if equal then according to their burst time
    void sortProcesses() {
        Collections.sort(processes, new Comparator<Process>() {
            @Override
            public int compare(Process o1, Process o2) {
                if (o1.arrivalTime == o2.arrivalTime) {
                    return o1.burstTime - o2.burstTime;
                }
                return o1.arrivalTime - o2.arrivalTime;
            }
        });
    }

    void execute() {
        sortProcesses();
        System.out.println("----------Processes execution order----------");
        for (int i = 0; i < processes.size(); i++) {
            System.out.print(processes.get(i).name + " ");
        }
        System.out.println();
        ArrayList<Integer> processesWaitingTime = new ArrayList<>();
        ArrayList<Integer> processesTurnaroundTime = new ArrayList<>();
        //print the waiting time for each process
        System.out.println("----------Waiting time for each process----------");
        int time = 0;
        for (int i = 0; i < processes.size(); i++) {
            System.out.print((time - processes.get(i).arrivalTime) + " ");
            processesWaitingTime.add(time - processes.get(i).arrivalTime);
            time += processes.get(i).burstTime + contextSwitching;
        }
        //print the turnaround time for each process
        //turnaround time = waiting time + burst time
        System.out.println();
        System.out.println("----------Turnaround time for each process----------");
        time = 0;
        for (int i = 0; i < processes.size(); i++) {
            System.out.print((time - processes.get(i).arrivalTime + processes.get(i).burstTime) + " ");
            processesTurnaroundTime.add(time - processes.get(i).arrivalTime + processes.get(i).burstTime);
            time += processes.get(i).burstTime + contextSwitching;
        }
        System.out.println();
        //print the average waiting time
        int sum = 0;
        for (int i = 0; i < processesWaitingTime.size(); i++) {
            sum += processesWaitingTime.get(i);
        }
        System.out.println("Average waiting time: " + (sum / processesWaitingTime.size()));
        //print the average turnaround time
        sum = 0;
        for (int i = 0; i < processesTurnaroundTime.size(); i++) {
            sum += processesTurnaroundTime.get(i);
        }
        System.out.println("Average turnaround time: " + (sum / processesTurnaroundTime.size()));
    }
}


class AGSchedule {
    ArrayList<Process> processes;
    ArrayList<Process> curProcess;
    ArrayList<Process> diedList;
    Queue<Process> queueProcess;

    public AGSchedule(ArrayList<Process> processes) {
        this.processes = processes;
        this.diedList = new ArrayList<>();
        this.curProcess = new ArrayList<>();
        this.queueProcess = new LinkedList<>();
    }

    int random() {
        return (int) (Math.random() * 20);
    }

    int AGfactor(Process process) {
        int rund = random();
        if (rund == 10) {
            int x = process.priorityNumber + process.arrivalTime + process.burstTime;
            process.AGFactor = x;
            return x;
        } else if (rund < 10) {
            int x = rund + process.arrivalTime + process.burstTime;
            process.AGFactor = x;
            return x;
        } else {
            int x = 10 + process.burstTime + process.arrivalTime;
            process.AGFactor = x;
            return x;
        }
    }

    void print(Map<String, Integer> r, Map<String, Integer> w){
        System.out.println("----------------------------------------------------------------");
        System.out.println("Waiting time for each process");
        for (Map.Entry<String, Integer> e : w.entrySet()) {
            System.out.println(e.getKey() + " Waiting time:  "
                    + e.getValue());
        }
        System.out.println("----------------------------------------------------------------");
        System.out.println("Turn around time time for each process");
        for (Map.Entry<String, Integer> e : r.entrySet()) {
            System.out.println(e.getKey() + " Turn around time:  "
                    + e.getValue());
        }
    }

    void printHistory(){
        System.out.print("(");
        for(int i=0;i<processes.size();i++){
            System.out.print(processes.get(i).name + ":" + processes.get(i).quantumTime);
            if( i < processes.size() - 1){
                System.out.print(", ");
            }
        }
        System.out.println(")");
    }

    void execute() {

        Map<String , Integer> turnTime = new HashMap<>();
        Map<String, Integer> waitTime = new HashMap<>();

        int lastProcessIdx = 0;
        int curTime = 0;
        int lastExecuteTime = 0;
        boolean fTimeForCurrentProcess = false;
        Process currentProcess = null;
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));
        while (diedList.size() < processes.size()) {
            // Start with getting all new processes

            while (lastProcessIdx < processes.size() && processes.get(lastProcessIdx).arrivalTime <= curTime) {

                processes.get(lastProcessIdx).AGFactor = AGfactor(processes.get(lastProcessIdx));
                curProcess.add(processes.get(lastProcessIdx));
                lastProcessIdx++;
            }
            //  all new processes be added to queueProcess, except the smallest AGFactor one,
            //  which should be compared with currentProcess, if it has smaller AGFactor, then it's the new currentProcess
            //  else it should be added to queueProcess [each curProcess.get(0) will be replaced with minProcess]
            

            Process minProcess = curProcess.get(0);
            for (Process value : curProcess) {
                if (minProcess.AGFactor > value.AGFactor) {
                    minProcess = value;
                }
            }

            if (currentProcess == null || fTimeForCurrentProcess){ // No process is executing, or new process is starting
                if (currentProcess == null)
                    currentProcess = curProcess.get(0);
                int halfTime = (int)Math.ceil(currentProcess.quantumTime * 0.5);
                int rlExecuteTime = Math.min(halfTime, currentProcess.remainingBurstTime);
                currentProcess.remainingBurstTime -= rlExecuteTime;
                lastExecuteTime = rlExecuteTime;
                curTime += rlExecuteTime;
                fTimeForCurrentProcess = false;

            } else { // There is a process executing in preemptive

                if (currentProcess.AGFactor > minProcess.AGFactor) {

                    currentProcess.quantumTime += (currentProcess.quantumTime - lastExecuteTime);

                    Process finalCurrentProcess = currentProcess;
                    curProcess.removeIf(process -> process.name.equals(finalCurrentProcess.name));

                    curProcess.add(currentProcess);

                    printHistory();

                    for (Process process : curProcess) {
                        if (!Objects.equals(process.name, minProcess.name) && !process.status) {
                            queueProcess.add(process);
                            process.status = true;
                        }
                    }
                    currentProcess = minProcess;
                    fTimeForCurrentProcess = true;
                }
                else { // currentProcess will continue executing, in preemptive

                    if (currentProcess.remainingBurstTime == 0){ // currentProcess is done
                        // currentProcess will be removed from curProcess
                        // add to diedList 
                        currentProcess.quantumTime = 0;

                        if(!diedList.contains(currentProcess)){
                            diedList.add(currentProcess);
                            int t = (curTime-currentProcess.arrivalTime);
                            int c = currentProcess.burstTime;

                            turnTime.put(currentProcess.name, t);
                            waitTime.put(currentProcess.name, (t-c));

                        }
                        
                        printHistory();

                        curProcess.remove(currentProcess);
                        currentProcess = queueProcess.poll();
                        if(currentProcess!=null) {
                            for (int i = 0; i < queueProcess.size(); i++) {
                                if (currentProcess.remainingBurstTime == 0) {
                                    currentProcess = queueProcess.poll();
                                }
                            }
                            if(currentProcess!=null) {
                                currentProcess.status = false;
                            }
                        }

                        fTimeForCurrentProcess = true;
                        continue;

                    }
                    else if (lastExecuteTime == currentProcess.quantumTime) {
                        // add currentProcess to queueProcess
                        // get head of queueProcess and make it currentProcess

                        // calculate mean
                        int avg = 0;
                        for (int i=0;i<curProcess.size();i++){
                            avg+=curProcess.get(i).quantumTime;
                        }
                        avg = (avg/curProcess.size());
                        currentProcess.quantumTime += (int) Math.ceil(avg * 0.1);

                        Process finalCurrentProcess = currentProcess;
                        curProcess.removeIf(process -> process.name.equals(finalCurrentProcess.name));

                        curProcess.add(currentProcess);
                       printHistory();

                        queueProcess.add(currentProcess);
                        currentProcess.status = true;

                        currentProcess = queueProcess.poll();

                        if(currentProcess!=null) {
                            for (int i = 0; i < queueProcess.size(); i++) {
                                if (currentProcess.remainingBurstTime == 0) {
                                    currentProcess = queueProcess.poll();
                                }
                            }
                            if(currentProcess!=null) {
                                currentProcess.status = false;
                            }
                        }
                        fTimeForCurrentProcess = true;
                        continue;
                    }
                    // currentProcess will execute for one second
                    curTime++;
                    currentProcess.remainingBurstTime -= 1;
                    lastExecuteTime += 1;
                }
            }
        }

        ////////////////////Printing//////////////////////////
        
        print(turnTime, waitTime);

        double avgAroundTime=0;
        for (double value : turnTime.values()) {
            avgAroundTime += value;
        }
        System.out.println("----------------------------------------------------------------");

        System.out.println("Average turn around time = " + (avgAroundTime/turnTime.size()));

        double avgwaitingTime=0;

        for (double integer : waitTime.values()) {
            avgwaitingTime += integer;
        }
        System.out.println("----------------------------------------------------------------");

        System.out.println("Average waiting time = " + (avgwaitingTime/waitTime.size()));

        System.out.println("----------------------------------------------------------------");

        System.out.println("Execution order: ");
        for (Process process : diedList) {
            System.out.print(process.name + " ");
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
            Process process = new Process("", "", 0, 0, 0, timeQuantum);
            System.out.println("----------Process " + (i + 1) + "----------");
            process.getProcessInfo();

            processes.add(process);
        }
        AGSchedule ag = new AGSchedule(processes);
        ag.execute();

//        System.out.println("--------------------------SJF--------------------------");
//        SJF sjf = new SJF(processes, contextSwitching);
//        sjf.execute();
//        System.out.println("-------------------------------------------------------");
    }
}
