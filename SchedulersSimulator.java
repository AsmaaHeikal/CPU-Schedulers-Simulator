import java.util.*;

class Process {
    String name;
    String color;
    int arrivalTime;
    int burstTime;
    int priorityNumber;
    int quantumTime;

    int AGFactor = 0;

    boolean status = false;
    int rundom;

//    void setQuantumTime(int q) {
//        this.quantumTime = q;
//    }
//
//    int getQuantumTime() {
//        return quantumTime;
//    }

    void setRundom(int x){
        this.rundom = x;
    }


    public Process(String name, String color, int arrivalTime, int burstTime, int priorityNumber, int run, int qn) {
        this.name = name;
        this.color = color;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priorityNumber = priorityNumber;
        this.rundom = run;
        this.quantumTime = qn;
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
        System.out.print("Enter the rundom time: ");
        rundom = scanner.nextInt();
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

    int minimumNumber() {
        int min = processes.get(0).arrivalTime;
        for (int i = 1; i < processes.size(); i++) {
            if (processes.get(i).arrivalTime < min) {
                min = processes.get(i).arrivalTime;
            }
        }
        return min;
    }

    int random() {
        return (int) (Math.random() * 20);
    }

    int AGFactor(Process process) {
//        int rund = random();
        if (process.rundom == 10) {
            int x = process.priorityNumber + process.arrivalTime + process.burstTime;
            process.AGFactor = x;
            return x;
        } else if (process.rundom < 10) {
            int x = process.rundom + process.arrivalTime + process.burstTime;
            process.AGFactor = x;
            return x;
        } else {
            int x = 10 + process.burstTime + process.arrivalTime;
            process.AGFactor = x;
            return x;
        }
    }

    void execute() {
        int lastProcessIdx = 0;
        int curTime = 0;
        int mean = 0;
        int lastExecuteTime = 0;
        boolean fTimeForCurrentProcess = false;
        Process currentProcess = null;
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));
        while (diedList.size() < processes.size()) {
            // Start with getting all new processes, correct

            while (lastProcessIdx < processes.size() && processes.get(lastProcessIdx).arrivalTime <= curTime) {
                processes.get(lastProcessIdx).AGFactor = AGFactor(processes.get(lastProcessIdx));
                curProcess.add(processes.get(lastProcessIdx));
//                 AGFactor(processes.get(lastProcessIdx));
                lastProcessIdx++;
            }
            // todo: all new processes should be added to queueProcess, except the smallest AGFactor one,
            //  which should be compared with currentProcess, if it has smaller AGFactor, then it's the new currentProcess
            //  else it should be added to queueProcess [each curProcess.get(0) will be replaced with minProcess]

            // 1. get min AGFactor process
            // 2. check if this is a new appearing process
                // 2.1 if yes, compare it with currentProcess
                    // 2.1.1 if it has smaller AGFactor, then it's the new currentProcess
                    // 2.1.2 else it should be added to queueProcess
                    // 2.2 else, it's not a new appearing process, so it should be compared with currentProcess
                    // 2.2.1 if it has smaller AGFactor, then it's the new currentProcess
            // 3. All other appearing processes should be added to queueProcess

            Process minProcess = curProcess.get(0);
            for (Process value : curProcess) {
                if (minProcess.AGFactor > value.AGFactor) {
                    minProcess = value;
                }
            }
            for (Process process : curProcess) {
                if (!Objects.equals(process.name, minProcess.name) && !process.status) {
                    queueProcess.add(process);
                    process.status = true;
                }
            }

            if (currentProcess == null || fTimeForCurrentProcess){ // No process is executing, or new process is starting
                if (currentProcess == null)
                    currentProcess = curProcess.get(0);
                int halfTime = (int)Math.ceil(currentProcess.quantumTime * 0.5);
                int rlExecuteTime = Math.min(halfTime, currentProcess.burstTime);
                currentProcess.burstTime -= rlExecuteTime;
                lastExecuteTime = rlExecuteTime;
                System.out.println(curTime);
                curTime += rlExecuteTime;
                fTimeForCurrentProcess = false;
                System.out.println(currentProcess.name);
                System.out.println(curTime);
            } else { // There is a process executing in preemptive

                if (currentProcess.AGFactor > minProcess.AGFactor) {
                    // if the process execute for first time    ??
                    // need to execute it with half of quantum time not one second     ??
                    currentProcess.quantumTime += (currentProcess.quantumTime - lastExecuteTime);
//                    queueProcess.add(currentProcess);
                    currentProcess.status = true;
                    currentProcess = minProcess;
                    fTimeForCurrentProcess = true;
                    System.out.println(currentProcess.name);
                }
                else { // currentProcess will continue executing, in preemptive

                    if (currentProcess.burstTime == 0){ // currentProcess is done
                        // currentProcess will be removed from curProcess
                        // add to diedList .. continue algorithm
                        currentProcess.quantumTime = 0;
                        diedList.add(currentProcess);
//                        Process finalCurrentProcess = currentProcess;
//                        curProcess.removeIf(process -> process.name.equals(finalCurrentProcess.name) );
                        curProcess.remove(currentProcess);
                        currentProcess = queueProcess.remove();
                        for (int i=0;i<queueProcess.size();i++){
                            if(currentProcess.burstTime == 0){
                                currentProcess = queueProcess.remove();
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

                        queueProcess.add(currentProcess);
                        currentProcess.status = true;
//                        if(queueProcess.peek().burstTime == 0){
//                            queueProcess.remove();
//                        }

                        currentProcess = queueProcess.remove();
//
                        for (int i=0;i<queueProcess.size();i++){
                            if(currentProcess.burstTime == 0){
                                currentProcess = queueProcess.remove();
                            }
                        }

                        fTimeForCurrentProcess = true;

                        continue;
                    }


                    // currentProcess will execute for one second
                    System.out.println(curTime);
                    System.out.println(currentProcess.name);
                    curTime++;
                    currentProcess.burstTime -= 1;
                    lastExecuteTime += 1;
                    System.out.println(curTime);




                }
            }
        }
    }
}

public class SchedulersSimulator {
    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        System.out.print("Enter the number of processes: ");
//        int numProcesses = scanner.nextInt();
//        System.out.print("Enter the Round Robin Time Quantum: ");
//        int timeQuantum = scanner.nextInt();
//        System.out.print("Enter context switching time: ");
//        int contextSwitching = scanner.nextInt();


        ArrayList<Process> processes = new ArrayList<>();
//        for (int i = 0; i < numProcesses; i++) {
//            Process process = new Process("", "", 0, 0, 0, 0, 0);
////            System.out.println("----------Process " + (i + 1) + "----------");
////            process.getProcessInfo();
////
////            processes.add(process);
////            process.setQuantumTime(timeQuantum);
//        }
        Process process1 = new Process("p1", "e", 0, 17, 4, 3, 4);
        Process process2 = new Process("p2", "e", 3, 6, 9, 8, 4);
        Process process3 = new Process("p3", "e", 4, 10, 2, 10, 4);
        Process process4 = new Process("p4", "e", 29, 4, 8, 12, 4);

        processes.add(process1);
        processes.add(process2);
        processes.add(process3);
        processes.add(process4);


        AGSchedule ag = new AGSchedule(processes);
        ag.execute();
//        System.out.println("--------------------------SJF--------------------------");
//        SJF sjf = new SJF(processes, contextSwitching);
//        sjf.execute();
//        System.out.println("-------------------------------------------------------");
    }
}
