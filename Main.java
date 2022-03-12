package com.company;

import java.util.*;

class process_work {
    public String getName() {
        return name;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }
    public String getColor(){
        return color;
    }
    public String name,color;
    public int start, end;
    public process_work(String name,String color, int start, int end) {
        this.name = name;
        this.start = start;
        this.end = end;
        this.color=color;
    }

    @Override
    public String toString() {
        return name + " "+ color +" "+start+" "+end;
    }
}

class process {
    private String name;
    private final String color;
    private int arrival_Time;
    private int burst_Time, wait_time, trun_around, termination, Quantum, AGAT_Factor;
    private final int fake_burst;
    private int priority_number;
    public boolean non_pree = true;

    public process(String name, String color, int arrival_Time, int burst_Time, int priority_number, int Quantum) {
        this.name = name;
        this.color = color;
        this.arrival_Time = arrival_Time;
        this.burst_Time = burst_Time;
        fake_burst = burst_Time;
        this.Quantum = Quantum;
        this.priority_number = priority_number;
        wait_time = 0;
        trun_around = 0;
        termination = 0;
        this.AGAT_Factor = 0;
    }


    public String getname() {
        return name;
    }

    public String getcolor() {
        return color;
    }

    public int get_arrival_Time() {
        return arrival_Time;
    }

    public int get_burst_Time() {
        return burst_Time;
    }

    public int get_wait_time() {
        return wait_time;
    }

    public int get_trun_around() {
        return trun_around;
    }

    public int get_priority_number() {
        return priority_number;
    }

    public void set_wait_time(int wait_time) {
        this.wait_time = wait_time;
    }

    public void set_trun_around(int trun_around) {
        this.trun_around = trun_around;
    }

    public void setTermination(int termination) {
        this.termination = termination;
    }

    public int getTermination() {
        return termination;
    }

    public void setBurst_Time(int burst_Time) {
        this.burst_Time = burst_Time;
    }

    public int getFake_burst() {
        return fake_burst;
    }

    public int getQuantum() {
        return Quantum;
    }

    public int getAGAT_Factor() {
        return AGAT_Factor;
    }

    public void setAGAT_Factor(int AGAT_Factor) {
        this.AGAT_Factor = AGAT_Factor;
    }

    public void setQuantum(int Quantum) {
        this.Quantum = Quantum;
    }

    @Override
    public String toString() {
        return name + " " + color + " " + priority_number+" "+wait_time+" "+trun_around;
    }
}

class print {
    static float avg_wait = 0;
    static float avg_trun = 0;
    static void print(ArrayList<process> g) {
        for (process process : g) {
            process.set_trun_around(process.getTermination() - process.get_arrival_Time());
            process.set_wait_time(process.get_trun_around() - process.getFake_burst());
            avg_trun += process.get_trun_around();
            avg_wait += process.get_wait_time();
            //System.out.println(process.getname() + " " + process.get_wait_time() + " " + process.get_trun_around());
        }
        avg_trun = avg_trun / g.size();
        avg_wait = avg_wait / g.size();
        //System.out.println("avg_waiting_Time " + avg_wait + " avg_trun_around " + avg_trun);
    }

    static public void sorted1(ArrayList<process> processes) {
        Comparator<process> byarrive = Comparator.comparing(process::get_arrival_Time);
        Comparator<process> bypri = Comparator.comparing(process::get_burst_Time);
        processes.sort(byarrive.thenComparing(bypri));
    }

    public static float getAvg_wait() {
        return avg_wait;
    }

    public static float getAvg_trun() {
        return avg_trun;
    }
}

class priority_method {
    public ArrayList<process> sorted1(ArrayList<process> processes) {
        Comparator<process> byarrive = Comparator.comparing(process::get_arrival_Time);
        Comparator<process> bypri = Comparator.comparing(process::get_priority_number);
        processes.sort(byarrive.thenComparing(bypri));
        return processes;
    }

    ArrayList<process> p;
    ArrayList<process> g = new ArrayList<process>();

    int context, minp, n;

    priority_method(ArrayList<process> p, int context) {
        this.p = p;
        this.context = context;
    }

    public ArrayList<process> solve() {
        p = this.sorted1(p);
        p.get(0).setTermination(p.get(0).get_arrival_Time() + p.get(0).get_burst_Time() + context);
        g.add(p.get(0));
        p.remove(0);
        n = p.size();
        for (int i = 0; i < n; i++) {
            minp = Integer.MAX_VALUE;
            for (process process : p) {
                if (g.get(g.size() - 1).getTermination() >= process.get_arrival_Time()) {
                    minp = Math.min(minp, process.get_priority_number());
                }
            }
            for (int j = 0; j < p.size(); j++) {
                if (p.get(j).get_priority_number() == minp) {
                    p.get(j).setTermination(g.get(g.size() - 1).getTermination() + p.get(j).get_burst_Time() + context);
                    g.add(p.get(j));
                    p.remove(j);
                    break;
                }
            }
        }
        return g;
    }
}


