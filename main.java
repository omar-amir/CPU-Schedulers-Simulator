import java.util.*;

public class Main {

    //used for problem 1
    public static process getLeastBurstTimeProcess(List<process>processes, int currentTime) {
        process shortestProcess = null;
        int shortestBurstTime = Integer.MAX_VALUE;
        for (process p : processes) {
            if (p.arrivalTime <= currentTime && p.burstTime < shortestBurstTime) {
                shortestProcess = p;
                shortestBurstTime = p.remainingTime;
            }
        }
        return shortestProcess;
    }

    //used for problem 2
    public static process getShortestProcess(List<process>processes, int currentTime) {
        process shortestProcess = null;
        int shortestTime = Integer.MAX_VALUE;
        for (process p : processes) {
            if (p.arrivalTime <= currentTime && p.remainingTime > 0 && p.remainingTime < shortestTime) {
                shortestProcess = p;
                shortestTime = p.remainingTime;
            }
        }
        return shortestProcess;
    }

    //used for problem 3
    public static process getHighestPriorityProcess(List<process>processes, int currentTime) {
        process returnedProcess = null;
        int highestPriority = Integer.MAX_VALUE;
        for (process p : processes) {
            if (p.arrivalTime <= currentTime && p.priorityNumber < highestPriority) {
                returnedProcess = p;
                highestPriority = p.priorityNumber;
            }
        }
        return returnedProcess;
    }

    //used for problem 4
    public static process getNextProcess(List<process>processes, int currentTime, process pr) {
        process nextProcess = null;
        int smallestAgFactor = pr.AgFactor;
        for (process p : processes) {
            if (p.arrivalTime <= currentTime && p.remainingTime > 0 && p.AgFactor < smallestAgFactor) {
                nextProcess = p;
                smallestAgFactor = p.AgFactor;
            }
        }
        if (nextProcess==null){
            return pr;
        }
        else{
            return nextProcess;
        }
    }

    //used for problem 4
    public static process getNextProcess(List<process>processes, int currentTime) {
        process nextProcess = null;
        int smallestAgFactor = Integer.MAX_VALUE;
        for (process p : processes) {
            if (p.arrivalTime <= currentTime && p.remainingTime > 0 && p.AgFactor < smallestAgFactor) {
                nextProcess = p;
                smallestAgFactor = p.AgFactor;
            }
        }
        return nextProcess;
    }
    public static class process {
        String name;
        int arrivalTime;
        int burstTime;
        int remainingTime;
        int priorityNumber;
        int randomNumber;
        int AgFactor;
        int quantum;
        ArrayList<Integer> quantumHistory;
        public process(String name, int arrivalTime, int burstTime, int priorityNumber, int quantum) {
            this.name = name;
            this.arrivalTime = arrivalTime;
            this.burstTime = burstTime;
            this.priorityNumber = priorityNumber;
            this.remainingTime = burstTime;
            Random random = new Random();
            this.randomNumber = random.nextInt(21);
            if (this.randomNumber<10){
                this.AgFactor=this.randomNumber+this.arrivalTime+this.burstTime;
            }
            else if (this.randomNumber>10){
                this.AgFactor=10+this.arrivalTime+this.burstTime;
            }
            else{
                this.AgFactor=this.priorityNumber+this.arrivalTime+this.burstTime;
            }
            this.quantum=quantum;
            quantumHistory=new ArrayList<>();
            this.quantumHistory.add(this.quantum);
        }
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of processes: ");
        int numOfProcesses = scanner.nextInt();
        System.out.print("Enter Round Robin Time Quantum: ");
        int RRTimeQuantum = scanner.nextInt();
        System.out.print("Enter context switching time: ");
        int contextSwitchingTime = scanner.nextInt();
        ArrayList<process> processes = new ArrayList<>();
        for (int i = 0; i < numOfProcesses; i++) {
            System.out.print("Enter Name Of Process " + (i + 1) + ": ");
            String name = scanner.next();
            System.out.print("Enter Arrival Time Of Process " + (i + 1) + ": ");
            int arrivalTime = scanner.nextInt();
            System.out.print("Enter Burst Time Of Process " + (i + 1) + ": ");
            int burstTime = scanner.nextInt();
            System.out.print("Enter Priority Number Of Process " + (i + 1) + ": ");
            int priority = scanner.nextInt();
            processes.add(new process(name, arrivalTime, burstTime, priority, RRTimeQuantum));
        }
        ArrayList<process> tempProcesses = new ArrayList<>(processes);
        ArrayList<process> outputProcesses = new ArrayList<>();
        Comparator<process> sortByArrivalTime = Comparator.comparingInt(p -> p.arrivalTime);

