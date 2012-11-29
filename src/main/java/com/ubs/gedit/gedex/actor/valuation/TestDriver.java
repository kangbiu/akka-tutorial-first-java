package com.ubs.gedit.gedex.actor.valuation;

public class TestDriver {

    public static final int NUMBER_OF_SIMULATION = 1000000;
    public static final int TIME_STEPS = 30;

    public static void main(String[] args) throws Exception {

        SingleThreadExample singleThread = new SingleThreadExample(36, 40, 4, 1, 24);
        System.out.println("Black Scholes result = " + singleThread.blackScholePrice());

        long startTime = System.currentTimeMillis();
        double result = singleThread.digitalOption(1, TIME_STEPS, NUMBER_OF_SIMULATION);  //4000 millisecs for 1,000,000
//        double result = singleThread.digitalOption(1, 30, 10000000);  //37 secs for 10,000,000
        double duration = (System.currentTimeMillis() - startTime);
        System.out.println("Digital Option with payoff $1, simulated return = " + result);
        System.out.println("(" + duration + " millisec); Java version = " + System.getProperty("java.version"));

        System.out.println("\n\nExecutor Model - Java 1.5-1.6");
        ExecutorExample executor = new ExecutorExample(36, 40, 4, 1, 24);

        startTime = System.currentTimeMillis();
        result = executor.digitalOption(1, TIME_STEPS, NUMBER_OF_SIMULATION);  //4000 millisecs for 1,000,000
//        result = executor.digitalOption(1, 30, 10000000);  //37 secs for 10,000,000
        duration = (System.currentTimeMillis() - startTime);
        System.out.println("Digital Option with payoff $1, simulated return = " + result);
        System.out.println("(" + duration + " millisec); Executor Model");

        System.out.println("\n\nNaive Fork Join Pool - Java 1.7");
        NaiveForkJoinPoolExample naivePool = new NaiveForkJoinPoolExample(36, 40, 4, 1, 24);

        startTime = System.currentTimeMillis();
        result = naivePool.digitalOption(1, TIME_STEPS, NUMBER_OF_SIMULATION);  //4000 millisecs for 1,000,000
//        result = executor.digitalOption(1, 30, 10000000);  //37 secs for 10,000,000
        duration = (System.currentTimeMillis() - startTime);
        System.out.println("Digital Option with payoff $1, simulated return = " + result);
        System.out.println("(" + duration + " millisec); Naive Fork Join Model");


        System.out.println("\n\nFork Join Pool - Java 1.7");
        ForkJoinPoolExample pool = new ForkJoinPoolExample(36, 40, 4, 1, 24);

        startTime = System.currentTimeMillis();
        result = pool.digitalOption(1, TIME_STEPS, NUMBER_OF_SIMULATION);  //4000 millisecs for 1,000,000
//        result = executor.digitalOption(1, 30, 10000000);  //37 secs for 10,000,000
        duration = (System.currentTimeMillis() - startTime);
        System.out.println("Digital Option with payoff $1, simulated return = " + result);
        System.out.println("(" + duration + " millisec); Fork Join Model");
    }

}
