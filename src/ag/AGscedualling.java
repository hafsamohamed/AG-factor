package ag;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Vector;

public class AGscedualling {

    public static Vector<process> vec = new Vector();
    //public static Vector < Double> cielQuantam = new Vector();
    public static HashMap<String, Double> cielQuantam = new HashMap<String, Double>();
    public static Vector<process> que = new Vector();
    public static Vector<process> readyQue = new Vector();
// public static Vector <process> original  = new Vector() ; 
    public static HashMap<String, Integer> originalArrival = new HashMap<String, Integer>();
    public static HashMap<String, Integer> originalBurst = new HashMap<String, Integer>();

    public static int Quantam;

    public static void inputs() {

        Scanner myObje = new Scanner(System.in);
        Scanner in = new Scanner(System.in);
        System.out.println("number of process: ");
        int processNum = myObje.nextInt();

        for (int i = 0; i < processNum; i++) {
            process obj = new process();
            process obj2 = new process();
            System.out.println("process name: ");
            String name = in.nextLine();
            obj.name = name;

            System.out.println("burst time: ");
            int burst = myObje.nextInt();
            obj.burstTime = burst;

            System.out.println("arrival: ");
            int arrival = myObje.nextInt();
            obj.arrivalTime = arrival;

            System.out.println("priority: ");
            int priority = myObje.nextInt();
            obj.Periority = priority;

            System.out.println("quantam: ");
            Quantam = myObje.nextInt();
            obj.quantam = Quantam;

            obj.AGfactor = priority + arrival + burst;

            vec.add(obj);

            // original.add(obj2);  
        }
        for (int l = 0; l < processNum; l++) {

            originalArrival.put(vec.get(l).name, vec.get(l).arrivalTime);
            originalBurst.put(vec.get(l).name, vec.get(l).burstTime);
        }
    }

    public static void updateCeilQuantam() {
        for (int i = 0; i < vec.size(); i++) {

            cielQuantam.put(vec.get(i).name, Math.ceil((vec.get(i).quantam / 2.0)));

        }
    }

    public static double scenario1(process p) {

        double x = p.quantam;
        double mean;

        for (int i = 0; i < que.size(); i++) {
            x += que.get(i).quantam;

        }

        mean = x / (que.size() + 1);

        return (Math.ceil(mean * (10.0 / 100.0)));

    }

    public static process minArrival(Vector<process> v) {
        int min = v.get(0).arrivalTime;
        process min2 = new process();
        min2 = v.get(0);
        for (int i = 1; i < v.size(); i++) {
            if (v.get(i).arrivalTime < min) {
                min = v.get(i).arrivalTime;
                min2 = v.get(i);
            }
        }

        return min2;

    }

    public static process minAGfactor(Vector<process> v) {
        int min = v.get(0).AGfactor;
        process min2 = new process();
        min2 = v.get(0);
        for (int i = 1; i < v.size(); i++) {
            if (v.get(i).AGfactor < min) {
                min = v.get(i).AGfactor;
                min2 = v.get(i);
            }
        }

        return min2;

    }

