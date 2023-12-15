import java.util.*;

import static java.lang.Math.abs;

class Process {
    String name;
    int arrivalTime;
    int burstTime;
    int remainingBurstTime;
    int priorityNumber;
    int finishTime;
    boolean isFinished;
    int turnAroundTime;
    int waitingTime;
    int quantumTime;
    int agingTime;
    int AGFactor;

    boolean status = false;

    int currenttime ;
    public Process(String name, int arrivalTime, int burstTime, int priorityNumber,   int qn) {
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priorityNumber = priorityNumber;
        finishTime = 0;
        isFinished = false;
        turnAroundTime = 0;
        waitingTime = 0;
        this.quantumTime = qn;
    }
    void getProcessInfo(){
        System.out.print("Enter the process name: ");
        Scanner scanner = new Scanner(System.in);
        name = scanner.nextLine();
        System.out.print("Enter the process arrival time: ");
        arrivalTime = scanner.nextInt();
        System.out.print("Enter the process burst time: ");
        burstTime = scanner.nextInt();
        System.out.print("Enter the process priority number: ");
        priorityNumber = scanner.nextInt();

        this.remainingBurstTime = burstTime;
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
    public int getBurstTime() {
        return burstTime;
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
        //print the average waiting time as double
        System.out.println("Average Waiting Time: "+(totalWaitingTime/(double)processes.size()));
        //print the average turn around time as double
        System.out.println("Average Turn Around Time: "+(totalTurnAroundTime/(double)processes.size()));
    }
}
class PriorityScheduling
{
    ArrayList<Process> processes;

    public PriorityScheduling(ArrayList<Process> processes3)
    {
        this.processes=processes3;
    }