        System.out.println("-----------------------------------------------------");

        System.out.println("                  ------1------");



        int currentTime = 0;
        double totalWaitingTime = 0;
        double totalTurnaroundTime = 0;
        double avgWaitingTime;
        double avgTurnaroundTime;
        while (!processes.isEmpty()) {
            process p = getLeastBurstTimeProcess(processes, currentTime);
            if (p!=null) {
                outputProcesses.add(p);
                int waitingTime = currentTime - p.arrivalTime;
                System.out.print("Waiting Time For Process " + p.name + ": " + waitingTime);
                System.out.println();
                totalWaitingTime += waitingTime;
                int turnaroundTime = waitingTime + p.burstTime;
                System.out.print("Turnaround Time For Process " + p.name + ": " + turnaroundTime);
                System.out.println();
                totalTurnaroundTime += turnaroundTime;
                currentTime += p.burstTime + contextSwitchingTime;
                processes.remove(p);
            }
            else{
                currentTime++;
            }
        }
        System.out.println("Execution Order for Non-Preemptive Shortest- Job First (SJF):");
        for (int i = 0; i < outputProcesses.size(); i++) {
            if (i != outputProcesses.size() - 1) {
                System.out.print(outputProcesses.get(i).name + "-");
            }
            else {
                System.out.print(outputProcesses.get(i).name);
            }
        }
        System.out.println();
        avgWaitingTime = totalWaitingTime / numOfProcesses;
        avgTurnaroundTime = totalTurnaroundTime / numOfProcesses;
        System.out.println("Average Waiting Time: " + avgWaitingTime);
        System.out.println("Average Turnaround Time: " + avgTurnaroundTime);
        outputProcesses.clear();
        processes.addAll(tempProcesses);

        System.out.println("-----------------------------------------------------");

        System.out.println("                  ------2------");


        currentTime = 0;
        totalWaitingTime = 0;
        totalTurnaroundTime = 0;
        while (!processes.isEmpty()) {
            process shortestProcess = getShortestProcess(processes, currentTime);
            if (shortestProcess != null) {
                outputProcesses.add(shortestProcess);
                currentTime++;
                shortestProcess.remainingTime--;
                if (shortestProcess.remainingTime == 0) {
                    System.out.println("Waiting time for process " + shortestProcess.name + ": " + (currentTime-shortestProcess.burstTime-shortestProcess.arrivalTime));
                    totalWaitingTime+=currentTime-shortestProcess.burstTime-shortestProcess.arrivalTime;
                    System.out.println("turnaround time for process " + shortestProcess.name + ": " + (currentTime-shortestProcess.arrivalTime));
                    totalTurnaroundTime+=currentTime-shortestProcess.arrivalTime;
                    processes.remove(shortestProcess);
                }
            }
            else {
                currentTime++;
            }
        }
        System.out.println("Execution Order for Shortest-Remaining Time First (SRTF) Scheduling");
        for (int i = 0; i < outputProcesses.size(); i++) {
            if (i != outputProcesses.size() - 1) {
                System.out.print(outputProcesses.get(i).name + "-");
            }
            else {
                System.out.print(outputProcesses.get(i).name);
            }
        }
        System.out.println();
        avgWaitingTime = totalWaitingTime / numOfProcesses;
        avgTurnaroundTime = totalTurnaroundTime / numOfProcesses;
        System.out.println("Average Waiting Time: " + avgWaitingTime);
        System.out.println("Average Turnaround Time: " + avgTurnaroundTime);
        outputProcesses.clear();
        processes.addAll(tempProcesses);
        for (int i=0; i<processes.size(); i++){
            processes.get(i).remainingTime=processes.get(i).burstTime;
        }

