package com.ubs.gedit.gedex.actor.valuation;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;

public class TotalPayoffWorker extends UntypedActor {

    public static final int TASK_THRESHOLD = 12 * 10000;
    private final double tau;
    private final double riskFreeRate;
    private final double annualVolatility;
    private final double currentPrice;
    private final int timePeriods;
    private final int simulation;
    private final double strike;
    private final double payoff;

    private double totalReturn = 0;
    private int messageReply = 0;

    public TotalPayoffWorker(double tau, double riskFreeRate, double annualVolatility, double currentPrice, int timePeriods, int simulation, double strike, double payoff) {
        this.tau = tau;
        this.riskFreeRate = riskFreeRate;
        this.annualVolatility = annualVolatility;
        this.currentPrice = currentPrice;
        this.timePeriods = timePeriods;
        this.simulation = simulation;
        this.strike = strike;
        this.payoff = payoff;
    }

    protected void compute() throws Exception {

        //Single thread if it's a small task
        if (simulation * timePeriods < TASK_THRESHOLD) {
            getContext().parent().tell(singleThreadCompute(simulation), getSelf());
            return;
        }

        //Divide and conqure
        final int simDoneByFirstOne = simulation / 2;
        final int simDoneBySecondOne = simulation - simDoneByFirstOne;

        ActorRef worker1 = getContext().actorOf(new Props(new UntypedActorFactory() {
            public UntypedActor create() {
                return new TotalPayoffWorker(tau, riskFreeRate, annualVolatility, currentPrice,
                        timePeriods, simDoneByFirstOne, strike, payoff);
            }
        }));
        worker1.tell("getToWork", getSelf());

        ActorRef worker2 = getContext().actorOf(new Props(new UntypedActorFactory() {
            public UntypedActor create() {
                return new TotalPayoffWorker(tau, riskFreeRate, annualVolatility, currentPrice,
                        timePeriods, simDoneBySecondOne, strike, payoff);
            }
        }));
        worker2.tell("getToWork", getSelf());
    }

    private Double singleThreadCompute(int simulation) throws Exception {
        FinalPriceCallableSimulation priceSim = new FinalPriceCallableSimulation(tau, riskFreeRate, annualVolatility, currentPrice, timePeriods);
        double totalPayoff = 0;
        for (int i = 0; i < simulation; i++) {
            double price = (Double) priceSim.call();
            totalPayoff += price >= strike ? payoff : 0;
        }
        return totalPayoff;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof String && "getToWork".equals(message)) {
            compute();
        } else if (message instanceof Double) {
            messageReply++;
            totalReturn += ((Double) message);

            if (messageReply == 2){
                getContext().parent().tell(totalReturn, getSelf());
            }
        } else {
            unhandled(message);
        }
    }
}