    void sortProcesses()
    {
        Collections.sort(processes, new Comparator<Process>() {
            @Override
            public int compare(Process o1, Process o2) {
                if (o1.arrivalTime==o2.arrivalTime)
                {

                    return o1.priorityNumber-o2.priorityNumber;

                }
                else if ((o1.arrivalTime==o2.arrivalTime)&&(o1.priorityNumber==o2.priorityNumber))
                {
                    return o1.burstTime-o2.burstTime;

                }
                else
                {
                    return o1.arrivalTime-o2.arrivalTime;
                }
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
            System.out.println( abs(processes.get(i).currenttime - processes.get(i).arrivalTime));
            processes.get(i).turnAroundTime =abs( processes.get(i).currenttime - processes.get(i).arrivalTime);
            totalturnaroundtime += processes.get(i).turnAroundTime;

        }
        averageturnaroundtime = totalturnaroundtime / processes.size();
        //waiting=turnaround-burst
        System.out.println();
        System.out.println("----------Waiting time for each process----------");

        for (int i = 0; i < processes.size(); i++) {
            System.out.println(abs(processes.get(i).turnAroundTime - processes.get(i).burstTime));
            processes.get(i).waitingTime = abs(processes.get(i).turnAroundTime - processes.get(i).burstTime);
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
    //  increase the priority of a process over time
    void Aging(ArrayList<Process> processes) {
        int aginglimit = 0;
        int sumofpriority =0;
        for (int i=0;i<processes.size();i++)
        {

            sumofpriority+=processes.get(i).priorityNumber;
        }
        aginglimit=sumofpriority / processes.size();
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



class SRTF{
    ArrayList<Process> processes;
    ArrayList<Process> readyProcessesQueue;
    ArrayList<Process> orderedProcesses;
    int currentTime=0;
    int agingLimit;
    //______________________________________________________________________________________________________________________
    public SRTF (ArrayList<Process> processesList)
    {
        this.processes = new ArrayList<>();
        for (Process process:processesList){
            Process p = new Process(process.name,process.arrivalTime,
                    process.burstTime,process.priorityNumber,process.quantumTime);
            processes.add(p);
        }
        this.readyProcessesQueue=new ArrayList<>();
        this.orderedProcesses=new ArrayList<>();
    }
//______________________________________________________________________________________________________________________


    // Helper function to sort the processes based on their arrival time,
    // if arrival time are equal, then sort based on burst time.
    void sortProcesses()
    {
        processes.sort(new Comparator<Process>() {
            @Override
            public int compare(Process o1, Process o2) {
                if (o1.arrivalTime == o2.arrivalTime) {
                    return o1.burstTime - o2.burstTime;
                }

                return o1.arrivalTime - o2.arrivalTime;
            }
        });
    }

//______________________________________________________________________________________________________________________

    void calculateAgingLimit(){
        int totalBurstTime =0;
        for (Process process : processes) {
            totalBurstTime+=process.burstTime;
        }
        agingLimit= totalBurstTime/ processes.size();
    }
    //______________________________________________________________________________________________________________________
    Process agedProcess (){
        for ( Process process : processes){
            if (process.agingTime>=agingLimit){
                return process;
            }
        }
        return null;
    }


//______________________________________________________________________________________________________________________


    void execute()
    {
        ArrayList<Process> ordered=new ArrayList<>();
        sortProcesses();
        calculateAgingLimit();

        for ( Process process : processes){
            process.remainingBurstTime=process.burstTime;
            process.turnAroundTime= process.burstTime;
        }
        while (!processes.isEmpty()) {
            for ( Process process : processes){
                if (process.arrivalTime==currentTime ){
                    readyProcessesQueue.add(process);
                }
            }
            if (!readyProcessesQueue.isEmpty()) {
                Process currentProcess=agedProcess();
                if (currentProcess==null){
                    readyProcessesQueue.sort(Comparator.comparingInt(Process::getBurstTime));
                    currentProcess=readyProcessesQueue.get(0);
                }
                currentProcess.remainingBurstTime--;
                currentProcess.agingTime=0;
                if (currentProcess.remainingBurstTime==0){
                    readyProcessesQueue.remove(currentProcess);
                    processes.remove(currentProcess);
                    orderedProcesses.add(currentProcess);
                }
                for ( Process process : readyProcessesQueue){
                    if (process != currentProcess){
                        process.waitingTime++;
                        process.turnAroundTime++;
                        process.agingTime++;
                    }
                }
                ordered.add(currentProcess);
            }

            currentTime++;
        }
        System.out.println("Execution order: ");
        for ( Process process : ordered){
            System.out.print(process.name + ' ');
        }
        System.out.println();
        System.out.println( "aging limit= "+ agingLimit );
        System.out.println("Process\t\tWaiting Time\t\tTurn Around Time");
        for (Process process : orderedProcesses) {
            System.out.println(process.name + "\t\t\t\t" + process.waitingTime + "\t\t\t\t\t" + process.turnAroundTime);
        }
        //print the average waiting time and average turn around time
        int totalWaitingTime = 0;
        int totalTurnAroundTime = 0;
        for (Process process : orderedProcesses) {
            totalWaitingTime += process.waitingTime;
            totalTurnAroundTime += process.turnAroundTime;
        }
        System.out.println("Average Waiting Time: "+(totalWaitingTime/(double)orderedProcesses.size()));
        System.out.println("Average Turn Around Time: "+(totalTurnAroundTime/(double)orderedProcesses.size()));
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


            Process minProcess;
            try {
                  minProcess = curProcess.get(0);
            } catch (IndexOutOfBoundsException e){
                curTime++;
                continue;
            }
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
            Process process = new Process("",  0, 0, 0, timeQuantum);
            System.out.println("----------Process "+(i+1)+"----------");
            process.getProcessInfo();
            processes.add(process);
        }
        System.out.println("--------------------------SJF--------------------------");
        //copy the processes to another array list to use it in the SJF class
        ArrayList<Process> processes2 = new ArrayList<>(processes);
        SJF sjf = new SJF(processes2, contextSwitching);
        sjf.execute();
        System.out.println("-------------------------------------------------------");
        System.out.println();
        System.out.println();
        System.out.println("--------------------------SRTF--------------------------");
        SRTF srtf = new SRTF(processes);
        srtf.execute();
        System.out.println("-------------------------------------------------------");
        System.out.println();
        System.out.println();
        System.out.println("--------------------------priority scheduler--------------------------");
        ArrayList<Process> processes3 = new ArrayList<>(processes);
        PriorityScheduling priority = new PriorityScheduling(processes3);
        priority.execute();
        priority.Aging(processes3);
        System.out.println("-------------------------------------------------------");
        System.out.println();
        System.out.println();

        System.out.println("--------------------------AG scheduler--------------------------");
        ArrayList<Process> processes4 = new ArrayList<>(processes);
        AGSchedule ag = new AGSchedule(processes4);
        ag.execute();
        System.out.println("-------------------------------------------------------");
        System.out.println();
        System.out.println();
    }
}