        System.out.println("-----------------------------------------------------");


        System.out.println("                 ------3------");


        currentTime = 0;
        totalWaitingTime = 0;
        totalTurnaroundTime = 0;
        int currentProcessNumber=0;
        while (!processes.isEmpty()) {
            process p = getHighestPriorityProcess(processes, currentTime);
            if (p!=null) {
                outputProcesses.add(p);
                int waitingTime = currentTime - p.arrivalTime;
                System.out.print("Waiting Time For Process " + ++currentProcessNumber + ": " + waitingTime);
                System.out.println();
                totalWaitingTime += waitingTime;
                int turnaroundTime = waitingTime + p.burstTime;
                System.out.print("Turnaround Time For Process " + ++currentProcessNumber + ": " + turnaroundTime);
                System.out.println();
                totalTurnaroundTime += turnaroundTime;
                currentTime += p.burstTime;
                processes.remove(p);
                //solving starvation by finding the lowest priority process and increasing its priority
                if (!processes.isEmpty()) {
                    process lowestpriorityProcess = Collections.max(processes, Comparator.comparingDouble(product -> product.priorityNumber));
                    lowestpriorityProcess.priorityNumber--;
                }
            }
            else{
                currentTime++;
            }
        }
        avgWaitingTime = totalWaitingTime / numOfProcesses;
        avgTurnaroundTime = totalTurnaroundTime / numOfProcesses;
        System.out.println("Average Waiting Time: " + avgWaitingTime);
        System.out.println("Average Turnaround Time: " + avgTurnaroundTime);
        System.out.println("Execution Order for Non-Preemptive Priority Scheduling:");
        for (int i = 0; i < outputProcesses.size(); i++) {
            if (i != outputProcesses.size() - 1) {
                System.out.print(outputProcesses.get(i).name + "-");
            }
            else {
                System.out.print(outputProcesses.get(i).name);
            }
        }
        outputProcesses.clear();
        processes.addAll(tempProcesses);
        System.out.println();


        System.out.println("-----------------------------------------------------");


        System.out.println("                  ------4------");