class sjf_method {
    ArrayList<process> p;
    ArrayList<process> g = new ArrayList<>();

    sjf_method(ArrayList<process> p) {
        this.p = p;
    }

    float minp;

    public ArrayList<process> solve() {
        print.sorted1(p);
        p.get(0).setTermination(p.get(0).get_arrival_Time() + p.get(0).get_burst_Time());
        g.add(p.get(0));
        p.remove(0);
        int n = p.size();

        for (int i = 0; i < n; i++) {
            minp = Integer.MAX_VALUE;
            boolean f = true;
            for (process process : p) {
                if (g.get(g.size() - 1).getTermination() >= process.get_arrival_Time()) {
                    f = false;
                    minp = Math.min(minp, process.get_burst_Time());
                } else {
                    minp = process.get_burst_Time();
                }
            }

            for (int j = 0; j < p.size(); j++) {
                if (p.get(j).get_burst_Time() == minp) {
                    if (!f) {
                        p.get(j).setTermination(p.get(j).get_burst_Time() + g.get(g.size() - 1).getTermination());
                    } else {
                        p.get(j).setTermination((p.get(j).get_arrival_Time() - g.get(g.size() - 1).getTermination()) + p.get(j).get_burst_Time() + g.get(g.size() - 1).getTermination());
                    }
                    g.add(p.get(j));
                    p.remove(j);
                    break;
                }

            }
        }
        return g;
    }
}


class srtf {
    ArrayList<process> p;
    ArrayList<process> g = new ArrayList<process>();

    srtf(ArrayList<process> p) {
        this.p = p;
    }

    float minp;

    public ArrayList<process> solve() {
        Set<String> ready = new HashSet<>();
        print.sorted1(p);
        p.get(0).setTermination(p.get(0).get_arrival_Time() + 1);
        p.get(0).setBurst_Time(p.get(0).get_burst_Time() - 1);
        g.add(p.get(0));
        int n = p.size();

        while (ready.size() != n) {
            minp = Integer.MAX_VALUE;

            for (process process : p) {
                if (g.get(g.size() - 1).getTermination() >= process.get_arrival_Time()) {
                    ready.add(process.getname());
                    minp = Math.min(minp, process.get_burst_Time());
                }
            }

            for (int j = 0; j < p.size(); j++) {
                if (p.get(j).get_burst_Time() == minp) {
                    p.get(j).setTermination(g.get(g.size() - 1).getTermination() + 1);
                    p.get(j).setBurst_Time(p.get(j).get_burst_Time() - 1);
                    g.add(p.get(j));
                    if (p.get(j).get_burst_Time() == 0) {
                        p.remove(j);
                    }
                    break;
                }
            }
        }

        n = p.size();
        for (int i = 0; i < n; i++) {
            minp = Integer.MAX_VALUE;
            boolean f = true;
            for (process process : p) {
                if (g.get(g.size() - 1).getTermination() >= process.get_arrival_Time()) {
                    f = false;
                    minp = Math.min(minp, process.get_burst_Time());
                } else {
                    minp = process.get_burst_Time();
                }
            }

            for (int j = 0; j < p.size(); j++) {
                if (p.get(j).get_burst_Time() == minp) {
                    if (!f) {
                        p.get(j).setTermination(p.get(j).get_burst_Time() + g.get(g.size() - 1).getTermination());
                    } else {
                        p.get(j).setTermination((p.get(j).get_arrival_Time() - g.get(g.size() - 1).getTermination()) + p.get(j).get_burst_Time() + g.get(g.size() - 1).getTermination());
                    }
                    g.add(p.get(j));
                    p.remove(j);
                    break;
                }
            }
        }
        return g;
    }
}


class AGAT {
    int inde = 1;
    //list have all process that user enter
    private List<process> all_processes = new ArrayList<process>();

    private List<process> all_processes_fix = new ArrayList<process>();
    //process enter in the right time
    private List<process> ready_p = new ArrayList<process>();
    //when process end the burst time enter this list
    private List<process> dead_p = new ArrayList<process>();
    //process work now in cpu scheduling
    private process active = new process("hi", "", 0, 0, 0, 0);
    //the start time  of the process
    private int starting_time;

