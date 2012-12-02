package com.ubs.gedit.gedex.actor.valuation;

import akka.actor.*;

public class TestDriver implements ResultListener {

    public static final int NUMBER_OF_SIMULATION = 1000000;
    public static final int TIME_STEPS = 30;
    private long startTime;

    public TestDriver() {
        this.startTime = System.currentTimeMillis();
    }

    public static void main(String[] args) throws Exception {

        SingleThreadExample singleThread = new SingleThreadExample(36, 40, 4, 1, 24);
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
        duration = (System.currentTimeMillis() - startTime);
        System.out.println("Digital Option with payoff $1, simulated return = " + result);
        System.out.println("(" + duration + " millisec); Fork Join Model");


        System.out.println("\n\nActor Model - Java 1.7");
        final TestDriver testDriver = new TestDriver();
        ActorSystem system = ActorSystem.create("ActorExample");
        ActorRef actorExample = system.actorOf(new Props(
                new UntypedActorFactory() {
                    public UntypedActor create() {
                        return new ActorExample(36, 40, 4, 1, 24, testDriver);
                    }
                }), "master");
        actorExample.tell(new DigitalOptionValuationInput(1, TIME_STEPS, NUMBER_OF_SIMULATION));

        System.out.println("1 Actor test is started!!");

        final TestDriver testDriver2 = new TestDriver();
        System.out.println("\n\nActor Model with recusive function like fork join - Java 1.7");
        ActorSystem system2 = ActorSystem.create("ActorExampleWithRecusiveFunction");
        ActorRef recursiveActorExample = system2.actorOf(new Props(
                new UntypedActorFactory() {
                    public UntypedActor create() {
                        return new RecursiveActorExample(36, 40, 4, 1, 24, testDriver2);
                    }
                }),"master");
//        recursiveActorExample.tell(new DigitalOptionValuationInput(1, 12, 1));
        recursiveActorExample.tell(new DigitalOptionValuationInput(1, TIME_STEPS, NUMBER_OF_SIMULATION));

        System.out.println("2 Actor test are started!!");
    }

    @Override
    public void gotResult(String result) {
        double duration = (System.currentTimeMillis() - startTime);
        System.out.println(result);
        System.out.println("(" + duration + " millisec); Actor Model");
    }
}