        processes.sort(sortByArrivalTime);
        ArrayList<process> readyQueue = new ArrayList<>();
        ArrayList<process> dieList = new ArrayList<>();
        currentTime = 0;
        totalWaitingTime = 0;
        totalTurnaroundTime = 0;
        process currentProcess = processes.get(0);
        process previousProcess = currentProcess;
        int remainingQuantumTime=currentProcess.quantum;
        int workedTime=0;
        while (!processes.isEmpty()){
            outputProcesses.add(currentProcess);
            currentTime++;
            currentProcess.remainingTime--;
            remainingQuantumTime--;
            workedTime++;
            if (currentProcess.remainingTime==0){
                currentProcess.quantum=0;
                currentProcess.quantumHistory.add(currentProcess.quantum);
                dieList.add(currentProcess);
                System.out.println("Waiting time for process " + currentProcess.name + ": " + (currentTime-currentProcess.burstTime-currentProcess.arrivalTime));
                totalWaitingTime+=currentTime-currentProcess.burstTime-currentProcess.arrivalTime;
                System.out.println("turnaround time for process " + currentProcess.name + ": " + (currentTime-currentProcess.arrivalTime));
                totalTurnaroundTime+=currentTime-currentProcess.arrivalTime;
                processes.remove(currentProcess);
                if (!readyQueue.isEmpty()) {
                    while (!readyQueue.isEmpty()) {
                        if (readyQueue.get(0).remainingTime == 0) {
                            readyQueue.remove(0);
                        }
                        else{
                            break;
                        }
                    }
                    if (!readyQueue.isEmpty()) {
                        currentProcess = readyQueue.get(0);
                        previousProcess = currentProcess;
                        remainingQuantumTime = currentProcess.quantum;
                        workedTime = 0;
                        readyQueue.remove(0);
                    }
                    else{
                        if (processes.isEmpty()){
                            break;
                        }
                        currentProcess=getNextProcess(processes, currentTime);
                        previousProcess=currentProcess;
                        workedTime=0;
                    }
                }
                else{
                    if (processes.isEmpty()){
                        break;
                    }
                    currentProcess=getNextProcess(processes, currentTime);
                    previousProcess=currentProcess;
                    workedTime=0;
                }
            }
            else if (remainingQuantumTime==0){
                double quantumMean=0;
                for (int i=0; i<processes.size(); i++){
                    quantumMean+=processes.get(i).quantum;
                }
                quantumMean/=processes.size();
                quantumMean*=0.1;
                quantumMean=Math.ceil(quantumMean);
                currentProcess.quantum+= (int) quantumMean;
                currentProcess.quantumHistory.add(currentProcess.quantum);
                readyQueue.add(currentProcess);
                if (!readyQueue.isEmpty()) {
                    while (!readyQueue.isEmpty()) {
                        if (readyQueue.get(0).remainingTime == 0) {
                            readyQueue.remove(0);
                        }
                        else{
                            break;
                        }
                    }
                    if (!readyQueue.isEmpty()) {
                        currentProcess = readyQueue.get(0);
                        previousProcess = currentProcess;
                        remainingQuantumTime = currentProcess.quantum;
                        workedTime = 0;
                        readyQueue.remove(0);
                    }
                    else{
                        if (processes.isEmpty()){
                            break;
                        }
                        currentProcess=getNextProcess(processes, currentTime);
                        previousProcess=currentProcess;
                        workedTime=0;
                    }
                }
                else{
                    if (processes.isEmpty()){
                        break;
                    }
                    currentProcess=getNextProcess(processes, currentTime);
                    previousProcess=currentProcess;
                    workedTime=0;
                }
            }
            else if (Math.ceil((double) currentProcess.quantum /2)<=workedTime){
                currentProcess=getNextProcess(processes, currentTime, currentProcess);
                if (currentProcess!=previousProcess){
                    previousProcess.quantum+=previousProcess.quantum-workedTime;
                    previousProcess.quantumHistory.add(previousProcess.quantum);
                    readyQueue.add(previousProcess);
                    previousProcess=currentProcess;
                    remainingQuantumTime=currentProcess.quantum;
                    workedTime=0;
                }
            }
        }
        avgWaitingTime = totalWaitingTime / numOfProcesses;
        avgTurnaroundTime = totalTurnaroundTime / numOfProcesses;
        System.out.println("Average Waiting Time: " + avgWaitingTime);
        System.out.println("Average Turnaround Time: " + avgTurnaroundTime);
        System.out.println("Execution Order for AG Scheduling:");
        for (int i = 0; i < outputProcesses.size(); i++) {
            if (i != outputProcesses.size() - 1) {
                System.out.print(outputProcesses.get(i).name + "-");
            }
            else {
                System.out.print(outputProcesses.get(i).name);
            }
        }
        System.out.println();
        System.out.println("Die List:");
        for (int i=0; i<dieList.size(); i++){
            if (i != dieList.size() - 1) {
                System.out.print(dieList.get(i).name + "-");
            }
            else {
                System.out.print(dieList.get(i).name);
            }
        }
        System.out.println();
        for (int i=0; i<tempProcesses.size(); i++){
            System.out.print("Quantum History For Process: " + tempProcesses.get(i).name + ": ");
            for (int j=0; j<tempProcesses.get(i).quantumHistory.size(); j++){
                if (j!=(tempProcesses.get(i).quantumHistory.size())-1) {
                    System.out.print(tempProcesses.get(i).quantumHistory.get(j) + " - ");
                }
                else{
                    System.out.print(tempProcesses.get(i).quantumHistory.get(j));
                }
            }
            System.out.println();
        }
        System.out.println();
        System.out.println("-----------------------------------------------------");
    }
}