    private ArrayList<process_work> processes_work = new ArrayList<>();

    //parameterized constructor takes all proc & total burst_times
    public AGAT(List<process> all_processes1) {
        this.all_processes = all_processes1;


        int working_time = 0;

        //object from process to check if there is a context_switch
        process temp = new process("", "", 0, 0, 0, 0);

        int Remaining_Burst_Time = Integer.MIN_VALUE;//have the greatest value of remaining burst_time
        int last_arrival_time = Integer.MIN_VALUE;//have the greatest value of last_arrival_time

        //to find the target and store it
        for (process process : all_processes) {
            if (process.get_arrival_Time() > last_arrival_time) {
                last_arrival_time = (int) process.get_arrival_Time();
            }
        }
        //for loop to organize and decide which is the next process will enter the cpu
        for (int current_time = 0; !all_processes.isEmpty(); current_time++) {

            this.starting_time = current_time;

            //take all arrived process in ready queue
            for (int j = 0; j < all_processes.size(); j++) {
                if (all_processes.get(j).get_arrival_Time() <= starting_time && !ready_p.contains(all_processes.get(j))) {
                    this.ready_p.add(all_processes.get(j));
                } else if (all_processes.get(j).get_arrival_Time() > starting_time) {
                    break;
                }
            }

            //work with process come in time zero which have less factor
//            if (starting_time == 0) {
//                for (process process : all_processes) {
//                    if (process.get_burst_Time() > Remaining_Burst_Time) {
//                        Remaining_Burst_Time = (int) process.get_burst_Time();
//                    }
//                }
//                calculate_factors(last_arrival_time, Remaining_Burst_Time);
//                choose_active(0, 0);
//            }

            //non_preemptive step 40 percent of the quantum time
            if (ready_p.get(0).non_pree) {
                for (process process : all_processes) {
                    if (process.get_burst_Time() > Remaining_Burst_Time) {
                        Remaining_Burst_Time = (int) process.get_burst_Time();
                    }
                }
                calculate_factors(last_arrival_time, Remaining_Burst_Time);

                int non_pree = (int) Math.round(this.ready_p.get(0).getQuantum() * 0.4);

                this.ready_p.get(0).setBurst_Time((int) (ready_p.get(0).get_burst_Time() - non_pree));
                this.ready_p.get(0).non_pree = false;

                working_time = non_pree;
                //if burst_time less than zero
                if (ready_p.get(0).get_burst_Time() < 0) {
                    current_time += (int) ((non_pree - 1) - (Math.abs(ready_p.get(0).get_burst_Time())));
                    //System.out.println(ready_p.get(0).getname() + " {" + starting_time + " : " + (current_time + 1) + "}");
                    processes_work.add(new process_work(ready_p.get(0).getname(),ready_p.get(0).getcolor(), starting_time, (current_time + 1)));
                } else {
                    current_time += non_pree - 1;
                    //System.out.println(ready_p.get(0).getname() + " {" + starting_time + " : " + (current_time + 1) + "}");
                    processes_work.add(new process_work(ready_p.get(0).getname(),ready_p.get(0).getcolor(), starting_time, (current_time + 1)));
                }

            } //preemptive work according to factor
            else {
                //to check that the working time less than quantum time
                if (ready_p.get(0).getQuantum() == working_time) {

                    ready_p.get(0).setQuantum(ready_p.get(0).getQuantum() + (2));

                    temp = ready_p.get(0);

                    ready_p.remove(temp);
                    ready_p.add(temp);
                    current_time--;

                } else {
                    //choose another process
                    int i = choose_active(last_arrival_time, Remaining_Burst_Time);
                    if (i != 0) {
                        ready_p.get(0).non_pree = true;

                        if (working_time != 0) {
                            ready_p.get(0).setQuantum(ready_p.get(0).getQuantum() + (ready_p.get(0).getQuantum() - working_time));
                        }
                        swap_(i);

                        //because no process in this time work only choose and recalculate
                        current_time--;
                    } else {
                        if (ready_p.get(0).get_burst_Time() > 0) {
                            this.ready_p.get(0).setBurst_Time((int) (ready_p.get(0).get_burst_Time() - 1));
                            working_time++;
                            //System.out.println(ready_p.get(0).getname() + " {" + starting_time + " : " + (current_time + 1) + "}");
                            processes_work.add(new process_work(ready_p.get(0).getname(),ready_p.get(0).getcolor(), starting_time, (current_time + 1)));
                        }
                    }
                    //if the new process equal old process
                    //con
                    //
                    //
                    //
                    // text switch happen

                }

            }
            //if burst time end remove from ready then add to the dead list
            if (ready_p.get(0).get_burst_Time() <= 0) {
                working_time = 0;

                ready_p.get(0).set_wait_time((int) ((current_time + 1) - ready_p.get(0).get_arrival_Time() - ready_p.get(0).getFake_burst()));

                ready_p.get(0).set_trun_around((int) ((current_time + 1) - ready_p.get(0).get_arrival_Time()));

                //System.out.println(ready_p.get(0).getname() + " removed! /W= " + ready_p.get(0).get_wait_time() + " /T= " + ready_p.get(0).get_trun_around());

                all_processes_fix.add(ready_p.get(0));
                all_processes1.remove(ready_p.get(0));

                dead_p.add(ready_p.get(0));
                ready_p.remove(ready_p.get(0));
            }

        }

        //System.out.println("Average Waiting Time: " + average_Waiting_Time());
        //System.out.println("Average Turnaround Time: " + average_Turnaround_Time());

    }