    public static void agSecdualling() {

        int completionT = 0;
        int ctr = 0;
        double workDone;
        int minArrivall;
        process pro = new process();
        inputs();
        updateCeilQuantam();
        int diff = 0;
        process output = new process();

        for (int n = 0; n < vec.size(); n++) {
            Vector<process> v = new Vector();

            Vector<process> v2 = new Vector();

            {

                for (int i = 0; i < vec.size(); i++) {

                    minArrivall = minArrival(vec).arrivalTime;

                    if (minArrivall == (vec.get(i).arrivalTime)) {
                        pro = vec.get(i);

                        {
                            v.add(pro);
                            output = minAGfactor(v);
                        }
                    }
                }

                for (int k = 0; k < vec.size(); k++) {

                    if (!(vec.get(k).name.equals(output.name))) {
                        v2.add(vec.get(k));
                    }
                }

                for (int k = 0; k < v2.size(); k++) {
                    for (int p = 0; p < que.size(); p++) {

                        if ((v2.get(k).name.equals(que.get(p).name))) {
                            v2.remove(v2.get(k));
                        }
                    }
                }

                if ((minArrival(v2).arrivalTime - output.arrivalTime) <= output.quantam && (minArrival(v2).arrivalTime - output.arrivalTime) > cielQuantam.get(output.name) && output.burstTime != 0) {

                    output.burstTime -= minArrival(v2).arrivalTime;

                    if ((minArrival(v2).arrivalTime - output.arrivalTime) == output.quantam) {

                        if (output.burstTime == 0) {
                            if (que.contains(output)) {
                                que.remove(output);
                            }

                            output.quantam = 0;
                        } else {

                            output.quantam = (int) (output.quantam + scenario1(output));
                            que.add(output);
                        }

                    } else {

                        output.quantam = output.quantam + (output.quantam - minArrival(v2).arrivalTime);

                    }
                    for (int j = 0; j < vec.size(); j++) {
                        if (vec.get(j).name.equals(output.name)) {
                            completionT += minArrival(v2).arrivalTime;
                            vec.get(j).quantam = output.quantam;
                            vec.get(j).arrivalTime += minArrival(v2).arrivalTime;
                            que.add(vec.get(j));

                            System.out.println("completion *** " + completionT);
                        }

                    }

                } else if ((minArrival(v2).arrivalTime - output.arrivalTime) <= output.quantam && (minArrival(v2).arrivalTime - output.arrivalTime) < cielQuantam.get(output.name) && output.burstTime != 0) {

                    output.quantam = (int) (output.quantam + (output.quantam - cielQuantam.get(output.name)));
                    for (int j = 0; j < vec.size(); j++) {
                        if (vec.get(j).name.equals(output.name)) {
                            if (minAGfactor(vec).AGfactor == output.AGfactor) {
                                vec.get(j).burstTime -= output.quantam;
                            } else {
                                vec.get(j).burstTime -= cielQuantam.get(output.name);
                            }
                            completionT += cielQuantam.get(output.name);

                            vec.get(j).quantam = output.quantam;
                            vec.get(j).arrivalTime += cielQuantam.get(output.name);
                            que.add(vec.get(j));

                            System.out.println("completionT *** " + completionT);
                        }

                    }
                    for (int j = 0; j < vec.size(); j++) { //b3ml update lel arrival le ay process 3ndy fl que
                        for (int l = 0; l < que.size(); l++) {
                            if (que.get(l).name.equals(vec.get(j).name)) {
                                vec.get(j).arrivalTime += cielQuantam.get(output.name);
                            }
                        }
                    }

                } else if ((minArrival(v2).arrivalTime - output.arrivalTime) > output.quantam) {
                    n++;
                    ctr++;

                    for (int j = 0; j < vec.size(); j++) {
                        if (vec.get(j).name.equals(output.name)) {
                            vec.get(j).burstTime -= output.quantam;
                            completionT += output.quantam;
                            output.quantam = (int) (output.quantam + scenario1(output));
                            vec.get(j).quantam = output.quantam;
                            vec.get(j).arrivalTime += cielQuantam.get(output.name);

                            System.out.println("completionT*** " + completionT);

                            if (vec.get(j).burstTime > 0) {
                                que.add(vec.get(j));
                            }
                        }
                    }

                }
                updateCeilQuantam();

                System.out.println(output.name + " Running");

                System.out.println("Quantam ");
                for (int l = 0; l < vec.size(); l++) {
                    System.out.println(vec.get(l).quantam);
                }
                System.out.println("ciel 50% ");
                for (String k : cielQuantam.keySet()) {
                    System.out.println(cielQuantam.get(k));
                }
            }
        }

        int ctr2 = vec.size();

        Vector<process> notInQue = new Vector();

        for (int k = 0; k < vec.size(); k++) {

            if (!que.contains(vec.get(k))) {
                notInQue.add(vec.get(k));
            }

        }

        while (ctr2 > 0) {

            //////////////////////////////////////////////////////////////
            for (int h = 0; h < notInQue.size(); h++) {

                if (que.size() > 0 && que.get(0).arrivalTime < notInQue.get(h).arrivalTime) {
                    output = que.get(0);
                } else {
                    output = notInQue.get(h);

                }
            }

            if (que.size() > 0 && output != minAGfactor(que)) {

                output = que.get(0);
                que.remove(output);

                for (int i = 0; i < notInQue.size(); i++) {

                    if (output.arrivalTime < notInQue.get(i).arrivalTime) {

                        for (int x = 0; x < vec.size(); x++) {
                            if (vec.get(x).name.equals(output.name)) {

                                completionT += cielQuantam.get(output.name);

                                vec.get(x).quantam = (int) (vec.get(x).quantam + (vec.get(x).quantam - cielQuantam.get(output.name)));
                                vec.get(x).burstTime -= cielQuantam.get(output.name);
                                vec.get(x).arrivalTime += cielQuantam.get(output.name);

                                System.out.println("complition *** " + completionT);
                                if (output.burstTime <= 0) {

                                    vec.get(x).quantam = 0;

                                    // for(int s = 0 ; s <vec.size() ; s++)
                                    {
                                        vec.get(x).turnaround = completionT - originalArrival.get(vec.get(x).name);
                                        System.out.println("completion 222 *** " + completionT + "..turn " + vec.get(x).turnaround);
                                    }

                                    ctr2--;

                                } else {
                                    que.add(vec.get(x));

                                }

                                updateCeilQuantam();
                                System.out.println(output.name + " Running");

                                System.out.println("Quantam ");
                                for (int l = 0; l < vec.size(); l++) {
                                    System.out.println(vec.get(l).quantam);
                                }
                                System.out.println("ciel 50% ");
                                for (String k : cielQuantam.keySet()) {
                                    System.out.println(cielQuantam.get(k));

                                }

                            }
                        }
                    }
                }

            }

            if (que.size() > 0) {
                output = minAGfactor(que);

            }

            que.remove(output);

            for (int i = 0; i < notInQue.size(); i++) {

                if (output.arrivalTime <= notInQue.get(i).arrivalTime) {

                    for (int x = 0; x < vec.size(); x++) {
                        if (vec.get(x).name.equals(output.name)) {

                            //vec.get(x).burstTime -=output.quantam;
                            vec.get(x).arrivalTime += vec.get(x).quantam;

                            if ((vec.get(x).quantam - vec.get(x).burstTime) > 0) {
                                System.out.println("q  " + vec.get(x).quantam + "burst " + vec.get(x).burstTime + " + - + " + (vec.get(x).quantam - vec.get(x).burstTime));
                                completionT += (vec.get(x).burstTime);
                                System.out.println("completionT ***1 " + completionT);
                            } else if (output.burstTime > 0) {
                                completionT += vec.get(x).quantam;
                                System.out.println("completionT ***2 " + completionT);
                            }
                            vec.get(x).burstTime -= output.quantam;

                            if (output.burstTime <= 0) {

                                vec.get(x).quantam = 0;
                                ctr2--;

                                vec.get(x).turnaround = completionT - originalArrival.get(vec.get(x).name);
                                System.out.println("turnaroundT * end " + completionT + "stored arrival: " + originalArrival.get(vec.get(x).name));

                            } else {

                                output.quantam = (int) (output.quantam + scenario1(output));
                                vec.get(x).quantam = output.quantam;
                                que.add(vec.get(x));
                                vec.get(x).turnaround = completionT - originalArrival.get(vec.get(x).name);
                                System.out.println("turnaroundT * end " + completionT + "stored arrival: " + originalArrival.get(vec.get(x).name));
                            }

                        }
                    }
                }
            }

            updateCeilQuantam();
            System.out.println(output.name + " Running");

            System.out.println("Quantam ");
            for (int l = 0; l < vec.size(); l++) {
                System.out.println(vec.get(l).quantam);
            }
            System.out.println("ciel 50% ");
            for (String k : cielQuantam.keySet()) {
                System.out.println(cielQuantam.get(k));
            }

        }

        for (int l = 0; l < vec.size(); l++) {
            vec.get(l).waitingTime = vec.get(l).turnaround - originalBurst.get(vec.get(l).name);
        }

        for (int l = 0; l < vec.size(); l++) {

            System.out.println("process: " + vec.get(l).name + " turnaround time= " + vec.get(l).turnaround);

            System.out.println("****** ");

            System.out.println("process: " + vec.get(l).name + " waiting time= " + vec.get(l).waitingTime);

            System.out.println("****** ");
        }

        double avgWaiting = 0.0;
        double avgTurnaround = 0.0;

        double sum1 = 0.0;
        double sum2 = 0.0;

        for (int l = 0; l < vec.size(); l++) {

            sum1 += vec.get(l).turnaround;
            sum2 += vec.get(l).waitingTime;

        }
        avgWaiting = (sum2 / vec.size());
        avgTurnaround = (sum1 / vec.size());

        System.out.println("average waiting time= " + avgWaiting + " ****** " + "average turnaround= " + avgTurnaround);

    }
}