    public double average_Turnaround_Time() {
        double avg = 0;
        for (process p : all_processes_fix) {
            avg += p.get_trun_around();
            System.out.println(p.get_trun_around());
        }
        return avg / (double) (all_processes_fix.size());
    }

    public double average_Waiting_Time() {
        double avg = 0;
        for (process p : all_processes_fix) {
            avg += p.get_wait_time();
            System.out.println(p.get_wait_time());
        }
        return avg / (double) (all_processes_fix.size());
    }

    public void setWait_p(List<process> wait_p) {
        this.all_processes = wait_p;
    }

    public void add_process(process process) {
        this.ready_p.add(process);
    }

    public void setActive(process active) {
        this.active = active;
    }

    public List<process> getWait_p() {
        return all_processes;
    }

    public void calculate_factors(float last_arrival_time, float Remaining_Burst_Time) {
        float V1, V2;
        if (last_arrival_time > 10) {
            V1 = (float) (last_arrival_time / 10.0);
        } else {
            V1 = 1;
        }
        if (Remaining_Burst_Time > 10) {
            V2 = (float) (Remaining_Burst_Time / 10.0);
        } else {
            V2 = 1;
        }

        //AGAT-Factor = (10-Priority) + ceiling(Arrival Time/v1) + ceiling(Remaining Burst Time/v2)
        for (int i = 0; i < all_processes.size(); i++) {
            int AGAT_Factor = (int) ((10 - all_processes.get(i).get_priority_number()) + Math.ceil(all_processes.get(i).get_arrival_Time() / V1) + Math.ceil(all_processes.get(i).get_burst_Time() / V2));
            all_processes.get(i).setAGAT_Factor(AGAT_Factor);
        }
    }

    public void swap_(int ind) {
        process p1 = ready_p.get(0);
        process p2 = ready_p.get(ind);
        ready_p.remove(p2);
        ready_p.remove(p1);
        ready_p.add(0, p2);
        ready_p.add(p1);

    }

    public int choose_active(int last_arrival_time, int Remaining_Burst_Time) {
        if (ready_p.size() == 1) {
            this.active = ready_p.get(0);
            return 0;
        } else {
            int ind = 0;
            int min = Integer.MAX_VALUE;
            for (int i = 0; i < ready_p.size(); i++) {
                if (ready_p.get(i).getAGAT_Factor() < min) {
                    ind = i;
                    min = ready_p.get(i).getAGAT_Factor();
                    this.active = ready_p.get(i);
                }
            }
            return ind;
        }
    }

    public ArrayList<process_work> getProcesses_work() {
        return processes_work;
    }
}


class Main {
    static Scanner input = new Scanner(System.in);
    public static ArrayList<process> Totalprocesses = new ArrayList<>();

    /*public static void main(String[] args) {
        String name, color;
        int arrival_Time, burst_Time;
        int priority_number;
        int number_processes = input.nextInt();
        int time_quantum = input.nextInt();
        int context_switching = input.nextInt();
        input.nextLine();

        System.out.println("Enter your processes");

        for (int i = 0; i < number_processes; i++) {
            System.out.println("process name");
            name = input.nextLine();
            System.out.println("process color");
            color = input.nextLine();
            System.out.println("process Arrival Time");
            arrival_Time = input.nextInt();
            System.out.println("process Burst Time");
            burst_Time = input.nextInt();
            System.out.println("process Priority Number");
            priority_number = input.nextInt();
            input.nextLine();
            Totalprocesses.add(new process(name, color, arrival_Time, burst_Time, priority_number, time_quantum));
        }
        AGAT agat = new AGAT(Totalprocesses);
    }*/
}